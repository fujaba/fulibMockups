package org.fulib.mockups;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class SpriteBoard  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public SpriteBoard setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final java.util.ArrayList<Sprite> EMPTY_content = new java.util.ArrayList<Sprite>()
   { @Override public boolean add(Sprite value){ throw new UnsupportedOperationException("No direct add! Use xy.withContent(obj)"); }};

   public static final String PROPERTY_content = "content";

   private java.util.ArrayList<Sprite> content = null;

   public java.util.ArrayList<Sprite> getContent()
   {
      if (this.content == null)
      {
         return EMPTY_content;
      }

      return this.content;
   }

   public SpriteBoard withContent(Object... value)
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
         else if (item instanceof Sprite)
         {
            if (this.content == null)
            {
               this.content = new java.util.ArrayList<Sprite>();
            }
            if ( ! this.content.contains(item))
            {
               this.content.add((Sprite)item);
               firePropertyChange("content", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }

   public SpriteBoard withoutContent(Object... value)
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
         else if (item instanceof Sprite)
         {
            if (this.content.contains(item))
            {
               this.content.remove((Sprite)item);
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


      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutContent(this.getContent().clone());


   }

}