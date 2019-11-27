package com.scheduler;

import com.scheduler.simulation.ActionOnQuantum;

public class RoundRobinMultiLayer {
    public static String run(ActionOnQuantum root, int quantum, int currentTime) {
        return root.nextAvailable(currentTime).receiveQuantum(quantum,currentTime);
    }
}
