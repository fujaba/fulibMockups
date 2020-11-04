package org.fulib.mockups;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Ui
{
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_DESCRIPTION = "description";
   public static final String PROPERTY_PARAMETERS = "parameters";
   public static final String PROPERTY_PARENT = "parent";
   public static final String PROPERTY_CONTENT = "content";
   private String id;
   private String description;
   private List<Parameter> parameters;
   private Ui parent;
   private List<Ui> content;
   protected PropertyChangeSupport listeners;

   public String getId()
   {
      return this.id;
   }

   public Ui setId(String value)
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

   public Ui setDescription(String value)
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

   public List<Parameter> getParameters()
   {
      return this.parameters != null ? Collections.unmodifiableList(this.parameters) : Collections.emptyList();
   }

   public Ui withParameters(Parameter value)
   {
      if (this.parameters == null)
      {
         this.parameters = new ArrayList<>();
      }
      if (!this.parameters.contains(value))
      {
         this.parameters.add(value);
         value.setOwner(this);
         this.firePropertyChange(PROPERTY_PARAMETERS, null, value);
      }
      return this;
   }

   public Ui withParameters(Parameter... value)
   {
      for (final Parameter item : value)
      {
         this.withParameters(item);
      }
      return this;
   }

   public Ui withParameters(Collection<? extends Parameter> value)
   {
      for (final Parameter item : value)
      {
         this.withParameters(item);
      }
      return this;
   }

   public Ui withoutParameters(Parameter value)
   {
      if (this.parameters != null && this.parameters.remove(value))
      {
         value.setOwner(null);
         this.firePropertyChange(PROPERTY_PARAMETERS, value, null);
      }
      return this;
   }

   public Ui withoutParameters(Parameter... value)
   {
      for (final Parameter item : value)
      {
         this.withoutParameters(item);
      }
      return this;
   }

   public Ui withoutParameters(Collection<? extends Parameter> value)
   {
      for (final Parameter item : value)
      {
         this.withoutParameters(item);
      }
      return this;
   }

   public Ui getParent()
   {
      return this.parent;
   }

   public Ui setParent(Ui value)
   {
      if (this.parent == value)
      {
         return this;
      }

      final Ui oldValue = this.parent;
      if (this.parent != null)
      {
         this.parent = null;
         oldValue.withoutContent(this);
      }
      this.parent = value;
      if (value != null)
      {
         value.withContent(this);
      }
      this.firePropertyChange(PROPERTY_PARENT, oldValue, value);
      return this;
   }

   public List<Ui> getContent()
   {
      return this.content != null ? Collections.unmodifiableList(this.content) : Collections.emptyList();
   }

   public Ui withContent(Ui value)
   {
      if (this.content == null)
      {
         this.content = new ArrayList<>();
      }
      if (!this.content.contains(value))
      {
         this.content.add(value);
         value.setParent(this);
         this.firePropertyChange(PROPERTY_CONTENT, null, value);
      }
      return this;
   }

   public Ui withContent(Ui... value)
   {
      for (final Ui item : value)
      {
         this.withContent(item);
      }
      return this;
   }

   public Ui withContent(Collection<? extends Ui> value)
   {
      for (final Ui item : value)
      {
         this.withContent(item);
      }
      return this;
   }

   public Ui withoutContent(Ui value)
   {
      if (this.content != null && this.content.remove(value))
      {
         value.setParent(null);
         this.firePropertyChange(PROPERTY_CONTENT, value, null);
      }
      return this;
   }

   public Ui withoutContent(Ui... value)
   {
      for (final Ui item : value)
      {
         this.withoutContent(item);
      }
      return this;
   }

   public Ui withoutContent(Collection<? extends Ui> value)
   {
      for (final Ui item : value)
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

   public void removeYou()
   {
      this.withoutParameters(new ArrayList<>(this.getParameters()));
      this.setParent(null);
      this.withoutContent(new ArrayList<>(this.getContent()));
   }
}
