package events;

public abstract class Event
{
	public final EventType eventType;

	public Event(EventType eventType)
	{
		this.eventType = eventType;
	}
}
