package com.entiv.sakuramove.progress;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.manager.StaminaManager;
import org.bukkit.entity.Player;

public interface Progress {

    StaminaManager staminaManager = Main.getInstance().getStaminaManager();

    void show(Player player, int before, int after);
}
