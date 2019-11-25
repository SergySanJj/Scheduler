package com.scheduler.scheduler;

import com.scheduler.scheduler.simulation.Simulation;

public class FairShare implements SchedulerAlgorithm {
    @Override
    public Simulation run(Simulation simulation) {
        int quantum = simulation.getQuantum();
        int curr = simulation.getRuntime();
        while (curr - quantum > 0) {
            System.out.println(RoundRobinMultiLayer.run(simulation, Math.min(curr,quantum)));
            curr -= quantum;
        }

        return simulation;
    }
}
