package com.entiv.sakuramove.listener;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockListener implements Listener {

    @EventHandler
    private void onPlayerBreakBlock(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final StaminaPlayer staminaPlayer = Main.getInstance().getStaminaManager().getPlayer(player);

        final int currentStamina = staminaPlayer.getCurrentStamina();

    }
}
