package de.kevin.knockout.api;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.kevin.knockout.main.Main;

public class ScoreboardManager {
	
	public Scoreboard getScoreboard(Player p) {
		if (p.hasMetadata("scoreboard")) {
			return (Scoreboard) p.getMetadata("scoreboard").get(0).value();
		}
		Scoreboard scoreboard = Main.getMain().getServer().getScoreboardManager().getNewScoreboard();
		Main.getMain().setMetadata(p, "scoreboard", scoreboard);
		return scoreboard;
	}
	
	public void setSidebar(Player p, String title, HashMap<String, Integer> sidebar) {
		Scoreboard scoreboard = getScoreboard(p);
		Objective objective = scoreboard.getObjective(p.getName());
		if (objective != null) {
			objective.unregister();
		}
		objective = scoreboard.registerNewObjective(p.getName(), "dummy");
		objective.setDisplayName(title);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		for(String str : sidebar.keySet()) {
			objective.getScore(str).setScore(sidebar.get(str));
		}
		p.setScoreboard(scoreboard);
	}
	
	public void createTeam(Player p, String name, String entry, String suffix) {
		Scoreboard scoreboard = getScoreboard(p);
		Team team = scoreboard.getTeam(name);
		if (team == null) {
			team = scoreboard.registerNewTeam(name);
		}
		team.addEntry(entry);
		team.setSuffix(suffix);
	}

}
