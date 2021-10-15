package com.entiv.sakuramove.progress;

public class ProgressState {

    private int current;
    private int after;

    public ProgressState(int current, int after) {
        this.current = current;
        this.after = after;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }
}
