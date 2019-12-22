package org.fulib.mockups;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Content  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public Content setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_description = "description";

   private String description;

   public String getDescription()
   {
      return description;
   }

   public Content setDescription(String value)
   {
      if (value == null ? this.description != null : ! value.equals(this.description))
      {
         String oldValue = this.description;
         this.description = value;
         firePropertyChange("description", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_value = "value";

   private String value;

   public String getValue()
   {
      return value;
   }

   public Content setValue(String value)
   {
      if (value == null ? this.value != null : ! value.equals(this.value))
      {
         String oldValue = this.value;
         this.value = value;
         firePropertyChange("value", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<Element> EMPTY_elements = new java.util.ArrayList<Element>()
   { @Override public boolean add(Element value){ throw new UnsupportedOperationException("No direct add! Use xy.withElements(obj)"); }};

   public static final String PROPERTY_elements = "elements";

   private java.util.ArrayList<Element> elements = null;

   public java.util.ArrayList<Element> getElements()
   {
      if (this.elements == null)
      {
         return EMPTY_elements;
      }

      return this.elements;
   }

   public Content withElements(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withElements(i);
            }
         }
         else if (item instanceof Element)
         {
            if (this.elements == null)
            {
               this.elements = new java.util.ArrayList<Element>();
            }
            if ( ! this.elements.contains(item))
            {
               this.elements.add((Element)item);
               firePropertyChange("elements", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public Content withoutElements(Object... value)
   {
      if (this.elements == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutElements(i);
            }
         }
         else if (item instanceof Element)
         {
            if (this.elements.contains(item))
            {
               this.elements.remove((Element)item);
               firePropertyChange("elements", item, null);
            }
         }
      }
      return this;
   }

   protected PropertyChangeSupport listeners = null;

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (listeners != null)
      {
         listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName,PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getId());
      result.append(" ").append(this.getDescription());
      result.append(" ").append(this.getValue());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutElements(this.getElements().clone());


   }

}