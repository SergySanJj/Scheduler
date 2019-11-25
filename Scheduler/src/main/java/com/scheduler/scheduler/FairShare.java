package com.scheduler.scheduler;

import com.StringMisc;
import com.scheduler.scheduler.simulation.Simulation;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FairShare implements SchedulerAlgorithm {
    @Override
    public Simulation run(Simulation simulation, PrintStream out) {
        int quantum = simulation.getQuantum();
        int curr = simulation.getRuntime();

        Logger.getLogger("FairShare").log(Level.INFO,"Starting work with processes batch...");

        while (curr - quantum > 0) {
            String step = "IDLE " + StringMisc.form(curr - quantum) + " left";
            if (simulation.countNotCompleted() != 0)
                step = RoundRobinMultiLayer.run(simulation, Math.min(curr, quantum));

            out.println(step);

            curr -= quantum;
        }

        Logger.getLogger("FairShare").log(Level.INFO,"Finished");

        return simulation;
    }
}
