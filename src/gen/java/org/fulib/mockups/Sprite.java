package org.fulib.mockups;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Sprite  
{

   public static final String PROPERTY_id = "id";

   private String id;

   public String getId()
   {
      return id;
   }

   public Sprite setId(String value)
   {
      if (value == null ? this.id != null : ! value.equals(this.id))
      {
         String oldValue = this.id;
         this.id = value;
         firePropertyChange("id", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_icon = "icon";

   private String icon;

   public String getIcon()
   {
      return icon;
   }

   public Sprite setIcon(String value)
   {
      if (value == null ? this.icon != null : ! value.equals(this.icon))
      {
         String oldValue = this.icon;
         this.icon = value;
         firePropertyChange("icon", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_x = "x";

   private int x;

   public int getX()
   {
      return x;
   }

   public Sprite setX(int value)
   {
      if (value != this.x)
      {
         int oldValue = this.x;
         this.x = value;
         firePropertyChange("x", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_y = "y";

   private int y;

   public int getY()
   {
      return y;
   }

   public Sprite setY(int value)
   {
      if (value != this.y)
      {
         int oldValue = this.y;
         this.y = value;
         firePropertyChange("y", oldValue, value);
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
      result.append(" ").append(this.getIcon());


      return result.substring(1);
   }

}