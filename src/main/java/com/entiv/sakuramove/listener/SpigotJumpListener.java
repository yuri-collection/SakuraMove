package com.entiv.sakuramove.listener;

import com.entiv.sakuramove.action.DamageableJump;
import com.entiv.sakuramove.event.SpigotJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// copy from https://www.spigotmc.org/resources/playerairevents-spigot-api-extension.39955/
public class SpigotJumpListener implements Listener {

    private final Map<UUID, Integer> hasJumped = new HashMap<>();
    private final Map<UUID, Boolean> wasAirborn = new HashMap<>();

    private final DamageableJump doubleJump = DamageableJump.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerInAir(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getEyeLocation().getBlock().getType().equals(Material.WATER) || player.getEyeLocation().getBlock().getType().equals(Material.LADDER)) {
            resetPlayer(player);
            return;
        }
        if (player.isOnGround()) {
            resetPlayer(player);
            return;
        }

        Location f = event.getFrom(), t = event.getTo();
        // Jump
        if (f.getBlock().getY() < t.getBlock().getY()
                && !(f.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR))
                && t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR)) {
            if (!(wasAirborn.containsKey(player.getUniqueId()))) {
                wasAirborn.put(player.getUniqueId(), true);
            }
            int jumped = 1;
            if (hasJumped.containsKey(player.getUniqueId())) {
                jumped = hasJumped.get(player.getUniqueId());
            }
            Bukkit.getServer().getPluginManager().callEvent(new SpigotJumpEvent(player, jumped, event.getFrom(), event.getTo()));
        }
    }

    public void resetPlayer(Player player) {
        hasJumped.remove(player.getUniqueId());
        wasAirborn.put(player.getUniqueId(), false);
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (doubleJump.canAccept(player)) {
            doubleJump.accept(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJump(SpigotJumpEvent event) {
        Player player = event.getPlayer();
        doubleJump.enable(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        doubleJump.clearCache(player);
    }
}
