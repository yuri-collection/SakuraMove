package com.entiv.sakuramove;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerListener implements Listener {

    private final Map<UUID, Long> coolDownPlayers = new HashMap<>();
    private final FileConfiguration config = Main.getInstance().getConfig();

    @EventHandler
    public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission("SakuraMove.Sprinting")) return;

        int coolDown = config.getInt("CoolDown");

        if (coolDown <= 0) {
            playerSprinting(player);
            return;
        }

        if (player.isSprinting() && !isCoolDown(player, coolDown)) {
            playerSprinting(player);
            setCoolDown(player);
        } else {
            String message = config.getString("Message.CoolDown");
            Message.sendActionBar(player, message);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // gc
        coolDownPlayers.remove(event.getPlayer().getUniqueId());
    }

    private void playerSprinting(Player player) {
        Location location = player.getLocation();
        double power = config.getDouble("Power");
        player.setVelocity(location.getDirection().setY(0).multiply(power));
    }

    private boolean isCoolDown(Player player, int coolDown) {
        Long value = coolDownPlayers.get(player.getUniqueId());
        return value != null && System.currentTimeMillis() - value < coolDown * 1000L;
    }

    private void setCoolDown(Player player) {
        coolDownPlayers.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
