package me.PyroMageEx.HungerGamesPlugin;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import me.PyroMageEx.HungerGamesPlugin.Commands.HGPCommand;
import me.PyroMageEx.HungerGamesPlugin.Commands.RandomItem;

public class Main extends JavaPlugin{
	public static ArrayList<RandomItem> items = new ArrayList<>();
	public static int totalProbability = 0;
	public void onEnable() {
		new HGPCommand(this);
	}
}
