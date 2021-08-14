package com.entiv.sakuramove.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.entiv.sakuramove.action.DamageableJump;
import com.entiv.sakuramove.event.SpigotJumpEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class DamageableJumpListener implements Listener {

    private final DamageableJump doubleJump = DamageableJump.getInstance();

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (doubleJump.canAccept(player)) {
            doubleJump.accept(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        doubleJump.enable(player);
    }

    @EventHandler
    public void onPlayerJump(SpigotJumpEvent event) {
        Player player = event.getPlayer();
        doubleJump.enable(player);
    }
}