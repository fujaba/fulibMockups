package org.fulib.mockups;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class Element extends Node
{
   public static final String PROPERTY_TEXT = "text";
   private String text;

   public String getText()
   {
      return this.text;
   }

   public Element setText(String value)
   {
      if (Objects.equals(value, this.text))
      {
         return this;
      }

      final String oldValue = this.text;
      this.text = value;
      this.firePropertyChange(PROPERTY_TEXT, oldValue, value);
      return this;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getText());
      return result.toString();
   }
}
