package org.fulib;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.fulib.yaml.YamlIdMap;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MockupTools
{
	// =============== Constants ===============

	public static final String ELEMENTS = "elements";
	public static final String ACTION   = "action";

	public static final String DESCRIPTION = "description";
	public static final String ID          = "id";
	public static final String CONTENT     = "content";
	public static final String TEXT        = "text";

	// language=HTML
	public static final String BOOTSTRAP = "<!-- Bootstrap CSS -->\n" + "<link rel=\"stylesheet\"\n"
	                                       + "      href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\"\n"
	                                       + "      integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\"\n"
	                                       + "      crossorigin=\"anonymous\">\n";

	public static final String COLS = "class='col col-lg-2 text-center'";

	// =============== Static Fields ===============

	private static LinkedHashMap<Object, ArrayList<String>> mapForStepLists = new LinkedHashMap<>();

	// =============== Fields ===============

	private ReflectorMap reflectorMap;

	// =============== Static Methods ===============

	public static MockupTools htmlTool()
	{
		return new MockupTools();
	}

	// =============== Methods ===============

	public void dump(String fileName, Object... rootList)
	{
		Object root = rootList[0];
		String packageName = root.getClass().getPackage().getName();

		this.reflectorMap = new ReflectorMap(packageName);

		if (fileName.endsWith(".tables.html"))
		{
			this.dumpTables(fileName, rootList);
		}
		else if (fileName.endsWith(".mockup.html"))
		{
			this.dumpMockup(fileName, root);
		}
		else
		{
			this.dumpScreen(fileName, root);
		}
	}

	public void dumpScreen(String fileName, Object root)
	{
		final String body = this.generateElement(root);

		this.putToStepList(root, body);

		try (final PrintWriter writer = new PrintWriter(fileName, "UTF-8"))
		{
			writer.write(BOOTSTRAP);
			writer.write(body);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void dumpMockup(String fileName, Object root)
	{
		try (PrintWriter writer = new PrintWriter(fileName, "UTF-8"))
		{
			writer.write(BOOTSTRAP);
			writer.write("<div id='thePanel'>\n</div>\n\n");
			writer.write("<script>\n   const stepList = [];\n\n");

			final List<String> stepList = mapForStepLists.get(root);

			if (stepList != null)
			{
				for (String stepText : stepList)
				{
					writer.write("   stepList.push(\"\" +\n");

					for (String line : stepText.split("\n"))
					{
						writer.write("      \"");
						writer.write(line); // TODO escape?
						writer.write("\\n\" +\n");
					}

					writer.write("         \"\");\n\n");
				}
			}

			// language=JavaScript
			writer.write("var stepCount = 0;\n" + "\n"
			             + "stepList.push('<h2 class=\\'row justify-content-center\\' style=\\'margin: 1rem\\'>The End</h2>');\n"
			             + "\n" + "document.getElementById('thePanel').innerHTML = stepList[stepCount];\n" + "\n"
			             + "const nextStep = function(event) {\n" + "\tif (event.ctrlKey) {\n" + "\t\tstepCount--;\n"
			             + "\t} else {\n" + "\t\tstepCount++;\n" + "\t}\n" + "\tif (stepList.length > stepCount) {\n"
			             + "\t\tdocument.getElementById('thePanel').innerHTML = stepList[stepCount];\n" + "\t}\n"
			             + "};\n" + "\n" + "document.getElementById('thePanel').onclick = nextStep;\n");
			writer.write("</script>\n");
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void dumpTables(String fileName, Object... rootList)
	{
		try (final Writer writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8))
		{
			this.dumpTables(writer, rootList);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void dumpTables(Writer writer, Object... rootList) throws IOException
	{
		final Object firstRoot = rootList[0];
		final String packageName = firstRoot.getClass().getPackage().getName();
		final YamlIdMap idMap = new YamlIdMap(packageName);

		idMap.encode(rootList);

		final Map<Class<?>, List<Object>> groupedObjects = idMap.getObjIdMap().values().stream()
		                                                        .collect(Collectors.groupingBy(Object::getClass));

		writer.write(BOOTSTRAP);
		writer.write("<div class='container'>\n");

		for (final Map.Entry<Class<?>, List<Object>> entry : groupedObjects.entrySet())
		{
			final String className = entry.getKey().getName();
			final List<Object> objects = entry.getValue();

			// --- Class Name ---

			writer.write("<div class='row justify-content-center '><div class='col text-center font-weight-bold'>");
			writer.write(className);
			writer.write("</div></div>\n");
			writer.write("<br>\n");

			// --- Property Names ---

			final Object firstObject = objects.get(0);
			Reflector reflector = idMap.getReflector(firstObject);

			writer.write("<div class='row justify-content-center '>");

			for (String property : reflector.getProperties())
			{
				writer.write("<div class='col text-center font-weight-bold border'>");
				writer.write(property);
				writer.write("</div>");
			}

			writer.write("</div>\n");
			writer.write("<br>\n");

			// --- Instances ---

			for (final Object oneObject : objects)
			{
				final String id = idMap.getIdObjMap().get(oneObject);

				writer.write("<div class='row justify-content-center' name='");
				writer.write(id);
				writer.write("'>");

				// --- Property Values ---

				for (final String property : reflector.getProperties())
				{
					final Object value = reflector.getValue(oneObject, property);

					final Collection<Object> valueList = value instanceof Collection ?
						                                     (Collection<Object>) value :
						                                     Collections.singletonList(value);

					writer.write("<div class='col text-center  border'>");

					for (Object valueElem : valueList)
					{
						final String valueKey = idMap.getIdObjMap().get(valueElem);
						if (valueKey != null)
						{
							final String valueName = this.getValueName(idMap, valueElem);
							writer.write("<a href='#");
							writer.write(valueKey);
							writer.write("'>");
							writer.write(valueName);
							writer.write("</a> ");
						}
						else
						{
							writer.write(valueElem.toString());
						}
					}

					writer.write("</div>");
				}

				writer.write("</div>\n");
				writer.write("<br>\n");
			}
		}

		writer.write("</div>\n");
	}

	// --------------- Helper Methods ---------------

	private Reflector getReflector(Object root)
	{
		if (this.reflectorMap == null)
		{
			String packageName = root.getClass().getPackage().getName();
			this.reflectorMap = new ReflectorMap(packageName);
		}
		return this.reflectorMap.getReflector(root);
	}

	private String getValueName(YamlIdMap idMap, Object valueElem)
	{
		Object valueName = idMap.getReflector(valueElem).getValue(valueElem, "id");
		if (valueName != null)
		{
			return "" + valueName;
		}

		valueName = idMap.getReflector(valueElem).getValue(valueElem, "name");
		if (valueName != null)
		{
			return "" + valueName;
		}

		return valueElem.getClass().getSimpleName();
	}

	private String generateElement(Object root)
	{
		final StringWriter writer = new StringWriter();
		try
		{
			this.generateElement(root, "", writer);
		}
		catch (IOException ignored) // cannot happen with StringWriter
		{
		}
		return writer.toString();
	}

	private void generateElement(Object root, String indent, StringWriter writer) throws IOException
	{
		final Reflector reflector = this.getReflector(root);

		writer.write(indent);
		writer.write("<div id='");

		final String rootId = reflector.getValue(root, ID).toString();
		writer.write(rootId);

		writer.write("' ");

		if (indent.isEmpty())
		{
			writer.write("class='container'");
		}

		writer.write(">\n");
		writer.write(indent);
		writer.write("   <div class='row justify-content-center'>\n");
		writer.write(indent);
		writer.write("      ");

		// --- Description ---

		final Object description = reflector.getValue(root, DESCRIPTION);
		if (description != null)
		{
			for (String elem : description.toString().split("\\|"))
			{
				this.generateOneCell(root, indent, reflector, elem.trim(), writer);
			}
		}

		// --- Elements ---

		final Object elements = reflector.getValue(root, ELEMENTS);
		if (elements instanceof Collection)
		{
			for (Object elemObject : (Collection) elements)
			{
				final Reflector elemReflector = this.getReflector(elemObject);
				final String elem = (String) elemReflector.getValue(elemObject, TEXT);
				if (elem != null)
				{
					this.generateOneCell(root, indent, reflector, elem.trim(), writer);
				}
			}
		}

		writer.write("\n");
		writer.write(indent);
		writer.write("   </div>\n");

		// --- Content ---

		final Object content = reflector.getValue(root, CONTENT);
		if (content instanceof Collection)
		{
			for (Object element : (Collection) content)
			{
				this.generateElement(element, indent + "   ", writer);
			}
		}
		else if (content != null)
		{
			this.generateElement(content, indent + "   ", writer);
		}

		writer.write(indent);
		writer.write("</div>\n");
	}

	private void generateOneCell(Object root, String indent, Reflector reflector, String rootDescription, Writer writer)
		throws IOException
	{
		if (rootDescription.startsWith("input "))
		{
			//        <div class="row justify-content-center" style="margin: 1rem">
			//            <input id="partyNameInput" placeholder="Name?" style="margin: 1rem"></input>
			//        </div>

			writer.write(indent);
			writer.write("   <input ");
			writer.write(COLS);
			writer.write(" placeholder='");

			if (rootDescription.startsWith("input prompt "))
			{
				writer.write(rootDescription, "input prompt ".length(), rootDescription.length());
			}
			else
			{
				writer.write(rootDescription, "input ".length(), rootDescription.length());
			}

			writer.write("'");

			final String value = (String) reflector.getValue(root, "value");
			if (value != null)
			{
				writer.write(" value='");
				writer.write(value);
				writer.write("'");
			}

			writer.write(" style='margin: 1rem'></input>\n");
			return;
		}
		else if (rootDescription.startsWith("button "))
		{
			//        <div class="row justify-content-center" style="margin: 1rem">
			//            <button style="margin: 1rem">next</button>
			//        </div>

			writer.write(indent);
			writer.write("   <div ");
			writer.write(COLS);
			writer.write("><button onclick='submit(");

			final String action = (String) reflector.getValue(root, ACTION);
			if (action != null)
			{
				writer.write(action);
			}

			writer.write(")' style='margin: 1rem'>");
			writer.write(rootDescription, "button ".length(), rootDescription.length());
			writer.write("</button></div>\n");

			return;
		}

		writer.write("<div ");
		writer.write(COLS);
		writer.write(" style='margin: 1rem'>");
		writer.write(rootDescription);
		writer.write("</div>\n");
	}

	private void putToStepList(Object root, String body)
	{
		final List<String> stepList = mapForStepLists.computeIfAbsent(root, k -> new ArrayList<>());
		stepList.add(body);
	}
}
