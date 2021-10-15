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

        Iterator<StaminaPlayer> iterator = staminaManager.players.values().iterator();

        while (iterator.hasNext()) {
            StaminaPlayer staminaPlayer = iterator.next();
            ProgressState progressState = staminaPlayer.getProgressState();

            int current = progressState.getCurrent();
            int after = progressState.getAfter();

            if (!staminaPlayer.getPlayer().isOnline()) {
                iterator.remove();
                continue;
            }

            if (current > after) {
                progressState.setCurrent(current - 1);
            } else if (current < after) {
                progressState.setCurrent(current + 1);
            }

            if (progressState.getCurrent() == staminaPlayer.getMaxStamina()) {
                iterator.remove();
            }
        }
    }
}
