package com.scheduler.scheduler.simulation;

import com.StringMisc;
import com.scheduler.scheduler.RoundRobinMultiLayer;

import java.util.ArrayList;
import java.util.List;

public class ProcessGroup implements ActionOnQuantum {
    private List<ProcessSimulation> processList;
    private String name;

    private ProcessState currentState = ProcessState.REGISTERED;

    private int lastWorked = 0;

    public ProcessGroup() {
        processList = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        int i = 0;
        for (ProcessSimulation process : processList) {
            res.append("Process ").append(StringMisc.form(i)).append(" ").append(process.toString()).append("\n");
            i++;
        }

        return res.toString();
    }

    @Override
    public String receiveQuantum(int quantum) {
        if (allCompleted()) {
            currentState = ProcessState.COMPLETED;
            return "";
        }

        RoundRobinMultiLayer.run(nextAvailable(), quantum);

        return getStatus();
    }

    @Override
    public String getStatus() {
        String processStatus = processList.get(lastWorked).getStatus();
        String res = "Group " + StringMisc.form(getName()) + " Process " + lastWorked + " " + processStatus;
        return res;
    }

    @Override
    public ActionOnQuantum nextAvailable() {
        return processList.get(selectNextAvailable());
    }

    private int selectNextAvailable() {
        while (processList.get(nextCycled()).getCurrentState() == ProcessState.COMPLETED) ;
        return lastWorked;
    }

    private int nextCycled() {
        lastWorked = (lastWorked + 1) % processList.size();
        return lastWorked;
    }

    public boolean allCompleted() {
        return active() == 0;
    }

    private int active() {
        int res = 0;
        for (ProcessSimulation process : processList) {
            if (process.getCurrentState() != ProcessState.COMPLETED) {
                res++;
            }
        }
        return res;
    }

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

    public ProcessState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ProcessState currentState) {
        this.currentState = currentState;
    }
}
