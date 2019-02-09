package de.kevin.knockout.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.kevin.knockout.api.MySQL;

public class SetCommands implements CommandExecutor {
	
	public boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			return true;
		}
		Player p = (Player) sender;
		if (p.hasPermission("knockout.setstats")) {
			switch (cmd.getName()) {
			case "setkills":
				if (args.length == 1) {
					MySQL.setKills(p, Integer.parseInt(args[0]));
					if (deutsch(p))
						p.sendMessage("§7[§6KnockOUT§7] §aDeine Kills wurden auf " + args[0] + " gesetzt.");
					else
						p.sendMessage("§7[§6KnockOUT§7] §aYour kills were set to " + args[0] + ".");
				} else if (args.length == 2) {
					@SuppressWarnings("deprecation")
					Player player = Bukkit.getOfflinePlayer(args[0]).getPlayer();
					if (MySQL.playerExists(player)) {
						MySQL.setKills(player, Integer.parseInt(args[1]));
						if (deutsch(p))
							p.sendMessage("§7[§6KnockOUT§7] §a" + player.getName() + "'s Kills wurden auf " + args[1]
									+ " gesetzt.");
						else
							p.sendMessage("§7[§6KnockOUT§7] §a" + player.getName() + "'s kills were set to " + args[1]
									+ ".");
						if (deutsch(player))
							player.sendMessage("§7[§6KnockOUT§7] §aDeine Kills wurden auf " + args[1] + " gesetzt.");
						else
							player.sendMessage("§7[§6KnockOUT§7] §aYour kills were set to " + args[1] + ".");
					}
				} else {
					p.sendMessage("§7[§6KnockOUT§7] §c/setkills <kills> | /setkills <player> <kills>");
				}
				break;
			case "setdeaths":
				if (args.length == 1) {
					MySQL.setDeaths(p, Integer.parseInt(args[0]));
					if (deutsch(p))
						p.sendMessage("§7[§6KnockOUT§7] §aDeine Tode wurden auf " + args[0] + " gesetzt.");
					else
						p.sendMessage("§7[§6KnockOUT§7] §aYour deaths were set to " + args[0] + ".");
				} else if (args.length == 2) {
					@SuppressWarnings("deprecation")
					Player player = Bukkit.getOfflinePlayer(args[0]).getPlayer();
					if (MySQL.playerExists(player)) {
						MySQL.setDeaths(player, Integer.parseInt(args[1]));
						if (deutsch(p))
							p.sendMessage("§7[§6KnockOUT§7] §a" + player.getName() + "'s Tode wurden auf " + args[1]
									+ " gesetzt.");
						else
							p.sendMessage("§7[§6KnockOUT§7] §a" + player.getName() + "'s deaths were set to " + args[1]
									+ ".");
						if (deutsch(player))
							player.sendMessage("§7[§6KnockOUT§7] §aDeine Tode wurden auf " + args[1] + " gesetzt.");
						else
							player.sendMessage("§7[§6KnockOUT§7] §aYour deaths were set to " + args[1] + ".");
					}
				} else {
					if (deutsch(p))
						p.sendMessage("§7[§6KnockOUT§7] §c/setdeaths <tode> | /setdeaths <player> <tode>");
					else
						p.sendMessage("§7[§6KnockOUT§7] §c/setdeaths <deaths> | /setdeaths <player> <deaths>");
				}
				break;
			case "setcoins":
				if (args.length == 1) {
					MySQL.setCoins(p, Integer.parseInt(args[0]));
					if (deutsch(p))
						p.sendMessage("§7[§6KnockOUT§7] §aDeine Coins wurden auf " + args[0] + " gesetzt.");
					else
						p.sendMessage("§7[§6KnockOUT§7] §aYour coins were set to " + args[0] + ".");
				} else if (args.length == 2) {
					@SuppressWarnings("deprecation")
					Player player = Bukkit.getOfflinePlayer(args[0]).getPlayer();
					if (MySQL.playerExists(player)) {
						MySQL.setCoins(player, Integer.parseInt(args[1]));
						if (deutsch(p))
							p.sendMessage("§7[§6KnockOUT§7] §a" + player.getName() + "'s Coins wurden auf " + args[1]
									+ " gesetzt.");
						else
							p.sendMessage("§7[§6KnockOUT§7] §a" + player.getName() + "'s coins were set to " + args[1]
									+ ".");
						if (deutsch(player))
							player.sendMessage("§7[§6KnockOUT§7] §aDeine Coins wurden auf " + args[1] + " gesetzt.");
						else
							player.sendMessage("§7[§6KnockOUT§7] §aYour coins were set to " + args[1] + ".");
					}
				} else {
					p.sendMessage("§7[§6KnockOUT§7] §c/setcoins <coins> | /setcoins <player> <coins>");
				}
				break;
			default:
				break;
			}
		}
		return false;
	}

}
