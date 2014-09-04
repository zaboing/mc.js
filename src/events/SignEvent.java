package events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public class SignEvent extends Event {

	private SignChangeEvent event;
	
	public String[] lines;
	public Block block;
	public Player player;
	
	public SignEvent(SignChangeEvent event) {
		super(EventType.SIGN_CHANGE);
		this.event = event;
		this.block = event.getBlock();
		this.lines = event.getLines();
		this.player = event.getPlayer();
	}

	public void cancel() {
		event.setCancelled(true);
	}
	
	public void setLine(int index, String line) {
		event.setLine(index, line);
	}
}
