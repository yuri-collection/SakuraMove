package com.entiv.sakuramove.task;

import com.entiv.sakuramove.action.DoubleJump;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class JumpingCheckTask extends TimerTask {

    public JumpingCheckTask(int period) {
        super(period);
    }

    @Override
    public void hasNext() {

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(Player::isOnGround)
                .filter(player -> player.hasPermission("sakuramove.doublejump"))
                .forEach(player -> DoubleJump.getInstance().enable(player));
    }
}
