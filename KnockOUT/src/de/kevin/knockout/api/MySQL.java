package de.kevin.knockout.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.kevin.knockout.main.Main;

public class MySQL {

	private static Connection con;
	static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(Main.config);
	private static String host = "localhost", database = "knockout", username = "knockout", password = "KN0CK0UT_PW";
	//private static String host = Main.getMain().getConfig().getString("host"), database = Main.getMain().getConfig().getString("database"), username = Main.getMain().getConfig().getString("user"), password = Main.getMain().getConfig().getString("password");
	private static int port = 3306;
	private static List<Player> buildingPlayers = new ArrayList<Player>();

	public static Connection getConnection() {
		return con;
	}

	public static void connect() {
		if (!Main.isBauPixel) {
			try {
				cfg.load(Main.config);
			} catch (IOException | InvalidConfigurationException e1) {
			}
			host = cfg.getString("host");
			database = cfg.getString("database");
			username = cfg.getString("user");
			password = cfg.getString("password");
		}
		if (!isConnected()) {
			try {
				con = DriverManager.getConnection(
						"jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username,
						password);
				Bukkit.getConsoleSender().sendMessage(Messages.prefix + "§aMySQL-Connection could be established!");
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage(Messages.prefix + "§4§lMySQL-Connection couldn't be established!");
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage(Messages.prefix + "§4§lThis plugin will be deactivated!");
				Bukkit.getPluginManager().disablePlugin(Main.getMain());
			}
		}
	}

