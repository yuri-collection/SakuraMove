package com.entiv.sakuramove.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TaskManager extends BukkitRunnable {

    private final List<TimerTask> runnableList = new ArrayList<>();
    long tick = 0;

    @Override
    public void run() {

        if (++tick == Long.MAX_VALUE) {
            tick = 0;
        }

        runnableList.forEach(Runnable::run);
    }

    public void start(Plugin plugin) {
        runTaskTimer(plugin, 0, 1);
    }

    public void addTask(TimerTask task) {
        runnableList.add(task);
    }

    public void removeTask(TimerTask task) {
        runnableList.remove(task);
    }

}