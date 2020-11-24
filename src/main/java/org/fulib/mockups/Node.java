package org.fulib.mockups;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Node
{
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_DESCRIPTION = "description";
   public static final String PROPERTY_VALUE = "value";
   public static final String PROPERTY_ACTION = "action";
   public static final String PROPERTY_ICARDS = "icards";
   public static final String PROPERTY_TABLES = "tables";
   public static final String PROPERTY_CONTENT = "content";
   public static final String PROPERTY_ELEMENTS = "elements";

   private String id;
   private String description;
   private String value;
   private String action;
   private List<Object> icards;
   private List<Object> tables;

   protected PropertyChangeSupport listeners;
   private List<Node> content;
   private List<Element> elements;

   public String getId()
   {
      return this.id;
   }

   public Node setId(String value)
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

   public Node setDescription(String value)
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

   public Node setValue(String value)
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

   public String getAction()
   {
      return this.action;
   }

   public Node setAction(String value)
   {
      if (Objects.equals(value, this.action))
      {
         return this;
      }

      final String oldValue = this.action;
      this.action = value;
      this.firePropertyChange(PROPERTY_ACTION, oldValue, value);
      return this;
   }

   public List<Object> getIcards()
   {
      return this.icards != null ? Collections.unmodifiableList(this.icards) : Collections.emptyList();
   }

   public Node withIcards(Object value)
   {
      if (this.icards == null)
      {
         this.icards = new ArrayList<>();
      }
      if (this.icards.add(value))
      {
         this.firePropertyChange(PROPERTY_ICARDS, null, value);
      }
      return this;
   }

   public Node withIcards(Object... value)
   {
      for (final Object item : value)
      {
         this.withIcards(item);
      }
      return this;
   }

   public Node withIcards(Collection<? extends Object> value)
   {
      for (final Object item : value)
      {
         this.withIcards(item);
      }
      return this;
   }

   public Node withoutIcards(Object value)
   {
      if (this.icards != null && this.icards.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_ICARDS, value, null);
      }
      return this;
   }

   public Node withoutIcards(Object... value)
   {
      for (final Object item : value)
      {
         this.withoutIcards(item);
      }
      return this;
   }

   public Node withoutIcards(Collection<? extends Object> value)
   {
      for (final Object item : value)
      {
         this.withoutIcards(item);
      }
      return this;
   }

   public List<Object> getTables()
   {
      return this.tables != null ? Collections.unmodifiableList(this.tables) : Collections.emptyList();
   }

   public Node withTables(Object value)
   {
      if (this.tables == null)
      {
         this.tables = new ArrayList<>();
      }
      if (this.tables.add(value))
      {
         this.firePropertyChange(PROPERTY_TABLES, null, value);
      }
      return this;
   }

   public Node withTables(Object... value)
   {
      for (final Object item : value)
      {
         this.withTables(item);
      }
      return this;
   }

   public Node withTables(Collection<? extends Object> value)
   {
      for (final Object item : value)
      {
         this.withTables(item);
      }
      return this;
   }

   public Node withoutTables(Object value)
   {
      if (this.tables != null && this.tables.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_TABLES, value, null);
      }
      return this;
   }

   public Node withoutTables(Object... value)
   {
      for (final Object item : value)
      {
         this.withoutTables(item);
      }
      return this;
   }

   public Node withoutTables(Collection<? extends Object> value)
   {
      for (final Object item : value)
      {
         this.withoutTables(item);
      }
      return this;
   }

   public List<Node> getContent()
   {
      return this.content != null ? Collections.unmodifiableList(this.content) : Collections.emptyList();
   }

   public Node withContent(Node value)
   {
      if (this.content == null)
      {
         this.content = new ArrayList<>();
      }
      if (!this.content.contains(value))
      {
         this.content.add(value);
         this.firePropertyChange(PROPERTY_CONTENT, null, value);
      }
      return this;
   }

   public Node withContent(Node... value)
   {
      for (final Node item : value)
      {
         this.withContent(item);
      }
      return this;
   }

   public Node withContent(Collection<? extends Node> value)
   {
      for (final Node item : value)
      {
         this.withContent(item);
      }
      return this;
   }

   public Node withoutContent(Node value)
   {
      if (this.content != null && this.content.remove(value))
      {
         this.firePropertyChange(PROPERTY_CONTENT, value, null);
      }
      return this;
   }

   public Node withoutContent(Node... value)
   {
      for (final Node item : value)
      {
         this.withoutContent(item);
      }
      return this;
   }

   public Node withoutContent(Collection<? extends Node> value)
   {
      for (final Node item : value)
      {
         this.withoutContent(item);
      }
      return this;
   }

   public List<Element> getElements()
   {
      return this.elements != null ? Collections.unmodifiableList(this.elements) : Collections.emptyList();
   }

   public Node withElements(Element value)
   {
      if (this.elements == null)
      {
         this.elements = new ArrayList<>();
      }
      if (!this.elements.contains(value))
      {
         this.elements.add(value);
         this.firePropertyChange(PROPERTY_ELEMENTS, null, value);
      }
      return this;
   }

   public Node withElements(Element... value)
   {
      for (final Element item : value)
      {
         this.withElements(item);
      }
      return this;
   }

   public Node withElements(Collection<? extends Element> value)
   {
      for (final Element item : value)
      {
         this.withElements(item);
      }
      return this;
   }

   public Node withoutElements(Element value)
   {
      if (this.elements != null && this.elements.remove(value))
      {
         this.firePropertyChange(PROPERTY_ELEMENTS, value, null);
      }
      return this;
   }

   public Node withoutElements(Element... value)
   {
      for (final Element item : value)
      {
         this.withoutElements(item);
      }
      return this;
   }

   public Node withoutElements(Collection<? extends Element> value)
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
      result.append(' ').append(this.getAction());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutContent(new ArrayList<>(this.getContent()));
      this.withoutElements(new ArrayList<>(this.getElements()));
   }
}
