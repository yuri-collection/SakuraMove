package com.entiv.sakuramove.listener;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.action.Sprint;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class SprintListener implements Listener {

    Sprint sprint = Sprint.getInstance();
    public static final Set<UUID> disablePlayers = new HashSet<>();

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {

        Action action = event.getAction();
        Player player = event.getPlayer();

        if (disablePlayers.contains(player.getUniqueId())) return;

        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (sprint.canAccept(player)) {
            sprint.accept(player);
            sprint.setCoolDown(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        sprint.clearCache(player);
        disablePlayers.remove(player.getUniqueId());
    }
}
