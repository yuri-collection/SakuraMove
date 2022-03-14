package com.entiv.sakuramove.action;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaManager;
import com.entiv.sakuramove.manager.StaminaPlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class MoveAction {

    private final String path;

    public MoveAction(String path) {
        this.path = path;
    }

    public void accept(Player player) {
        StaminaManager staminaManager = getStaminaManager();
        StaminaPlayer staminaPlayer = staminaManager.getPlayer(player);
        if (staminaPlayer.getCurrentStamina() < getStamina()) return;

        staminaPlayer.decreaseStamina(getStamina());
        behavior().accept(player);
    }

    public ConfigurationSection getConfig() {
        return Main.getInstance().getConfig().getConfigurationSection(path);
    }

    public int getStamina() {
        return getConfig().getInt("所需体力");
    }

    public double getPower() {
        return getConfig().getDouble("行动强度");
    }

    public StaminaManager getStaminaManager() {
        return Main.getInstance().getStaminaManager();
    }

    protected abstract Consumer<Player> behavior();

    public abstract boolean canAccept(Player player);
}
