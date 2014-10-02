package events;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;

public class DamageBlockEvent extends Event
{
	private BlockDamageEvent event;

	public Player player;

	public DamageBlockEvent(BlockDamageEvent event)
	{
		super(EventType.BLOCK_DAMAGE);
		this.event = event;
		this.player = event.getPlayer();
	}

	public void cancel()
	{
		event.setCancelled(true);
	}
}
