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

    private static Map<UUID, Long> playerSprintTime;
    private final FileConfiguration config = Main.getInstance().getConfig();

    PlayerListener() {
        int coolDown = config.getInt("CoolDown");
        if (coolDown != 0) {
            playerSprintTime = new HashMap<>();
        }
    }

    @EventHandler
    public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent event) {

        Player player = event.getPlayer();
        int coolDown = config.getInt("CoolDown");

        if (coolDown <= 0) {
            playerSprinting(player);
            return;
        }

        long nowTime = System.currentTimeMillis() / 1000;
        Long lastTime = playerSprintTime.get(player.getUniqueId());

        if (player.isSprinting() && (lastTime == null || nowTime - lastTime > coolDown)) {
            playerSprinting(player);
            playerSprintTime.put(player.getUniqueId(), nowTime);
        } else {
            String message = config.getString("Message.CoolDown");
            Message.send(player, message);
        }
    }

    private void playerSprinting(Player player) {
        Location location = player.getLocation();
        double power = config.getDouble("Power");
        player.setVelocity(location.getDirection().setY(0).multiply(power));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        int coolDown = config.getInt("CoolDown");
        if (coolDown <= 0) return;

        playerSprintTime.remove(event.getPlayer().getUniqueId());
    }
}
