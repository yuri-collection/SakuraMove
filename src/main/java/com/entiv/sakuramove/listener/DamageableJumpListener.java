package com.entiv.sakuramove.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.entiv.sakuramove.action.DamageableJump;
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

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR || player.hasPermission("sakuramove.fly")) {
            return;
        }

        if (!player.isOp() && player.hasPermission("sakuramove.fly")) {
            return;
        }

        doubleJump.accept(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        doubleJump.enable(player);
    }
}