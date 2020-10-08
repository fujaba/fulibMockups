package org.fulib.mockups;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MockupTools
{
	public static MockupTools htmlTool()
	{
		return new MockupTools();
	}

	public void dump(String file, Object page)
	{
		final Map<String, String> params = new LinkedHashMap<>();

		try
		{
			final Method getParameters = page.getClass().getMethod("getParameters");
			final Collection<?> paramSet = (Collection<?>) getParameters.invoke(page);
			if (paramSet != null)
			{
				for (Object param : paramSet)
				{
					final Class<?> clazz = param.getClass();
					final String key = (String) clazz.getMethod("getKey").invoke(param);
					final String value = (String) clazz.getMethod("getValue").invoke(param);
					params.put(key, value);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
