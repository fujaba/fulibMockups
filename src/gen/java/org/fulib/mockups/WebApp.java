package org.fulib.mockups;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class WebApp  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public WebApp setId(String value)
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

   public WebApp setDescription(String value)
   {
      if (value == null ? this.description != null : ! value.equals(this.description))
      {
         String oldValue = this.description;
         this.description = value;
         firePropertyChange("description", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<Page> EMPTY_content = new java.util.ArrayList<Page>()
   { @Override public boolean add(Page value){ throw new UnsupportedOperationException("No direct add! Use xy.withContent(obj)"); }};

   public static final String PROPERTY_content = "content";

   private java.util.ArrayList<Page> content = null;

   public java.util.ArrayList<Page> getContent()
   {
      if (this.content == null)
      {
         return EMPTY_content;
      }

      return this.content;
   }

   public WebApp withContent(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withContent(i);
            }
         }
         else if (item instanceof Page)
         {
            if (this.content == null)
            {
               this.content = new java.util.ArrayList<Page>();
            }
            if ( ! this.content.contains(item))
            {
               this.content.add((Page)item);
               firePropertyChange("content", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public WebApp withoutContent(Object... value)
   {
      if (this.content == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutContent(i);
            }
         }
         else if (item instanceof Page)
         {
            if (this.content.contains(item))
            {
               this.content.remove((Page)item);
               firePropertyChange("content", item, null);
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


      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutContent(this.getContent().clone());


   }

}