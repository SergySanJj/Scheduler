package com.scheduler;

import com.scheduler.simulation.SimulationController;

import java.io.PrintStream;

public interface SchedulerAlgorithm {
    SimulationController run(SimulationController simulationController, PrintStream out);
}
