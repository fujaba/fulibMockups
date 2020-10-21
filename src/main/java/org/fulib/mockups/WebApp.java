package org.fulib.mockups;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class WebApp
{
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_DESCRIPTION = "description";
   public static final String PROPERTY_CONTENT = "content";
   private String id;
   private String description;
   private List<Page> content;
   protected PropertyChangeSupport listeners;

   public String getId()
   {
      return this.id;
   }

   public WebApp setId(String value)
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

   public WebApp setDescription(String value)
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

   public List<Page> getContent()
   {
      return this.content != null ? Collections.unmodifiableList(this.content) : Collections.emptyList();
   }

   public WebApp withContent(Page value)
   {
      if (this.content == null)
      {
         this.content = new ArrayList<>();
      }
      if (this.content.add(value))
      {
         this.firePropertyChange(PROPERTY_CONTENT, null, value);
      }
      return this;
   }

   public WebApp withContent(Page... value)
   {
      for (final Page item : value)
      {
         this.withContent(item);
      }
      return this;
   }

   public WebApp withContent(Collection<? extends Page> value)
   {
      for (final Page item : value)
      {
         this.withContent(item);
      }
      return this;
   }

   public WebApp withoutContent(Page value)
   {
      if (this.content != null && this.content.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_CONTENT, value, null);
      }
      return this;
   }

   public WebApp withoutContent(Page... value)
   {
      for (final Page item : value)
      {
         this.withoutContent(item);
      }
      return this;
   }

   public WebApp withoutContent(Collection<? extends Page> value)
   {
      for (final Page item : value)
      {
         this.withoutContent(item);
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
      return result.substring(1);
   }
}
