package org.fulib.mockups;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.fulib.scenarios.MockupTools;
import org.fulib.service.Service;
import org.junit.Test;
import org.fulib.FulibTools;

public class ModelTest  
{

   @Test
   public void scenarioModelClasses() { 
      Element e1 = new Element();
      Element e2 = new Element();
      e1.setId("e1");
      e2.setId("e2");
      e1.setText("E1");
      e2.setText("E2");
      Content c1 = new Content();
      Content c2 = new Content();
      c1.setId("c1");
      c2.setId("c2");
      c1.setDescription("C1");
      c2.setDescription("C2");
      List<Element> temp1 = new ArrayList<>(Arrays.asList(e1, e2, e1, e2));
      c1.withElements(temp1);
      c2.withElements(temp1);
      c1.setValue("v1");
      c2.setValue("v2");
      SpriteBoard b1 = new SpriteBoard();
      b1.setId("b1");
      Sprite s1 = new Sprite();
      Sprite s2 = new Sprite();
      s1.setId("s1");
      s2.setId("s2");
      s1.setIcon("car");
      s2.setIcon("home");
      s1.setX(4.0);
      s2.setX(16.0);
      s1.setY(12.0);
      s2.setY(6.0);
      Page p1 = new Page();
      Page p2 = new Page();
      p1.setId("p1");
      p2.setId("p2");
      p1.setDescription("P1");
      p2.setDescription("P2");
      // with content     c1, c2, c1, c2.
      WebApp app = new WebApp();
      app.setId("App");
      app.setDescription("Desc.");
      app.withContent(p1, p2);
      b1.withContent(s1, s2);
      p1.withContent(c1, "board");
      p2.withContent(c2, "board");
      WebApp shroomApp = new WebApp();
      shroomApp.setId("ShroomApp");
      shroomApp.setDescription("Shroom Wars");
      Page mainPage = new Page();
      mainPage.setId("mainPage");
      shroomApp.withContent(mainPage);
      SpriteBoard mainBoard = new SpriteBoard();
      mainBoard.setId("mainBoard");
      Content headLine = new Content();
      headLine.setId("headLine");
      headLine.setDescription("button move car");
      mainPage.withContent(headLine, mainBoard);
      Sprite car1 = new Sprite();
      Sprite home2 = new Sprite();
      car1.setId("car1");
      home2.setId("home2");
      car1.setIcon("car");
      home2.setIcon("home");
      car1.setX(4.0);
      home2.setX(16.0);
      car1.setY(12.0);
      home2.setY(6.0);
      mainBoard.withContent(car1, home2);
      FulibTools.objectDiagrams().dumpSVG("src/main/scenarios/org/fulib/mockups/shroomwars.svg", shroomApp);
      MockupTools.htmlTool().dump("src/main/scenarios/org/fulib/mockups/shroomwars.html", shroomApp);
   }

   public static void main(String[] args)
   {
      Service.main(new String[]{WebApp.class.getName()});
   }

}
