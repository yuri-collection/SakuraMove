package com.entiv.sakuramove.progress;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaPlayer;
import com.entiv.sakuramove.utils.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class ActionbarProgress implements Progress {

    @Override
    public void show(Player player, int before, int after) {

        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("体力展示设置.ActionBar");

        if (section == null) throw new NullPointerException("配置文件异常! 请检查配置文件!");

        boolean isEnable = section.getBoolean("开启");

        if (!isEnable) return;

        String icon = section.getString("体力槽样式");
        String emptyColor = section.getString("空体力槽颜色代码");
        String fullColor = section.getString("体力槽颜色代码");

        StaminaPlayer staminaPlayer = staminaManager.getPlayer(player);
        int maxStamina = staminaPlayer.getMaxStamina();

        ProgressState progressState = staminaPlayer.getProgressState();
        progressState.setAfter(after);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                StringBuilder progressBuilder = new StringBuilder();

                for (int i = 0; i < maxStamina; i++) {
                    progressBuilder.append(icon);
                }

                int current = progressState.getCurrent();
                int after = progressState.getAfter();

                String progress = progressBuilder
                        .insert(current, Message.toColor(emptyColor))
                        .insert(0, Message.toColor(fullColor))
                        .toString();
                Message.sendActionBar(player, progress);

                if (current == after) {
                    cancel();
                }
            }
        };

        runnable.runTaskTimer(Main.getInstance(), 0, 1);
    }
}
