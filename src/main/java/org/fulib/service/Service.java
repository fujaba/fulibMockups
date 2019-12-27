package org.fulib.service;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.fulib.scenarios.MockupTools;
import org.fulib.yaml.StrUtil;
import org.fulib.yaml.YamlIdMap;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service {
   private HttpServer server;
   private ExecutorService executor;
   private Object webApp;
   private int port;

   public Service setPort(int port) {
      this.port = port;
      return this;
   }

   public static void main(String[] args) {
      String webAppClassName = "webapp.WebApp";
      int portParameter = 6677;
      if (args != null && args.length > 0) {
         webAppClassName = args[0];
         if (args.length > 1) {
            portParameter = Integer.parseInt(args[1]);
         }
      }

      new Service().setPort(portParameter)
            .start(webAppClassName);
   }

   public Service start(String webAppClassName) {
      try {
         Class<?> webAppClass = this.getClass().getClassLoader().loadClass(webAppClassName);
         webApp = webAppClass.getConstructor().newInstance();
         Method init = webAppClass.getMethod("init");
         init.invoke(webApp);

         idMap = new YamlIdMap(webApp.getClass().getPackage().getName());

         server = HttpServer.create(new InetSocketAddress(this.port), 0);
         executor = Executors.newSingleThreadExecutor();
         server.setExecutor(executor);

         HttpContext doContext = server.createContext("/");
         doContext.setHandler(x -> handleRoot(x));

         HttpContext initContext = server.createContext("/index.html");
         initContext.setHandler(x -> handleRoot(x));

         HttpContext cmdContext = server.createContext("/cmd");
         cmdContext.setHandler(x -> handleCmd(x));

         server.start();
         System.out.println(String.format("Server is listening on port %s", this.port) );
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return this;
   }

   public Service stop() {
      this.server.stop(1);
      return this;
   }

   private YamlIdMap idMap;

   private void handleCmd(HttpExchange x)
   {
      String cmd = getBody(x);
      ArrayList<String> params = getParamsList(cmd);

      String packageName = webApp.getClass().getPackage().getName();

      // call builder method generically.
      if (params.size() > 2 || ! params.get(0).equals(params.get(params.size()-1))) {
         callButtonAction(params, packageName);
      }

      callNewPage(params.get(params.size()-1));

      handleRoot(x);

   }

   private void callNewPage(String param)
   {
      try
      {
         param = StrUtil.downFirstChar(param);
         Method method = webApp.getClass().getMethod(param);
         method.invoke(webApp);
      }
      catch (Exception e)
      {
         System.err.println("could not find page method " + param);
         // e.printStackTrace();
      }
   }

   private void callButtonAction(ArrayList<String> params, String packageName)
   {
      String cmdName = params.get(0);

      Method[] methods = webApp.getClass().getMethods();

      for (Method method : methods)
      {
         String methodName = method.getName();
         if (method.getName().startsWith(cmdName)){
            // handle params
            Class<?>[] parameterTypes = method.getParameterTypes();
            ArrayList<Object> actualParamsList = new ArrayList<>();
            int i = 0;
            for (Class<?> parameterType : parameterTypes)
            {
               i++;
               String paramTypeName = parameterType.getName();
               if (paramTypeName.startsWith(packageName)) {
                  // it is a model class
                  String simpleName = parameterType.getSimpleName();
                  String objId = params.get(i);
                  String findMethodName = "find" + simpleName;
                  Object object = null;
                  try
                  {
                     Method findMethod = webApp.getClass().getMethod(findMethodName, new Class[]{String.class});
                     object = findMethod.invoke(webApp, new Object[]{objId});
                  }
                  catch (Exception e)
                  {
                     object = idMap.decode("- " + objId + ": " + simpleName + " id: " + objId + " name: " + objId);
                  }
                  actualParamsList.add(object);
               }
               else if (paramTypeName.equals("int")){
                  Integer value = Integer.valueOf(params.get(i));
                  actualParamsList.add(value);
               }
               else if (paramTypeName.equals("java.lang.String")){
                  actualParamsList.add(params.get(i));
               }
               else {
                  System.out.println("Do not know how to handle param of type " + paramTypeName);
               }
            }

            try
            {
               method.invoke(webApp, actualParamsList.toArray());
               return;
            }
            catch (IllegalAccessException e)
            {
               e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
               e.printStackTrace();
            }
         }
      }

      System.err.println("could not find action method " + cmdName);
   }


   private ArrayList<String> getParamsList(String cmd)
   {
      ArrayList<String> cmdMap = new ArrayList<>();
      if (cmd.startsWith("{")) {
         String content = cmd.substring(1, cmd.lastIndexOf('}'));
         String[] split = content.split(",");
         for (String pair : split)
         {
            String[] keyValue = pair.split(":");
            String key = keyValue[0];
            key = key.substring(1, key.length()-1);
            String value = keyValue[1];
            value = value.substring(1, value.length()-1);
            cmdMap.add(value);
         }
      }
      return cmdMap;
   }


   private LinkedHashMap<String, String> getParamsMap(String cmd)
   {
      LinkedHashMap<String, String> cmdMap = new LinkedHashMap<>();
      if (cmd.startsWith("{")) {
         String content = cmd.substring(1, cmd.lastIndexOf('}'));
         String[] split = content.split(",");
         for (String pair : split)
         {
            String[] keyValue = pair.split(":");
            String key = keyValue[0];
            key = key.substring(1, key.length()-1);
            String value = keyValue[1];
            value = value.substring(1, value.length()-1);
            cmdMap.put(key, value);
         }
      }
      return cmdMap;
   }


   public static String getBody(HttpExchange exchange)
   {
      try
      {
         URI requestURI = exchange.getRequestURI();
         InputStream requestBody = exchange.getRequestBody();
         BufferedReader buf = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8));
         StringBuilder text = new StringBuilder();

         while (true) {
            String line = buf.readLine();
            if (line == null) {
               break;
            }

            text.append(line).append("\n");
         }

         String yaml = text.toString();
         return yaml;
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      return null;
   }

   private void writeAnswer(HttpExchange x, String page)
   {
      try
      {
         byte[] bytes = page.getBytes();
         x.sendResponseHeaders(200, bytes.length);
         OutputStream responseBody = x.getResponseBody();
         responseBody.write(bytes);
         responseBody.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }


   private void handleRoot(HttpExchange x)
   {
      try
      {
         StringWriter stringWriter = new StringWriter();
         MockupTools.htmlTool().dumpScreen(stringWriter, webApp);
         String page = stringWriter.toString();
         writeAnswer(x, page);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
