package com.entiv.sakuramove;

import com.entiv.sakuramove.manager.StaminaManager;
import com.entiv.sakuramove.manager.StaminaPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SakuraMovePlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "SakuraMove";
    }

    @Override
    public @NotNull String getAuthor() {
        return "EnTIv";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        if (!params.startsWith("stamina_")) return "占位符错误";

        String param = params.substring("stamina_".length());
        StaminaManager manager = Main.getInstance().getStaminaManager();
        StaminaPlayer staminaPlayer = manager.getPlayer(player);

        // %SakuraMove_stamina_current%
        // %SakuraMove_stamina_progress%
        if (param.equalsIgnoreCase("max")) {
            return String.valueOf(staminaPlayer.getMaxStamina());
        } else if (param.equalsIgnoreCase("current")) {
            return String.valueOf(staminaPlayer.getCurrentStamina());
        } else if (param.equalsIgnoreCase("progress")) {
            return String.valueOf(staminaPlayer.getProgressState().getCurrent());
        }

        return "占位符错误";
    }

}
