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

        for (Player player : Bukkit.getOnlinePlayers()) {
            final boolean isFalling = player.getVelocity().getY() < -0.1;

            if (player.isOnGround()) {
                DoubleJump.getInstance().enable(player);
            } else if (isFalling) {
                DoubleJump.getInstance().disable(player);
            }
        }
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(Player::isOnGround)
                .forEach(player -> DoubleJump.getInstance().enable(player));
    }
}
