package org.fulib.mockups;

import spark.Request;
import spark.Response;
import spark.Service;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FulibService
{
	private Object app;

	public FulibService setApp(Object app)
	{
		this.app = app;
		return this;
	}

	public FulibService start()
	{
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Service spark = Service.ignite();
		spark.port(45678);
		spark.get("/:cmd", (request, response) -> executor.submit(() -> getPage(request, response)).get());
		return this;
	}

	private String getPage(Request req, Response res)
	{
		try
		{
			String cmd = req.params(":cmd");
			if (cmd == null)
			{
				return "";
			}

			final Map<String, String> params = new LinkedHashMap<>();
			final int dollarIndex = cmd.indexOf('$');
			if (dollarIndex > 0)
			{
				final String dollarParam = cmd.substring(dollarIndex + 1);
				params.put("$", dollarParam);
				cmd = cmd.substring(0, dollarIndex);
			}
			for (String param : req.queryParams())
			{
				final String value = req.queryParams(param);
				params.put(param, value);
			}

			final Class<?> appClass = app.getClass();
			final Method method = appClass.getMethod(cmd, Map.class);
			return (String) method.invoke(this.app, params);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
