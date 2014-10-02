package events;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class DestroyBlockEvent extends Event
{
	private BlockBreakEvent event;

	public Player player;

	public DestroyBlockEvent(BlockBreakEvent event)
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
