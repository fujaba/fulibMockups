package org.fulib.mockups;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Content
{
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_DESCRIPTION = "description";
   public static final String PROPERTY_VALUE = "value";
   public static final String PROPERTY_ELEMENTS = "elements";
   private String id;
   private String description;
   private String value;
   private List<Element> elements;
   protected PropertyChangeSupport listeners;

   public String getId()
   {
      return this.id;
   }

   public Content setId(String value)
   {
      if (Objects.equals(value, this.id))
      {
         return this;
      }

      final String oldValue = this.id;
      this.id = value;
      this.firePropertyChange(PROPERTY_ID, oldValue, value);
      return this;
   }

   public String getDescription()
   {
      return this.description;
   }

   public Content setDescription(String value)
   {
      if (Objects.equals(value, this.description))
      {
         return this;
      }

      final String oldValue = this.description;
      this.description = value;
      this.firePropertyChange(PROPERTY_DESCRIPTION, oldValue, value);
      return this;
   }

   public String getValue()
   {
      return this.value;
   }

   public Content setValue(String value)
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

   public List<Element> getElements()
   {
      return this.elements != null ? Collections.unmodifiableList(this.elements) : Collections.emptyList();
   }

   public Content withElements(Element value)
   {
      if (this.elements == null)
      {
         this.elements = new ArrayList<>();
      }
      if (this.elements.add(value))
      {
         this.firePropertyChange(PROPERTY_ELEMENTS, null, value);
      }
      return this;
   }

   public Content withElements(Element... value)
   {
      for (final Element item : value)
      {
         this.withElements(item);
      }
      return this;
   }

   public Content withElements(Collection<? extends Element> value)
   {
      for (final Element item : value)
      {
         this.withElements(item);
      }
      return this;
   }

   public Content withoutElements(Element value)
   {
      if (this.elements != null && this.elements.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_ELEMENTS, value, null);
      }
      return this;
   }

   public Content withoutElements(Element... value)
   {
      for (final Element item : value)
      {
         this.withoutElements(item);
      }
      return this;
   }

   public Content withoutElements(Collection<? extends Element> value)
   {
      for (final Element item : value)
      {
         this.withoutElements(item);
      }
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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getId());
      result.append(' ').append(this.getDescription());
      result.append(' ').append(this.getValue());
      return result.substring(1);
   }
}
