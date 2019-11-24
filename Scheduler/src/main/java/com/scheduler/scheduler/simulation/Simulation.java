package com.scheduler.scheduler.simulation;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private int quantum = 50;
    private int meandev;
    private int standdev;
    private int runtime;
    private List<ProcessGroup> processGroups;

    public Simulation() {
        processGroups = new ArrayList<>();
    }

    public int getMeandev() {
        return meandev;
    }

    public void setMeandev(int meandev) {
        this.meandev = meandev;
    }

    public int getStanddev() {
        return standdev;
    }

    public void setStanddev(int standdev) {
        this.standdev = standdev;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<ProcessGroup> getProcessGroups() {
        return processGroups;
    }

    public void setProcessGroups(List<ProcessGroup> processGroups) {
        this.processGroups = processGroups;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void addGroup(ProcessGroup processGroup) {
        processGroups.add(processGroup);
    }

    public int size() {
        return processGroups.size();
    }
}
