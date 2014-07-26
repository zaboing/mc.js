package javascriptserver;

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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import scripts.ScriptInterface;
import events.BukkitListener;

public class MainPlugin extends JavaPlugin {

	private ScriptEngineManager factory;
	private ScriptEngine javaScript;
	private HashMap<String, File> aliases;

	private Material block = Material.STONE;
	private Material light = Material.GLOWSTONE;
	private Material clear = Material.AIR;

	public Material getBlock() {
		return block;
	}

	public void setBlock(Material block) {
		this.block = block;
	}

	public Material getLight() {
		return light;
	}

	public void setLight(Material light) {
		this.light = light;
	}

	public Material getClear() {
		return clear;
	}

	public void setClear(Material clear) {
		this.clear = clear;
	}

	@SuppressWarnings("unchecked")
	public void onEnable() {
		ScriptInterface.server = getServer();
		factory = new ScriptEngineManager(getClass().getClassLoader());
		javaScript = factory.getEngineByName("JavaScript");
		javaScript.put("$", new ScriptInterface(this));
		// NASHORN FIX FOR RHINO METHODS
		execute("load(\"nashorn:mozilla_compat.js\");");
		execute("importClass(Packages.org.bukkit.Server);");
		execute("$.broadcast('�8�lEnabled JavaScript.�l');");
		File db = new File("plugins/jsserver.obj");
		if (!db.exists())
			aliases = new HashMap<>();
		else {
			try {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(db));
				aliases = (HashMap<String, File>) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				execute("$.broadcast(Error while loading Alias DB");
			}

		}
		getServer().getPluginManager().registerEvents(new BukkitListener(),
				this);
	}

	public void onDisable() {
		BukkitListener.areas.clear();
		BukkitListener.genericListeners.clear();
		BukkitListener.areaEnterListeners.clear();
		BukkitListener.areaExitListeners.clear();
		BukkitListener.areaMoveListeners.clear();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream("plugins/jsserver.obj"));
			oos.writeObject(aliases);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			execute("$.broadcast(Error while saving Alias DB");
		}
		execute("$.broadcast('�8�lDisabled JavaScript.�l');");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("javascript")) {
			List<String> realArgs = new ArrayList<String>();

			String current = new String();

			boolean inString = false;

			for (int i = 0; i < args.length; i++) {
				for (char c : args[i].toCharArray()) {
					if (c == '"') {
						inString = !inString;
					} else {
						current += c;
					}
				}
				if (!inString) {
					realArgs.add(current);
					current = new String();
				} else {
					current += " ";
				}
			}
			if (!current.trim().isEmpty()) {
				realArgs.add(current);
			}

			if (realArgs.isEmpty()) {
				return false;
			}

			StringBuilder input = new StringBuilder();
			BufferedWriter output = null;
			boolean consoleOutput = false;
			boolean fileOutput = false;

			for (int i = 0; i < realArgs.size(); i++) {
				String arg = realArgs.get(i);
				if (arg.startsWith("-")) {
					switch (arg.substring(1)) {
					case "d":
						input.append(realArgs.get(i + 1));
						break;

					case "f":
						try (BufferedReader reader = new BufferedReader(
								new FileReader(realArgs.get(i + 1)))) {
							char[] cbuf = new char[1024];
							int len;
							while ((len = reader.read(cbuf)) != -1) {
								input.append(cbuf, 0, len);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						fileOutput = true;
						break;

					case "o":
						try {
							output = new BufferedWriter(new FileWriter(
									realArgs.get(i + 1)));
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;

					case "c":
						consoleOutput = true;
						break;
					case "a":
						String file,
						alias;
						File f = null;
						file = realArgs.get(i + 2);
						alias = realArgs.get(i + 1);
						f = new File(file);
						if (!f.exists()) {
							sender.sendMessage("[§cWARN§r] Target for alias not existing, alias may not work!");
						}
						if (aliases.containsKey(alias.toLowerCase())) {
							sender.sendMessage("[§cWARN§r] Overwriting already existing alias!");
						}
						aliases.put(alias, f);
						sender.sendMessage("[INFO] Added alias <" + alias
								+ "> for file <" + file + ">");
						sender.sendMessage("[INFO] State: "
								+ (f.exists() ? " §aOK§r" : "§cFAIL§r"));
						break;
					}
				}
			}
			Object ret = execute(input.toString(), sender);
			if (ret != null) {
				if (consoleOutput) {
					sender.sendMessage(ret.toString());
				}
				if (fileOutput) {
					try {
						output.write(ret.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return true;
		} else if (cmd.getName().toLowerCase().startsWith("js")) {
			if (args.length >= 1) {
				String name = args[0].toLowerCase();
				sender.sendMessage("[INFO] Looking up " + name);
				if (aliases.containsKey(name)) {
					File f = aliases.get(name);
					if (!f.exists()) {
						sender.sendMessage("[§cWARN§r] Alias references not existing file!");
						return false;
					} else {
						StringBuilder input = new StringBuilder();
						try (BufferedReader reader = new BufferedReader(
								new FileReader(f))) {
							char[] cbuf = new char[1024];
							int len;
							while ((len = reader.read(cbuf)) != -1) {
								input.append(cbuf, 0, len);
							}
						} catch (IOException e) {
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
			for (String s : aliases.keySet()) {
				sender.sendMessage((aliases.get(s).exists() ? "[ §aOK§r ] "
						: "[§cFAIL§r] ")
						+ s
						+ " - "
						+ aliases.get(s).getName());
			}
		}
		return false;
	}

	public Object execute(String script) {
		try {
			return javaScript.eval(script);
		} catch (ScriptException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object execute(String script, CommandSender sender) {
		try {
			// Put individual context for every script
			ScriptContext context = new SimpleScriptContext();
			Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
			ScriptInterface in = new ScriptInterface(this);
			in.setSender(sender);
			if (sender instanceof Player) {
				in.player = (Player) sender;
			}
			bindings.put("$", in);
			Object tmp = javaScript.eval(script, context);
			return tmp;
		} catch (ScriptException e) {
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<String, File> getAliases() {
		return aliases;
	}

}
