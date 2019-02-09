package de.kevin.knockout.api;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.kevin.knockout.main.Main;

public class CombatTag implements Listener {

    private Player p;
    private Player pp;
    private double time;
    public static HashMap<Player, Double> combat = new HashMap<>();
    public static HashMap<Player, Player> tag = new HashMap<>();
    private JavaPlugin plugin = Main.getMain();

    public CombatTag(Player hitter, double time, Player damaged) {
        this.p = hitter;
        this.time = time;
        this.pp = damaged;
    }
    
    public void start() {
        if(combat.containsKey(p)) {
            combat.put(p, time);
            tag.put(p, pp);
        } else {
            combat.put(p, time);
            tag.put(p, pp);
            new BukkitRunnable() {
           
                @Override
                public void run() {
                    if(!isInCombat(p)) {
                        cancel();
                        return;
                    }
                    if(!p.isOnline()) {
                        removeCombatTag(p);
                        tag.remove(p);
                        cancel();
                        return;
                    }
                    if(getTimeLeft(p) <= 0) {
                        removeCombatTag(p);
                        tag.remove(p);
                        cancel();
                        return;
                    }
                    if(getTimeLeft(p) > 0) {
                        combat.put(p, combat.get(p) - 0.1);
                    }
                }
            }.runTaskTimer(plugin, 2, 2);
        }
    }
    
    public static boolean isInCombat(Player p) {
        if(combat.containsKey(p)) {
            return true;
        }
        return false;
    }
    
    public double getTimeLeft(Player p) {
        if(combat.containsKey(p)) {
            return combat.get(p);
        }
        return 0;
    }

    public static void removeCombatTag(Player p) {
        if(isInCombat(p)) {
            combat.remove(p);
        }
    }
    
    public static Player getDamaged(Player p) {
    	return tag.get(p);
    }
    
    public static Player getDamager(Player p) {
    	return tag.get(p);
    }

}