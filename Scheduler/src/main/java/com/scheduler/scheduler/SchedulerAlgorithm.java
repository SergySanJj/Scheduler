package com.scheduler.scheduler;

import com.scheduler.scheduler.simulation.ProcessGroup;
import com.scheduler.scheduler.simulation.Simulation;

import java.io.PrintStream;
import java.util.List;

public interface SchedulerAlgorithm {
    Simulation run(Simulation simulation, PrintStream out);
}
