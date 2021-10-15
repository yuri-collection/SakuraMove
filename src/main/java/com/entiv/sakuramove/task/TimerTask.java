package com.entiv.sakuramove.task;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class TimerTask implements Runnable {

    @Getter
    protected final int period;
    protected int tick;

    public TimerTask(int period) {
        this.period = period;
    }

    @Override
    public final void run() {
        if (++tick >= period) {
            hasNext();
            tick = 0;
        }
    }

    public abstract void hasNext();

}
