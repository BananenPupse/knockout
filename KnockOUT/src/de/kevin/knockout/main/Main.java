package de.kevin.knockout.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.kevin.baupixel.mysql.API;
import de.kevin.knockout.api.Messages;
import de.kevin.knockout.api.MySQL;
import de.kevin.knockout.api.ScoreboardManager;
import de.kevin.knockout.commands.Arena;
import de.kevin.knockout.commands.Build;
import de.kevin.knockout.commands.Kits;
import de.kevin.knockout.commands.KnockOutCommand;
import de.kevin.knockout.commands.SetCommands;
import de.kevin.knockout.commands.Shop;
import de.kevin.knockout.commands.Stats;
import de.kevin.knockout.commands.Top10;
import de.kevin.knockout.commands.Villager;
import de.kevin.knockout.commands.VillagerTabComplete;
import de.kevin.knockout.listeners.AntiBuild;
import de.kevin.knockout.listeners.Chat;
import de.kevin.knockout.listeners.Enterhaken;
import de.kevin.knockout.listeners.JoinLeaveListener;
import de.kevin.knockout.listeners.KampfListener;
import de.kevin.knockout.listeners.KitsChooseMenu;
import de.kevin.knockout.listeners.WeatherLock;

public class Main extends JavaPlugin {

	private static Main main;
	public static boolean isBauPixel = true;
	
	private ScoreboardManager scoreboardManager;

	public static int spawny, deathy;
	public static String servername = "§aDein-Server.de";

	public static void setSpawny(int spawny) {
		Main.spawny = spawny;
	}

	public static void setDeathy(int deathy) {
		Main.deathy = deathy;
	}

	public static File config = new File("plugins/KnockOUT/config.yml");
	File spawn = new File("plugins/KnockOUT/spawn.yml");

	public static Main getMain() {
		return main;
	}

