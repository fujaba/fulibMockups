package org.fulib.mockups;

import org.fulib.mockups.model.Ui;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Map;

public class UiRenderer
{
	private static final STGroupFile HTML_TEMPLATES = new STGroupFile(UiRenderer.class.getResource("html.stg"));

	private Map<String, String> params;

	public static String render(Ui page, Map<String, String> params)
	{
		return new UiRenderer().setParams(params).html(page);
	}

	private UiRenderer setParams(Map<String, String> params)
	{
		this.params = params;
		return this;
	}

	private String html(Ui page)
	{
		final String content = getContent(page);

		return getPage(content);
	}

	private String getPage(String content)
	{
		final ST st = HTML_TEMPLATES.getInstanceOf("page");
		st.add("root", "root");
		st.add("content", content);
		return st.render();
	}

	private String getBlock(Ui currentObject)
	{
		final String description = currentObject.getDescription();
		final StringBuilder html = new StringBuilder();

		for (String div : description.split("\n|\\|\\|"))
		{
			final String text = getDivContent(div);
			final ST st = HTML_TEMPLATES.getInstanceOf("div");
			st.add("type", "line");
			st.add("offset", "0");
			st.add("content", text);
			html.append(st.render());
		}

		return html.toString();
	}

	private String getDivContent(String description)
	{
		final StringBuilder text = new StringBuilder();
		for (String elem : description.split("\\|"))
		{
			elem = elem.trim();
			if (elem.startsWith("button"))
			{
				String[] words = elem.split(" ");
				String buttonText = words[1];
				String target = buttonText;
				if (words.length > 2)
				{
					target = words[2];
				}
				// button(text, target)
				ST st = HTML_TEMPLATES.getInstanceOf("button");
				st.add("text", buttonText);
				st.add("target", target);
				text.append(st.render()).append(" ");
			}
			else if (elem.startsWith("hidden"))
			{
				String[] tokens = elem.split(" ");
				String value = params.get(tokens[1]);
				if (tokens.length > 2)
				{
					value = tokens[2];
				}
				// hidden(name, value)
				ST st = HTML_TEMPLATES.getInstanceOf("hidden");
				st.add("name", tokens[1]);
				st.add("value", value);
				text.append(st.render()).append(" ");
			}
			else if (elem.startsWith("input"))
			{
				String[] tokens = elem.split(" ");
				String inputName = tokens[1];
				int pos = elem.indexOf(inputName);
				String prompt = elem.substring(pos + inputName.length()).trim();
				String value = params.get(inputName);
				// input(id, prompt, value)
				ST st = HTML_TEMPLATES.getInstanceOf("input");
				st.add("id", inputName);
				st.add("prompt", prompt);
				st.add("value", value);
				text.append(st.render()).append(" ");
			}
			else if (elem.startsWith("cell"))
			{
				String[] tokens = elem.split(" ");
				// cell(text)
				ST st = HTML_TEMPLATES.getInstanceOf("cell");
				st.add("text", tokens[1]);
				text.append(st.render()).append(" ");
			}
			else if (elem.startsWith("---"))
			{
				text.append("<hr></hr>");
			}
			else
			{
				text.append(elem).append(" ");
			}
		}
		return text.toString();
	}

	private String getContent(Ui page)
	{
		final StringBuilder buf = new StringBuilder();

		for (Ui kid : page.getContent())
		{
			String kidHtml = getBlock(kid);
			buf.append(kidHtml).append("\n");
		}

		return buf.toString();
	}
}