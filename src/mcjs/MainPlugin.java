package mcjs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import scripts.GlobalScriptInterface;
import scripts.LocalScriptInterface;
import wrappers.AreaWrapper;
import events.BukkitListener;
import events.Event;
import events.EventType;
import events.SignEvent;

public class MainPlugin extends JavaPlugin
{

	private static final String ALIAS_DB_NAME = "aliases.ser";
	private ScriptEngineManager factory;
	private ScriptEngine javaScript;
	private HashMap<String, File> aliases;

	private ScriptContext globalContext;

	private Material block = Material.STONE;
	private Material light = Material.GLOWSTONE;
	private Material clear = Material.AIR;

	public Material getBlock()
	{
		return block;
	}

	public void setBlock(Material block)
	{
		this.block = block;
	}

	public Material getLight()
	{
		return light;
	}

	public void setLight(Material light)
	{
		this.light = light;
	}

	public Material getClear()
	{
		return clear;
	}

	public void setClear(Material clear)
	{
		this.clear = clear;
	}

	@SuppressWarnings("unchecked")
	public void onEnable()
	{
		GlobalScriptInterface.server = getServer();
		globalContext = new SimpleScriptContext();
		factory = new ScriptEngineManager(getClass().getClassLoader());
		javaScript = factory.getEngineByName("JavaScript");
		Bindings globalBindings = globalContext.getBindings(ScriptContext.ENGINE_SCOPE);
		globalBindings.put("$", new GlobalScriptInterface(this));
		globalBindings.put("server", getServer());
		globalBindings.put("Area", new AreaWrapper());
		// NASHORN FIX FOR RHINO METHODS
		execute("load(\"nashorn:mozilla_compat.js\");");
		execute("importClass(Packages.org.bukkit.Server);");
		execute("$.broadcast('§8§lEnabled JavaScript.§r');");
		File db = new File(this.getDataFolder(), ALIAS_DB_NAME);
		if (!db.exists())
		{
			aliases = new HashMap<>();
		}
		else
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(db));
				aliases = (HashMap<String, File>) ois.readObject();
				ois.close();
				if (aliases == null)
				{
					aliases = new HashMap<>();
				}
			} catch (IOException | ClassNotFoundException e)
			{
				aliases = new HashMap<>();
			}

		}
		getServer().getPluginManager().registerEvents(new BukkitListener(), this);
		BukkitListener.on(EventType.SIGN_CHANGE, this::signChanged);
	}

	private void signChanged(Event event)
	{
		if (event instanceof SignEvent)
		{
			SignEvent signEvent = (SignEvent) event;
			String text = String.join("", signEvent.lines).trim();
			if (text.startsWith("/js "))
			{
				List<String> log = new ArrayList<String>();
				Object ret = executeCommand(getRealArgs(text.substring(4).trim().split(" ")), signEvent.player, log);
				log.forEach(line -> signEvent.player.sendMessage(line));
				signEvent.setLine(0, "");
				signEvent.setLine(1, String.valueOf(ret));
				signEvent.setLine(2, "");
				signEvent.setLine(3, "");
			}
		}
	}

	public void onDisable()
	{
		this.getDataFolder().mkdirs();
		getServer().resetRecipes();
		BukkitListener.pluginDisabled();
		BukkitListener.cleanUp();
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(this.getDataFolder(), ALIAS_DB_NAME)));
			oos.writeObject(aliases);
			oos.flush();
			oos.close();
		} catch (IOException e)
		{
			execute("$.broadcast('Error while saving Alias DB');");
			getServer().broadcastMessage(e.toString());
			e.printStackTrace();
			// execute("$.broadcast('" + e + "');");
		}
		aliases = null;
		execute("$.broadcast('§8§lDisabled JavaScript.§r');");
	}

	private List<String> getRealArgs(String... args)
	{
		List<String> realArgs = new ArrayList<String>();

		String current = new String();

		boolean inString = false;

		for (int i = 0; i < args.length; i++)
		{
			for (char c : args[i].toCharArray())
			{
				if (c == '"')
				{
					inString = !inString;
				}
				else
				{
					current += c;
				}
			}
			if (!inString)
			{
				realArgs.add(current);
				current = new String();
			}
			else
			{
				current += " ";
			}
		}
		if (!current.trim().isEmpty())
		{
			realArgs.add(current);
		}
		return realArgs;
	}

	/**
	 * 
	 * @param realArgs
	 *            The arguments for the /javascript command
	 * @return The returned value
	 */
	private Object executeCommand(List<String> realArgs, CommandSender sender, List<String> log)
	{
		StringBuilder input = new StringBuilder();
		BufferedWriter output = null;
		boolean consoleOutput = false;
		boolean fileOutput = false;

		boolean global = false;

		for (int i = 0; i < realArgs.size(); i++)
		{
			String arg = realArgs.get(i);
			if (arg.startsWith("-"))
			{
				switch (arg.substring(1))
				{
					case "d":
						if (realArgs.size() <= i + 1)
						{
							return false;
						}
						else
						{
							input.append(realArgs.get(i + 1));
						}
						break;

					case "f":
					{
						if (realArgs.size() <= i + 1)
						{
							return false;
						}
						File f = new File(realArgs.get(i + 1));
						if (!f.isFile())
						{
							sender.sendMessage("FILE NOT FOUND: " + f);
							return "error";
						}
						try
						{
							List<String> lines = Files.readAllLines(f.toPath());
							for (String line : lines)
							{
								input.append(line);
								input.append('\n');
							}
						} catch (IOException e)
						{
							e.printStackTrace();
						}
						break;
					}
					case "o":
						if (realArgs.size() <= i + 1)
						{
							return false;
						}
						try
						{
							output = new BufferedWriter(new FileWriter(realArgs.get(i + 1)));
						} catch (IOException e)
						{
							e.printStackTrace();
						}
						fileOutput = true;
						break;

					case "c":
						consoleOutput = true;
						break;
					case "a":
					{
						if (realArgs.size() <= i + 2)
						{
							return false;
						}
						String file, alias;
						File f = null;
						file = realArgs.get(i + 2);
						alias = realArgs.get(i + 1);
						f = new File(file);
						if (!f.exists())
						{
							log.add("[Â§cWARNÂ§r] Target for alias not existing, alias may not work!");
						}
						if (aliases.containsKey(alias.toLowerCase()))
						{
							log.add("[Â§cWARNÂ§r] Overwriting already existing alias!");
						}
						aliases.put(alias, f);
						log.add("[INFO] Added alias <" + alias + "> for file <" + file + ">");
						log.add("[INFO] State: " + (f.exists() ? " Â§aOKÂ§r" : "Â§cFAILÂ§r"));
						break;
					}
					case "-global":
						global = true;
						break;
				}
			}
		}
		Object ret;
		if (global)
		{
			ret = execute(input.toString());
		}
		else
		{
			ret = execute(input.toString(), sender);
		}

		if (ret != null)
		{
			if (consoleOutput)
			{
				log.add(ret.toString());
			}
			if (fileOutput)
			{
				try
				{
					output.write(ret.toString());
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		if (output != null)
		{
			try
			{
				output.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return ret;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("javascript"))
		{
			List<String> realArgs = getRealArgs(args);

			if (realArgs.isEmpty())
			{
				return false;
			}

			List<String> log = new ArrayList<String>();
			executeCommand(realArgs, sender, log);
			log.forEach(line -> sender.sendMessage(line));

			return true;
		}
		else if (cmd.getName().toLowerCase().startsWith("js"))
		{
			if (args.length >= 1)
			{
				String name = args[0].toLowerCase();
				sender.sendMessage("[INFO] Looking up " + name);
				if (aliases.containsKey(name))
				{
					File f = aliases.get(name);
					if (!f.exists())
					{
						sender.sendMessage("[§cWARN§r] Alias references not existing file!");
						return false;
					}
					else
					{
						StringBuilder input = new StringBuilder();
						try (BufferedReader reader = new BufferedReader(new FileReader(f)))
						{
							char[] cbuf = new char[1024];
							int len;
							while ((len = reader.read(cbuf)) != -1)
							{
								input.append(cbuf, 0, len);
							}
						} catch (IOException e)
						{
							e.printStackTrace();
						}
						Object ret = execute(input.toString(), sender);
						if (ret != null)
							sender.sendMessage(ret.toString());
						return true;
					}
				}
			}
			sender.sendMessage("Available Aliases:");
			for (String s : aliases.keySet())
			{
				sender.sendMessage((aliases.get(s).exists() ? "[ §aOK§r ] " : "[§cFAIL§r] ") + s + " - " + aliases.get(s).getName());
			}
		}
		return false;
	}

	public Object execute(String script)
	{
		try
		{
			// Use global context
			return javaScript.eval(script, globalContext);
		} catch (ScriptException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Object execute(String script, CommandSender sender)
	{
		try
		{
			// Put individual context for every script
			ScriptContext context = new SimpleScriptContext();
			Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
			bindings.putAll(globalContext.getBindings(ScriptContext.ENGINE_SCOPE));
			LocalScriptInterface in = new LocalScriptInterface(this);
			in.setSender(sender);
			bindings.put("$", in);
			Object tmp = javaScript.eval(script, context);
			return tmp;
		} catch (ScriptException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<String, File> getAliases()
	{
		return aliases;
	}

}
