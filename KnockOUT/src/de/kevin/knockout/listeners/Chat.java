package de.kevin.knockout.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

public class Chat implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		boolean colored = p.hasPermission("knockout.chat.colored") || p.isOp() ? true : false;
		if (colored)
			e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		else
			e.setMessage(ChatColor.stripColor(e.getMessage()));
	}

}
