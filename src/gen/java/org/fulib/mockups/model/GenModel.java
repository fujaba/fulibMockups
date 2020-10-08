package org.fulib.mockups.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.Type;
import org.fulib.classmodel.Clazz;

public class GenModel implements ClassModelDecorator
{
	@Override
	public void decorate(ClassModelManager cmm)
	{
		final Clazz parameter = cmm.haveClass("Parameter", c -> {
			c.attribute("key", Type.STRING);
			c.attribute("value", Type.STRING);
		});

		final Clazz ui = cmm.haveClass("Ui", c -> {
			c.attribute("id", Type.STRING);
			c.attribute("description", Type.STRING);
		});

		cmm.associate(parameter, "owner", Type.ONE, ui, "parameters", Type.MANY);

		cmm.associate(ui, "parent", Type.ONE, ui, "content", Type.MANY);
	}
}
