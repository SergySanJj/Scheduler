package com.scheduler;

import com.scheduler.simulation.Simulation;

import java.io.PrintStream;

public interface SchedulerAlgorithm {
    Simulation run(Simulation simulation, PrintStream out);
}
