package de.kevin.knockout.api;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {
	
	public static File file = new File("plugins/KnockOUT/messages.yml");
	public static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static String prefix = "§7[§6KnockOUT§7] §r";
	public static String perms = "§cDu hast dafür keine Rechte!";

}
