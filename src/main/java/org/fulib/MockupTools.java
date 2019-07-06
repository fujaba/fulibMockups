package org.fulib;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.fulib.yaml.YamlIdMap;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MockupTools
{
	// =============== Constants ===============

	public static final String ELEMENTS = "elements";
	public static final String ACTION   = "action";

	public static final String DESCRIPTION = "description";
	public static final String ID          = "id";
	public static final String CONTENT     = "content";

	// language=HTML
	public static final String BOOTSTRAP = "<!-- Bootstrap CSS -->\n" + "<link rel=\"stylesheet\"\n"
	                                       + "      href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\"\n"
	                                       + "      integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\"\n"
	                                       + "      crossorigin=\"anonymous\">\n";

	// language=JavaScript
	public static final String SCRIPT_END = "var stepCount = 0;\n" + "\n"
	                                        + "stepList.push('<h2 class=\\'row justify-content-center\\' style=\\'margin: 1rem\\'>The End</h2>');\n"
	                                        + "\n"
	                                        + "document.getElementById('thePanel').innerHTML = stepList[stepCount];\n"
	                                        + "\n" + "const nextStep = function(event) {\n" + "\tif (event.ctrlKey) {\n"
	                                        + "\t\tstepCount--;\n" + "\t} else {\n" + "\t\tstepCount++;\n" + "\t}\n"
	                                        + "\tif (stepList.length > stepCount) {\n"
	                                        + "\t\tdocument.getElementById('thePanel').innerHTML = stepList[stepCount];\n"
	                                        + "\t}\n" + "};\n" + "\n"
	                                        + "document.getElementById('thePanel').onclick = nextStep;\n";

	public static final String SCRIPT_START   = "" + "<script>\n" + "   const stepList = [];\n\n";
	public static final String ROOT_DIV       = "" + "<div id='thePanel'>\n" + "</div>\n\n";
	public static final String SCRIPT_END_TAG = "</script>\n";

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
		String body = this.generateElement(root, "");

		this.putToStepList(root, body);

		try
		{
			Files.write(Paths.get(fileName), (BOOTSTRAP + body).getBytes(StandardCharsets.UTF_8));
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
			writer.write(ROOT_DIV);
			writer.write(SCRIPT_START);

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

			writer.write(SCRIPT_END);
			writer.write(SCRIPT_END_TAG);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void dumpTables(String fileName, Object... rootList)
	{
		Object firstRoot = rootList[0];
		String packageName = firstRoot.getClass().getPackage().getName();
		YamlIdMap idMap = new YamlIdMap(packageName);
		idMap.encode(rootList);

		LinkedHashMap<String, ArrayList<String>> tableMap = new LinkedHashMap<>();

		LinkedHashMap<String, Object> idObjMap = idMap.getObjIdMap();
		for (Map.Entry<String, Object> entry : idObjMap.entrySet())
		{
			StringBuilder oneLine = new StringBuilder();
			Object oneObject = entry.getValue();
			String className = oneObject.getClass().getSimpleName();
			Reflector reflector = idMap.getReflector(oneObject);

			ArrayList<String> oneTable = tableMap.get(className);
			if (oneTable == null)
			{
				oneTable = new ArrayList<>();
				tableMap.put(className, oneTable);
				oneTable.add(String.format(
					"<div class='row justify-content-center '><div class='col text-center font-weight-bold'>%s</div></div>\n",
					className));

				StringBuilder colNames = new StringBuilder(); // "<div class='col text-center font-weight-bold border'>_id</div>";
				for (String property : reflector.getProperties())
				{
					colNames.append(
						String.format("<div class='col text-center font-weight-bold border'>%s</div>", property));
				}

				String colLine = String.format("<div class='row justify-content-center '>%s</div>\n",
				                               colNames.toString());
				oneTable.add(colLine);
			}

			for (String property : reflector.getProperties())
			{
				Object value = reflector.getValue(oneObject, property);

				Collection<Object> valueList = new ArrayList<>();
				if (value instanceof Collection)
				{
					valueList = (Collection<Object>) value;
				}
				else
				{
					valueList.add(value);
				}
				StringBuilder valueString = new StringBuilder();
				for (Object valueElem : valueList)
				{
					String valueKey = idMap.getIdObjMap().get(valueElem);

					if (valueKey != null)
					{
						String valueName = this.getValueName(idMap, valueElem);
						valueElem = String.format("<a href='#%s'>%s</a> ", valueKey, valueName);
					}

					valueString.append(valueElem);
				}
				oneLine.append(String.format("<div class='col text-center  border'>%s</div>", valueString.toString()));
			}

			String lineWithId = String.format("<div class='row justify-content-center' name='%s'>" +
			                                  // "<div class='col text-center border'><a name='%s'>%s</a></div>" +
			                                  "%s</div>", entry.getKey(), oneLine.toString());
			oneTable.add(lineWithId);
		}

		try (final PrintWriter writer = new PrintWriter(fileName, "UTF-8"))
		{
			writer.write(BOOTSTRAP);
			writer.write("<div class='container'>\n");

			for (List<String> table : tableMap.values())
			{
				for (String line : table)
				{
					writer.write(line);
					writer.write('\n');
				}
				writer.write("<br>\n");
			}

			writer.write("</div>\n");
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
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

	private String generateElement(Object root, String indent)
	{
		String containerClass = "";
		if ("".equals(indent))
		{
			containerClass = "class='container'";
		}

		Reflector reflector = this.getReflector(root);
		Collection<Object> content = null;
		Object bareContent = reflector.getValue(root, CONTENT);
		if (bareContent != null)
		{
			if (bareContent instanceof Collection)
			{
				content = (Collection<Object>) bareContent;
			}
			else
			{
				content = new ArrayList<>();
				content.add(bareContent);
			}
		}

		StringBuilder contentBuf = new StringBuilder();
		if (content != null)
		{
			for (Object element : content)
			{
				String elementHtml = this.generateElement(element, indent + "   ");
				contentBuf.append(elementHtml);
			}
		}

		String rootId = reflector.getValue(root, ID).toString();

		Object value = reflector.getValue(root, DESCRIPTION);
		String rootDescription = value == null ? "" : value.toString();

		StringBuilder cellList = new StringBuilder();

		String[] split = rootDescription.split("\\|");

		for (String elem : split)
		{
			String cell = this.generateOneCell(root, indent, reflector, elem.trim());
			cellList.append(cell);
		}

		Object elementsValue = reflector.getValue(root, ELEMENTS);

		Collection<Object> elements = elementsValue == null ? new ArrayList<>() : (Collection<Object>) elementsValue;

		for (Object elemObject : elements)
		{
			Reflector elemReflector = this.getReflector(elemObject);
			String elem = (String) elemReflector.getValue(elemObject, "text");
			if (elem != null)
			{
				String cell = this.generateOneCell(root, indent, reflector, elem.trim());
				cellList.append(cell);
			}
		}

		return String.format(
			"" + indent + "<div id='%s' %s>\n" + indent + "   <div class='row justify-content-center'>\n" + indent
			+ "      %s\n" + indent + "   </div>\n" + "%s" + indent + "</div>\n", rootId, containerClass,
			cellList.toString(), contentBuf.toString());
	}

	private String generateOneCell(Object root, String indent, Reflector reflector, String rootDescription)
	{
		String cols = "class='col col-lg-2 text-center'";

		String elem = String.format("<div %s style='margin: 1rem'>" + rootDescription + "</div>\n", cols);

		if (rootDescription.startsWith("input"))
		{
			//        <div class="row justify-content-center" style="margin: 1rem">
			//            <input id="partyNameInput" placeholder="Name?" style="margin: 1rem"></input>
			//        </div>

			String prompt;
			int pos = rootDescription.indexOf("prompt");
			if (pos >= 0)
			{
				prompt = rootDescription.substring(pos + "prompt ".length());
			}
			else
			{
				prompt = rootDescription.substring("input ".length());
			}

			String value = (String) reflector.getValue(root, "value");
			if (value == null)
			{
				value = "";
			}
			else
			{
				value = String.format("value='%s'", value);
			}

			elem = String.format("" + indent + "   <input %s placeholder='%s' %s style='margin: 1rem'></input>\n", cols,
			                     prompt, value);
		}
		else if (rootDescription.startsWith("button"))
		{
			//        <div class="row justify-content-center" style="margin: 1rem">
			//            <button style="margin: 1rem">next</button>
			//        </div>
			String text = rootDescription.substring("button ".length());

			String action = (String) reflector.getValue(root, ACTION);
			if (action == null)
			{
				action = "";
			}

			elem = String.format(
				indent + "   <div %s><button onclick='submit(%s)' style='margin: 1rem'>%s</button></div>\n", cols,
				action, text);
		}

		return elem;
	}

	private void putToStepList(Object root, String body)
	{
		final List<String> stepList = mapForStepLists.computeIfAbsent(root, k -> new ArrayList<>());
		stepList.add(body);
	}
}
