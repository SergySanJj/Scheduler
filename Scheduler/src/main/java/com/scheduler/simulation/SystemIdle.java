package com.scheduler.simulation;

public class SystemIdle implements ActionOnQuantum {
    private static SystemIdle systemIdle = new SystemIdle();
    private ActionOnQuantum caller;
    private int calledCount = 0;
    private static int d = 1;

    private SystemIdle() {
    }

    public static SystemIdle get(ActionOnQuantum caller) {
        systemIdle.caller = caller;
        return systemIdle;
    }

    @Override
    public String receiveQuantum(int quantum, int currentTime) {
        calledCount++;

        quantum -= d;
        currentTime += 1;
        if (quantum > 0)
            caller.nextAvailable(currentTime).receiveQuantum(quantum, currentTime);
        return getStatus();
    }

    @Override
    public String getStatus() {
        return "System Idle for " + d;
    }

    @Override
    public ActionOnQuantum nextAvailable(int currentTime) {
        return systemIdle;
    }
}
