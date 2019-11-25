package com.scheduler.scheduler.simulation;

import java.util.List;

public interface ActionOnQuantum {
    String receiveQuantum(int quantum, int currentTime);

    String getStatus();

    ActionOnQuantum nextAvailable();
}
