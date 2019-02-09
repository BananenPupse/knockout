package de.kevin.knockout.api;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class TitleAPI {
	
	public static void sendTitle(Player p, String msg, int fadein, int stay, int fadeout) {

		PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.TITLE,
				ChatSerializer.a("{\"text\":\"" + msg + "\"}"), fadein, stay, fadeout);

		PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;

		con.sendPacket(packet);

	}

	public static void sendSubTitle(Player p, String msg, int fadein, int stay, int fadeout) {

		PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
				ChatSerializer.a("{\"text\":\"" + msg + "\"}"), fadein, stay, fadeout);

		PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;

		con.sendPacket(packet);

	}

	public static void sendActionBar(Player p, String msg) {

		CraftPlayer player = (CraftPlayer) p;

		IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + msg + "\"}");

		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);

		player.getHandle().playerConnection.sendPacket(ppoc);

	}

}