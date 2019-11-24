package com.scheduler.scheduler;

import com.scheduler.scheduler.simulation.ProcessGroup;

import java.util.List;

public interface SchedulerAlgorithm {
    List<ProcessGroup> run(List<ProcessGroup> groupsToRun);
}
