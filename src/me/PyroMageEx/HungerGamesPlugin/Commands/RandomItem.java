package me.PyroMageEx.HungerGamesPlugin.Commands;

import org.bukkit.inventory.ItemStack;

public class RandomItem {
	ItemStack item;
	int probability;
	double relativeMin;
	double relativeMax;
	public String toString() {
		return "<Name:"+item.getItemMeta().getDisplayName()+"|Type:"+item.getType()+"|Quantity:"+item.getAmount()+"|Probability:"+probability+">";
	}
}
