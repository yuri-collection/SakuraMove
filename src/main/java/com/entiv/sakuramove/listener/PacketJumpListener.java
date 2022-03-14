package com.entiv.sakuramove.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.action.DoubleJump;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class PacketJumpListener extends PacketAdapter implements Listener {

    private final DoubleJump doubleJump = DoubleJump.getInstance();

    public PacketJumpListener() {
        super(Main.getInstance(), PacketType.Play.Server.ABILITIES, PacketType.Play.Client.ABILITIES);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        // 如果服务器更新玩家 canFly 状态 强制改为true 让玩家以为自己能飞行
        event.getPacket().getBooleans().write(2, true);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        // 仅在玩家切换飞行状态的时候会接收到 Abilities 数据包
        Player player = event.getPlayer();
        // 如果玩家不被允许飞行 则该效果是二连跳
        if (!player.getAllowFlight()) {
            // 阻止数据包
            event.setCancelled(true);

            // 二段跳
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (doubleJump.canAccept(player)) {
                    doubleJump.accept(player);
                }
            });
        }
    }
}
