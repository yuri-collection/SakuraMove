package com.entiv.sakuramove.action;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.entiv.sakuramove.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class DoubleJump extends MoveAction {

    private static final DoubleJump doubleJump = new DoubleJump("移动行为.二段跳");
    private final Set<UUID> jumpingCache = new HashSet<>();

    public DoubleJump(String path) {
        super(path);
    }

    @Override
    protected Consumer<Player> behavior() {
        return player -> {
            Location location = player.getLocation();
            player.setVelocity(location.getDirection().multiply(getSpringPower()).setY(getPower()));
            player.playSound(location, Sound.ENTITY_BAT_TAKEOFF, 10, 0);
            player.getWorld().spawnParticle(Particle.CRIT, location, 15);

            disable(player);
        };
    }

    public double getSpringPower() {
        return getConfig().getDouble("冲刺力度");
    }

    @Override
    public boolean canAccept(Player player) {
        if (!player.hasPermission("sakuramove.doublejump")) {
            return false;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return false;
        }

        return !jumpingCache.contains(player.getUniqueId());
    }

    public void enable(Player player) {
        if (getStaminaManager().getPlayer(player).hasEnoughStamina(getStamina())) {
            player.setAllowFlight(true);
            player.setFlying(false);

            jumpingCache.remove(player.getUniqueId());
        }
    }

    public void disable(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    @Override
    public void accept(Player player) {
        super.accept(player);
        jumpingCache.add(player.getUniqueId());
        disable(player);
    }

    public static DoubleJump getInstance() {
        return doubleJump;
    }

}

