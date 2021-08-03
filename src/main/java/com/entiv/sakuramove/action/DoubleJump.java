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

public class DoubleJump extends MoveAction {

    private static final DoubleJump doubleJump = new DoubleJump("移动行为.二段跳");
    private final List<UUID> jumpingPlayer = new ArrayList<>();

    private final double springPower;



    public DoubleJump(String path) {
        super(path);

        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("移动行为.二段跳");
        springPower = section.getDouble("冲刺力度");

        JumpManager jumpingChecker = new JumpManager();
        jumpingChecker.runTaskTimer(Main.getInstance(), 10, 10);
    }

    @Override
    protected Consumer<Player> behavior() {
        return player -> {
            Location location = player.getLocation();

            disable(player);

            player.setVelocity(location.getDirection().multiply(springPower).setY(power));
            player.playSound(location, Sound.ENTITY_BAT_TAKEOFF, 10, 0);
            player.getWorld().spawnParticle(Particle.CRIT, location, 10);

            jumpingPlayer.add(player.getUniqueId());
        };
    }

    public void enable(Player player) {
        player.setAllowFlight(true);
        player.setFlying(false);
    }

    public void disable(Player player) {
        player.setAllowFlight(false);
    }

    public static DoubleJump getInstance() {
        return doubleJump;
    }

    @Override
    public void clearCache(Player player) {
        super.clearCache(player);

        jumpingPlayer.remove(player.getUniqueId());
    }

    public class JumpManager extends BukkitRunnable {

        @Override
        public void run() {
            for (UUID uuid : jumpingPlayer) {
                Player player = Main.getInstance().getServer().getPlayer(uuid);

                if (player != null && player.isOnGround()) {
                    enable(player);
                }
            }
        }

    }
}

