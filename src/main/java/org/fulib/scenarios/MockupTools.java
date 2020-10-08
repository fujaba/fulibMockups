package org.fulib.scenarios;

import org.fulib.mockups.FulibMockups;
import org.fulib.mockups.HtmlTool;

/**
 * @deprecated since 0.3; use {@link HtmlTool} instead
 */
@Deprecated
public class MockupTools extends HtmlTool
{
	// =============== Static Methods ===============

	/**
	 * @deprecated since 0.3; use {@link FulibMockups#htmlTool()} instead
	 */
	@Deprecated
	public static MockupTools htmlTool()
	{
		return new MockupTools();
	}
}
