package de.kevin.knockout.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class KnockOutCommand implements CommandExecutor {
	
	public boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
		if (sender instanceof ConsoleCommandSender)
			return false;
		Player p = (Player) sender;
		p.sendMessage("§8-------------§6KnockOUT§8-------------");
		help(p, "/stats", "Siehe die Stats von dir oder eines Spielers");
		help(p, "/top10", "Siehe die zehn besten der jeweiligen Kategorie");
		help(p, "/kit", "Bekomme das Kit-Menu");
		if (p.hasPermission("knockout.admin")) {
			help(p, "/createarena", "Erstelle eine Arena");
			help(p, "/setpos", "Setze verschiedene Positionen einer Arena");
			help(p, "/createvillager", "Setze einen Villager");
			help(p, "/removevillager", "Entferne einen Villager");
			help(p, "/setcoins", "Setze die Coins eines Spielers");
			help(p, "/setkills", "Setze die Kills eines Spielers");
			help(p, "/setdeaths", "Setze die Tode eines Spielers");
		}
		p.sendMessage("§8-------------§6KnockOUT§8-------------");
		return false;
	}
	
	void help(Player p, String cmd, String info) {
		p.sendMessage("§e" + cmd + " §7- " + info);
	}

}
