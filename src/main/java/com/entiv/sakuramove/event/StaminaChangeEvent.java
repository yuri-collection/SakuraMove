package com.entiv.sakuramove.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class StaminaChangeEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final int before, after;

    public StaminaChangeEvent(@NotNull Player who, int before, int after) {
        super(who);
        this.before = before;
        this.after = after;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public int getBefore() {
        return before;
    }

    public int getAfter() {
        return after;
    }
}
