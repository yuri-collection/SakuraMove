package com.entiv.sakuramove;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }

    public static String toColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
