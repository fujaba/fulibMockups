package org.fulib.mockups;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Map;

public class UIRenderer
{
	private static final STGroupFile HTML_TEMPLATES = new STGroupFile(UIRenderer.class.getResource("html.stg"));

	private Map<String, String> params;

	public static String render(UI page, Map<String, String> params)
	{
		return new UIRenderer().setParams(params).renderPage(page);
	}

	private UIRenderer setParams(Map<String, String> params)
	{
		this.params = params;
		return this;
	}

	private String renderPage(UI page)
	{
		final String content = this.render(page);

		final ST st = HTML_TEMPLATES.getInstanceOf("page");
		st.add("root", "root");
		st.add("content", content);
		return st.render();
	}

	private String render(UI ui)
	{
		final StringBuilder builder = new StringBuilder();
		this.render(ui, builder);
		return builder.toString();
	}

	private void render(UI ui, StringBuilder out)
	{
		final String description = ui.getDescription();
		if (description != null)
		{
			for (String descriptionItem : description.split("\n|\\|\\|"))
			{
				final String text = getDivContent(descriptionItem);
				final ST st = HTML_TEMPLATES.getInstanceOf("div");
				st.add("type", "line");
				st.add("content", text);
				out.append(st.render());
			}
		}

		for (UI kid : ui.getContent())
		{
			this.render(kid, out);
			out.append("\n");
		}
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
				text.append(st.render());
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
				text.append(st.render());
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
				text.append(st.render());
			}
			else if (elem.startsWith("cell"))
			{
				String[] tokens = elem.split(" ");
				// cell(text)
				ST st = HTML_TEMPLATES.getInstanceOf("cell");
				st.add("text", tokens[1]);
				text.append(st.render());
			}
			else if (elem.startsWith("---"))
			{
				text.append("<hr>\n");
			}
			else
			{
				text.append(elem).append('\n');
			}
		}
		return text.toString();
	}
}
