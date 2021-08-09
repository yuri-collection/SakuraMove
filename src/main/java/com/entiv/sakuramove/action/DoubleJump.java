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
    private JumpingChecker jumpingChecker;

    public DoubleJump(String path) {
        super(path);

        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("移动行为.二段跳");
        springPower = section.getDouble("冲刺力度");
    }

    @Override
    protected Consumer<Player> behavior() {
        return player -> {
            jumpingPlayer.add(player.getUniqueId());
            disable(player);

            Location location = player.getLocation();
            player.setVelocity(location.getDirection().multiply(springPower).setY(power));
            player.playSound(location, Sound.ENTITY_BAT_TAKEOFF, 10, 0);
            player.getWorld().spawnParticle(Particle.CRIT, location, 10);

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

    public void enableJumpingChecker() {
        jumpingChecker = new JumpingChecker();
        jumpingChecker.runTaskTimer(Main.getInstance(), 10, 5);
    }

    public void disableJumpingChecker() {
        if (jumpingChecker != null) {
            jumpingChecker.cancel();
        }
    }

    public static DoubleJump getInstance() {
        return doubleJump;
    }

    @Override
    public void clearCache(Player player) {
        super.clearCache(player);
        jumpingPlayer.remove(player.getUniqueId());
    }

    private class JumpingChecker extends BukkitRunnable {

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

