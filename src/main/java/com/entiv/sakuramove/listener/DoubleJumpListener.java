package com.entiv.sakuramove.listener;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.action.DoubleJump;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

public class DoubleJumpListener implements Listener {

    private final DoubleJump doubleJump = DoubleJump.getInstance();

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if (doubleJump.canAccept(player)) {
            doubleJump.accept(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        Player player = event.getEntity() instanceof Player ? ((Player) event.getEntity()) : null;
        boolean isFall = event.getCause().equals(EntityDamageEvent.DamageCause.FALL);
        boolean enable = Main.getInstance().getConfig().getBoolean("移动行为.二段跳.摔落伤害");

        if (!enable && player != null && isFall) {
            event.setCancelled(true);
        }
    }
}