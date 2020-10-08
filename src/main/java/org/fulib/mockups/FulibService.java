package org.fulib.mockups;

import org.stringtemplate.v4.STGroupFile;
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
   private ExecutorService executor;
   private Service spark;
   private STGroupFile group;
   private Object app;

   public FulibService setApp(Object app)
   {
      this.app = app;
      return this;
   }

   public FulibService start() {
      executor = Executors.newSingleThreadExecutor();
      spark = Service.ignite();
      spark.port(45678);
      spark.get("/:cmd", (request, response) -> executor.submit( () -> getPage(request, response)).get());
      System.out.println("Group Service is running .... ");
      return this;
   }

   private String getPage(Request req, Response res)
   {
      try {
         String cmd = req.params(":cmd");
         String page = "";

         if (cmd != null) {
            LinkedHashMap<String, String> paramMap = new LinkedHashMap<>();
            if (cmd.indexOf('$') > 0) {
               String dollarParam = cmd.substring(cmd.indexOf('$') + 1);
               paramMap.put("$", dollarParam);
               cmd = cmd.substring(0, cmd.indexOf('$'));
            }
            for (String param : req.queryParams()) {
               String value = req.queryParams(param);
               paramMap.put(param, value);
            }

            Class<?> appClass = app.getClass();
            Method method = appClass.getMethod(cmd, Map.class);
            page = (String) method.invoke(this.app, paramMap);
         }

         System.out.println("get page: " + page);
         return page;
      }
      catch (Exception e) {
         e.printStackTrace();
         return e.getMessage();
      }
   }


}
