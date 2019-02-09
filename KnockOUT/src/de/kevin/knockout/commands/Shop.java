package de.kevin.knockout.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.kevin.knockout.api.MySQL;

public class Shop implements CommandExecutor, Listener{
	
	public static boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
		if (sender instanceof ConsoleCommandSender)
			return false;
		Player p = (Player) sender;
		p.openInventory(getShop(p));
		if (deutsch(p))
			p.sendMessage("§cBei Tod verschwinden Effekte und Items!");
		else
			p.sendMessage("§cOn death you lose items and effects");
		return false;
	}
	
	public static Inventory getShop(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*2, "Shop");
		inv.setItem(0, getSchneeballItemShop(p));
		inv.setItem(1, getBogenItemShop(p));
		inv.setItem(10, getPfeileItemShop(p));
		return inv;
	}
	
	static ItemStack getSchneeballItemShop(Player p) {
		ItemStack itm = new ItemStack(Material.SNOW_BALL);
		ItemMeta meta = itm.getItemMeta();
		if (deutsch(p))
			meta.setDisplayName("§fSchneeball");
		else
			meta.setDisplayName("§fSnowball");
		if (deutsch(p))
			meta.setLore(Arrays.asList("§4Preis: 3 Coins pro Schneeball"));
		else
			meta.setLore(Arrays.asList("§4Price: 3 Coins per Snowball"));
		itm.setItemMeta(meta);
		return itm;
	}
	static ItemStack getSchneeballItem(Player p) {
		ItemStack itm = new ItemStack(Material.SNOW_BALL);
		ItemMeta meta = itm.getItemMeta();
		if (deutsch(p))
			meta.setDisplayName("§fSchneeball");
		else
			meta.setDisplayName("§fSnowball");
		itm.setItemMeta(meta);
		return itm;
	}
	
	static ItemStack getBogenItemShop(Player p) {
		ItemStack itm = new ItemStack(Material.BOW);
		ItemMeta meta = itm.getItemMeta();
		meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
		if (deutsch(p))
			meta.setDisplayName("§fMiesepeter");
		else
			meta.setDisplayName("§fMoaner");
		if (deutsch(p))
			meta.setLore(Arrays.asList("§4Preis: 100 Coins"));
		else
			meta.setLore(Arrays.asList("§4Price: 100 Coins"));
		itm.setItemMeta(meta);
		return itm;
	}
	static ItemStack getBogenItem(Player p) {
		ItemStack itm = new ItemStack(Material.BOW);
		ItemMeta meta = itm.getItemMeta();
		meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
		if (deutsch(p))
			meta.setDisplayName("§fMiesepeter");
		else
			meta.setDisplayName("§fMoaner");
		itm.setItemMeta(meta);
		return itm;
	}

	static ItemStack getPfeileItemShop(Player p) {
		ItemStack itm = new ItemStack(Material.ARROW);
		ItemMeta meta = itm.getItemMeta();
		if (deutsch(p))
			meta.setDisplayName("§fPfeile");
		else
			meta.setDisplayName("§fArrows");
		if (deutsch(p))
			meta.setLore(Arrays.asList("§4Preis: 10 Coins"));
		else
			meta.setLore(Arrays.asList("§4Price: 10 Coins"));
		itm.setItemMeta(meta);
		return itm;
	}
	static ItemStack getPfeileItem(Player p) {
		ItemStack itm = new ItemStack(Material.ARROW);
		ItemMeta meta = itm.getItemMeta();
		if (deutsch(p))
			meta.setDisplayName("§fPfeile");
		else
			meta.setDisplayName("§fArrows");
		itm.setItemMeta(meta);
		return itm;
	}
	
	@EventHandler()
	public void onShop(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null && e.getClickedInventory().getName().equals("Shop")) {
			e.setCancelled(true);
			if (e.getCurrentItem().isSimilar(getSchneeballItemShop(p))) {
				if (MySQL.getCoins((Player) e.getWhoClicked()) >= 3) {
					MySQL.addCoins((Player) e.getWhoClicked(), -3);
					e.getWhoClicked().getInventory().addItem(getSchneeballItem(p));
				}
			}
			if (e.getCurrentItem().isSimilar(getBogenItemShop(p))) {
				if (MySQL.getCoins((Player) e.getWhoClicked()) >= 100) {
					MySQL.addCoins((Player) e.getWhoClicked(), -100);
					e.getWhoClicked().getInventory().addItem(getBogenItem(p));
				}
			}
			if (e.getCurrentItem().isSimilar(getPfeileItemShop(p))) {
				if (MySQL.getCoins((Player) e.getWhoClicked()) >= 10) {
					MySQL.addCoins((Player) e.getWhoClicked(), -10);
					e.getWhoClicked().getInventory().addItem(getPfeileItem(p));
				}
			}
		}
	}

}
