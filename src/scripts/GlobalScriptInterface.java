package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import jdk.nashorn.internal.runtime.ScriptObject;
import mcjs.Area;
import mcjs.CircularArea;
import mcjs.MainPlugin;
import mcjs.RectangularArea;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import scripts.BlockSelection.BlockInfo;
import events.AreaEnterListener;
import events.AreaExitListener;
import events.AreaMoveListener;
import events.BukkitListener;
import events.Event;
import events.EventType;
import events.GenericListener;

public class GlobalScriptInterface
{

	public static Server server;
	protected MainPlugin plugin;

	public static int getPlayerDirection(Player player)
	{
		double rotation = (player.getLocation().getYaw() - 90) % 360;
		if (rotation < 0)
			rotation += 360;
		if (rotation <= 0 && rotation < 45)
		{
			return 1;
		}
		else if (rotation <= 45 && rotation < 135)
		{
			return 0;
		}
		else if (rotation <= 135 && rotation < 225)
		{
			return 3;
		}
		else if (rotation <= 225 && rotation < 315)
		{
			return 2;
		}
		else
		{
			return 1;
		}
	}

	@SuppressWarnings("deprecation")
	public void setBlock(String worldName, int id, int x, int y, int z)
	{
		server.getWorld(worldName).getBlockAt(x, y, z).setType(Material.getMaterial(id));
	}

	public void setBlock(String worldName, String blockName, int x, int y, int z)
	{
		server.getWorld(worldName).getBlockAt(x, y, z).setType(Material.getMaterial(blockName));
	}

	public List<Object> createList()
	{
		return new ArrayList<Object>();
	}

	public BlockSelection selectAbsolute(String query)
	{
		BlockSelection bs = new BlockSelection(plugin, server.getWorld("world"));
		String[] parts = query.split(";");
		boolean rangeX, rangeY, rangeZ;
		int x_start = 0, x_end = 0, y_start = 0, y_end = 0, z_start = 0, z_end = 0;
		{
			String val = parts[0].trim();
			if (val.startsWith("-"))
			{
				if (val.contains("&"))
				{
					x_start = Integer.parseInt(val.substring(0, val.indexOf("&", 1)).trim());
					x_end = Integer.parseInt(val.substring(val.indexOf("&", 1) + 1).trim());
					rangeX = false;
				}
				else if (val.substring(1).contains("-"))
				{
					x_start = Integer.parseInt(val.substring(0, val.indexOf("-", 1)).trim());
					x_end = Integer.parseInt(val.substring(val.indexOf("-", 1) + 1).trim());
					rangeX = true;
				}
				else
				{
					x_start = x_end = Integer.parseInt(val);
					rangeX = true;
				}
			}
			else
			{
				if (val.contains("&"))
				{
					x_start = Integer.parseInt(val.substring(0, val.indexOf("&")).trim());
					x_end = Integer.parseInt(val.substring(val.indexOf("&") + 1).trim());
					rangeX = false;
				}
				else if (val.contains("-"))
				{
					x_start = Integer.parseInt(val.substring(0, val.indexOf("-")).trim());
					x_end = Integer.parseInt(val.substring(val.indexOf("-") + 1).trim());
					rangeX = true;
				}
				else
				{
					x_start = x_end = Integer.parseInt(val);
					rangeX = true;
				}
			}
			// System.out.println("xStart: " + x_start + ", xEnd: " + x_end);
		}
		{
			String val = parts[1].trim();
			if (val.startsWith("-"))
			{
				if (val.contains("&"))
				{
					y_start = Integer.parseInt(val.substring(0, val.indexOf("&", 1)).trim());
					y_end = Integer.parseInt(val.substring(val.indexOf("&", 1) + 1).trim());
					rangeY = false;
				}
				else if (val.substring(1).contains("-"))
				{
					y_start = Integer.parseInt(val.substring(0, val.indexOf("-", 1)).trim());
					y_end = Integer.parseInt(val.substring(val.indexOf("-", 1) + 1).trim());
					rangeY = true;
				}
				else
				{
					y_start = y_end = Integer.parseInt(val);
					rangeY = true;
				}
			}
			else
			{
				if (val.contains("&"))
				{
					y_start = Integer.parseInt(val.substring(0, val.indexOf("&", 1)).trim());
					y_end = Integer.parseInt(val.substring(val.indexOf("&", 1) + 1).trim());
					rangeY = false;
				}
				else if (val.contains("-"))
				{
					y_start = Integer.parseInt(val.substring(0, val.indexOf("-")).trim());
					y_end = Integer.parseInt(val.substring(val.indexOf("-") + 1).trim());
					rangeY = true;
				}
				else
				{
					y_start = y_end = Integer.parseInt(val);
					rangeY = true;
				}
			}
			// System.out.println("xStart: " + x_start + ", xEnd: " + x_end);
		}
		{
			String val = parts[2].trim();
			if (val.startsWith("-"))
			{
				if (val.contains("&"))
				{
					z_start = Integer.parseInt(val.substring(0, val.indexOf("&", 1)).trim());
					z_end = Integer.parseInt(val.substring(val.indexOf("&", 1) + 1).trim());
					rangeZ = false;
				}
				else if (val.substring(1).contains("-"))
				{
					z_start = Integer.parseInt(val.substring(0, val.indexOf("-", 1)).trim());
					z_end = Integer.parseInt(val.substring(val.indexOf("-", 1) + 1).trim());
					rangeZ = true;
				}
				else
				{
					z_start = z_end = Integer.parseInt(val);
					rangeZ = true;
				}
			}
			else
			{
				if (val.contains("&"))
				{
					z_start = Integer.parseInt(val.substring(0, val.indexOf("&", 1)).trim());
					z_end = Integer.parseInt(val.substring(val.indexOf("&", 1) + 1).trim());
					rangeZ = false;
				}
				else if (val.contains("-"))
				{
					z_start = Integer.parseInt(val.substring(0, val.indexOf("-")).trim());
					z_end = Integer.parseInt(val.substring(val.indexOf("-") + 1).trim());
					rangeZ = true;
				}
				else
				{
					z_start = z_end = Integer.parseInt(val);
					rangeZ = true;
				}
			}
			// System.out.println("xStart: " + x_start + ", xEnd: " + x_end);
		}
		if (x_start > x_end)
		{
			int tmp = x_end;
			x_end = x_start;
			x_start = tmp;
		}
		if (y_start > y_end)
		{
			int tmp = y_end;
			y_end = y_start;
			y_start = tmp;
		}
		if (z_start > z_end)
		{
			int tmp = z_end;
			z_end = z_start;
			z_start = tmp;
		}
		for (int i = x_start; i <= x_end; i++)
		{
			if (!rangeX && i != x_start && i != x_end)
			{
				continue;
			}
			for (int j = y_start; j <= y_end; j++)
			{
				if (!rangeY && j != y_start && j != y_end)
				{
					continue;
				}
				for (int k = z_start; k <= z_end; k++)
				{
					if (!rangeZ && k != z_start && k != z_end)
					{
						continue;
					}
					BlockInfo b = new BlockInfo();
					b.X = i;
					b.Y = j;
					b.Z = k;
					bs.addBlock(b);
				}
			}
		}
		return bs;
	}

