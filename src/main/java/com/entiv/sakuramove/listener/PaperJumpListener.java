package com.entiv.sakuramove.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.action.DoubleJump;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

public class PaperJumpListener implements Listener {

    private final DoubleJump doubleJump = DoubleJump.getInstance();

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
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isOnGround()) {
            doubleJump.disable(player);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        Player player = event.getEntity() instanceof Player ? ((Player) event.getEntity()) : null;
        boolean isFall = event.getCause().equals(EntityDamageEvent.DamageCause.FALL);
        boolean enable = Main.getInstance().getConfig().getBoolean("冲刺.二段跳.摔落伤害");

        if (enable && player != null && isFall) {
            event.setCancelled(true);
        }
    }
}