package com.scheduler.scheduler;

import com.scheduler.scheduler.simulation.ProcessGroup;
import com.scheduler.scheduler.simulation.Simulation;

import java.util.List;

public interface SchedulerAlgorithm {
    Simulation run(Simulation simulation);
}
