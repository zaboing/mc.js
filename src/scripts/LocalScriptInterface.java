package scripts;

import mcjs.MainPlugin;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import scripts.BlockSelection.BlockInfo;

public class LocalScriptInterface extends GlobalScriptInterface
{

	public CommandSender sender;
	public Player player;

	public LocalScriptInterface(MainPlugin plugin)
	{
		super(plugin);
	}

	public BlockSelection selectRelative(String query)
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
		int d = getPlayerDirection(player);
		if (d % 3 == 0)
		{
			x_start *= -1;
			x_end *= -1;
		}
		if (d % 2 == 1)
		{
			int tmp = x_start;
			x_start = z_start;
			z_start = tmp;
			tmp = x_end;
			x_end = z_end;
			z_end = tmp;
		}
		x_start += player.getLocation().getX();
		x_end += player.getLocation().getX();
		y_start += player.getLocation().getY();
		y_end += player.getLocation().getY();
		z_start += player.getLocation().getZ();
		z_end += player.getLocation().getZ();
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

	public void log(String message)
	{
		sender.sendMessage(message);
	}

	public void move(String transform)
	{
		String[] parts = transform.split(";");
		int x, y, z;
		x = Integer.parseInt(parts[0]);
		y = Integer.parseInt(parts[1]);
		z = Integer.parseInt(parts[2]);
		Location loc = new Location(player.getWorld(), player.getLocation().getX() + x, player.getLocation().getY() + y, player.getLocation().getZ() + z);
		player.teleport(loc);
	}

	public void move(String transform, String msg)
	{
		String[] parts = transform.split(";");
		int x, y, z;
		x = Integer.parseInt(parts[0]);
		y = Integer.parseInt(parts[1]);
		z = Integer.parseInt(parts[2]);
		Location loc = new Location(player.getWorld(), player.getLocation().getX() + x, player.getLocation().getY() + y, player.getLocation().getZ() + z);
		player.teleport(loc);
		player.sendMessage(msg);
	}

	public void setSender(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			player = (Player) sender;
		}
		else
		{
			player = null;
		}
		this.sender = sender;
	}
}
