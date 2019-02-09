package de.kevin.knockout.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.kevin.knockout.api.MySQL;

public class Build implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			return false;
		}
		Player p = (Player) sender;
		if (args.length == 0) {
			if (p.hasPermission("knockout.build")) {
				MySQL.switchBuilding(p);
			}
		} else if (args.length == 1) {
			if (!p.hasPermission("knockout.build.other"))
				return false;
			Player p1 = Bukkit.getPlayer(args[0]);
			MySQL.switchBuilding(p1);
		}
		return false;
	}

}
