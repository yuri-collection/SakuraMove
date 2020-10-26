package com.entiv.sakuramove;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {

    HashMap<UUID, Long> playerSprintMap = new HashMap<>();
    
    //TODO 增加冷却时间

    @EventHandler
    public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();

        if (player.isSprinting()) {
            Location launchLocation = player.getLocation();
            player.setVelocity(launchLocation.getDirection().setY(0));
            playerSprintMap.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

}
