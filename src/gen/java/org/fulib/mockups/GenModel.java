package org.fulib.mockups;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.Type;
import org.fulib.classmodel.Clazz;
import org.fulib.classmodel.CollectionType;

public class GenModel implements ClassModelDecorator
{
	@Override
	public void decorate(ClassModelManager mm)
	{
		final Clazz node = mm.haveClass("Node", c -> {
			c.attribute("id", Type.STRING);

			c.attribute("description", Type.STRING);
			c.attribute("value", Type.STRING); // for inputs
			c.attribute("action", Type.STRING); // for buttons
			c.attribute("icards", "Object").setCollectionType(CollectionType.ArrayList);
			c.attribute("tables", "Object").setCollectionType(CollectionType.ArrayList);
		});

		mm.haveClass("Content", node, c -> {});
		mm.haveClass("Line", node, c -> {});
		mm.haveClass("Section", node, c -> {});
		mm.haveClass("Page", node, c -> {});
		mm.haveClass("WebApp", node, c -> {});

		final Clazz element = mm.haveClass("Element", node, c -> {
			c.attribute("text", Type.STRING);
		});

		mm.haveRole(node, "content", Type.MANY, node, null, 0);
		mm.haveRole(node, "elements", Type.MANY, element, null, 0);

		final Clazz parameter = mm.haveClass("Parameter", c -> {
			c.attribute("key", Type.STRING);
			c.attribute("value", Type.STRING);
		});

		final Clazz ui = mm.haveClass("Ui", c -> {
			c.attribute("id", Type.STRING);
			c.attribute("description", Type.STRING);
		});

		mm.associate(parameter, "owner", Type.ONE, ui, "parameters", Type.MANY);

		mm.associate(ui, "parent", Type.ONE, ui, "content", Type.MANY);
	}
}
