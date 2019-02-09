package de.kevin.knockout.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.kevin.knockout.api.KitItems;
import de.kevin.knockout.api.MySQL;
import de.kevin.knockout.main.Main;

public class KitsChooseMenu extends KitItems implements Listener {

	public static boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

	public static HashMap<Player, String> playerkit = new HashMap<>();
	
	@EventHandler
	public void onChestClicked(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack chest = getKitsChest(p);
		ItemStack flieger = getFlieger(p);
		
		try {
			if (e.getItem().isSimilar(chest)) {
				e.setCancelled(true);
				createKitsChooseInventory(p);
			}

			if (e.getItem().isSimilar(flieger)) {
				p.setAllowFlight(true);
				p.setFlying(true);
				p.setItemInHand(new ItemStack(Material.AIR));
				if (deutsch(p))
					p.sendMessage("§7[§6KnockOUT§7] §9§lDu kannst nun für 3 Sekunden fliegen.");
				else
					p.sendMessage("§7[§6KnockOUT§7] §9§lYou can now fly for 3 seconds.");
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {

					@Override
					public void run() {
						if (deutsch(p))
							p.sendMessage("§7[§6KnockOUT§7] §9§lDu kannst noch 2 Sekunden fliegen.");
						else
							p.sendMessage("§7[§6KnockOUT§7] §9§lYou can fly for 2 seconds.");
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {

							@Override
							public void run() {
								if (deutsch(p))
									p.sendMessage("§7[§6KnockOUT§7] §9§lDu kannst noch 1 Sekunden fliegen.");
								else
									p.sendMessage("§7[§6KnockOUT§7] §9§lYou can fly for one more second!");
								Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {
									@Override
									public void run() {
										if (deutsch(p))
											p.sendMessage("§7[§6KnockOUT§7] §9§lDu kannst nicht mehr fliegen.");
										else
											p.sendMessage("§7[§6KnockOUT§7] §9§lYou cannot fly anymore.");
										p.setFlying(false);
										p.setAllowFlight(false);
									}
								}, 20);
							}
						}, 20);
					}
				}, 20);
			}

		} catch (Exception e1) {
		}
	}

	public static void createKitsChooseInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9 * 1, "§6§lKits");

		ItemStack stick = getStick(p);
		ItemStack stickplus = getStickPlusShop(p);
		ItemStack maurer = getMaurerShop(p);
		ItemStack flieger = getFliegerShop(p);
		ItemStack haken = getHakenShop(p);

		inv.setItem(0, stick);
		inv.setItem(2, stickplus);
		inv.setItem(4, maurer);
		inv.setItem(6, flieger);
		inv.setItem(8, haken);

		p.openInventory(inv);
	}

	@EventHandler
	public void onKitClicked(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		// Alle Kits aus dem Kits Inventar
		ItemStack stickplusShop = getStickPlusShop(p);
		ItemStack maurerShop = getMaurerShop(p);
		ItemStack fliegerShop = getFliegerShop(p);
		ItemStack hakenShop = getHakenShop(p);
		
		// Standard
		ItemStack stick = getStick(p);
		// Knockback+
		ItemStack stickplus = getStickPlus(p);
		// Maurer
		ItemStack maurer = getMaurer(p);
		ItemStack stick2 = getMaurerStick(p);
		// Flieger
		ItemStack flieger = getFlieger(p);
		ItemStack stick3 = getFliegerStick(p);
		// Enterhaken
		ItemStack haken = getHaken(p);
		ItemStack stick4 = getHakenStick(p);
		
		
		if (e.getClickedInventory().getName().equals("§6§lKits")) {
			e.setCancelled(true);
			// Standard
			if (e.getCurrentItem().isSimilar(stick)) {
				if (deutsch(p))
					e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6Du hast das Kit §7Standard §6gewählt.");
				else
					e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6You chose the Kit §7Standard§6.");
				e.getWhoClicked().closeInventory();
				e.getWhoClicked().getInventory().clear();
				e.getWhoClicked().getInventory().setItem(0, stick);
				playerkit.put(p, "standard");
			}

			// Knockback+
			if (e.getCurrentItem().isSimilar(stickplusShop)) {
				if (MySQL.hasKitAccess(p, "knockback+")) {
					if (deutsch(p))
						e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6Du hast das Kit §7Knockback+ §6gewählt.");
					else
						e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6You chose the Kit §7Knockback+§6.");
					e.getWhoClicked().closeInventory();
					e.getWhoClicked().getInventory().clear();
					e.getWhoClicked().getInventory().setItem(0, stickplus);
					playerkit.put(p, "knockback+");
				} else {
					if (MySQL.getCoins(p) >= 300) {
						MySQL.addKit(p, "knockback+");
						MySQL.addCoins(p, -300);
						if (deutsch(p))
							e.getWhoClicked().sendMessage(
									"§7[§6KnockOUT§7] §6Du hast das Kit §7Knockback+ §6gekauft und es wurde direkt ausgewählt.");
						else
							e.getWhoClicked().sendMessage(
									"§7[§6KnockOUT§7] §6You have bought the Kit §7Knockback+ §6and it got chosen.");
						e.getWhoClicked().closeInventory();
						e.getWhoClicked().getInventory().clear();
						e.getWhoClicked().getInventory().setItem(0, stickplus);
						playerkit.put(p, "knockback+");
					} else {
						if (deutsch(p))
							p.sendMessage("§7[§6KnockOUT§7] §cDu hast nicht genug Coins um dieses Kit zu kaufen!");
						else
							p.sendMessage("§7[§6KnockOUT§7] §cYou don't have enough Coins to buy this Kit!");
					}
				}
			}

			// Maurer
			if (e.getCurrentItem().isSimilar(maurerShop)) {
				if (MySQL.hasKitAccess(p, "maurer")) {
					if (deutsch(p))
						e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6Du hast das Kit §7Maurer §6gewählt.");
					else
						e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6You chose the Kit §7Maurer§6.");
					e.getWhoClicked().closeInventory();
					e.getWhoClicked().getInventory().clear();
					e.getWhoClicked().getInventory().setItem(0, stick2);
					e.getWhoClicked().getInventory().setItem(1, maurer);
					playerkit.put(p, "maurer");
				} else {
					if (MySQL.getCoins(p) >= 500) {
						MySQL.addKit(p, "maurer");
						MySQL.addCoins(p, -500);
						if (deutsch(p))
							e.getWhoClicked().sendMessage(
									"§7[§6KnockOUT§7] §6Du hast das Kit §7Maurer §6gekauft und es wurde direkt ausgewählt.");
						else
							e.getWhoClicked().sendMessage(
									"§7[§6KnockOUT§7] §6You have bought the Kit §7Maurer §6and it got chosen.");
						e.getWhoClicked().closeInventory();
						e.getWhoClicked().getInventory().clear();
						e.getWhoClicked().getInventory().setItem(0, stick2);
						e.getWhoClicked().getInventory().setItem(1, maurer);
						playerkit.put(p, "maurer");
					} else {
						if (deutsch(p))
							p.sendMessage("§7[§6KnockOUT§7] §cDu hast nicht genug Coins um dieses Kit zu kaufen!");
						else
							p.sendMessage("§7[§6KnockOUT§7] §cYou don't have enough Coins to buy this Kit!");
					}
				}
			}

			// Flieger
			if (e.getCurrentItem().isSimilar(fliegerShop)) {
				if (MySQL.hasKitAccess(p, "flieger")) {
					if (deutsch(p))
						e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6Du hast das Kit §7Flieger §6gewählt.");
					else
						e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6You have chosen the Kit §7Flieger§6.");
					e.getWhoClicked().closeInventory();
					e.getWhoClicked().getInventory().clear();
					e.getWhoClicked().getInventory().setItem(0, stick3);
					e.getWhoClicked().getInventory().setItem(1, flieger);
					playerkit.put(p, "flieger");
				} else {
					if (MySQL.getCoins(p) >= 700) {
						MySQL.addKit(p, "flieger");
						MySQL.addCoins(p, -700);
						if (deutsch(p))
							e.getWhoClicked().sendMessage(
									"§7[§6KnockOUT§7] §6Du hast das Kit §7Flieger §6gekauft und es wurde direkt ausgewählt.");
						else
							e.getWhoClicked().sendMessage(
									"§7[§6KnockOUT§7] §6You have bought the Kit §7Flieger §6and it got chosen.");
						e.getWhoClicked().closeInventory();
						e.getWhoClicked().getInventory().clear();
						e.getWhoClicked().getInventory().setItem(0, stick3);
						e.getWhoClicked().getInventory().setItem(1, flieger);
						playerkit.put(p, "flieger");
					} else {
						if (deutsch(p))
							p.sendMessage("§7[§6KnockOUT§7] §cDu hast nicht genug Coins um dieses Kit zu kaufen!");
						else
							p.sendMessage("§7[§6KnockOUT§7] §cYou don't have enough Coins to buy this Kit!");
					}
				}
			}

			// Enterhaken
			if (e.getCurrentItem().isSimilar(hakenShop)) {
				if (MySQL.hasKitAccess(p, "enterhaken")) {
					if (deutsch(p))
						e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6Du hast das Kit §7Enterhaken §6gewählt.");
					else
						e.getWhoClicked().sendMessage("§7[§6KnockOUT§7] §6You have chosen the Kit §7Enterhaken§6.");
					e.getWhoClicked().closeInventory();
					e.getWhoClicked().getInventory().clear();
					e.getWhoClicked().getInventory().setItem(0, stick4);
					e.getWhoClicked().getInventory().setItem(1, haken);
					playerkit.put(p, "enterhaken");
				} else {
					if (MySQL.getCoins(p) >= 900) {
						MySQL.addKit(p, "enterhaken");
						MySQL.addCoins(p, -900);
						if (deutsch(p))
							e.getWhoClicked().sendMessage(
									"§7[§6KnockOUT§7] §6Du hast das Kit §7Enterhaken §6gekauft und es wurde direkt ausgewählt.");
						else
							e.getWhoClicked().sendMessage(
									"§7[§6KnockOUT§7] §6You have bought the Kit §7Enterhaken §6and it got chosen.");
						e.getWhoClicked().closeInventory();
						e.getWhoClicked().getInventory().clear();
						e.getWhoClicked().getInventory().setItem(0, stick4);
						e.getWhoClicked().getInventory().setItem(1, haken);
						playerkit.put(p, "enterhaken");
					} else {
						if (deutsch(p))
							p.sendMessage("§7[§6KnockOUT§7] §cDu hast nicht genug Coins um dieses Kit zu kaufen!");
						else
							p.sendMessage("§7[§6KnockOUT§7] §cYou don't have enough Coins to buy this Kit!");
					}
				}
			}
		}
	}

	public static void setInventoryToKit(Player p) {
		String kit;
		try {
			kit = playerkit.get(p);
		} catch (Exception e) {
			p.getInventory().clear();
			ItemStack chest = getKitsChest(p);
			p.getInventory().setItem(4, chest);
			return;
		}
		// Standard
		ItemStack stick = getStick(p);
		// Knockback+
		ItemStack stickplus = getStickPlus(p);
		// Maurer
		ItemStack maurer = getMaurer(p);
		ItemStack stick2 = getMaurerStick(p);
		// Flieger
		ItemStack stick3 = getFliegerStick(p);
		ItemStack flieger = getFlieger(p);
		// Enterhaken
		ItemStack haken = getHaken(p);
		ItemStack stick4 = getHakenStick(p);
		try {
			switch (kit) {
			case "knockback+":
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stickplus);
				break;
			case "maurer":
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stick2);
				p.getInventory().setItem(1, maurer);
				break;
			case "flieger":
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stick3);
				p.getInventory().setItem(1, flieger);
				break;
			case "enterhaken":
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stick4);
				p.getInventory().setItem(1, haken);
				break;
			case "error":
				p.getInventory().clear();
				ItemStack chest = getKitsChest(p);
				p.getInventory().setItem(4, chest);
				break;
			default:
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stick);
				break;
			}
		} catch (Exception e) {
			p.getInventory().clear();
			ItemStack chest = getKitsChest(p);
			p.getInventory().setItem(4, chest);
		}
	}

	public static void setInventoryToKit(Player p, String kit) {
		try {
		} catch (Exception e) {
			p.getInventory().clear();
			ItemStack chest = getKitsChest(p);
			p.getInventory().setItem(4, chest);
			return;
		}
		// Standard
		ItemStack stick = getStick(p);
		// Knockback+
		ItemStack stickplus = getStickPlus(p);
		// Maurer
		ItemStack maurer = getMaurer(p);
		ItemStack stick2 = getMaurerStick(p);
		// Flieger
		ItemStack stick3 = getFliegerStick(p);
		ItemStack flieger = getFlieger(p);
		// Enterhaken
		ItemStack haken = getHaken(p);
		ItemStack stick4 = getHakenStick(p);
		try {
			switch (kit) {
			case "knockback+":
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stickplus);
				break;
			case "maurer":
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stick2);
				p.getInventory().setItem(1, maurer);
				break;
			case "flieger":
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stick3);
				p.getInventory().setItem(1, flieger);
				break;
			case "enterhaken":
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stick4);
				p.getInventory().setItem(1, haken);
				break;
			case "error":
				p.getInventory().clear();
				ItemStack chest = getKitsChest(p);
				p.getInventory().setItem(4, chest);
				break;
			default:
				p.closeInventory();
				p.getInventory().clear();
				p.getInventory().setItem(0, stick);
				break;
			}
		} catch (Exception e) {
			p.getInventory().clear();
			ItemStack chest = getKitsChest(p);
			p.getInventory().setItem(4, chest);
		}
	}

}
