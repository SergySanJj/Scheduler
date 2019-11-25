package com.scheduler.scheduler;

import com.StringMisc;
import com.scheduler.scheduler.simulation.Simulation;

public class FairShare implements SchedulerAlgorithm {
    @Override
    public Simulation run(Simulation simulation) {
        int quantum = simulation.getQuantum();
        int curr = simulation.getRuntime();
        while (curr - quantum > 0) {
            String step = "IDLE " + StringMisc.form(curr - quantum) + " left";
            if (simulation.countNotCompleted() != 0)
                step = RoundRobinMultiLayer.run(simulation, Math.min(curr, quantum));
            System.out.println(step);
            curr -= quantum;
        }

        return simulation;
    }
}
