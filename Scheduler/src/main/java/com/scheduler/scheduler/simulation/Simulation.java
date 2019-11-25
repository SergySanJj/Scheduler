package com.scheduler.scheduler.simulation;

import com.StringMisc;
import com.scheduler.scheduler.RoundRobinMultiLayer;

import java.util.ArrayList;
import java.util.List;

import com.StringMisc.*;

public class Simulation implements ActionOnQuantum {
    private int quantum = 50;
    private int meandev;
    private int standdev;
    private int runtime;
    private List<ProcessGroup> processGroups;

    private int lastWorked = 0;

    public Simulation() {
        processGroups = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(StringMisc.form("Quantum   ")).append(StringMisc.form(quantum)).append("\n");
        res.append(StringMisc.form("Mean      ")).append(StringMisc.form(meandev)).append("\n");
        res.append(StringMisc.form("Deviation ")).append(StringMisc.form(standdev)).append("\n");
        res.append(StringMisc.form("Runtime   ")).append(StringMisc.form(runtime)).append("\n");

        res.append(getTitle()).append("\n");

        for (ProcessGroup group : processGroups) {
            res.append("Group ").append(StringMisc.form(group.getName())).append("\n");
            res.append(group.toString());
        }
        return res.toString();
    }

    private String getTitle() {
        return StringMisc.form("Process") +
                StringMisc.form(" name") +
                StringMisc.form("Cpu need", 12) +
                StringMisc.form("Block", 8) +
                StringMisc.form("Work", 8) +
                StringMisc.form("Blocked", 15) +
                StringMisc.form("Status", 0);
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

    public int countNotCompleted() {
        int cnt = 0;
        for (ProcessGroup group : processGroups) {
            if (group.getCurrentState() != ProcessState.COMPLETED)
                cnt++;
        }
        return cnt;
    }

    @Override
    public String receiveQuantum(int quantum) {
        RoundRobinMultiLayer.run(nextAvailable(), quantum);
        return null;
    }

    @Override
    public String getStatus() {
        return processGroups.get(lastWorked).getStatus();
    }

    @Override
    public ActionOnQuantum nextAvailable() {
        return processGroups.get(selectNextAvailable());
    }

    private int selectNextAvailable() {
        while (processGroups.get(nextCycled()).getCurrentState() == ProcessState.COMPLETED) ;
        return lastWorked;
    }

    private int nextCycled() {
        lastWorked = (lastWorked + 1) % processGroups.size();
        return lastWorked;
    }
}
