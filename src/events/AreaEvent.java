package events;

import javascriptserver.Area;

import org.bukkit.entity.Player;

public class AreaEvent extends Event {
	public Area area;
	public Player player;

	public AreaEvent(Area area, Player player, EventType type) {
		super(type);
		this.area = area;
		this.player = player;
	}
}
