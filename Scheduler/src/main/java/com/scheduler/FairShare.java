package com.scheduler;

import com.StringMisc;
import com.scheduler.simulation.SimulationController;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FairShare implements SchedulerAlgorithm {
    @Override
    public SimulationController run(SimulationController simulationController, PrintStream out) {
        int quantum = simulationController.getQuantum();
        int runtime = simulationController.getRuntime();

        Logger.getLogger("FairShare").log(Level.INFO, "Starting work with processes batch...");

        int currTime = 0;
        while (currTime < runtime) {
            String step = "IDLE " + StringMisc.form(runtime - currTime) + " left";
            if (simulationController.countNotCompleted() != 0)
                step = RoundRobinMultiLayer.run(simulationController, Math.min(runtime - currTime, quantum), currTime);

            out.println(step);

            currTime += Math.min(runtime - currTime, quantum);
        }

        Logger.getLogger("FairShare").log(Level.INFO, "Finished");

        return simulationController;
    }
}
