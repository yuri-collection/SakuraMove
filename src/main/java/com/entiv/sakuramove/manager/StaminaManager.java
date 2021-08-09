package com.entiv.sakuramove.manager;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

//TODO 增加消耗体力时的进度条扣除动画, 需要单独弄一个类渲染, 数据和展示分离
public class StaminaManager extends BukkitRunnable {

    private static final String ICON = "█";
    private static final String CALIBRATION = "▉";

    private final Map<UUID, Integer> playerStamina = new HashMap<>();
    private final Map<UUID, Long> recoveryStaminaCoolDown = new HashMap<>();

    public void showProgressPercentage(Player player) {

        int stamina = getStamina(player);
        int maxStamina = getMaxStamina(player);

        StringBuilder progressBuilder = new StringBuilder();

        for (int i = 0; i < maxStamina; i++) {
            progressBuilder.append(ICON);
        }

        for (int i = 100; i < maxStamina; i += 20) {
            progressBuilder.replace(i - 1, i, CALIBRATION);
        }

        String progress = progressBuilder
                .insert(stamina, Message.toColor("&7"))
                .insert(0, Message.toColor("&a"))
                .toString();
        Message.sendActionBar(player, progress);
    }

    public void start() {
        ConfigurationSection config = Main.getInstance().getConfig().getConfigurationSection("体力恢复设置");
        runTaskTimer(Main.getInstance(), 0, config == null ? 1 : config.getInt("恢复速度"));
    }

    @Override
    public void run() {
        Iterator<UUID> iterator = playerStamina.keySet().iterator();

        while (iterator.hasNext()) {
            UUID uuid = iterator.next();

            Player player = Bukkit.getPlayer(uuid);

            if (!isRecoveryStaminaCoolDown(player)) {
                increaseStamina(player, 1);
            }

            if (getStamina(player) >= getMaxStamina(player)) {
                iterator.remove();
            }
        }
    }

    public int getStamina(Player player) {
        Integer endurance = playerStamina.get(player.getUniqueId());
        return endurance == null ? getMaxStamina(player) : endurance;
    }
    public boolean hasStamina(Player player, int stamina) {
        return getStamina(player) >= stamina;
    }

    public void decreaseStamina(Player player, int stamina) {
        UUID uniqueId = player.getUniqueId();

        int value = getStamina(player) - stamina;
        if (value <= 0) value = 0;

        playerStamina.put(uniqueId, value);
        recoveryStaminaCoolDown.put(uniqueId, System.currentTimeMillis());

        showProgressPercentage(player);
    }

    public void increaseStamina(Player player, int v) {
        int stamina = getStamina(player);
        int maxStamina = getMaxStamina(player);

        UUID uniqueId = player.getUniqueId();

        if (stamina < maxStamina) {
            playerStamina.put(uniqueId, stamina + v);
        } else {
            playerStamina.put(uniqueId, maxStamina);
        }

        showProgressPercentage(player);
    }

    public int getMaxStamina(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();

        ConfigurationSection section = config.getConfigurationSection("体力上限设置");

        int stamina = 0;

        for (String key : section.getKeys(false)) {

            String permission = "sakuramove.stamina." + key;

            if (player.hasPermission(permission)) {
                stamina = section.getInt(key);
            }
        }

        if (stamina == 0) stamina = section.getInt("default");

        return stamina;
    }

    private boolean isRecoveryStaminaCoolDown(Player player) {
        ConfigurationSection config = Main.getInstance().getConfig().getConfigurationSection("体力恢复设置");
        int cooldown = config.getInt("恢复间隔");

        Long value = recoveryStaminaCoolDown.get(player.getUniqueId());
        return value != null && System.currentTimeMillis() - value < cooldown;
    }

    public void clearCache(Player player) {
        UUID uniqueId = player.getUniqueId();

        playerStamina.remove(uniqueId);
        recoveryStaminaCoolDown.remove(uniqueId);
    }
}
