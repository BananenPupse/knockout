package de.kevin.knockout.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kevin.knockout.api.MySQL;

public class Stats implements CommandExecutor {

	public boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reset")) {
				MySQL.resetStats(p);
				if (deutsch(p))
					p.sendMessage("§7[§6KnockOUT§7] §4§lDu hast deine Stats gelöscht.");
				else
					p.sendMessage("§7[§6KnockOUT§7] §4§lYou have reset your stats.");
				return true;
			}
			OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
			int kills = MySQL.getKills(player), tode = MySQL.getDeaths(player), coins = MySQL.getCoins(player);
			p.sendMessage("§7===========Stats===========");
			if (deutsch(p))
				p.sendMessage("§7Spieler: " + player.getName());
			else
				p.sendMessage("§7Player: " + player.getName());
			p.sendMessage("§7Kills: " + kills);
			if (deutsch(p))
				p.sendMessage("§7Tode: " + tode);
			else
				p.sendMessage("§7Deaths: " + tode);
			p.sendMessage("§7Coins: " + coins);
			p.sendMessage("§7===========================");
		} else {
			int kills = MySQL.getKills(p), tode = MySQL.getDeaths(p), coins = MySQL.getCoins(p);
			p.sendMessage("§7===========Stats===========");
			p.sendMessage("§7Kills: " + kills);
			if (deutsch(p))
				p.sendMessage("§7Tode: " + tode);
			else
				p.sendMessage("§7Deaths: " + tode);
			p.sendMessage("§7Coins: " + coins);
			p.sendMessage("§7===========================");
		}
		return true;
	}
	
}
