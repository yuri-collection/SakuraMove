package com.entiv.sakuramove.action;

import com.entiv.sakuramove.Main;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
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
        ConfigurationSection section = config.getConfigurationSection("移动行为.冲刺.允许冲刺物品");

        Validate.notNull(section, "配置文件错误, 请检查配置文件");

        boolean isAllowItem = false;

        List<String> allowID = section.getStringList("id");
        List<String> allowName = section.getStringList("name");
        List<String> allowLore = section.getStringList("lore");

        for (String id : allowID) {
            if (itemStack.getType().toString().contains(id)) {
                isAllowItem = true;
            }
        }

        for (String name : allowName) {
            if (itemStack.getItemMeta().getDisplayName().contains(name)) {
                isAllowItem = true;
            }
        }

        for (String key : allowLore) {
            List<String> lore = itemStack.getItemMeta().getLore();
            if (lore == null) break;

            for (String string : lore) {
                if (string.contains(key)) {
                    isAllowItem = true;
                    break;
                }
            }
        }

        return player.isSprinting() && !sprint.isCoolDown(player) && isAllowItem;
    }

    public boolean isCoolDown(Player player) {
        Long value = coolDownPlayers.get(player.getUniqueId());
        return value != null && System.currentTimeMillis() - value < 500;
    }

    public void setCoolDown(Player player) {
        coolDownPlayers.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void clearCache(Player player) {
        coolDownPlayers.remove(player.getUniqueId());
    }

    public static Sprint getInstance() {
        return sprint;
    }
}
