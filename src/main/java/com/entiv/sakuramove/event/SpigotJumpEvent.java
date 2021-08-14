package com.entiv.sakuramove.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpigotJumpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Location from;

    private Boolean isCancelled;
    private final Integer jumped;
    private final Player player;

    private final Location to;

    public SpigotJumpEvent(Player player, Integer jumped, Location from, Location to) {
        this.player = player;
        this.jumped = jumped;
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return this.from;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Integer getJumpedBlocks() {
        return this.jumped;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Location getTo() {
        return this.to;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

}