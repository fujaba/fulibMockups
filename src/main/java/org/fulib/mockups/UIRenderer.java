package org.fulib.mockups;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UIRenderer
{
	private static final STGroupFile HTML_TEMPLATES = new STGroupFile(UIRenderer.class.getResource("html.stg"));

	private static final Map<String, Renderer> RENDERERS;

	interface Renderer
	{
		String render(String[] args, Map<String, String> params);
	}

	static
	{
		Map<String, Renderer> renderers = new HashMap<>();
		renderers.put("button", UIRenderer::renderButton);
		renderers.put("hidden", UIRenderer::renderHidden);
		renderers.put("input", UIRenderer::renderInput);
		renderers.put("cell", UIRenderer::renderCell);
		renderers.put("---", UIRenderer::renderLine);
		RENDERERS = Collections.unmodifiableMap(renderers);
	}

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
		st.add("root", page.getId());
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
			final String[] args = elem.trim().split(" ");
			if (args.length == 0)
			{
				continue;
			}

			final Renderer renderer = RENDERERS.get(args[0]);
			if (renderer != null)
			{
				text.append(renderer.render(args, this.params));
			}
			else
			{
				text.append(elem).append('\n');
			}
		}
		return text.toString();
	}

	private static String renderButton(String[] args, Map<String, String> params)
	{
		String buttonText = args[1];
		String target = args.length > 2 ? args[2] : buttonText;
		// button(text, target)
		ST st = HTML_TEMPLATES.getInstanceOf("button");
		st.add("text", buttonText);
		st.add("target", target);
		return st.render();
	}

	private static String renderHidden(String[] args, Map<String, String> params)
	{
		final String name = args[1];
		final String value = args.length > 2 ? args[2] : params.get(name);

		// hidden(name, value)
		ST st = HTML_TEMPLATES.getInstanceOf("hidden");
		st.add("name", name);
		st.add("value", value);
		return st.render();
	}

	private static String renderInput(String[] args, Map<String, String> params)
	{
		String inputName = args[1];
		String prompt = args[2];
		String value = params.get(inputName);
		// input(id, prompt, value)
		ST st = HTML_TEMPLATES.getInstanceOf("input");
		st.add("id", inputName);
		st.add("prompt", prompt);
		st.add("value", value);
		return st.render();
	}

	private static String renderCell(String[] args, Map<String, String> params)
	{
		// cell(text)
		ST st = HTML_TEMPLATES.getInstanceOf("cell");
		st.add("text", args[1]);
		return st.render();
	}

	private static String renderLine(String[] args, Map<String, String> params)
	{
		return "<hr/>\n";
	}
}
