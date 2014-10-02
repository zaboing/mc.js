package events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent extends Event
{

	public Player player;
	public String message;

	private AsyncPlayerChatEvent event;

	public ChatEvent(AsyncPlayerChatEvent event)
	{
		super(EventType.PLAYER_CHAT);
		this.player = event.getPlayer();
		this.message = event.getMessage();
		this.event = event;
	}

	public void cancel()
	{
		event.setCancelled(true);
	}
}
