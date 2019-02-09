package de.kevin.knockout.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.kevin.knockout.main.Main;

public class Kits implements CommandExecutor {
	
	public boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {
			return true;
		}
		Player p = (Player) sender;
		if (p.getLocation().getBlockY() <= Main.spawny) {
			if (deutsch(p))
				p.sendMessage("§7[§6KnockOUT§7] §cDu musst am Spawn sein, um diesen Befehl nutzen zu können.");
			else
				p.sendMessage("§7[§6KnockOUT§7] §cYou need to be at spawn to use this command.");
			return false;
		}
		p.getInventory().clear();
		ItemStack chest = new ItemStack(Material.CHEST);
		ItemMeta chestMeta = chest.getItemMeta();
		chestMeta.setDisplayName("§6§lKits");
		chestMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 10, true);
		if (deutsch(p))
			chestMeta.setLore(Arrays.asList("Rechtsklicke um dein Kit auszuwählen"));
		else
			chestMeta.setLore(Arrays.asList("Rightclick to choose your Kit"));
		chest.setItemMeta(chestMeta);
		p.getInventory().setItem(4, chest);
		return false;
	}

}
