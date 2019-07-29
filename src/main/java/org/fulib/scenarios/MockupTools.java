
package org.fulib.scenarios;


import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.fulib.yaml.YamlIdMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MockupTools
{

	private static final String ELEMENTS = "elements";
	private static final String ACTION   = "action";

	private static final String DESCRIPTION = "description";
	private static final String ID          = "id";
	private static final String CONTENT     = "content";
	private static final String TEXT        = "text";
	private static final String ICARDS      = "icards";
	private static final String NAME        = "name";

	private static final Pattern SCREEN_FILE_NAME_PATTERN = Pattern.compile(".*?(\\d+)\\.html");
	private static final Pattern MOCKUP_FILE_NAME_PATTERN = Pattern.compile(".*?(\\d+)-(\\d+)\\.mockup\\.html");

	// language=HTML
	private static final String BOOTSTRAP = "<!-- Bootstrap CSS -->\n" + "<link rel=\"stylesheet\"\n"
	                                        + "      href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\"\n"
	                                        + "      integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\"\n"
	                                        + "      crossorigin=\"anonymous\">\n" +
			"\n" +
			"<script>\n" +
			"    function handler (response) {\n" +
			"        console.log(response);\n" +
			"        document.documentElement.innerHTML = response;\n" +
			"    }\n" +
			"\n" +
			"    function submit(cmd) {\n" +
			"        if (cmd) {\n" +
			"            const words = cmd.split(' ');\n" +
			"            // collect actual parameters\n" +
			"            var request = {};\n" +
			"            request['_cmd'] = words[0];\n" +
			"\n" +
			"            for (var i = 1; i < words.length - 1; i++) {\n" +
			"                const divElem = document.getElementById(words[i]);\n" +
			"                const subDiv = divElem.getElementsByTagName('div')[0];\n" +
			"                const inputElem = subDiv.getElementsByTagName('input')[0];\n" +
			"                const subSubDiv = subDiv.getElementsByTagName('div')[0];\n" +
			"                var value = words[i];\n" +
			"                if (inputElem) {\n" +
			"                    value = inputElem.value;\n" +
			"                }\n" +
			"                else if (subSubDiv) {\n" +
			"                    value = subSubDiv.textContent;\n" +
			"                }\n" +
			"                request[words[i]] = value;\n" +
			"            }\n" +
			"            request['_newPage'] = words[words.length-1];\n" +
			"            const requestString = JSON.stringify(request);\n" +
			"            const httpRequest = new XMLHttpRequest();\n" +
			"            httpRequest.overrideMimeType('application/json');\n" +
			"            httpRequest.addEventListener('load', function() {\n" +
			"                handler(this.responseText);\n" +
			"            });\n" +
			"            httpRequest.open('POST', '/cmd', true);\n" +
			"            httpRequest.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');\n" +
			"            httpRequest.send(requestString);\n" +
			"        }\n" +
			"    }\n" +
			"</script>\n\n";

	private static final String COLS = "class='col col-lg-2 text-center'";
	public static final String TABLES = "tables";

	// =============== Fields ===============

	private ReflectorMap reflectorMap;

	// =============== Static Methods ===============

	public static org.fulib.scenarios.MockupTools htmlTool()
	{
		return new org.fulib.scenarios.MockupTools();
	}

	// =============== Methods ===============

	/**
	 * @param fileName
	 * 	the target file name
	 * @param rootList
	 * 	the list of objects to process
	 *
	 * @deprecated use one of {@link #dumpScreen(String, Object)}, {@link #dumpMockup(String)}
	 * or {@link #dumpTables(String, Object...)}.
	 */
	@Deprecated
	public void dump(String fileName, Object... rootList)
	{
		if (fileName.endsWith(".tables.html"))
		{
			this.dumpTables(fileName, rootList);
		}
		else if (fileName.endsWith(".mockup.html"))
		{
			this.dumpMockup(fileName);
		}
		else
		{
			this.dumpScreen(fileName, rootList[0]);
		}
	}


	public void dumpToString(String fileName, Object... rootList)
	{
		try (final Writer writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8))
		{
			for (Object obj : rootList)
			{
				if (obj == null) {
					continue;
				}
				writer.write(obj.toString());
				writer.write('\n');
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	// --------------- Single Screens ---------------

	public void dumpScreen(String fileName, Object root)
	{
		try (final Writer writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8))
		{
			this.dumpScreen(writer, root);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void dumpScreen(Writer writer, Object root) throws IOException
	{
		writer.write(BOOTSTRAP);
		this.generateElement(root, "", writer);
	}

	// --------------- Mockups ---------------

	public void dumpMockup(String fileName)
	{
		final File[] stepFiles = this.getStepFiles(fileName);
		if (stepFiles == null)
		{
			return; // TODO exception?
		}

		try (final Writer writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8))
		{
			this.dumpMockup(writer, stepFiles);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void dumpMockup(Writer writer, File... stepFiles) throws IOException
	{
		writer.write(BOOTSTRAP);
		// language=HTML
		writer.write("<div id='thePanel'>\n</div>\n\n");
		writer.write("<script>\n");
		// language=JavaScript
		writer.write("    const thePanel = document.getElementById('thePanel');\n");
		writer.write("    const stepList = [\n");

		for (final File stepFile : stepFiles)
		{
			if (stepFile == null)
			{
				continue;
			}

			writer.write("/* ");
			writer.write(stepFile.getName());
			writer.write(" */\n");
			writer.write('`');

			try (final BufferedReader reader = Files.newBufferedReader(stepFile.toPath()))
			{
				// copy, but strip leading BOOTSTRAP characters

				final char[] buf = new char[8192];
				int read = reader.read(buf);
				final int bootstrapLen = BOOTSTRAP.length();

				if (read > bootstrapLen && BOOTSTRAP.equals(new String(buf, 0, bootstrapLen)))
				{
					writer.write(buf, bootstrapLen, read - bootstrapLen);
				}
				else if (read > 0)
				{
					writer.write(buf, 0, read);
				}

				while ((read = reader.read(buf)) > 0)
				{
					writer.write(buf, 0, read);
				}
			}

			writer.write("`,\n");
		}

		writer.write("/* end */\n");
		writer.write('`');
		// language=HTML
		writer.write("<h2 class='row justify-content-center' style='margin: 1rem'>The End</h2>\n");
		writer.write("`\n");
		writer.write("    ];\n");

		// language=JavaScript
		writer.write("    var stepCount = 0;\n" + "    \n" + "    thePanel.innerHTML = stepList[stepCount];\n"
		             + "    thePanel.onclick = nextStep;\n" + "    \n" + "    function nextStep(event) {\n"
		             + "        if (event && (event.ctrlKey || event.shiftKey)) {\n" + "            if (stepCount > 0) {\n"
		             + "                stepCount--;\n" + "                thePanel.innerHTML = stepList[stepCount];\n" + "            }\n"
		             + "        } else if (stepCount + 1 < stepList.length) {\n" + "            stepCount++;\n"
		             + "            thePanel.innerHTML = stepList[stepCount];\n" + "        }\n" + "    }\n");

		writer.write("</script>\n");
	}

	// --------------- Tables ---------------

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
		writer.write(BOOTSTRAP);

		dumpPlainTables(writer, rootList);
	}

	private void dumpPlainTables(Writer writer, Object... rootList) throws IOException
	{
		ArrayList flatList = new ArrayList();
		for (Object obj : rootList)
		{
			if (obj instanceof Collection) {
				flatList.addAll((Collection) obj);
			}
			else {
				flatList.add(obj);
			}
		}

		rootList = flatList.toArray();
		final Object firstRoot = rootList[0];
		final String packageName = firstRoot.getClass().getPackage().getName();
		final YamlIdMap idMap = new YamlIdMap(packageName);

		idMap.encode(rootList);

		final Map<Class<?>, List<Object>> groupedObjects = idMap.getObjIdMap().values().stream()
		                                                        .collect(Collectors.groupingBy(Object::getClass));

		writer.write("<div class='container'>\n");

		for (final Map.Entry<Class<?>, List<Object>> entry : groupedObjects.entrySet())
		{
			final String className = entry.getKey().getSimpleName();
			final List<Object> objects = entry.getValue();

			// --- Class Name ---

			writer.write("    <div class='row justify-content-center '>\n");

			writer.write("        <div class='col text-center font-weight-bold'>");
			writer.write(className);
			writer.write("</div>\n");

			writer.write("    </div>\n");
			writer.write("    <br>\n");

			// --- Property Names ---

			final Object firstObject = objects.get(0);
			Reflector reflector = idMap.getReflector(firstObject);

			writer.write("    <div class='row justify-content-center '>\n");

			for (String property : reflector.getProperties())
			{
				writer.write("        <div class='col text-center font-weight-bold border'>");
				writer.write(property);
				writer.write("</div>\n");
			}

			writer.write("    </div>\n");

			// --- Instances ---

			for (final Object oneObject : objects)
			{
				final String id = idMap.getIdObjMap().get(oneObject);

				writer.write("    <div class='row justify-content-center' id='");
				writer.write(id);
				writer.write("'>\n");

				// --- Property Values ---

				for (final String property : reflector.getProperties())
				{
					final Object value = reflector.getValue(oneObject, property);

					final Collection<Object> valueList = value instanceof Collection ?
						                                     (Collection<Object>) value :
						                                     Collections.singletonList(value);

					writer.write("        <div class='col text-center border'>");

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
						else if (valueElem != null)
						{
							writer.write(valueElem.toString());
						}
						else
						{
							writer.write("N/A");
						}
					}

					writer.write("</div>\n");
				}

				writer.write("    </div>\n");
			}

			writer.write("    <br>\n");
		}

		writer.write("</div>\n");
	}

	// --------------- Helper Methods ---------------

	private File[] getStepFiles(String fileName)
	{
		final File dir = new File(fileName).getParentFile();
		if (!dir.exists() || !dir.isDirectory())
		{
			return null;
		}

		final File[] files = dir.listFiles();
		if (files == null)
		{
			return null;
		}

		final Matcher matcher = MOCKUP_FILE_NAME_PATTERN.matcher(fileName);
		if (!matcher.matches())
		{
			return null;
		}

		final int min = Integer.parseInt(matcher.group(1));
		final int max = Integer.parseInt(matcher.group(2));
		final File[] result = new File[max - min + 1];

		for (final File file : files)
		{
			final Matcher matcher1 = SCREEN_FILE_NAME_PATTERN.matcher(file.getName());
			if (!matcher1.matches())
			{
				continue;
			}

			final int step = Integer.parseInt(matcher1.group(1));
			if (step >= min && step <= max)
			{
				result[step - min] = file;
			}
		}

		return result;
	}

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
		final Reflector reflector = idMap.getReflector(valueElem);
		final String id = this.getUserKey(valueElem);
		if (id != null)
		{
			return id;
		}

		String yamlId = idMap.getIdObjMap().get(valueElem);
		if (yamlId == null) {
			yamlId = valueElem.getClass().getSimpleName();
		}

		return yamlId;
	}

	private String getUserKey(Object card)
	{
		if (card == null)
		{
			return "N/A";
		}
		if (card instanceof String || card instanceof Integer || card instanceof Double
			|| card instanceof Long) {
			return card.toString();
		}

		if (card instanceof Collection)
		{
			return ((Collection<?>) card).stream().map(this::getUserKey).collect(Collectors.joining(", "));
		}

		if (card.getClass().getName().startsWith("java/"))
		{
			return card.toString();
		}

		final String id = this.getUserKey(card, this.getReflector(card));
		if (id != null)
		{
			return id;
		}

		return null;
	}

	private String getUserKey(Object card, Reflector reflector)
	{
		Object userKey = reflector.getValue(card, ID);
		if (userKey != null)
		{
			return userKey.toString();
		}

		userKey = reflector.getValue(card, NAME);
		if (userKey != null)
		{
			return userKey.toString();
		}

		return null;
	}

	private void generateElement(Object root, String indent, Writer writer) throws IOException
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
		writer.write("    <div class='row justify-content-center'>\n");

		// --- Description ---

		final Object description = reflector.getValue(root, DESCRIPTION);
		if (description != null)
		{
			for (String elem : description.toString().split("\\|"))
			{
				writer.write(indent);
				writer.write('\t');
				writer.write('\t');
				this.generateOneCell(root, reflector, elem.trim(), writer);
				writer.write('\n');
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
					writer.write(indent);
					writer.write('\t');
					writer.write('\t');
					this.generateOneCell(elemObject, elemReflector, elem.trim(), writer);
					writer.write('\n');
				}
			}
		}

		// --- Cards ---
		final Object cards = reflector.getValue(root, ICARDS);
		if (cards instanceof Collection)
		{
			for (Object card : (Collection) cards)
			{
				final Reflector cardReflector = this.getReflector(card);
				final String userKey = this.getUserKey(card);

				writer.write(indent);
				writer.write("        <div class='col col-lg-3 text-center' style='margin: 1rem'>\n");
				writer.write("            <div class='border border-dark container'>\n");

				writer.write("                <div class='row justify-content-center text-center' style='margin: 1px'><u>");
				writer.write(userKey);
				writer.write(": ");
				writer.write(card.getClass().getSimpleName());
				writer.write("</u></div>\n");

				for (String property : cardReflector.getProperties())
				{
					if ("line".equals(property))
					{
						continue;
					}

					final Object propValue = cardReflector.getValue(card, property);
					final String valueKey = this.getUserKey(propValue);

					writer.write("                    <div class='row justify-content-left ' style='margin: 1px'>");
					writer.write(property);
					writer.write(": ");
					writer.write(valueKey);
					writer.write("</div>\n");
				}

				writer.write("            </div>\n");
				writer.write("        </div>");
				writer.write('\n');
			}
		}

		writer.write(indent);
		writer.write("    </div>\n");

		// --- Content ---

		final Object content = reflector.getValue(root, CONTENT);
		if (content instanceof Collection)
		{
			for (Object element : (Collection) content)
			{
				this.generateElement(element, indent + '\t', writer);
			}
		}
		else if (content != null)
		{
			this.generateElement(content, indent + '\t', writer);
		}

		// --- tables ---
		Collection<Object> tables = (Collection<Object>) reflector.getValue(root, TABLES);

		if (tables != null && tables.size() > 0) {
			this.dumpPlainTables(writer, tables);
		}

		writer.write(indent);
		writer.write("</div>\n");
	}

	private void generateOneCell(Object root, Reflector reflector, String rootDescription, Writer writer)
		throws IOException
	{
		if (rootDescription.startsWith("input "))
		{
			//        <div class="row justify-content-center" style="margin: 1rem">
			//            <input id="partyNameInput" placeholder="Name?" style="margin: 1rem"></input>
			//        </div>

			writer.write("<input ");
			writer.write(COLS);
			writer.write(" placeholder='");

			if (rootDescription.startsWith("input prompt "))
			{
				writer.write(rootDescription.substring("input prompt ".length()));
			}
			else
			{
				writer.write(rootDescription.substring("input ".length()));
			}

			writer.write("'");

			final String value = (String) reflector.getValue(root, "value");
			if (value != null)
			{
				writer.write(" value='");
				writer.write(value);
				writer.write("'");
			}

			writer.write(" style='margin: 1rem'></input>");
			return;
		}
		else if (rootDescription.startsWith("button "))
		{
			//        <div class="row justify-content-center" style="margin: 1rem">
			//            <button style="margin: 1rem">next</button>
			//        </div>

			writer.write("<div ");
			writer.write(COLS);
			writer.write("><button onclick='submit(\"");

			final String action = (String) reflector.getValue(root, ACTION);

			if (action != null) {
				writer.write(action);
			}
			else {
				String buttonName = rootDescription.substring("button ".length());
				writer.write(buttonName);
			}

			writer.write("\")' style='margin: 1rem'>");
			writer.write(rootDescription.substring("button ".length()));
			writer.write("</button></div>");

			return;
		}

		writer.write("<div ");
		writer.write(COLS);
		writer.write(" style='margin: 1rem'>");
		writer.write(rootDescription);
		writer.write("</div>");
	}
}
