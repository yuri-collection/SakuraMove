package com.entiv.sakuramove.manager;

import com.entiv.sakuramove.Main;
import com.entiv.sakuramove.utils.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaminaManager implements Listener {

    public final CooldownManager cooldownManager = new CooldownManager(Main.getInstance());
    public final Map<UUID, StaminaPlayer> players = new HashMap<>();

    public StaminaManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public StaminaPlayer getPlayer(Player player) {
        return players.compute(player.getUniqueId(), (k, v) -> v == null ? new StaminaPlayer(player, loadMaxStamina(player)) : v);
    }

    private int loadMaxStamina(Player player) {

        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("体力上限设置");

        int stamina = 0;

        for (String key : section.getKeys(false)) {

            String permission = "sakuramove.stamina." + key;

            if (player.hasPermission(permission)) {
                stamina = section.getInt(key);
            }
        }

        if (stamina == 0) stamina = section.getInt("default", 20);

        return stamina;
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        players.remove(event.getPlayer().getUniqueId());
    }
}
