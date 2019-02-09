package de.kevin.knockout.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;

import de.kevin.knockout.api.Messages;
import de.kevin.knockout.api.NoAI;

public class Villager implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
		if (sender instanceof ConsoleCommandSender)
			return false;
		Player p = (Player) sender;
		if (p.hasPermission("knockout.admin")) {
			if (command.getName().equalsIgnoreCase("createvillager")) {
				if (args.length != 1) {
					p.sendMessage("§c/createvillager <kit/shop>");
					return false;
				}
				if (args[0].equalsIgnoreCase("kits")) {
					org.bukkit.entity.Villager v = (org.bukkit.entity.Villager) p.getWorld()
							.spawnEntity(p.getLocation(), EntityType.VILLAGER);
					v.setCustomName("§6§lKits");
					v.setCustomNameVisible(true);
					v.setAgeLock(true);
					v.setProfession(Profession.PRIEST);
					NoAI.setAIEnabled(v, false);
				}
				if (args[0].equalsIgnoreCase("shop")) {
					org.bukkit.entity.Villager v = (org.bukkit.entity.Villager) p.getWorld()
							.spawnEntity(p.getLocation(), EntityType.VILLAGER);
					v.setCustomName("§c§lShop");
					v.setCustomNameVisible(true);
					v.setAgeLock(true);
					v.setProfession(Profession.PRIEST);
					NoAI.setAIEnabled(v, false);
				}
			}
			if (command.getName().equalsIgnoreCase("removevillager")) {
				p.getWorld().getNearbyEntities(p.getLocation(), 3, 8, 3).forEach(entity -> {
					if (entity instanceof org.bukkit.entity.Villager) {
						((org.bukkit.entity.Villager) entity).remove();
					}
				});
			}
		} else {
			p.sendMessage(Messages.perms);
		}
		return false;
	}

}
