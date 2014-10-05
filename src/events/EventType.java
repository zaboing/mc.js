package events;

import java.util.Arrays;

public enum EventType
{
	AREA_ENTER("area.enter"), AREA_EXIT("area.exit"), AREA_MOVE("area.move"), PLAYER_CHAT("player.chat"), PLAYER_CLICK("player.click"), PLAYER_DIE("player.die"), SIGN_CHANGE("sign.change"), BLOCK_DAMAGE("block.damage"), BLOCK_DESTROY(
			"block.destroy"), BLOCK_PLACE("block.place"), PLUGIN_ENABLED("plugin.enabled"), PLUGIN_DISABLED("plugin.disabled");

	public String descriptor;

	EventType(String descriptor)
	{
		this.descriptor = descriptor;
	}

	public static EventType byDescriptor(String descriptor)
	{
		return Arrays.asList(EventType.values()).stream().filter(type -> type.descriptor.equals(descriptor)).findFirst().get();
	}
}
