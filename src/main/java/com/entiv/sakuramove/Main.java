package com.entiv.sakuramove;

import com.entiv.sakuramove.listener.PaperJumpListener;
import com.entiv.sakuramove.listener.SpigotJumpListener;
import com.entiv.sakuramove.listener.SpringListener;
import com.entiv.sakuramove.listener.StaminaChangeListener;
import com.entiv.sakuramove.manager.StaminaManager;
import com.entiv.sakuramove.task.DynamicProgressTask;
import com.entiv.sakuramove.task.RecoveryStaminaTask;
import com.entiv.sakuramove.task.TaskManager;
import com.entiv.sakuramove.utils.Message;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    @Getter
    private StaminaManager staminaManager;

    @Getter
    private TaskManager taskManager;
    private Listener doubleJump;

    @Override
    public void onEnable() {

        plugin = this;
        staminaManager = new StaminaManager();
        taskManager = new TaskManager();
        taskManager.start(this);

        String[] message = {
                "§a樱花移动插件§e v" + getDescription().getVersion() + " §a已启用",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);

        if (isPaperFork()) {
            doubleJump = new PaperJumpListener();
        } else {
            doubleJump = new SpigotJumpListener();
        }

        reload();
        saveDefaultConfig();

        if (getConfig().getBoolean("移动行为.冲刺.开启")) {
            Bukkit.getPluginManager().registerEvents(new SpringListener(), this);
        }

        addTask();
        registerPlaceholder();

        Bukkit.getPluginManager().registerEvents(new StaminaChangeListener(), this);
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

        boolean enableFallDamage = getConfig().getBoolean("移动行为.二段跳.摔落伤害");

        HandlerList.unregisterAll(doubleJump);

        if (!getConfig().getBoolean("移动行为.二段跳.开启")) return;

        if (enableFallDamage) {
            Bukkit.getPluginManager().registerEvents(doubleJump, this);
        }

    }

    private void registerPlaceholder() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            SakuraMovePlaceholder placeholder = new SakuraMovePlaceholder();
            placeholder.register();
        }
    }

    private void addTask() {
        RecoveryStaminaTask recoveryStaminaTask = new RecoveryStaminaTask(getConfig().getInt("体力恢复设置.恢复速度", 1));
        DynamicProgressTask dynamicProgressTask = new DynamicProgressTask(1);

        taskManager.addTask(recoveryStaminaTask);
        taskManager.addTask(dynamicProgressTask);
    }

    public static Main getInstance() {
        return plugin;
    }

    private static boolean isPaperFork() {
        try {
            Class.forName("com.destroystokyo.paper.event.player.PlayerJumpEvent");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
