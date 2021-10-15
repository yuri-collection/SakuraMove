package com.entiv.sakuramove.listener;

import com.entiv.sakuramove.event.StaminaChangeEvent;
import com.entiv.sakuramove.progress.ActionbarProgress;
import com.entiv.sakuramove.progress.Progress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StaminaChangeListener implements Listener {

    @EventHandler
    private void onStaminaChange(StaminaChangeEvent event) {

        Progress process = new ActionbarProgress();

        Player player = event.getPlayer();
        int before = event.getBefore();
        int after = event.getAfter();

        process.show(player, before, after);
    }
}
