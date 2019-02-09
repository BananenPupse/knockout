package de.kevin.knockout.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.dytanic.cloudnet.bridge.CloudServer;
import de.kevin.baupixel.mysql.API;
import de.kevin.knockout.api.Messages;
import de.kevin.knockout.api.MySQL;
import de.kevin.knockout.commands.Arena;
import de.kevin.knockout.main.Main;

public class JoinLeaveListener implements Listener {
	
	public boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();

		e.setJoinMessage(null);
		try {
			MySQL.registerPlayer(p);
		} catch (Exception e2) {
			Main.log("§cThe mysql connection failed! Please check your config asap.");
		}

		new BukkitRunnable() {
			
			@Override
			public void run() {
				StringBuilder b = new StringBuilder();
				b.append("§7[§a+§7] §a" + e.getPlayer().getName());
				if (deutsch(p))
					p.sendMessage("§7[§6KnockOUT§7] §6Die Map ist gerade §9" + Arena.getArenaName());
				else
					p.sendMessage("§7[§6KnockOUT§7] §6The current Map is §9" + Arena.getArenaName());
				new BukkitRunnable() {
					public void run() {
						p.setHealth(0.0D);
					}
				}.runTaskLater(Main.getMain(), 20L);

				p.getInventory().clear();

				ItemStack chest = new ItemStack(Material.CHEST);
				ItemMeta chestMeta = chest.getItemMeta();
				chestMeta.setDisplayName("§6§lKits");
				chestMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 10, true);
				if (deutsch(p))
					chestMeta.setLore(Arrays.asList(new String[] { "Rechtsklicke um dein Kit auszuwählen" }));
				else
					chestMeta.setLore(Arrays.asList(new String[] { "Rightclick to choose your Kit" }));
				chest.setItemMeta(chestMeta);
				p.getInventory().setItem(4, chest);
				p.teleport(Arena.getArenaSpawn());
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.sendMessage(b.toString());
					if (!player.getName().equals(p.getName())) {
						player.showPlayer(p);
						p.showPlayer(player);
					}
				}
				try {
					Main.getMain().setDefaultSidebar(p);
					CloudServer.getInstance().updateNameTags(p);
				} catch (Exception e1) {
				}
			}
		}.runTaskLater(Main.getMain(), 10);
		if (Main.isBauPixel)
			API.registriereSpieler(p);
	}

	@EventHandler
	public void onLogin(final PlayerLoginEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {
			public void run() {
				try {
					e.getPlayer().teleport(Arena.getArenaSpawn());
				} catch (Exception e1) {
					e.getPlayer().sendMessage(Messages.prefix + "§cEs wurde noch kein Spawn oder keine Arena erstellt! Bitte mache /createarena und dann /setpos");
				}
			}
		}, 26L);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		StringBuilder b = new StringBuilder();
		b.append("§7[§c-§7] §c" + e.getPlayer().getName());
		e.setQuitMessage(b.toString());
	}
	
	@EventHandler
	public void onVillager(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof Villager) {
			KitsChooseMenu.createKitsChooseInventory(e.getPlayer());
		}
	}
}
