package com.entiv.sakuramove.action;

import com.entiv.sakuramove.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class DoubleJump extends MoveAction {

    private static final DoubleJump doubleJump = new DoubleJump("移动行为.二段跳");
    private final double springPower;

    public DoubleJump(String path) {
        super(path);

        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("移动行为.二段跳");
        springPower = section.getDouble("冲刺力度");
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

    @Override
    public boolean canAccept(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR || player.hasPermission("sakuramove.fly")) {
            return false;
        }

        return player.isOp() || !player.hasPermission("sakuramove.fly");
    }

    public void enable(Player player) {
        if (getStaminaManager().getPlayer(player).hasEnoughStamina(stamina)) {
            player.setAllowFlight(true);
            player.setFlying(false);
        }
    }

    public void disable(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    @Override
    public void accept(Player player) {
        super.accept(player);
    }

    public static DoubleJump getInstance() {
        return doubleJump;
    }
}

