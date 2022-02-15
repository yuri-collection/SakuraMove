package com.entiv.sakuramove.task;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaManager;
import com.entiv.sakuramove.manager.StaminaPlayer;
import com.entiv.sakuramove.progress.ProgressState;

import java.util.Iterator;

public class DynamicProgressTask extends TimerTask {

    private final StaminaManager staminaManager = Main.getInstance().getStaminaManager();

    public DynamicProgressTask(int period) {
        super(period);
    }

    @Override
    public void hasNext() {
        for (StaminaPlayer staminaPlayer : staminaManager.players.values()) {
            ProgressState progressState = staminaPlayer.getProgressState();

            int current = progressState.getCurrent();
            int after = progressState.getAfter();

            if (!staminaPlayer.getPlayer().isOnline() || progressState.getAfter() > staminaPlayer.getMaxStamina()) {
                continue;
            }

            if (current > after) {
                progressState.setCurrent(current - 1);
            } else if (current < after) {
                progressState.setCurrent(current + 1);
            }
        }
    }
}
