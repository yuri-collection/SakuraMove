package com.entiv.sakuramove.action;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Sprint extends MoveAction {

    private static final Sprint sprint = new Sprint("移动行为.冲刺");
    private final Map<UUID, Long> coolDownPlayers = new HashMap<>();

    public Sprint(String path) {
        super(path);
    }

    @Override
    protected Consumer<Player> behavior() {
        return player -> {
            World world = player.getWorld();
            Location location = player.getLocation();

            player.playSound(location, Sound.ENTITY_BAT_TAKEOFF, 10, 0);
            player.setVelocity(location.getDirection().setY(0).multiply(power));
            world.spawnParticle(Particle.CRIT, location, 10);
        };
    }

    @Override
    public boolean canAccept(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        FileConfiguration config = Main.getInstance().getConfig();
        List<String> allowItems = config.getStringList("移动行为.冲刺.允许物品");

        for (String allowItem : allowItems) {
            boolean isAllowItem = itemStack.getType().toString().contains(allowItem);

            if (!isAllowItem) continue;

            if (player.isSprinting() && !sprint.isCoolDown(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCoolDown(Player player) {
        Long value = coolDownPlayers.get(player.getUniqueId());
        return value != null && System.currentTimeMillis() - value < 500;
    }

    public void setCoolDown(Player player) {
        coolDownPlayers.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @Override
    public void clearCache(Player player) {
        super.clearCache(player);
        coolDownPlayers.remove(player.getUniqueId());
    }

    public static Sprint getInstance() {
        return sprint;
    }
}
