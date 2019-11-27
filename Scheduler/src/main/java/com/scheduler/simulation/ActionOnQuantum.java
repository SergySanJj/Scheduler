package com.scheduler.simulation;

public interface ActionOnQuantum {
    String receiveQuantum(int quantum, int currentTime);

    String getStatus();

    ActionOnQuantum nextAvailable(int currentTime);
}