	public void spawnEntity(Location location, EntityType type)
	{
		location.getWorld().spawnEntity(location, type);
	}

	public void spawnEntity(double x, double y, double z, EntityType type)
	{
		spawnEntity(new Location(server.getWorld("world"), x, y, z), type);
	}

	public void broadcast(String messsage, String broadcastChannel)
	{
		server.broadcast(messsage, broadcastChannel);
	}

	public void door(int x, int y, int z)
	{
	}

	public Location loc(double x, double y, double z)
	{
		return new Location(server.getWorld("world"), x, y, z);
	}

	public void block(String block)
	{
		plugin.setBlock(Material.getMaterial(block));
	}

	public void blockLight(String block)
	{
		plugin.setLight(Material.getMaterial(block));
	}

	/**
	 * @deprecated Use on("area.enter", function(event) {}) instead
	 * @param listener
	 *            The listener (Nashorn casts JS-methods to interfaces)
	 */
	@Deprecated
	public void addAreaEnterListener(AreaEnterListener listener)
	{
		BukkitListener.areaEnterListeners.add(listener);
	}

	/**
	 * @deprecated Use on("area.exit", function(event) {}) instead
	 * @param listener
	 *            The listener (Nashorn casts JS-methods to interfaces)
	 */
	@Deprecated
	public void addAreaExitListener(AreaExitListener listener)
	{
		BukkitListener.areaExitListeners.add(listener);
	}

	/**
	 * @deprecated Use on("area.move", function(event) {}) instead
	 * @param listener
	 *            The listener (Nashorn casts JS-methods to interfaces)
	 */
	@Deprecated
	public void addAreaMoveListener(AreaMoveListener listener)
	{
		BukkitListener.areaMoveListeners.add(listener);
	}

	public void addBlockClickListener(Location location, GenericListener listener)
	{
		if (!BukkitListener.blockClickListeners.containsKey(location))
		{
			BukkitListener.blockClickListeners.put(location, new HashSet<GenericListener>());
		}
		BukkitListener.blockClickListeners.get(location).add(listener);
	}

