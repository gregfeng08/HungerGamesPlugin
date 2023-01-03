package me.PyroMageEx.HungerGamesPlugin.Commands;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.PyroMageEx.HungerGamesPlugin.Main;
import net.md_5.bungee.api.ChatColor;


public class HGPCommand implements CommandExecutor{
	private Main plugin;
	public HGPCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("hgp").setExecutor(this);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Usage: /hgp <command> <args>
		if(args!=null) {
			String command = args[0].toLowerCase();
			switch(command) {
			//Prints the list
			case "list":
				if(Main.items.isEmpty()) {
					sender.sendMessage("List is currently empty");
				} else {
					int count = 1;
					sender.sendMessage("Current List:");
					for(RandomItem i:Main.items) {
						sender.sendMessage(count+":"+i.toString());
						if(i.item.getItemMeta().getEnchants().size()>0) {
							for(Enchantment e:i.item.getItemMeta().getEnchants().keySet()) {
								sender.sendMessage('\t'+e.toString());
							}
						}
						count++;
					}
				}
				break;
			//Uploads the item in main hand to the list
			case "upload":
				//Usage: /hgp upload <relative%>
				RandomItem r = new RandomItem();
				if(((Player)sender).getInventory().getItemInMainHand()!=null) {
					r.item = ((Player)sender).getInventory().getItemInMainHand();
					r.probability = Integer.parseInt(args[1]);
					Main.totalProbability+=r.probability;
					Main.items.add(r);
					sender.sendMessage("Item in Main Hand Uploaded!");
				} else {
					sender.sendMessage(ChatColor.RED+"ERR: Main Hand Empty");
				}
				break;
			//Removes the item at a certain index
			case "remove":
				//Usage: /hgp remove <index>
				int itemNum = Integer.parseInt(args[1])-1;
				Main.totalProbability-=Main.items.get(itemNum).probability;
				Main.items.remove(itemNum);
				sender.sendMessage("Item "+(itemNum+1)+" Removed!");
				break;
			case "empty":
				//Usage: /hgp empty
				Main.totalProbability=0;
				Main.items.clear();
				sender.sendMessage("List Cleared!");
				break;
			//Begins spawning the chests
			case "start":
				//Usage: /hgp start <minItemsPerChest> <maxItemsPerChest> <spawn%>
				int minItemsPerChest = Integer.parseInt(args[1]);
				int maxItemsPerChest = Integer.parseInt(args[2]);
				double probability = Double.parseDouble(args[3]);
				//ArrayList<Block> surface = new ArrayList<>();
				int borderSize = (int)((Player)sender).getWorld().getWorldBorder().getSize();
				int centerX = (int) Math.round(((Player)sender).getWorld().getWorldBorder().getCenter().getX());
				int centerZ = (int) Math.round(((Player)sender).getWorld().getWorldBorder().getCenter().getZ());
				for(int x=-borderSize/2+centerX;x<borderSize/2+centerX;x++) {
					for(int z=-borderSize/2+centerZ;z<borderSize/2+centerZ;z++) {
						double pseudorandom = Math.random()*100;
						if(pseudorandom <= probability) {
							//Block b = ((Player)sender).getWorld().getHighestBlockAt(x, z).getLocation().add(0,1,0).getBlock();
							
							Block lower = ((Player)sender).getWorld().getHighestBlockAt(x, z).getLocation().getBlock();
							while(lower.getType().equals(Material.WATER)) {
								lower=lower.getLocation().add(0,-1,0).getBlock();
							}
							Block b = lower.getLocation().add(0,1,0).getBlock();
							
							b.setType(Material.CHEST);
							int itemsPerChest = (int)Math.floor(Math.random()*(maxItemsPerChest-minItemsPerChest+1)+minItemsPerChest);
							for(int i=0;i<itemsPerChest;i++) {
								((Chest)b.getState()).getInventory().addItem(pickItem().item);
							}
						}
					}
				}
				Bukkit.broadcastMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Chests Spawned!");
				break;
			default:
				sender.sendMessage(ChatColor.RED+"ERR: Invalid Command");
				sender.sendMessage("Commands: <list|upload|remove|empty|start>");
				break;	
			}
		} else {
			sender.sendMessage(ChatColor.RED+"ERR: Invalid Usage");
			sender.sendMessage("Usage: /hgp <command> <args>");
		}
		return false;
	}
	public RandomItem pickItem() {
		double current = 0;
		RandomItem r = null;
		for(RandomItem i:Main.items) {
			i.relativeMin=current;
			i.relativeMax=current+i.probability;
			current+=i.probability;
		}
		double random = Math.random()*Main.totalProbability;
		for(RandomItem i:Main.items) {
			if(random<=i.relativeMax&&random>=i.relativeMin) {
				r=i;
			}
		}
		return r;
	}
	/*public ItemStack[] scramble(Inventory inv) {
		ItemStack[] is = inv.getContents();
		for(ItemStack i:is) {
			int randomIndex = (int)Math.round(Math.random()*);
		}
		ItemStack[] result = new ItemStack[inv.get];
		return result;
		
	}*/
}
