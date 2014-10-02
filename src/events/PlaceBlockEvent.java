package events;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockEvent extends Event
{
	private BlockPlaceEvent event;

	public Player player;

	public PlaceBlockEvent(BlockPlaceEvent event)
	{
		super(EventType.BLOCK_DESTROY);
		this.event = event;
		this.player = event.getPlayer();
	}

	public void cancel()
	{
		event.setCancelled(true);
	}
}
