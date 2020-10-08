package org.fulib.mockups;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.fulib.mockups.model.Parameter;
import org.fulib.mockups.model.Ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class MockupTools
{
	public static MockupTools htmlTool()
	{
		return new MockupTools();
	}

	public void dump(String file, Ui page)
	{
		final Map<String, String> params = new LinkedHashMap<>();
		for (Parameter param : page.getParameters())
		{
			params.put(param.getKey(), param.getValue());
		}

		final String html = FulibHtml.get(page, params);

		final Path path = Paths.get(file);
		try
		{
			Files.createDirectories(path.getParent());
			Files.write(path, html.getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// TODO produce svg, too
		Configuration.browserSize = "400x600";
		Configuration.reportsFolder = ".";
		try
		{
			Selenide.open(path.toUri().toURL());
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		Selenide.screenshot(file);
	}
}
