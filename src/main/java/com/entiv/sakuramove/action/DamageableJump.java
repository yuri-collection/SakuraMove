package com.entiv.sakuramove.action;

import com.entiv.sakuramove.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DamageableJump extends DoubleJump {

    private static final DamageableJump damageableJump = new DamageableJump("移动行为.二段跳");
    private final List<UUID> jumpingPlayer = new ArrayList<>();

    private JumpingChecker jumpingChecker;

    public DamageableJump(String path) {
        super(path);
    }

    @Override
    public void accept(Player player) {
        super.accept(player);
        jumpingPlayer.add(player.getUniqueId());
    }

    public static DamageableJump getInstance() {
        return damageableJump;
    }

    @Override
    public void clearCache(Player player) {
        super.clearCache(player);
        jumpingPlayer.remove(player.getUniqueId());
    }

    public void enableJumpingChecker() {
        jumpingChecker = new JumpingChecker();
        jumpingChecker.runTaskTimer(Main.getInstance(), 10, 5);
    }

    public void disableJumpingChecker() {
        if (jumpingChecker != null) {
            jumpingChecker.cancel();
        }
    }

    public class JumpingChecker extends BukkitRunnable {

        @Override
        public void run() {
            for (UUID uuid : jumpingPlayer) {
                Player player = Main.getInstance().getServer().getPlayer(uuid);

                if (player != null && player.isOnGround()) {
                    enable(player);
                }
            }
        }
    }
}