	public static void setMain(Main main) {
		Main.main = main;
	}
	
	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}
	
	public static void log(String... args) {
		for (String string : args) {
			Bukkit.getConsoleSender().sendMessage(Messages.prefix + string);
		}
	}
	
	private void initScoreScheduler() {
		getServer().getScheduler().runTaskTimer(this, () -> {
			getServer().getOnlinePlayers().forEach(player -> {
				int kills = MySQL.getKills(player);
				int tode = MySQL.getDeaths(player);
				String kit = "wählen";
				if (KitsChooseMenu.playerkit.containsKey(player)) {
					switch (KitsChooseMenu.playerkit.get(player)) {
					case "knockback+":
						kit = "Knockback+";
						break;
					case "maurer":
						kit = "Maurer";
						break;
					case "flieger":
						kit = "Flieger";
						break;
					case "enterhaken":
						kit = "Enterhaken";
						break;
					default:
						kit = "Standart";
						break;
					}
				}
				getScoreboardManager().createTeam(player, "kills", "§7» §a", kills + "");
				getScoreboardManager().createTeam(player, "tode", "§7» §c", tode + "");
				getScoreboardManager().createTeam(player, "coins", "§7» §6", MySQL.getCoins(player) + "");
				getScoreboardManager().createTeam(player, "kit", "§7» §3", kit);
				if (isBauPixel)
					getScoreboardManager().createTeam(player, "pixel", "§7» §a§a", API.getPixel(player) + "");
			});
		}, 20L, 20L * 3);
	}
	
	public boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}
	
	public void setDefaultSidebar(Player p) {
		HashMap<String, Integer> sidebar = new HashMap<String, Integer>();
		sidebar.put("§1", 0);
		if (deutsch(p))
			sidebar.put("§7Du spielst gerade:", -1);
		else
			sidebar.put("§7You are playing:", -1);
		sidebar.put("§7» §bKnockOUT", -2);
		sidebar.put("§2", -3);
		sidebar.put("§7Kills:", -4);
		sidebar.put("§7» §a", -5);
		sidebar.put("§3", -6);
		if (deutsch(p))
			sidebar.put("§7Tode:", -7);
		else
			sidebar.put("§7Deaths:", -7);
		sidebar.put("§7» §c", -8);
		sidebar.put("§4", -9);
		sidebar.put("§7Coins:", -10);
		sidebar.put("§7» §6", -11);
		sidebar.put("§5", -12);
		sidebar.put("§7Dein Kit:", -13);
		sidebar.put("§7» §3", -14);
		if (isBauPixel) {
			sidebar.put("§5", -15);
			sidebar.put("§7Pixel:", -16);
			sidebar.put("§7» §a§a", -17);
		}
		if (isBauPixel)
			getScoreboardManager().setSidebar(p, "§a§lBau-Pixel.net", sidebar);
		else
			getScoreboardManager().setSidebar(p, ChatColor.translateAlternateColorCodes('&', servername), sidebar);
	}

	@Override
	public void onEnable() {
		setMain(this);
		this.scoreboardManager = new ScoreboardManager();
		if (!isBauPixel) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
			try {
				cfg.load(config);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			servername = cfg.getString("servername");
		}
		Bukkit.getOnlinePlayers().forEach(p -> {
			try {
				Main.getMain().setDefaultSidebar(p);
			} catch (Exception e1) {
			}
		});
		initScoreScheduler();
		
		createConfigIfNotExists(config);

		spawny = Arena.getArenaSpawnY();
		deathy = Arena.getArenaDeathY();

		MySQL.connect();
		MySQL.createTable();

		if (new File("plugins/KnockOUT/arenas/").listFiles().length > 1) {
			Arena.startMapSwitchTimer();
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) {
			} else {
				p.setAllowFlight(false);
				p.setFlying(false);
			}
		}

		if (!MySQL.isConnected())
			return;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (World w: Bukkit.getWorlds()) {
					w.setThundering(false);
					w.setStorm(false);
					w.setTime(6000);
				}
			}
		}.runTaskTimer(this, 20, 20*60);

		if (isBauPixel) {
			Bukkit.getOnlinePlayers().forEach(p -> {
				if (deutsch(p))
					p.sendMessage("§7[§6KnockOUT§7] §a§lDas Plugin wurde für Bau-Pixel.net programmiert.");
				else
					p.sendMessage("§7[§6KnockOUT§7] §a§lThis Plugin was developed for Bau-Pixel.net");
			});
		}
		
		register(new JoinLeaveListener());
		register(new KitsChooseMenu());
		register(new KampfListener());
		register(new AntiBuild());
		register(new WeatherLock());
		register(new Enterhaken());
		register(new Shop());
		register(new Chat());
		command("kit", new Kits());
		command("createarena", new Arena());
		command("setpos", new Arena());
		command("mapchange", new Arena());
		command("randommap", new Arena());
		command("mapswitchstop", new Arena());
		command("mapswitchstart", new Arena());
		command("maps", new Arena());
		command("stats", new Stats());
		command("build", new Build());
		command("setkills", new SetCommands());
		command("setdeaths", new SetCommands());
		command("setcoins", new SetCommands());
		command("createvillager", new Villager());
		command("createvillager", new VillagerTabComplete());
		command("removevillager", new Villager());
		command("shop", new Shop());
		command("top10", new Top10());
		command("knockout", new KnockOutCommand());
	}

	@Override
	public void onDisable() {
		MySQL.close();

		Arena.stopMapSwitchTimer();
	}

	void command(String name, CommandExecutor executor) {
		getCommand(name).setExecutor(executor);
	}

	void command(String name, TabCompleter tabcompleter) {
		getCommand(name).setTabCompleter(tabcompleter);
	}

	void register(Listener listener) {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(listener, this);
	}

	void createFileIfNotExists(File file) {
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void createConfigIfNotExists(File config) {
		config.getParentFile().mkdirs();
		try {
			if (config.createNewFile()) {
				try {
					config.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
				cfg.set("Spawn_Y", 88);
				cfg.set("Death_Y", 10);
				cfg.set("servername", "§aDein-Server.de");
				cfg.set("host", "localhost");
				cfg.set("database", "database");
				cfg.set("user", "user");
				cfg.set("password", "password");
				try {
					cfg.save(config);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
		}
	}
	
	public void removeMetadata(Entity entity, String name) {
		if (entity.hasMetadata(name)) {
			entity.removeMetadata(name, this);
		}
	}
	
	public void setMetadata(Entity entity, String name, Object object) {
		removeMetadata(entity, name);
		entity.setMetadata(name, new FixedMetadataValue(this, object));
	}

}
