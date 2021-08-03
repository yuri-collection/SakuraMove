package com.entiv.sakuramove;

import com.entiv.sakuramove.listener.DoubleJumpListener;
import com.entiv.sakuramove.listener.PlayerListener;
import com.entiv.sakuramove.manager.StaminaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;
    private StaminaManager staminaManager;

    @Override
    public void onEnable() {

        plugin = this;
        staminaManager = new StaminaManager();
        staminaManager.start();

        String[] message = {
                "§a樱花移动插件§e v" + getDescription().getVersion() + " §a已启用",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DoubleJumpListener(), this);

        saveResource("config.yml", true);
        saveDefaultConfig();
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
            Main plugin = Main.getInstance();
            plugin.reloadConfig();

            staminaManager.cancel();
            staminaManager = null;

            staminaManager = new StaminaManager();
            staminaManager.start();

            Message.send(sender, plugin.getConfig().getString("Message.Reload"));
        }
        return true;
    }

    public StaminaManager getStaminaManager() {
        return staminaManager;
    }

    public static Main getInstance() {
        return plugin;
    }
}
