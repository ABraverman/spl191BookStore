package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    private int tick;
    private int duration;

    public TickBroadcast (int t, int d) {
        this.tick = t;
        this.duration = d;
    }

    public int getTick() {
        return tick;
    }

    public int getDuration() {
        return  duration;
    }

}