	public static void close() {
		if (isConnected()) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isConnected() {
		return con != null;
	}

	public static void createTable() {
		if (isConnected()) {
			try {
				con.createStatement().executeUpdate(
						"CREATE TABLE IF NOT EXISTS players(player LONGTEXT, kills INT, tode INT, coins INT, points INT, kits VARCHAR(100), uuid VARCHAR(100) UNIQUE, playername VARCHAR(100))");
			} catch (SQLException e) {
				System.out.println("Konnte MySQL Tabelle nicht erstellen:");
				e.printStackTrace();
			}
		}
	}

	public static void update(String qry) {
		if (isConnected()) {
			try {
				con.createStatement().executeUpdate(qry);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static ResultSet getResult(String qry) {
		if (isConnected()) {
			try {
				return con.createStatement().executeQuery(qry);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static boolean playerExists(Player p) {
		try {
			PreparedStatement prepared = con.prepareStatement("SELECT * FROM players WHERE uuid=?");
			prepared.setString(1, p.getUniqueId().toString());
			ResultSet resultSet = prepared.executeQuery();
			boolean next = resultSet.next();
			resultSet.close();
			return next;
		} catch (SQLException e) {
			return false;
		}
	}

	public static void switchBuilding(Player p) {
		if (isBuilding(p)) {
			if (deutsch(p))
				p.sendMessage("§7[§6KnockOUT§7] §cDu kannst nun nicht mehr bauen!");
			else
				p.sendMessage("§7[§6KnockOUT§7] §cYou cannot build anymore!");
			buildingPlayers.remove(p);
			p.setGameMode(GameMode.SURVIVAL);
			return;
		} else {
			if (deutsch(p))
				p.sendMessage("§7[§6KnockOUT§7] §aDu kannst nun bauen!");
			else
				p.sendMessage("§7[§6KnockOUT§7] §aYou can now build!");
			buildingPlayers.add(p);
			p.setGameMode(GameMode.CREATIVE);
			return;
		}
	}

	public static boolean isBuilding(Player p) {
		return buildingPlayers.contains(p);
	}

	public static void resetStats(Player p) {
		update("DELETE FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
		registerPlayer(p);
	}

	public static void setKills(Player p, int kills) {
		update("UPDATE players SET kills=" + kills + " WHERE uuid='" + p.getUniqueId().toString() + "'");
		setPoint(p);
	}

	public static void setDeaths(Player p, int deaths) {
		update("UPDATE players SET tode=" + deaths + " WHERE uuid='" + p.getUniqueId().toString() + "'");
		setPoint(p);
	}

	public static void setCoins(Player p, int coins) {
		update("UPDATE players SET coins=" + coins + " WHERE uuid='" + p.getUniqueId().toString() + "'");
	}

	public static void addKill(Player p) {
		update("UPDATE players SET kills=" + (getKills(p) + 1) + " WHERE uuid='" + p.getUniqueId().toString() + "'");
		setPoint(p);
	}

	public static void addDeath(Player p) {
		update("UPDATE players SET tode=" + (getDeaths(p) + 1) + " WHERE uuid='" + p.getUniqueId().toString() + "'");
		setPoint(p);
	}

	public static void addCoins(Player p, int coins) {
		update("UPDATE players SET coins=" + (getCoins(p) + coins) + " WHERE uuid='" + p.getUniqueId().toString()
				+ "'");
	}

	public static String getKits(Player p) {
		try {
			ResultSet result = getResult("SELECT kits FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			if (result.next()) {
				String i = result.getString(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "standard;";
	}

	public static void addKit(Player p, String kitname) {
		update("UPDATE players SET kits='" + (getKits(p) + kitname) + ";' WHERE uuid='" + p.getUniqueId().toString()
				+ "'");
	}

	public static boolean hasKitAccess(Player p, String kit) {
		try {
			PreparedStatement prepared = con.prepareStatement("SELECT kits FROM players WHERE uuid=?");
			prepared.setString(1, p.getUniqueId().toString());
			ResultSet resultSet = prepared.executeQuery();
			boolean next = false;
			if (resultSet.next()) {
				next = resultSet.getString(1).contains(kit);
			}
			resultSet.close();
			return next;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void registerPlayer(Player p) {
		if (playerExists(p))
			setPlayername(p);
		if (!playerExists(p))
			update("INSERT INTO players(player, kills, tode, coins, points, kits, uuid, playername) VALUES ('" + p + "',0, 0, 0, 0, 'standard;', '"
					+ p.getUniqueId().toString() + "', '" + p.getName() + "')");
	}

	public static int getKills(Player p) {
		try {
			ResultSet result = getResult("SELECT kills FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getDeaths(Player p) {
		try {
			ResultSet result = getResult("SELECT tode FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getCoins(Player p) {
		try {
			ResultSet result = getResult("SELECT coins FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getKills(OfflinePlayer p) {
		try {
			ResultSet result = getResult("SELECT kills FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getDeaths(OfflinePlayer p) {
		try {
			ResultSet result = getResult("SELECT tode FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getCoins(OfflinePlayer p) {
		try {
			ResultSet result = getResult("SELECT coins FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getKills(String playername) {
		try {
			ResultSet result = getResult("SELECT kills FROM players WHERE playername='" + playername + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getDeaths(String playername) {
		try {
			ResultSet result = getResult("SELECT tode FROM players WHERE playername='" + playername + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getCoins(String playername) {
		try {
			ResultSet result = getResult("SELECT coins FROM players WHERE playername='" + playername + "'");
			if (result.next()) {
				int i = result.getInt(1);
				result.close();
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void setPoint(Player p) {
		int kills = 0, tode = 0, points = 0;
		kills = getKills(p);
		tode = getDeaths(p);
		points = (kills * 12) - (tode * 3);
		update("UPDATE players SET points=" + points + " WHERE uuid='" + p.getUniqueId().toString() + "'");
	}

	public static int getPoints(Player p) {
		int kills = 0, tode = 0, points = 0;
		kills = getKills(p);
		tode = getDeaths(p);
		points = (kills * 12) - (tode * 3);
		return points;
	}
	
	public static String getPlayername(Player p) {
		try {
			ResultSet result = getResult("SELECT playername FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			String str = p.getUniqueId().toString();
			if (result.next()) {
				str = result.getString(1);
			}
			result.close();
			return str;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p.getUniqueId().toString();
	}
	
	public static String getPlayername(OfflinePlayer p) {
		try {
			ResultSet result = getResult("SELECT playername FROM players WHERE uuid='" + p.getUniqueId().toString() + "'");
			String str = p.getUniqueId().toString();
			if (result.next()) {
				str = result.getString(1);
			}
			result.close();
			return str;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p.getUniqueId().toString();
	}

	public static String getPlayername(String uuid) {
		try {
			ResultSet result = getResult("SELECT playername FROM players WHERE uuid='" + uuid + "'");
			String str = uuid;
			if (result.next()) {
				str = result.getString(1);
			}
			if (str == "") {
				str = uuid;
			}
			result.close();
			return str;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return uuid;
	}

	public static void setPlayername(Player p) {
		update("UPDATE players SET playername='" + p.getName() + "' WHERE uuid='" + p.getUniqueId().toString() + "'");
	}


	public static HashMap<Integer, String> getTop10() {
		HashMap<Integer, String> names = new HashMap<Integer, String>();
		try {
			ResultSet result = getResult("SELECT * FROM `players` ORDER BY `players`.`kills` DESC LIMIT 10");
			int i = 0;
			while (result.next()) {
				names.put(++i, "§e" + i + ". §7" + getPlayername(result.getString("uuid")) + " §b"
						+ result.getString("kills"));
				if (i == 11) {
					break;
				}
			}
			result.next();
			result.close();
		} catch (SQLException e) {
		}
		return names;
	}

	public static HashMap<Integer, String> getTop10Tode() {
		HashMap<Integer, String> names = new HashMap<Integer, String>();
		try {
			ResultSet result = getResult("SELECT * FROM `players` ORDER BY `players`.`tode` DESC LIMIT 10");
			int i = 0;
			while (result.next()) {
				names.put(++i, "§e" + i + ". §7" + getPlayername(result.getString("uuid")) + " §b"
						+ result.getString("tode"));
				if (i == 11) {
					break;
				}
			}
			result.next();
			result.close();
		} catch (SQLException e) {
		}
		return names;
	}

	public static HashMap<Integer, String> getTop10Coins() {
		HashMap<Integer, String> names = new HashMap<Integer, String>();
		try {
			ResultSet result = getResult("SELECT * FROM `players` ORDER BY `players`.`coins` DESC LIMIT 10");
			int i = 0;
			while (result.next()) {
				names.put(++i, "§e" + i + ". §7" + getPlayername(result.getString("uuid")) + " §b"
						+ result.getString("coins"));
				if (i == 11) {
					break;
				}
			}
			result.next();
			result.close();
		} catch (SQLException e) {
		}
		return names;
	}

	public static Player getPlayer(String string, boolean isUUID) {
		Player p = null;
		if (!isUUID) {
			try {
				ResultSet result = getResult("SELECT player FROM players WHERE playername='" + username + "'");
				if (result.next()) {
					p = (Player) result.getObject(1);
				}
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ResultSet result = getResult("SELECT player FROM players WHERE uuid='" + username + "'");
				if (result.next()) {
					p = (Player) result.getObject(1);
				}
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	public static boolean deutsch(Player p) {
		return p.spigot().getLocale().equals("de_DE") ? true : false;
	}

}
