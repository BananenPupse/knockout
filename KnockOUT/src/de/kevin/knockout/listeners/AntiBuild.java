package de.kevin.knockout.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import de.kevin.knockout.api.MySQL;
import de.kevin.knockout.commands.Shop;
import de.kevin.knockout.main.Main;

public class AntiBuild implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (MySQL.isBuilding(e.getPlayer())) {
			e.setCancelled(false);
		} else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
			e.setDamage(0);
		} else if (e.getCause().equals(DamageCause.FALL)) {
			e.setCancelled(true);
		} else if (e.getCause().equals(DamageCause.VOID)) {
			e.setDamage(10);
		}
	}

	@EventHandler
	public void onBucket(PlayerBucketEmptyEvent e) {
		if (!MySQL.isBuilding(e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBucket(PlayerBucketFillEvent e) {
		if (!MySQL.isBuilding(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (MySQL.isBuilding(e.getPlayer()))
			e.setCancelled(false);
		else
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onFeed(FoodLevelChangeEvent e) {
		if (e.getFoodLevel() != 40) {
			e.setFoodLevel(40);
		} else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Block block = e.getBlockPlaced();
		if (MySQL.isBuilding(e.getPlayer())) {
			e.setCancelled(false);
		} else {
			if (e.getBlockPlaced().getType().equals(Material.BRICK)) {
				if (e.getBlockPlaced().getLocation().getBlockY() >= Main.spawny) {
					e.setCancelled(true);
					return;
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {

					@Override
					public void run() {
						block.setType(Material.REDSTONE_BLOCK);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {

							@Override
							public void run() {
								block.setType(Material.AIR);
							}
						}, 20 * 2);
					}
				}, 20 * 3);
			} else {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
			if (e.getRightClicked().getName().contains("Kits"))
				KitsChooseMenu.createKitsChooseInventory(e.getPlayer());
			if (e.getRightClicked().getName().contains("Shop"))
				e.getPlayer().openInventory(Shop.getShop(e.getPlayer()));
		}
	}

	@EventHandler
	public void on(InventoryOpenEvent e) {
		if (e.getView().getType().equals(InventoryType.MERCHANT)) {
			e.setCancelled(true);
		}
	}
	
}
