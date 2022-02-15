package com.entiv.sakuramove.progress;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaPlayer;
import com.entiv.sakuramove.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class BossBarProgress implements Progress {

    private static final BossBar bossBar = Bukkit.createBossBar(
            Message.toColor(Main.getInstance().getConfig().getString("体力展示设置.BossBar.标题")),
            BarColor.RED,
            BarStyle.SOLID
    );

    @Override
    public void show(Player player, int before, int after) {

        final Main plugin = Main.getInstance();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("体力展示设置.BossBar");

        if (section == null) throw new NullPointerException("配置文件异常! 请检查配置文件!");

        bossBar.addPlayer(player);

        StaminaPlayer staminaPlayer = staminaManager.getPlayer(player);
        int maxStamina = staminaPlayer.getMaxStamina();
        ProgressState progressState = staminaPlayer.getProgressState();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                int current = progressState.getCurrent();

                double progress = (double) current / maxStamina;
                bossBar.setProgress(progress);

                if (current == maxStamina) {
                    cancel();
                    Bukkit.getScheduler().runTaskLater(plugin, () -> bossBar.removePlayer(player), 20L);
                }
            }
        };

        runnable.runTaskTimer(plugin, 1, 1);
    }
}
