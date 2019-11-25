package com.scheduler.scheduler.simulation;

import java.util.List;

public interface ActionOnQuantum {
    String receiveQuantum(int quantum);

    String getStatus();

    ActionOnQuantum nextAvailable();

}
