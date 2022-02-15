package com.entiv.sakuramove.task;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaManager;
import com.entiv.sakuramove.manager.StaminaPlayer;
import com.entiv.sakuramove.utils.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class RecoveryStaminaTask extends TimerTask {

    public RecoveryStaminaTask(int period) {
        super(period);
    }

    @Override
    public void hasNext() {
        StaminaManager staminaManager = Main.getInstance().getStaminaManager();

        Map<UUID, StaminaPlayer> players = staminaManager.players;
        Iterator<UUID> iterator = players.keySet().iterator();
        CooldownManager cooldownManager = staminaManager.cooldownManager;

        while (iterator.hasNext()) {
            UUID uuid = iterator.next();

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                continue;
            }

            StaminaPlayer staminaPlayer = staminaManager.getPlayer(player);

            if (!cooldownManager.isCooldown(player) && staminaPlayer.getCurrentStamina() != staminaPlayer.getMaxStamina()) {
                int amount = Main.getInstance().getConfig().getInt("体力恢复设置.恢复数量");
                staminaPlayer.increaseStamina(amount);
            }
        }
    }
}
