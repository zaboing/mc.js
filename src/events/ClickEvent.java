package events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickEvent extends Event {
	public PlayerInteractEvent event;
	
	public Player player;
	
	public ClickEvent(PlayerInteractEvent event) {
		super(EventType.PLAYER_CLICK);
		this.event = event;
		this.player = event.getPlayer();
	}
}
