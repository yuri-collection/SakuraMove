package com.entiv.sakuramove;

import com.entiv.sakuramove.action.DamageableJump;
import com.entiv.sakuramove.action.DoubleJump;
import com.entiv.sakuramove.listener.DoubleJumpListener;
import com.entiv.sakuramove.listener.DamageableJumpListener;
import com.entiv.sakuramove.listener.SpringListener;
import com.entiv.sakuramove.manager.StaminaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;
    private StaminaManager staminaManager;

    private DoubleJumpListener doubleJump;
    private DamageableJumpListener damageableJump;

    @Override
    public void onEnable() {

        plugin = this;

        String[] message = {
                "§a樱花移动插件§e v" + getDescription().getVersion() + " §a已启用",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);

        damageableJump = new DamageableJumpListener();
        doubleJump = new DoubleJumpListener();

        reload();
        saveDefaultConfig();

        if (getConfig().getBoolean("冲刺.开启")) {
            Bukkit.getPluginManager().registerEvents(new SpringListener(), this);
        }
    }

    @Override
    public void onDisable() {
        String[] message = {
                "§a樱花移动插件§e v" + getDescription().getVersion() + " §a已卸载",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {

            reload();

            Message.send(sender, plugin.getConfig().getString("Message.Reload"));
        }
        return true;
    }

    public void reload() {

        reloadConfig();

        if (staminaManager != null) {
            staminaManager.cancel();
        }

        staminaManager = new StaminaManager();
        staminaManager.start();

        boolean enableFallDamage = getConfig().getBoolean("移动行为.二段跳.摔落伤害");

        HandlerList.unregisterAll(damageableJump);
        HandlerList.unregisterAll(doubleJump);

        if (!getConfig().getBoolean("二段跳.开启")) return;

        if (enableFallDamage) {
            Bukkit.getPluginManager().registerEvents(damageableJump, this);
            DoubleJump.getInstance().disableJumpingChecker();

        } else {
            Bukkit.getPluginManager().registerEvents(doubleJump, this);
            DoubleJump.getInstance().enableJumpingChecker();
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setAllowFlight(true);
                player.setFlying(false);
            });
        }

    }

    public StaminaManager getStaminaManager() {
        return staminaManager;
    }

    public static Main getInstance() {
        return plugin;
    }
}
