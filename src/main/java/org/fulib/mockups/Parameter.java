package org.fulib.mockups;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class Parameter
{
   public static final String PROPERTY_KEY = "key";
   public static final String PROPERTY_VALUE = "value";
   public static final String PROPERTY_OWNER = "owner";
   private String key;
   private String value;
   private UI owner;
   protected PropertyChangeSupport listeners;

   public String getKey()
   {
      return this.key;
   }

   public Parameter setKey(String value)
   {
      if (Objects.equals(value, this.key))
      {
         return this;
      }

      final String oldValue = this.key;
      this.key = value;
      this.firePropertyChange(PROPERTY_KEY, oldValue, value);
      return this;
   }

   public String getValue()
   {
      return this.value;
   }

   public Parameter setValue(String value)
   {
      if (Objects.equals(value, this.value))
      {
         return this;
      }

      final String oldValue = this.value;
      this.value = value;
      this.firePropertyChange(PROPERTY_VALUE, oldValue, value);
      return this;
   }

   public UI getOwner()
   {
      return this.owner;
   }

   public Parameter setOwner(UI value)
   {
      if (this.owner == value)
      {
         return this;
      }

      final UI oldValue = this.owner;
      if (this.owner != null)
      {
         this.owner = null;
         oldValue.withoutParameters(this);
      }
      this.owner = value;
      if (value != null)
      {
         value.withParameters(this);
      }
      this.firePropertyChange(PROPERTY_OWNER, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getKey());
      result.append(' ').append(this.getValue());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setOwner(null);
   }
}
