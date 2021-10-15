package com.entiv.sakuramove.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager implements Listener {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    public CooldownManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean isCooldown(@NotNull Player player) {
        return isCooldown(player.getUniqueId());
    }

    public void setCooldownMillisecond(@NotNull Player player, long millisecond) {
        setCooldownMillisecond(player.getUniqueId(), millisecond);
    }

    public void setCooldownSecond(@NotNull Player player, long second) {
        setCooldownSecond(player.getUniqueId(), second);
    }

    public boolean isCooldown(UUID uuid) {
        Long value = cooldown.get(uuid);
        return value != null && System.currentTimeMillis() < value;
    }

    public void setCooldownMillisecond(UUID uuid, long millisecond) {
        cooldown.put(uuid, System.currentTimeMillis() + millisecond);
    }

    public void setCooldownSecond(UUID uuid, long second) {
        cooldown.put(uuid, System.currentTimeMillis() + second * 1000);
    }


    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        cooldown.remove(uuid);
    }
}
