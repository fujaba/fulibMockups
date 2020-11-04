package org.fulib.mockups;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class MockupTool
{
	public void dump(String file, Ui page)
	{
		final String htmlFile;
		final String pngFile;
		if (file.endsWith(".html.png"))
		{
			htmlFile = file.substring(0, file.length() - ".png".length());
			pngFile = file;
		}
		else if (file.endsWith(".png"))
		{
			htmlFile = null;
			pngFile = file;
		}
		else if (file.endsWith(".html"))
		{
			htmlFile = file;
			pngFile = null;
		}
		else
		{
			throw new IllegalArgumentException(
				"invalid file name '" + file + "' - extension must be .html.png, .png or .html");
		}

		final Map<String, String> params = new LinkedHashMap<>();
		for (Parameter param : page.getParameters())
		{
			params.put(param.getKey(), param.getValue());
		}

		final String html = UiRenderer.render(page, params);

		if (htmlFile != null)
		{
			writeHtmlFile(htmlFile, html);
		}

		if (pngFile != null)
		{
			writePngFile(pngFile, html);
		}
	}

	private static void writeHtmlFile(String htmlFile, String html)
	{
		final Path path = Paths.get(htmlFile);
		final Path parent = path.getParent();
		try
		{
			Files.createDirectories(parent);

			final Path styleCss = parent.resolve("style.css");
			if (Files.notExists(styleCss))
			{
				try (final InputStream input = MockupTool.class.getResourceAsStream("style.css"))
				{
					Files.copy(input, styleCss);
				}
			}

			Files.write(path, html.getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void writePngFile(String pngFile, String html)
	{
		Configuration.browserSize = "640x480";
		Configuration.headless = true;
		Configuration.savePageSource = false;

		final String dataUrl = "data:text/html," + html;

		Selenide.open(dataUrl);

		final File screenshotFile = Selenide.screenshot(OutputType.FILE);
		final File dest = new File(pngFile);

		dest.delete();
		dest.getParentFile().mkdirs();
		screenshotFile.renameTo(dest);
	}
}
