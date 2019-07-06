package org.fulib;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.fulib.yaml.YamlIdMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MockupTools
{

	public static final String ELEMENTS = "elements";
	public static final String ACTION = "action";
	private static LinkedHashMap<Object, ArrayList<String>> mapForStepLists = new LinkedHashMap<>();


	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String CONTENT = "content";
	private ReflectorMap reflectorMap;

	public static MockupTools htmlTool() {
		return new MockupTools();
	}


	public int dump(String fileName, Object... rootList) {

		Object root = rootList[0];
		String packageName = root.getClass().getPackage().getName();

		reflectorMap = new ReflectorMap(packageName);

		String bootstrap = "<!-- Bootstrap CSS -->\n" +
				"    <link rel=\"stylesheet\"\n" +
				"          href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\"\n" +
				"          integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\"\n" +
				"          crossorigin=\"anonymous\">\n\n";

		if (fileName.endsWith(".tables.html")) {
			generateTables(fileName, rootList, bootstrap);
		}
		else if (fileName.endsWith(".mockup.html")) {
			generateMockup(fileName, root, bootstrap);
		}
		else
		{
			generateSingleScreen(fileName, root, bootstrap);
		}

		return 0;
	}

	private void generateTables(String fileName, Object[] rootList, String bootstrap)
	{
		StringBuilder body = new StringBuilder();

		Object firstRoot = rootList[0];
		String packageName = firstRoot.getClass().getPackage().getName();
		YamlIdMap idMap = new YamlIdMap(packageName);
		String encode = idMap.encode(rootList);

		LinkedHashMap<String,ArrayList<String>> tableMap = new LinkedHashMap<>();

		LinkedHashMap<String, Object> idObjMap = idMap.getObjIdMap();
		for (Map.Entry<String, Object> entry : idObjMap.entrySet())
		{
			String oneLine = "";
			Object oneObject = entry.getValue();
			String className = oneObject.getClass().getSimpleName();
			Reflector reflector = idMap.getReflector(oneObject);

			ArrayList<String> oneTable = tableMap.get(className);
			if (oneTable == null) {
				oneTable = new ArrayList<>();
				tableMap.put(className, oneTable);
				oneTable.add(String.format("<div class='row justify-content-center '><div class='col text-center font-weight-bold'>%s</div></div>\n", className));

				String colNames = ""; // "<div class='col text-center font-weight-bold border'>_id</div>";
				for (String property : reflector.getProperties())
				{
					colNames += String.format("<div class='col text-center font-weight-bold border'>%s</div>", property);
				}

				String colLine = String.format("<div class='row justify-content-center '>%s</div>\n", colNames);
				oneTable.add(colLine);
			}


			for (String property : reflector.getProperties())
			{
				Object value = reflector.getValue(oneObject, property);

				Collection<Object> valueList = new ArrayList<>();
				if (value instanceof Collection) {
					valueList = (Collection<Object>) value;
				}
				else {
					((ArrayList<Object>) valueList).add(value);
				}
				String valueString = "";
				for (Object valueElem : valueList)
				{
					String valueKey = idMap.getIdObjMap().get(valueElem);

					if (valueKey != null) {
						String valueName = getValueName(idMap, valueElem);
						valueElem = String.format("<a href='#%s'>%s</a> ", valueKey, valueName);
					}

					valueString += valueElem;

				}
				oneLine += String.format("<div class='col text-center  border'>%s</div>", valueString);
			}


			String lineWithId = String.format("<div class='row justify-content-center' name='%s'>" +
					// "<div class='col text-center border'><a name='%s'>%s</a></div>" +
					"%s</div>",
					entry.getKey(), oneLine);
			oneTable.add(lineWithId);
		}

		for (ArrayList<String> table : tableMap.values())
		{
			for (String line : table)
			{
				body.append(line).append("\n");
			}
			body.append("<br>\n");
		}



		String content = bootstrap + String.format("<div class='container'>\n%s</div>\n", body.toString());
		try
		{
			Files.write(Paths.get(fileName), content.getBytes(StandardCharsets.UTF_8));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private String getValueName(YamlIdMap idMap, Object valueElem)
	{
		Object valueName = idMap.getReflector(valueElem).getValue(valueElem, "id");
		if (valueName != null) {
			return "" + valueName;
		}

		valueName = idMap.getReflector(valueElem).getValue(valueElem, "name");
		if (valueName != null) {
			return "" + valueName;
		}

		return valueElem.getClass().getSimpleName();
	}

	private void generateMockup(String fileName, Object root, String bootstrap)
	{
		StringBuilder body = new StringBuilder();
		body.append(bootstrap);

		String rootDiv = "" +
				"<div id='thePanel'>\n" +
				"</div>\n\n";

		body.append(rootDiv);

		String scriptStart = "" +
				"<script>\n" +
				"   const stepList = [];\n\n";

		body.append(scriptStart);

		ArrayList<String> stepList = mapForStepLists.get(root);

		int i = 0;
		if (stepList != null) {
			for (String stepText : stepList)
			{
				String[] lines = stepText.split("\n");
				String firstLine = "   stepList.push(\"\" +\n";
				body.append(firstLine);

				for (String line : lines)
				{
					body.append("      \"").append(line).append("\\n\" +\n");
				}

				String lastLine = String.format("         \"\");\n\n", i++);
				body.append(lastLine);
			}
		}

		String scriptEnd = "" +
				"   var stepCount = 0;\n" +
				"\n" +
				"   stepList.push(\"<h2 class='row justify-content-center' style='margin: 1rem'>The End</h2>\");\n" +
				"\n" +
				"   document.getElementById('thePanel').innerHTML = stepList[stepCount];\n" +
				"\n" +
				"   const nextStep = function (event) {\n" +
				"        if (event.ctrlKey) {\n" +
				"           stepCount--\n" +
				"        } else {\n" +
				"           stepCount++;\n" +
				"        }\n" +
				"        if (stepList.length > stepCount) {\n" +
				"            document.getElementById('thePanel').innerHTML = stepList[stepCount];\n" +
				"        }\n" +
				"    }\n" +
				"\n" +
				"    document.getElementById('thePanel').onclick = nextStep;\n" +
				"\n" +
				"</script>\n";

		body.append(scriptEnd);

		try
		{
			Files.write(Paths.get(fileName), body.toString().getBytes(StandardCharsets.UTF_8));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}



	private void generateSingleScreen(String fileName, Object root, String bootstrap)
	{
		String body = generateElement(root, "");

		putToStepList(root, body);

		try
		{
			Files.write(Paths.get(fileName), (bootstrap + body).getBytes(StandardCharsets.UTF_8));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void putToStepList(Object root, String body)
	{
		ArrayList<String> stepList = mapForStepLists.get(root);
		if (stepList == null) {
			stepList = new ArrayList<>();
			mapForStepLists.put(root, stepList);
		}
		stepList.add(body);
	}

	public String generateElement(Object root)
	{
		String bootstrap = "<!-- Bootstrap CSS -->\n" +
				"    <link rel=\"stylesheet\"\n" +
				"          href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\"\n" +
				"          integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\"\n" +
				"          crossorigin=\"anonymous\">\n\n";

		String body = generateElement(root, "");

		return bootstrap + body;
	}



	public String generateElement(Object root, String indent)
	{
		String containerClass = "";
		if ("".equals(indent)) {
			containerClass = "class='container'";
		}

		Reflector reflector = getReflector(root);
		Collection content = null;
		Object bareContent = reflector.getValue(root, CONTENT);
		if (bareContent != null) {
			if (bareContent instanceof Collection) {
				content = (Collection) bareContent;
			}
			else {
				content = new ArrayList();
				((ArrayList) content).add(bareContent);
			}
		}

		StringBuilder contentBuf = new StringBuilder();
		if (content != null)
		{
			for (Object element : content)
			{
				String elementHtml = generateElement(element, indent + "   ");
				contentBuf.append(elementHtml);
			}
		}

		String rootId = reflector.getValue(root, ID).toString();


		Object value = reflector.getValue(root, DESCRIPTION);
		String rootDescription = value == null ? "" : value.toString();

		String cellList = "";

		String[] split = rootDescription.split("\\|");

		for (String elem : split)
		{
			String cell = generateOneCell(root, indent, reflector, elem.trim(), split.length);
			cellList += cell;
		}

		Object elementsValue = reflector.getValue(root, ELEMENTS);

		Collection<Object> elements = elementsValue == null ? new ArrayList<>() : (Collection<Object>) elementsValue;

		for (Object elemObject : elements)
		{
			Reflector elemReflector = getReflector(elemObject);
			String elem = (String) elemReflector.getValue(elemObject, "text");
			if (elem != null) {
				String cell = generateOneCell(root, indent, reflector, elem.trim(), split.length);
				cellList += cell;
			}
		}

		String body = String.format("" +
				indent + "<div id='%s' %s>\n" +
				indent + "   <div class='row justify-content-center'>\n" +
				indent + "      %s\n" +
				indent + "   </div>\n" +
				"%s" +
				indent + "</div>\n", rootId, containerClass, cellList, contentBuf.toString());

		return body;
	}

	private Reflector getReflector(Object root)
	{
		if (reflectorMap == null) {
			String packageName = root.getClass().getPackage().getName();
			reflectorMap = new ReflectorMap(packageName);
		}
		return reflectorMap.getReflector(root);
	}

	private String generateOneCell(Object root, String indent, Reflector reflector, String rootDescription, int numberOfElemsPerLine)
	{
		String cols = "class='col col-lg-2 text-center'";

		String elem = String.format("<div %s style='margin: 1rem'>" + rootDescription + "</div>\n", cols);

		if (rootDescription.startsWith("input"))
		{
			//        <div class="row justify-content-center" style="margin: 1rem">
			//            <input id="partyNameInput" placeholder="Name?" style="margin: 1rem"></input>
			//        </div>

			String prompt = "?";
			int pos = rootDescription.indexOf("prompt");
			if (pos >= 0) {
				prompt = rootDescription.substring(pos + "prompt ".length());
			}
			else {
				prompt = rootDescription.substring("input ".length());
			}

			String value = (String) reflector.getValue(root, "value");
			if (value == null) {
				value = "";
			}
			else {
				value = String.format("value='%s'", value);
			}

			elem = String.format("" +
					indent + "   <input %s placeholder='%s' %s style='margin: 1rem'></input>\n",
					cols, prompt, value);

		}
		else if (rootDescription.startsWith("button")) {
			//        <div class="row justify-content-center" style="margin: 1rem">
			//            <button style="margin: 1rem">next</button>
			//        </div>
			String text = rootDescription.substring("button ".length());

			String action = (String) reflector.getValue(root, ACTION);
			if (action == null) {
				action = "";
			}

			elem = String.format(indent + "   <div %s><button onclick='submit(%s)' style='margin: 1rem'>%s</button></div>\n", cols, action, text);
		}

		return elem;
	}
}
