package events;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDieEvent extends Event
{
	public final PlayerDeathEvent event;

	public Player player;

	public PlayerDieEvent(PlayerDeathEvent event)
	{
		super(EventType.PLAYER_DIE);
		this.event = event;
		player = event.getEntity();
	}
}
