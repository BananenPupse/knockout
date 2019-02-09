package de.kevin.knockout.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;

import de.kevin.baupixel.mysql.API;
import de.kevin.knockout.api.MySQL;
import de.kevin.knockout.api.TitleAPI;
import de.kevin.knockout.commands.Arena;
import de.kevin.knockout.main.Main;

public class KampfListener implements Listener {
	double combattime = 10.0D;
	HashMap<Player, Player> lasthit = new HashMap<Player, Player>();
	HashMap<Player, BukkitTask> timer = new HashMap<Player, BukkitTask>();

	@EventHandler
	public void on(EntityDamageByEntityEvent e) {
		Player attacker;
		if (e.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) e.getDamager();
			attacker = (Player) s.getShooter();
			if ((e.getEntity() instanceof Villager)) {
				if (MySQL.isBuilding(attacker)) {
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
				}
				return;
			}
			final Player noob = (Player) e.getEntity();
			if (attacker == noob) {
				return;
			}
			if (noob.getLocation().getBlockY() >= Main.spawny) {
				e.setCancelled(true);
				return;
			}
			if (this.lasthit.containsKey(noob)) {
				Bukkit.getScheduler().cancelTask(((BukkitTask) this.timer.get(noob)).getTaskId());
				this.timer.remove(noob);
			}
			this.lasthit.put(noob, attacker);
			BukkitTask id = Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
				public void run() {
					KampfListener.this.lasthit.remove(noob);
				}
			}, 200L);
			this.timer.put(noob, id);
		}
		if (e.getDamager() instanceof Arrow) {
			e.setDamage(0);
			Arrow a = (Arrow) e.getDamager();
			attacker = (Player) a.getShooter();
			final Player noob = (Player) e.getEntity();
			if (attacker == noob) {
				return;
			}
			if (noob.getLocation().getBlockY() >= Main.spawny) {
				e.setCancelled(true);
				return;
			}
			if (this.lasthit.containsKey(noob)) {
				Bukkit.getScheduler().cancelTask(((BukkitTask) this.timer.get(noob)).getTaskId());
				this.timer.remove(noob);
			}
			this.lasthit.put(noob, attacker);
			BukkitTask id = Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
				public void run() {
					KampfListener.this.lasthit.remove(noob);
				}
			}, 200L);
			this.timer.put(noob, id);
		}
		try {
			Player damager = (Player) e.getDamager();
			
			if ((e.getEntity() instanceof Villager)) {
				if (MySQL.isBuilding(damager)) {
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
				}
				return;
			}
			final Player noob = (Player) e.getEntity();
			if (noob.getLocation().getBlockY() >= Main.spawny) {
				e.setCancelled(true);
				return;
			}
			if (this.lasthit.containsKey(noob)) {
				Bukkit.getScheduler().cancelTask(((BukkitTask) this.timer.get(noob)).getTaskId());
				this.timer.remove(noob);
			}
			this.lasthit.put(noob, damager);
			BukkitTask id = Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
				public void run() {
					KampfListener.this.lasthit.remove(noob);
				}
			}, 200L);
			this.timer.put(noob, id);
		} catch (Exception localException) {
		}
	}

	public boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (e.getTo().getBlockY() <= Main.deathy) {
			if (e.getPlayer().isDead()) {
				e.getPlayer().teleport(Arena.getArenaSpawn());
				return;
			}
			e.getPlayer().setHealth(0.0D);
			KitsChooseMenu.setInventoryToKit(e.getPlayer());
			if (this.lasthit.containsKey(e.getPlayer())) {
				MySQL.addKill((Player) this.lasthit.get(e.getPlayer()));
				API.addPixel((Player) this.lasthit.get(e.getPlayer()), 1);
				MySQL.addCoins((Player) this.lasthit.get(e.getPlayer()), 10);
				((Player) this.lasthit.get(e.getPlayer())).playSound(this.lasthit.get(e.getPlayer()).getLocation(), Sound.LEVEL_UP, 1, 100);
				TitleAPI.sendActionBar((Player) this.lasthit.get(e.getPlayer()), "§a+10 Coins");
				MySQL.addDeath(e.getPlayer());
				if ((MySQL.getCoins(e.getPlayer()) <= 3) && (MySQL.getCoins(e.getPlayer()) >= 0)) {
					MySQL.setCoins(e.getPlayer(), 0);
					TitleAPI.sendActionBar(e.getPlayer(), "§c-" + MySQL.getCoins(e.getPlayer()) + " Coins");
				} else {
					MySQL.addCoins(e.getPlayer(), -3);
					TitleAPI.sendActionBar(e.getPlayer(), "§c-3 Coins");
				}

				if (deutsch(p))
					e.getPlayer().sendMessage("§7[§6KnockOUT§7] §9Du wurdest von "
							+ ((Player) this.lasthit.get(e.getPlayer())).getName() + " runtergeschubst.");
				else
					e.getPlayer().sendMessage("§7[§6KnockOUT§7] §9You were pushed down by "
							+ ((Player) this.lasthit.get(e.getPlayer())).getName() + ".");
				if (deutsch((Player) this.lasthit.get(e.getPlayer())))
					((Player) this.lasthit.get(e.getPlayer()))
							.sendMessage("§7[§6KnockOUT§7] §9Du hast " + e.getPlayer().getName() + " runtergeschubst.");
				else
					((Player) this.lasthit.get(e.getPlayer()))
							.sendMessage("§7[§6KnockOUT§7] §9You have pushed down " + e.getPlayer().getName() + ".");
				this.lasthit.remove(e.getPlayer());
				Bukkit.getScheduler().cancelTask(((BukkitTask) this.timer.get(e.getPlayer())).getTaskId());
				this.timer.remove(e.getPlayer());
			} else {
				if (deutsch(p))
					e.getPlayer().sendMessage("§7[§6KnockOUT§7] §9Du bist gestorben.");
				else
					e.getPlayer().sendMessage("§7[§6KnockOUT§7] §9You died.");
				MySQL.addDeath(e.getPlayer());
				if ((MySQL.getCoins(e.getPlayer()) <= 2) && (MySQL.getCoins(e.getPlayer()) >= 0)) {
					MySQL.setCoins(e.getPlayer(), 0);
					TitleAPI.sendActionBar(e.getPlayer(), "§c-" + MySQL.getCoins(e.getPlayer()) + " Coins");
				} else {
					MySQL.addCoins(e.getPlayer(), -2);
					TitleAPI.sendActionBar(e.getPlayer(), "§c-2 Coins");
				}
			}
		}
		if ((e.getTo().getBlockY() == Main.spawny) && (!KitsChooseMenu.playerkit.containsKey(e.getPlayer()))) {
			KitsChooseMenu.setInventoryToKit(e.getPlayer(), "standard");
			KitsChooseMenu.playerkit.put(e.getPlayer(), "standard");
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent e) {
		e.setKeepInventory(true);
		e.setDeathMessage(null);
		Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
			public void run() {
				if (e.getEntity().isDead()) {
					e.getEntity().spigot().respawn();
				}
			}
		}, 10L);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(Arena.getArenaSpawn());
	}

	@EventHandler
	public void on(EntityDamageEvent e) {
		if ((e.getEntity() instanceof Player)) {
			Player p = (Player) e.getEntity();
			if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
				e.setCancelled(false);
				return;
			}
			if (p.getLocation().getY() >= Main.spawny) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFishHook(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof FishHook) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpawnInteract(PlayerInteractEvent e) {
		if (e.getPlayer().getLocation().getBlockY() >= Main.spawny) {
			e.setCancelled(true);
		}
	}
}
