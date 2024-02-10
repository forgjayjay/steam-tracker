package eo.forg.steamtracker.model;

import java.util.Timer;
import java.util.TimerTask;

public class CustomTimer{

    private boolean active = false;

    public void startTimer(){
        active = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                active = !active;
            }
        }, 5 * 60 * 1000);
    }

    public boolean isActive(){
        return active;
    }

    // @Override
    // public String toString() {
    //     return String.valueOf(isActive());
    // }

}
