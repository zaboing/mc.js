package events;

import java.util.function.Consumer;

import mcjs.Area;

public class AreaListener extends GenericListener
{

	private Area area;

	public AreaListener(Consumer<Event> callback, Area area)
	{
		super(callback);
		this.area = area;
	}

	public void onEvent(Event event)
	{
		if (!(event instanceof AreaEvent))
		{
			return;
		}
		AreaEvent areaEvent = (AreaEvent) event;
		if (areaEvent.area.equals(area))
		{
			super.onEvent(event);
		}
	}
}
