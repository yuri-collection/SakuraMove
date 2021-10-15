package com.entiv.sakuramove.listener;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.event.StaminaChangeEvent;
import com.entiv.sakuramove.progress.ActionBarProgress;
import com.entiv.sakuramove.progress.BossBarProgress;
import com.entiv.sakuramove.progress.Progress;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StaminaChangeListener implements Listener {

    @EventHandler
    private void onStaminaChange(StaminaChangeEvent event) {

        Player player = event.getPlayer();

        int before = event.getBefore();
        int after = event.getAfter();

        FileConfiguration config = Main.getInstance().getConfig();

        if (config.getBoolean("体力展示设置.ActionBar.开启")) {
            Progress process = new ActionBarProgress();
            process.show(player, before, after);
        }

        if (config.getBoolean("体力展示设置.BossBar.开启")) {
            Progress process = new BossBarProgress();
            process.show(player, before, after);
        }
    }
}
