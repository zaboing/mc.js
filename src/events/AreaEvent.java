package events;

import mcjs.Area;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerMoveEvent;

public class AreaEvent extends Event
{
	public Area area;
	public Player player;
	public Cancellable base;

	public AreaEvent(Area area, PlayerMoveEvent event, EventType type)
	{
		super(type);
		this.area = area;
		this.player = event.getPlayer();
		this.base = event;
	}

	public void cancel()
	{
		base.setCancelled(true);
	}
}
