package com.entiv.sakuramove.listener;

import com.entiv.sakuramove.action.DoubleJump;
import com.entiv.sakuramove.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJumpListener implements Listener {

    private final DoubleJump doubleJump = DoubleJump.getInstance();

    public DoubleJumpListener() {
        Bukkit.getOnlinePlayers().forEach(doubleJump::enable);
    }

    @EventHandler
    public void setFly(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        doubleJump.enable(player);
    }

    @EventHandler
    public void setVelocity(PlayerToggleFlightEvent event) {

        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR || player.hasPermission("sakuramove.fly")){
            return;
        }

        doubleJump.accept(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        doubleJump.enable(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        doubleJump.clearCache(player);
    }
}