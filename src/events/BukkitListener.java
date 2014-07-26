package events;

import java.util.HashSet;
import java.util.Set;

import javascriptserver.Area;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BukkitListener implements Listener {
	
	public static final Set<Area> areas = new HashSet<Area>();
	
	public static final Set<AreaEnterListener> areaEnterListeners = new HashSet<AreaEnterListener>();
	public static final Set<AreaExitListener> areaExitListeners = new HashSet<AreaExitListener>();
	
	@EventHandler
	public void playerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();
		
		for (Area area : areas) {
			boolean isFrom = area.isInArea(from.getBlockX(), from.getBlockY(), from.getBlockZ());
			boolean isTo = area.isInArea(to.getBlockX(), to.getBlockY(), to.getBlockZ());
			if (!isFrom && !isTo) {
				// NOT IN AREA
				continue;
			}
			if (!isFrom && isTo) {
				for (AreaEnterListener listener: areaEnterListeners) {
					listener.onAreaEnter(new AreaEvent(area, player));
				}
				continue;
			}
			if (isFrom && !isTo) {
				for (AreaExitListener listener : areaExitListeners) {
					listener.onAreaExit(new AreaEvent(area, player));
				}
				continue;
			}
			if (isFrom && isTo) {
				// TODO: AREA MOVE
				continue;
			}
		}
	}
}
