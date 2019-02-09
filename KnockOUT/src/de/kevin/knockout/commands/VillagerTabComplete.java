package de.kevin.knockout.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class VillagerTabComplete implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		return Arrays.asList("kits", "shop");
	}

}
