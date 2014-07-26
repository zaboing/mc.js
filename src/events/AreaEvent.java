package events;

import javascriptserver.Area;

import org.bukkit.entity.Player;

public class AreaEvent {
	public Area area;
	public Player player;
	
	public AreaEvent(Area area, Player player) {
		this.area = area;
		this.player = player;
	}
}
