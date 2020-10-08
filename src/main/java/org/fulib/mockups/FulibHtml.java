package org.fulib.mockups;

import org.fulib.yaml.Reflector;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.Map;

public class FulibHtml
{
   private STGroupFile group;
   private Map<String, String> params;

   public static String get(Object page, Map<String, String> params)
   {
      return new FulibHtml().setParams(params).html(page);
   }

   private FulibHtml setParams(Map<String, String> params)
   {
      this.params = params;
      return this;
   }

   private String html(Object page)
   {
      group = new STGroupFile(this.getClass().getResource("html.stg"), "UTF-8", '{', '}');

      String content = getContent(page);

      String html = getPage(content);

      return html;
   }

   private String getPage(String content)
   {
      ST st = group.getInstanceOf("page");
      st.add("root", "root");
      st.add("content", content);
      String html = st.render();
      return html;
   }

   private String getBlock(Object currentObject)
   {
      String content = getContent(currentObject);

      Reflector reflector = new Reflector().setClazz(currentObject.getClass());

      Object obj = reflector.getValue(currentObject, "description");
      String description = "";
      if (obj != null) {
         description = obj.toString();
      }

      String[] split = description.split("\n|\\|\\|");
      String html = "";
      for (String div : split) {
         String text = getDivContent(div);
         ST st = group.getInstanceOf("div");
         st.add("type", "line");
         st.add("offset", "0");
         st.add("content", text);
         html += st.render();
      }

      return html;
   }

   private String getDivContent(String description)
   {
      String[] split = description.split("\\|");

      String text = "";
      for (String elem : split) {
         elem = elem.trim();
         if (elem.startsWith("button")) {
            String[] words = elem.split(" ");
            String buttonText = words[1];
            String target = buttonText;
            if (words.length > 2) {
               target = words[2];
            }
            // button(text, target)
            ST st = group.getInstanceOf("button");
            st.add("text", buttonText);
            st.add("target", target);
            text += st.render() + " ";
         }
         else if (elem.startsWith("hidden")) {
            String[] tokens = elem.split(" ");
            String value = params.get(tokens[1]);
            if (tokens.length > 2) {
               value = tokens[2];
            }
            // hidden(name, value)
            ST st = group.getInstanceOf("hidden");
            st.add("name", tokens[1]);
            st.add("value", value);
            text += st.render() + " ";

         }
         else if (elem.startsWith("input")) {
            String[] tokens = elem.split(" ");
            String inputName = tokens[1];
            int pos = elem.indexOf(inputName);
            String prompt = elem.substring(pos + inputName.length()).trim();
            String value = params.get(inputName);
            // input(id, prompt, value)
            ST st = group.getInstanceOf("input");
            st.add("id", inputName);
            st.add("prompt", prompt);
            st.add("value", value);
            text += st.render() + " ";
         }
         else if (elem.startsWith("cell")) {
            String[] tokens = elem.split(" ");
            // cell(text)
            ST st = group.getInstanceOf("cell");
            st.add("text", tokens[1]);
            text += st.render() + " ";

         }
         else if (elem.startsWith("---")) {
            text += "<hr></hr>";

         }
         else {
            text += elem + " ";
         }
      }
      return text;
   }

   private String getContent(Object page)
   {
      Reflector reflector = new Reflector().setClazz(page.getClass());
      Object currentObject = reflector.getValue(page, "content");
      StringBuilder buf = new StringBuilder();
      if (currentObject != null && currentObject instanceof Collection) {
         Collection kids = (Collection) currentObject;
         for (Object kid : kids) {
            String kidHtml = getBlock(kid);
            buf.append(kidHtml).append("\n");
         }
      }

      String content = buf.toString();
      return content;
   }
}
