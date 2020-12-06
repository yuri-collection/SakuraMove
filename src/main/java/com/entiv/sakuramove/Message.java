package com.entiv.sakuramove;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    public static void send(CommandSender sender, String message) {
        if (message == null || message.isEmpty()) return;
        sender.sendMessage(toColor(message));
    }

    public static void sendActionBar(Player player, String message) {
        if (message == null || message.isEmpty()) return;
        player.sendActionBar(toColor(message));
    }

    public static String toColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
