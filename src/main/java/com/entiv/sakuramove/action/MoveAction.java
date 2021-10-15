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

    protected final int stamina;
    protected final double power;

    public MoveAction(int stamina) {
        this.stamina = stamina;
        this.power = 1;
    }

    public MoveAction(double power, int stamina) {
        this.stamina = stamina;
        this.power = power;
    }

    public MoveAction(String path) {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection(path);

        Validate.notNull(section, "配置文件错误, 请检查配置文件");

        power = section.getDouble("行动强度");
        stamina = section.getInt("所需体力");
    }

    public void accept(Player player) {

        StaminaManager staminaManager = getStaminaManager();
        StaminaPlayer staminaPlayer = staminaManager.getPlayer(player);
        if (staminaPlayer.getCurrentStamina() < stamina) return;

        staminaPlayer.decreaseStamina(stamina);
        behavior().accept(player);
    }

    public StaminaManager getStaminaManager() {
        return Main.getInstance().getStaminaManager();
    }

    protected abstract Consumer<Player> behavior();

    public abstract boolean canAccept(Player player);
}
