package com.scheduler.scheduler;

import com.scheduler.scheduler.simulation.ActionOnQuantum;

import java.util.List;

public class RoundRobinMultiLayer {
    public static String run(ActionOnQuantum root, int quantum, int currentTime) {
        return root.nextAvailable(currentTime).receiveQuantum(quantum,currentTime);
    }
}
