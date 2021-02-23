package org.fulib.mockups;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

@SuppressWarnings("unused")
public class GenModel implements ClassModelDecorator
{
	class Node
	{
		String id;
		String description;
		String value;
		String action;
		List<Object> icards;
		List<Object> tables;

		@Link
		List<Node> content;

		@Link
		List<Element> elements;
	}

	class Content extends Node {}

	class Line extends Node {}

	class Section extends Node {}

	class Page extends Node {}

	class WebApp extends Node {}

	class Element extends Node
	{
		String text;
	}

	class Parameter
	{
		String key;
		String value;

		@Link("parameters")
		UI owner;
	}

	class UI
	{
		String id;
		String description;

		@Link("owner")
		List<Parameter> parameters;

		@Link("content")
		UI parent;

		@Link("parent")
		List<UI> content;
	}

	@Override
	public void decorate(ClassModelManager mm)
	{
		mm.haveNestedClasses(GenModel.class);
	}
}
