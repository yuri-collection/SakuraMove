package com.entiv.sakuramove.manager;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.event.StaminaChangeEvent;
import com.entiv.sakuramove.progress.ProgressState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaminaPlayer {

    @Getter
    private final Player player;

    @Getter
    @Setter
    private int maxStamina;

    @Getter
    @Setter
    private int currentStamina;

    @Getter
    private final ProgressState progressState;

    public StaminaPlayer(Player player, int maxStamina) {
        this.player = player;
        this.currentStamina = maxStamina;
        this.maxStamina = maxStamina;

        progressState = new ProgressState(currentStamina, currentStamina);
    }

    public void increaseStamina(int add) {
        int before = currentStamina;
        int after = currentStamina + add;

        if (after >= maxStamina) {
            after = maxStamina;
        }

        currentStamina = after;
        progressState.setAfter(after);

        Bukkit.getPluginManager().callEvent(new StaminaChangeEvent(player, before, after));
    }

    public void decreaseStamina(int stamina) {
        int before = currentStamina;
        int after = before - stamina;
        if (after <= 0) after = 0;

        currentStamina = after;
        progressState.setAfter(after);

        Main.getInstance().getStaminaManager().cooldownManager.setCooldownMillisecond(player, Main.getInstance().getConfig().getInt("体力恢复设置.恢复间隔", 1000));
        Bukkit.getPluginManager().callEvent(new StaminaChangeEvent(player, before, after));
    }

    public boolean hasEnoughStamina(int stamina) {
        return currentStamina >= stamina;
    }


}
