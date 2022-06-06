package com.entiv.sakuramove.listener;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerBreakBlock(BlockBreakEvent event) {
        final FileConfiguration config = Main.getInstance().getConfig();
        final ConfigurationSection section = config.getConfigurationSection("其他行为.破坏方块");

        if (section == null || !section.getBoolean("开启")) return;

        final Player player = event.getPlayer();
        final StaminaPlayer staminaPlayer = Main.getInstance().getStaminaManager().getPlayer(player);

        final int currentStamina = staminaPlayer.getCurrentStamina();

        final int needStamina = section.getInt("所需体力");

        if (currentStamina < needStamina) {
            event.setCancelled(true);

            final String message = config.getString("Message.破坏方块");

            if (message != null && !message.isEmpty()) {
                player.sendMessage(message);
            }
        } else {
            staminaPlayer.decreaseStamina(needStamina);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerPlaceBlock(BlockPlaceEvent event) {
        final FileConfiguration config = Main.getInstance().getConfig();
        final ConfigurationSection section = config.getConfigurationSection("其他行为.放置方块");

        if (section == null || !section.getBoolean("开启")) return;

        final Player player = event.getPlayer();
        final StaminaPlayer staminaPlayer = Main.getInstance().getStaminaManager().getPlayer(player);

        final int currentStamina = staminaPlayer.getCurrentStamina();

        if (currentStamina < section.getInt("所需体力")) {
            event.setCancelled(true);

            final String message = Main.getInstance().getConfig().getString("Message.放置方块");

            if (message != null && !message.isEmpty()) {
                player.sendMessage(message);
            }
        } else {
            staminaPlayer.decreaseStamina(section.getInt("所需体力"));
        }
    }
}
