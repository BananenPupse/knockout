package de.kevin.knockout.commands;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.kevin.knockout.api.TitleAPI;
import de.kevin.knockout.main.Main;

public class Arena implements CommandExecutor {

	static File arena = new File("plugins/KnockOUT/arenas/arena1.yml");
	static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(arena);
	public static int id = 1;
	public static int mapswitchtaskid;
	public static int time;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {
			return false;
		}
		Player p = (Player) sender;
		if (p.hasPermission("knockout.admin")) {
			// /createarena id name
			if (cmd.getName().equalsIgnoreCase("createarena")) {
				if (args.length == 2) {
					int id = 1;
					id = Integer.parseInt(args[0]);
					String name = args[1];
					arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
					cfg = YamlConfiguration.loadConfiguration(arena);
					try {
						cfg.load(arena);
					} catch (IOException | InvalidConfigurationException e) {
						e.printStackTrace();
					}
					cfg.set("Name", name);
					try {
						cfg.save(arena);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (deutsch(p))
						p.sendMessage("§7[§6KnockOUT§7] §aArena erstellt.");
					else
						p.sendMessage("§7[§6KnockOUT§7] §aArena created.");
					return false;
				}
				p.sendMessage("§c/createarena <id> <name> | for first arena use id = 1");
			}

			// /setpos id spawn,spawn_y,death_y
			if (cmd.getName().equalsIgnoreCase("setpos")) {
				if (args.length == 2) {
					int id = Integer.parseInt(args[0]);
					if (args[1].equalsIgnoreCase("spawn")) {
						Location loc = p.getLocation();
						arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
						cfg = YamlConfiguration.loadConfiguration(arena);
						try {
							cfg.load(arena);
						} catch (IOException | InvalidConfigurationException e) {
							e.printStackTrace();
						}
						cfg.set("Location", loc);
						try {
							cfg.save(arena);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return false;
				}

				if (args.length == 3) {
					int id = Integer.parseInt(args[0]);
					if (args[1].equalsIgnoreCase("spawny")) {
						arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
						cfg = YamlConfiguration.loadConfiguration(arena);
						try {
							cfg.load(arena);
						} catch (IOException | InvalidConfigurationException e) {
							e.printStackTrace();
						}
						cfg.set("Spawn_Y", args[2]);
						try {
							cfg.save(arena);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (args[1].equalsIgnoreCase("deathy")) {
						arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
						cfg = YamlConfiguration.loadConfiguration(arena);
						try {
							cfg.load(arena);
						} catch (IOException | InvalidConfigurationException e) {
							e.printStackTrace();
						}
						cfg.set("Death_Y", args[2]);
						try {
							cfg.save(arena);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						
					}
					return false;
				}
				p.sendMessage("§c/setpos <id> <spawn>");
				p.sendMessage("§c/setpos <id> <spawny/deathy> <height>");
			}

			// /mapchange id
			if (cmd.getName().equalsIgnoreCase("mapchange")) {
				try {
					int id = Integer.parseInt(args[0]);
					Arena.id = Integer.parseInt(args[0]);
					switchMap(id);
				} catch (Exception e) {
					p.sendMessage("§7[§6KnockOUT§7] §cFalse MapID:");
					p.performCommand("maps");
				}
			}

			// /randommap
			if (cmd.getName().equalsIgnoreCase("randommap")) {
				switchMap();
			}

			// /getmaps
			if (cmd.getName().equalsIgnoreCase("maps")) {
				p.sendMessage(getMaps());
			}

			// /mapswitchstop
			if (cmd.getName().equalsIgnoreCase("mapswitchstop")) {
				stopMapSwitchTimer();
			}

			// /mapswitchstart
			if (cmd.getName().equalsIgnoreCase("mapswitchstart")) {
				startMapSwitchTimer();
			}

		} else if (p.hasPermission("knockout.mapchange")) {

			// /mapchange id
			if (cmd.getName().equalsIgnoreCase("mapchange")) {
				try {
					int id = Integer.parseInt(args[0]);
					Arena.id = Integer.parseInt(args[0]);
					switchMap(id);
				} catch (Exception e) {
					p.sendMessage("§7[§6KnockOUT§7] §cFalse MapID:");
					p.performCommand("maps");
				}
			}

			// /randommap
			if (cmd.getName().equalsIgnoreCase("randommap")) {
				switchMap();
			}

			// /getmaps
			if (cmd.getName().equalsIgnoreCase("maps")) {
				p.sendMessage(getMaps());
			}

		}
		return false;
	}

	public static Location getArenaSpawn() {
		arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
		cfg = YamlConfiguration.loadConfiguration(arena);
		try {
			cfg.load(arena);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return (Location) cfg.get("Location");
	}

	public static Location getArenaSpawn(int id) {
		arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
		cfg = YamlConfiguration.loadConfiguration(arena);
		try {
			cfg.load(arena);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return (Location) cfg.get("Location");
	}

	public static int getArenaSpawnY() {
		arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
		cfg = YamlConfiguration.loadConfiguration(arena);
		try {
			cfg.load(arena);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			return 100;
		}
		return Integer.parseInt(cfg.getString("Spawn_Y"));
	}

	public static int getArenaDeathY() {
		arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
		cfg = YamlConfiguration.loadConfiguration(arena);
		try {
			cfg.load(arena);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			return 0;
		}
		return Integer.parseInt(cfg.getString("Death_Y"));
	}

	public static String getArenaName() {
		arena = new File("plugins/KnockOUT/arenas/arena" + id + ".yml");
		cfg = YamlConfiguration.loadConfiguration(arena);
		try {
			cfg.load(arena);
		} catch (IOException | InvalidConfigurationException e) {
			return "Error";
		}
		return cfg.getString("Name");
	}

	public static void switchMap() {
		Random random = new Random();
		int files = 0;
		for (@SuppressWarnings("unused")
		File f : new File("plugins/KnockOUT/arenas/").listFiles()) {
			files++;
		}
		id = (random.nextInt(files) + 1);
		for (Player p : Bukkit.getOnlinePlayers()) {
			TitleAPI.sendSubTitle(p, "§6ist nun die neue Map", 20, 20 * 2, 20);
			TitleAPI.sendTitle(p, "§9§l" + getArenaName(), 20, 20 * 2, 20);
			p.teleport(getArenaSpawn());
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 100);
		}
		Main.setSpawny(Arena.getArenaSpawnY());
		Main.setDeathy(Arena.getArenaDeathY());
		stopMapSwitchTimer();
		startMapSwitchTimer();
	}

	public static void switchMap(int mapid) {
		id = mapid;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (deutsch(p))
				TitleAPI.sendSubTitle(p, "§6ist nun die neue Map", 20, 20 * 2, 20);
			else
				TitleAPI.sendSubTitle(p, "§6is the new map", 20, 20 * 2, 20);
			TitleAPI.sendTitle(p, "§9§l" + getArenaName(), 20, 20 * 2, 20);
			p.teleport(getArenaSpawn());
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 100);
		}
		Main.setSpawny(Arena.getArenaSpawnY());
		Main.setDeathy(Arena.getArenaDeathY());
		stopMapSwitchTimer();
		startMapSwitchTimer();
	}

	public static boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

	public static void startMapSwitchTimer() {
		time = 300;
		mapswitchtaskid = Bukkit.getScheduler().runTaskTimer(Main.getMain(), new Runnable() {
			@Override
			public void run() {
				String sekunden, sekunde;

				for (Player p : Bukkit.getOnlinePlayers()) {
					if (deutsch(p))
						sekunden = "§6Mapwechsel in §c" + time + " Sekunden";
					else
						sekunden = "§6Mapchange in §c" + time + " Seconds";
					if (deutsch(p))
						sekunde = "§6Mapwechsel in §ceiner §6Sekunde";
					else
						sekunde = "§6Mapchange in §cone §6Second";
					String msg = sekunden;
					if (time <= 0) {
						switchMap();
					}
					if (time == 1) {
						msg = sekunde;
					}
					TitleAPI.sendActionBar(p, msg);
				}
				if (Bukkit.getOnlinePlayers().size() == 0) {
					switchMap();
				}
				time--;
			}
		}, 20, 20).getTaskId();
	}

	public static void stopMapSwitchTimer() {
		Bukkit.getScheduler().cancelTask(mapswitchtaskid);
	}

	public static String getMaps() {
		YamlConfiguration cfg;
		StringBuilder b = new StringBuilder("§7[§6KnockOUT§7] §6Maps:§9");
		int mapid = 1;
		if (new File("plugins/KnockOUT/arenas/").listFiles().length == 0) {
			return b.append("No maps!").toString();
		}
		for (File f : new File("plugins/KnockOUT/arenas/").listFiles()) {
			cfg = YamlConfiguration.loadConfiguration(f);
			try {
				cfg.load(f);
			} catch (IOException | InvalidConfigurationException e) {
			}
			b.append(" " + cfg.get("Name") + "(#" + mapid + "),");
			mapid++;
		}
		int chars = (b.toString().toCharArray().length - 1);
		b.deleteCharAt(chars);
		return b.toString();
	}

}
