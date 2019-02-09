package de.kevin.knockout.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.kevin.knockout.api.MySQL;

public class Top10 implements CommandExecutor {
	
	public boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
		if (sender instanceof ConsoleCommandSender)
			return false;
		Player p = (Player) sender;
		if (args.length == 0) {
			if (deutsch(p))
				p.sendMessage("§c/top10 KILLS/TODE/COINS");
			else
				p.sendMessage("§c/top10 KILLS/DEATHS/COINS");
			return true;
		}
		
		switch (args[0].toLowerCase()) {
		case "kills":
			HashMap<Integer, String> platz = MySQL.getTop10();
			String platz1 = platz.get(1);
			String platz2 = platz.get(2);
			String platz3 = platz.get(3);
			String platz4 = platz.get(4);
			String platz5 = platz.get(5);
			String platz6 = platz.get(6);
			String platz7 = platz.get(7);
			String platz8 = platz.get(8);
			String platz9 = platz.get(9);
			String platz10 = platz.get(10);
			
			p.sendMessage("§7===========Top10 Kills===========");
			p.sendMessage(platz1);
			p.sendMessage(platz2);
			p.sendMessage(platz3);
			p.sendMessage(platz4);
			p.sendMessage(platz5);
			p.sendMessage(platz6);
			p.sendMessage(platz7);
			p.sendMessage(platz8);
			p.sendMessage(platz9);
			p.sendMessage(platz10);
			p.sendMessage("§7==============================");
			break;
		case "tode":
			HashMap<Integer, String> platzTode = MySQL.getTop10Tode();
			String platz11 = platzTode.get(1);
			String platz21 = platzTode.get(2);
			String platz31 = platzTode.get(3);
			String platz41 = platzTode.get(4);
			String platz51 = platzTode.get(5);
			String platz61 = platzTode.get(6);
			String platz71 = platzTode.get(7);
			String platz81 = platzTode.get(8);
			String platz91 = platzTode.get(9);
			String platz101 = platzTode.get(10);
			
			if (deutsch(p))
				p.sendMessage("§7===========Top10 Tode===========");
			else
				p.sendMessage("§7===========Top10 Deaths===========");
			p.sendMessage(platz11);
			p.sendMessage(platz21);
			p.sendMessage(platz31);
			p.sendMessage(platz41);
			p.sendMessage(platz51);
			p.sendMessage(platz61);
			p.sendMessage(platz71);
			p.sendMessage(platz81);
			p.sendMessage(platz91);
			p.sendMessage(platz101);
			p.sendMessage("§7==============================");
			break;
		case "deaths":
			HashMap<Integer, String> platzTode1 = MySQL.getTop10Tode();
			String platz111 = platzTode1.get(1);
			String platz211 = platzTode1.get(2);
			String platz311 = platzTode1.get(3);
			String platz411 = platzTode1.get(4);
			String platz511 = platzTode1.get(5);
			String platz611 = platzTode1.get(6);
			String platz711 = platzTode1.get(7);
			String platz811 = platzTode1.get(8);
			String platz911 = platzTode1.get(9);
			String platz1011 = platzTode1.get(10);
			
			if (deutsch(p))
				p.sendMessage("§7===========Top10 Tode===========");
			else
				p.sendMessage("§7===========Top10 Deaths===========");
			p.sendMessage(platz111);
			p.sendMessage(platz211);
			p.sendMessage(platz311);
			p.sendMessage(platz411);
			p.sendMessage(platz511);
			p.sendMessage(platz611);
			p.sendMessage(platz711);
			p.sendMessage(platz811);
			p.sendMessage(platz911);
			p.sendMessage(platz1011);
			p.sendMessage("§7==============================");
			break;
		case "coins":
			HashMap<Integer, String> platzCoins = MySQL.getTop10Coins();
			String platz1111 = platzCoins.get(1);
			String platz2111 = platzCoins.get(2);
			String platz3111 = platzCoins.get(3);
			String platz4111 = platzCoins.get(4);
			String platz5111 = platzCoins.get(5);
			String platz6111 = platzCoins.get(6);
			String platz7111 = platzCoins.get(7);
			String platz8111 = platzCoins.get(8);
			String platz9111 = platzCoins.get(9);
			String platz10111 = platzCoins.get(10);
			
			p.sendMessage("§7===========Top10 Coins===========");
			p.sendMessage(platz1111);
			p.sendMessage(platz2111);
			p.sendMessage(platz3111);
			p.sendMessage(platz4111);
			p.sendMessage(platz5111);
			p.sendMessage(platz6111);
			p.sendMessage(platz7111);
			p.sendMessage(platz8111);
			p.sendMessage(platz9111);
			p.sendMessage(platz10111);
			p.sendMessage("§7==============================");
			break;
		default:
			break;
		}
		
		return false;
	}

}
