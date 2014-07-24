package scripts;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;

import javascriptserver.MainPlugin;

public class BlockSelection {

	private ArrayList<Block> blocks;
	private MainPlugin plugin;
	private World world;
	
	public BlockSelection(MainPlugin plugin, World world) {
		this.plugin = plugin;
		this.world = world;
		this.blocks = new ArrayList<>();
	}
	
	public void fill() {
		for (Block block : blocks) {
			world.getBlockAt(block.X, block.Y, block.Z).setType(plugin.getBlock());
		}
	}
	
	public void fill(String material) {
		for (Block block : blocks) {
			world.getBlockAt(block.X, block.Y, block.Z).setType(Material.getMaterial(material));
		}
	}
	
	public void fillLight() {
		for (Block block : blocks) {
			world.getBlockAt(block.X, block.Y, block.Z).setType(plugin.getLight());
		}
	}
	
	public void clear() {
		for (Block block : blocks) {
			world.getBlockAt(block.X, block.Y, block.Z).setType(plugin.getClear());
		}
	}
	
	public void addBlock(Block b) {
		blocks.add(b);
	}
	
	public static class Block {
		public int X, Y, Z;

		@Override
		public String toString() {
			return "Block [X=" + X + ", Y=" + Y + ", Z=" + Z + "]";
		}
	}

	@Override
	public String toString() {
		return "BlockSelection [blocks=" + blocks + "]";
	}
	
	public int getBlockCount() {
		return blocks.size();
	}

}
