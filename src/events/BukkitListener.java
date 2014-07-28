package events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javascriptserver.Area;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BukkitListener implements Listener {

	public static final Set<Area> areas = new HashSet<Area>();

	public static final Set<AreaEnterListener> areaEnterListeners = new HashSet<AreaEnterListener>();
	public static final Set<AreaExitListener> areaExitListeners = new HashSet<AreaExitListener>();
	public static final Set<AreaMoveListener> areaMoveListeners = new HashSet<AreaMoveListener>();

	public static final Set<PlayerChatListener> playerChatListeners = new HashSet<PlayerChatListener>();

	public static final Map<EventType, Set<GenericListener>> genericListeners = new HashMap<EventType, Set<GenericListener>>();

	@EventHandler
	public void playerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();

		for (Area area : areas) {
			boolean isFrom = area.isInArea(from.getBlockX(), from.getBlockY(),
					from.getBlockZ());
			boolean isTo = area.isInArea(to.getBlockX(), to.getBlockY(),
					to.getBlockZ());
			if (!isFrom && !isTo) {
				// NOT IN AREA
				continue;
			}
			if (!isFrom && isTo) {
				final AreaEvent areaEvent = new AreaEvent(area, player,
						EventType.AREA_ENTER);
				areaEnterListeners.forEach(listener -> listener
						.onAreaEnter(areaEvent));
				notifyGenerics(areaEvent);
				continue;
			}
			if (isFrom && !isTo) {
				final AreaEvent areaEvent = new AreaEvent(area, player,
						EventType.AREA_EXIT);
				areaExitListeners.forEach(listener -> listener
						.onAreaExit(areaEvent));
				notifyGenerics(areaEvent);
				continue;
			}
			if (isFrom && isTo) {
				final AreaEvent areaEvent = new AreaEvent(area, player,
						EventType.AREA_MOVE);
				areaMoveListeners.forEach(listener -> listener
						.onMoveInArea(areaEvent));
				notifyGenerics(areaEvent);
				continue;
			}
		}
	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		final ChatEvent chatEvent = new ChatEvent(event);

		playerChatListeners.forEach(listener -> listener
				.onPlayerChat(chatEvent));
		notifyGenerics(chatEvent);
	}

	private void notifyGenerics(Event event) {
		genericListeners.get(event.eventType).forEach(
				listener -> listener.onEvent(event));
	}
	
	public static void cleanUp() {
		areas.clear();
		genericListeners.clear();
		areaEnterListeners.clear();
		areaExitListeners.clear();
		areaMoveListeners.clear();
		playerChatListeners.clear();
	}

	static {
		Arrays.asList(EventType.values())
				.stream()
				.forEach(
						type -> genericListeners.put(type,
								new HashSet<GenericListener>()));
	}
}
