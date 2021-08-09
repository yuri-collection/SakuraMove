package com.entiv.sakuramove.action;

import com.entiv.sakuramove.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class DamageableJump extends MoveAction {

    private static final DamageableJump damageableJump = new DamageableJump("移动行为.二段跳");
    private final double springPower;

    public DamageableJump(String path) {
        super(path);

        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("移动行为.二段跳");
        springPower = section.getDouble("冲刺力度");
    }

    @Override
    public void accept(Player player) {
        super.accept(player);
    }

    public static DamageableJump getInstance() {
        return damageableJump;
    }

    @Override
    public void clearCache(Player player) {
        super.clearCache(player);
    }

    @Override
    protected Consumer<Player> behavior() {
        return player -> {
            Location location = player.getLocation();
            player.setVelocity(location.getDirection().multiply(springPower).setY(power));
            player.playSound(location, Sound.ENTITY_BAT_TAKEOFF, 10, 0);
            player.getWorld().spawnParticle(Particle.CRIT, location, 15);

            disable(player);
        };
    }

    public void enable(Player player) {
        if (getStaminaManager().hasStamina(player, stamina)) {
            player.setAllowFlight(true);
            player.setFlying(false);
        }
    }

    public void disable(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}