	public void addBlockClickListener(ScriptObject location, Consumer<Event> listener)
	{
		addBlockClickListener(new Location(server.getWorld("world"), location.getDouble("x"), location.getDouble("y"), location.getDouble("z")), new GenericListener(listener));
	}

	/**
	 * Registers an area to be used by the global event handler.
	 * 
	 * @param area
	 *            The Area
	 */
	public void registerArea(Area area)
	{
		BukkitListener.areas.add(area);
	}

	/**
	 * Registers a rectangular area. See {@link #registerArea(Area)} and
	 * {@link RectangularArea}
	 * 
	 * @param x
	 *            The X-Coordinate of the corner of the area
	 * @param y
	 *            The Y-Coordinate of the corner of the area
	 * @param z
	 *            The Z-Coordinate of the corner of the area
	 * @param width
	 *            The width of the area
	 * @param height
	 *            The height of the area
	 * @param depth
	 *            The depth of the area
	 */
	public void registerArea(int x, int y, int z, int width, int height, int depth)
	{
		registerArea(new RectangularArea(x, y, z, width, height, depth));
	}

	/**
	 * Registers a circular area. See {@link #registerArea(Area)} and
	 * {@link CircularArea}
	 * 
	 * @param x
	 *            The X-Coordinate of the center of the area
	 * @param y
	 *            The Y-Coordinate of the center of the area
	 * @param z
	 *            The Z-Coordinate of the center of the area
	 * @param radius
	 *            The radius of the sphere
	 */
	public void registerArea(int x, int y, int z, int radius)
	{
		registerArea(new CircularArea(x, y, z, radius));
	}

	/**
	 * Registers an area with either rectangular or circular shape, depending on
	 * the parameter. See {@link #registerArea(int, int, int, int)} and
	 * {@link #registerArea(int, int, int, int, int, int)}
	 * 
	 * @param object
	 *            The JS-Object holding the area information
	 */
	public void registerArea(ScriptObject object)
	{
		registerArea(Area.convertFromJSObject(object));
	}

	public void clear(String block)
	{
		plugin.setClear(Material.getMaterial(block));
	}

	public void log(String message)
	{
		System.out.println(message);
	}

	public void addAlias(String alias, String file)
	{
		File f = new File(file);
		if (!f.exists())
		{
			log("[§cWARN§r] Target for alias not existing, alias may not work!");
		}
		if (plugin.getAliases().containsKey(alias.toLowerCase()))
		{
			log("[§cWARN§r] Overwriting already existing alias!");
		}
		plugin.getAliases().put(alias, f);
		log("[INFO] Added alias <" + alias + "> for file <" + file);
		log("[INFO] State: " + (f.exists() ? " §aOK§r" : "§cFAIL§r"));
	}

	public void copyFile(String src, String dest)
	{
		File src_ = new File(src);
		File dest_ = new File(dest);
		if (src_.exists())
		{
			try
			{
				dest_.getParentFile().mkdirs();
				FileInputStream fis = new FileInputStream(src_);
				FileOutputStream fos = new FileOutputStream(dest_);
				byte tmp[] = new byte[1024];
				while (fis.read(tmp) > 0)
				{
					fos.write(tmp);
				}
				fis.close();
				fos.close();
				log("[ §aOK§r ] Succeded in Copying File!");
			} catch (Exception e)
			{
				log("[§cFAIL§r] " + e.getMessage());
			}
		}
		else
		{
			log("[§cFAIL§r] Source file not found!");
		}
	}

	public void broadcast(String message)
	{
		server.broadcastMessage(message);
	}

	public void spawnPlatform(String worldName, int x, int y, int z, String blockName)
	{
		for (int i = -2; i <= 2; i++)
		{
			for (int j = -2; j <= 2; j++)
			{
				setBlock(worldName, blockName, x + i, y, z + j);
			}
		}
	}

	public void runLater(Callable<? super Object> c, int delay)
	{
		new DelayedRunnable(c, delay, plugin).start();
	}

	public void log(Object o)
	{
		log(o == null ? "null" : o.toString() + " [" + o.getClass() + "]");
	}

	public void on(EventType type, Consumer<Event> listener)
	{
		BukkitListener.on(type, listener);
	}

	public void on(String typeDescriptor, Consumer<Event> listener)
	{
		on(EventType.byDescriptor(typeDescriptor), listener);
	}

	@SuppressWarnings("deprecation")
	public Player getPlayer(String name)
	{
		return server.getPlayer(name);
	}

	public GlobalScriptInterface(MainPlugin plugin)
	{
		this.plugin = plugin;
	}
}
