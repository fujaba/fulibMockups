package org.fulib.mockups;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.css.StyleElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.fulib.scenarios.MockupTools;
import org.fulib.service.Service;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MockupGUITests
{
   @Test
   public void testHtmlUnitOnSprites() throws IOException
   {
      // start server
      Service spriteService = new Service().setPort(6678).start(WebApp.class.getName());

      // use it
      WebClient webClient = new WebClient(BrowserVersion.CHROME);
      try {
         HtmlPage page = webClient.getPage("http://localhost:6678/");
         DomElement car1 = page.getElementById("car1");
         StyleElement left = car1.getStyleElement("left");
         String oldValue = left.getValue();
         DomNodeList<DomElement> buttonList = page.getElementsByTagName("button");
         HtmlButton moveButton = (HtmlButton) buttonList.get(0);
         moveButton.click();

         Thread.sleep(2000);

         HtmlPage resultPage = webClient.getPage("http://localhost:6678/");
         DomElement resultCar = resultPage.getElementById("car1");
         StyleElement newLeft = resultCar.getStyleElement("left");
         String newValue = newLeft.getValue();

         assertThat(oldValue, is("80px"));
         assertThat("100px".compareTo(newValue) < 0, is(true));
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      finally {
         // tear down
         webClient.close();
         spriteService.stop();
      }

   }



   @Test
   public void testMockupIsWellFormed() throws IOException
   {
      WebApp webApp = new WebApp();
      webApp.init();
      StringWriter stringWriter = new StringWriter();
      MockupTools.htmlTool().dumpScreen(stringWriter, webApp);
      String text = stringWriter.toString();

      assertThat(text, containsString("<html"));
      assertThat(text, containsString("</html>"));

      Files.write(Paths.get("./spritemockup.html"), text.getBytes());
      // System.out.println(text);
   }
}
