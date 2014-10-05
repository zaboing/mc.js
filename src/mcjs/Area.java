package mcjs;

import java.io.Serializable;
import java.util.function.Consumer;

import jdk.nashorn.internal.runtime.ScriptObject;
import events.AreaListener;
import events.BukkitListener;
import events.Event;
import events.EventType;

public abstract class Area implements Serializable
{
	private static final long serialVersionUID = -6116480712664633245L;

	public abstract boolean isInArea(int x, int y, int z);

	public boolean equals(Object o)
	{
		if (o instanceof ScriptObject)
		{
			return isArea(convertFromJSObject((ScriptObject) o));
		}
		if (o instanceof Area)
		{
			return isArea((Area) o);
		}
		return false;
	}

	public abstract boolean isArea(Area area);

	public Area append(Area area)
	{
		CompositeArea comp = new CompositeArea();
		comp.areas.add(this);
		comp.areas.add(area);
		return comp;
	}

	public static Area convertFromJSObject(ScriptObject object)
	{
		Area area;
		if (object.has("radius"))
		{
			area = new CircularArea(object.getInt("x"), object.getInt("y"), object.getInt("z"), object.getInt("radius"));
		}
		else if (object.has("rad_sq"))
		{
			area = new CircularArea(object.getInt("x"), object.getInt("y"), object.getInt("z"), (int) Math.sqrt(object.getInt("radius")));
		}
		else if (object.has("width") && object.has("height") && object.has("depth"))
		{
			area = new RectangularArea(object.getInt("x"), object.getInt("y"), object.getInt("z"), object.getInt("width"), object.getInt("height"), object.getInt("depth"));
		}
		else
		{
			return null;
		}
		return area;
	}

	/**
	 * Convenience method. Returns the same as
	 * {@link #convertFromJSObject(ScriptObject)}
	 * 
	 * @param object
	 *            The Javascript object to be converted to an area.
	 * @return The converted area
	 */
	public static Area convert(ScriptObject object)
	{
		return convertFromJSObject(object);
	}

	public void on(String eventTypeDescriptor, Consumer<Event> listener)
	{
		on(EventType.byDescriptor(eventTypeDescriptor), listener);
	}

	public void on(EventType eventType, Consumer<Event> listener)
	{
		if (eventType.descriptor.startsWith("area"))
		{
			BukkitListener.genericListeners.get(eventType).add(new AreaListener(listener, this));
		}
	}
}
