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
        int runtime = simulation.getRuntime();

        Logger.getLogger("FairShare").log(Level.INFO, "Starting work with processes batch...");

        int currTime = 0;
        while (currTime < runtime) {
            String step = "IDLE " + StringMisc.form(runtime - currTime) + " left";
            if (simulation.countNotCompleted() != 0)
                step = RoundRobinMultiLayer.run(simulation, Math.min(runtime - currTime, quantum), currTime);

            out.println(step);

            currTime += Math.min(runtime - currTime, quantum);
        }

        Logger.getLogger("FairShare").log(Level.INFO, "Finished");

        return simulation;
    }
}
