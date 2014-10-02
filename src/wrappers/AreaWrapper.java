package wrappers;

import jdk.nashorn.internal.runtime.ScriptObject;
import mcjs.Area;

public class AreaWrapper
{
	public Area convert(ScriptObject object)
	{
		return Area.convert(object);
	}
}
