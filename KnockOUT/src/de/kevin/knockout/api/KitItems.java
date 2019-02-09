package de.kevin.knockout.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitItems {
	
	public static ItemStack getKitsChest(Player p) {
		ItemStack chest = new ItemStack(Material.CHEST);
		ItemMeta chestMeta = chest.getItemMeta();
		chestMeta.setDisplayName("§6§lKits");
		chestMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 10, true);
		if (deutsch(p))
			chestMeta.setLore(Arrays.asList("Rechtsklicke um dein Kit auszuwählen"));
		else
			chestMeta.setLore(Arrays.asList("Rightclick to choose your Kit"));
		chest.setItemMeta(chestMeta);
		return chest;
	}
	
	public static ItemStack getStick(Player p) {
		ItemStack stick = new ItemStack(Material.STICK);
		ItemMeta stickMeta = stick.getItemMeta();
		stickMeta.setDisplayName("§7Standard");
		stickMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		stick.setItemMeta(stickMeta);
		return stick;
	}
	
	public static ItemStack getStickPlusShop(Player p) {
		ItemStack stickplus = new ItemStack(Material.STICK);
		ItemMeta stickplusMeta = stickplus.getItemMeta();
		stickplusMeta.setDisplayName("§7Knockback+");
		stickplusMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
		handleLore(p, stickplusMeta, "knockback+", 300);
		stickplus.setItemMeta(stickplusMeta);
		return stickplus;
	}
	
	public static ItemStack getMaurerShop(Player p) {
		ItemStack maurer = new ItemStack(Material.BRICK);
		ItemMeta maurerMeta = maurer.getItemMeta();
		maurerMeta.setDisplayName("§7Maurer");
		maurer.setAmount(32);
		handleLore(p, maurerMeta, "maurer", 500);
		maurer.setItemMeta(maurerMeta);
		return maurer;
	}
	
	public static ItemStack getFliegerShop(Player p) {
		ItemStack flieger = new ItemStack(Material.FEATHER);
		ItemMeta fliegerMeta = flieger.getItemMeta();
		fliegerMeta.setDisplayName("§7Flieger");
		handleLore(p, fliegerMeta, "flieger", 700);
		flieger.setItemMeta(fliegerMeta);
		return flieger;
	}
	
	public static ItemStack getHakenShop(Player p) {
		ItemStack haken = new ItemStack(Material.FISHING_ROD);
		ItemMeta hakenMeta = haken.getItemMeta();
		hakenMeta.setDisplayName("§7Enterhaken");
		handleLore(p, hakenMeta, "enterhaken", 900);
		haken.setItemMeta(hakenMeta);
		return haken;
	}
	
	public static ItemStack getStickPlus(Player p) {
		ItemStack stickplus = new ItemStack(Material.STICK);
		ItemMeta stickplusMeta = stickplus.getItemMeta();
		stickplusMeta.setDisplayName("§7Knockback+");
		stickplusMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
		stickplus.setItemMeta(stickplusMeta);
		return stickplus;
	}
	
	public static ItemStack getMaurer(Player p) {
		ItemStack maurer = new ItemStack(Material.BRICK);
		ItemMeta maurerMeta = maurer.getItemMeta();
		maurerMeta.setDisplayName("§7Maurer");
		maurer.setAmount(32);
		maurer.setItemMeta(maurerMeta);
		return maurer;
	}
	
	public static ItemStack getMaurerStick(Player p) {
		ItemStack stick2 = new ItemStack(Material.STICK);
		ItemMeta stick2Meta = stick2.getItemMeta();
		stick2Meta.setDisplayName("§7Maurerkelle");
		stick2Meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		stick2.setItemMeta(stick2Meta);
		return stick2;
	}
	
	public static ItemStack getFlieger(Player p) {
		ItemStack flieger = new ItemStack(Material.FEATHER);
		ItemMeta fliegerMeta = flieger.getItemMeta();
		fliegerMeta.setDisplayName("§7Flieger");
		if (deutsch(p))
			fliegerMeta.setLore(
					Arrays.asList("Rechtsklicke die Feder um für 3 Sekunden zu fliegen."));
		else
			fliegerMeta.setLore(
					Arrays.asList("Rightclick the feather to fly for 3 seconds."));
		flieger.setItemMeta(fliegerMeta);
		return flieger;
	}
	
	public static ItemStack getFliegerStick(Player p) {
		ItemStack stick3 = new ItemStack(Material.STICK);
		ItemMeta stick3Meta = stick3.getItemMeta();
		stick3Meta.setDisplayName("§7Vogelbein");
		stick3Meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		stick3.setItemMeta(stick3Meta);
		return stick3;
	}
	
	public static ItemStack getHaken(Player p) {
		ItemStack haken = new ItemStack(Material.FISHING_ROD);
		ItemMeta hakenMeta = haken.getItemMeta();
		hakenMeta.setDisplayName("§7Enterhaken");
		haken.setItemMeta(hakenMeta);
		return haken;
	}
	
	public static ItemStack getHakenStick(Player p) {
		ItemStack stick4 = new ItemStack(Material.STICK);
		ItemMeta stick4Meta = stick4.getItemMeta();
		stick4Meta.setDisplayName("§7Enderstick");
		stick4Meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		stick4.setItemMeta(stick4Meta);
		return stick4;
	}

	static boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}
	
	public static List<String> getGekauftLore(Player p, String kit) {
		List<String> lore = new ArrayList<String>();
		lore.clear();
		if (MySQL.hasKitAccess(p, kit)) {
			if (deutsch(p))
				lore.add("§7Gekauft: §a✔");
			else
				lore.add("§7Bought: §a✔");
		} else {
			if (deutsch(p))
				lore.add("§7Gekauft: §c✘");
			else
				lore.add("§7Bought: §c✘");
		}
		return lore;
	}
	
	public static List<String> addPreisLore(List<String> list, int price, Player p) {
		if (MySQL.getCoins(p) >= price) {
			if (deutsch(p))
				list.add("§7Preis: §a" + price);
			else
				list.add("§7Price: §a" + price);
		} else {
			if (deutsch(p))
				list.add("§7Preis: §c" + price);
			else
				list.add("§7Price: §c" + price);
		}
		return list;
	}
	
	public static void handleLore(Player p, ItemMeta meta, String kit, int price) {
		meta.setLore(getGekauftLore(p, kit));
		List<String> lore = meta.getLore();
		if (!MySQL.hasKitAccess(p, kit)) {
			addPreisLore(lore, price, p);
			meta.setLore(lore);
		}
	}

}
