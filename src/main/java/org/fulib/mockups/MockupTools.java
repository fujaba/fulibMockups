package org.fulib.mockups;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashMap;

public class MockupTools
{
   public static MockupTools htmlTool()
   {
      return new MockupTools();
   }

   public void dump(String file, Object page)
   {
      LinkedHashMap<String, String> params = new LinkedHashMap<>();

      Class<?> clazz = page.getClass();
      try {
         Method getParameters = clazz.getMethod("getParameters");
         Collection paramSet = (Collection) getParameters.invoke(page);
         if (paramSet != null) {
            for (Object param : paramSet) {
               clazz = param.getClass();
               Method getKey = clazz.getMethod("getKey");
               String key = (String) getKey.invoke(param);
               Method getValue = clazz.getMethod("getValue");
               String value = (String) getValue.invoke(param);
               params.put(key, value);
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }


      String html = FulibHtml.get(page, params);

      Path path = Paths.get(file);
      Path absolutePath = path.toAbsolutePath();
      Path dir = path.getParent();
      try {
         Files.createDirectories(dir);
         Files.write(path, html.getBytes());
      }
      catch (IOException e) {
         e.printStackTrace();
      }

      // produce svg, too
      Configuration.browserSize = "400x600";
      Configuration.reportsFolder = ".";
      Selenide.open("file:///" + absolutePath.toString());
      String result = Selenide.screenshot(file);

      System.out.println();

   }
}
