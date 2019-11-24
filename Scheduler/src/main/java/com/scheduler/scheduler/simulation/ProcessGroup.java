package com.scheduler.scheduler.simulation;

import java.util.List;

public class ProcessGroup {
    private List<ProcessSimulation> processList;
    private String name;


    public List<ProcessSimulation> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessSimulation> processList) {
        this.processList = processList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProcess(ProcessSimulation processSimulation) {
        processList.add(processSimulation);
    }

    public int size() {
        return processList.size();
    }
}
