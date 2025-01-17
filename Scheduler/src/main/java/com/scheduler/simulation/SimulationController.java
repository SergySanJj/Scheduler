package com.scheduler.simulation;

import com.StringMisc;
import com.scheduler.RoundRobinMultiLayer;

import java.util.ArrayList;
import java.util.List;

public class SimulationController implements ActionOnQuantum {
    private int quantum = 50;
    private int meandev;
    private int standdev;
    private int blockMean;
    private int blockDeviation;
    private int runtime;
    private List<GroupController> groupControllers;

    private int lastWorked = 0;

    public SimulationController() {
        groupControllers = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(StringMisc.form("Quantum   ")).append(StringMisc.form(quantum)).append("\n");
        res.append(StringMisc.form("Mean      ")).append(StringMisc.form(meandev)).append("\n");
        res.append(StringMisc.form("Deviation ")).append(StringMisc.form(standdev)).append("\n");
        res.append(StringMisc.form("Block mean")).append(StringMisc.form(blockMean)).append("\n");
        res.append(StringMisc.form("Block dev ")).append(StringMisc.form(blockDeviation)).append("\n");
        res.append(StringMisc.form("Runtime   ")).append(StringMisc.form(runtime)).append("\n");

        res.append(getTitle()).append("\n");

        for (GroupController group : groupControllers) {
            res.append("Group ").append(StringMisc.form(group.getName())).append("\n");
            res.append(group.toString());
        }

        Summary summary = getSummary();
        res.append("\nSummary:\n");
        res.append(summary.getTitle()).append("\n");
        res.append(summary).append("\n");
        return res.toString();
    }

    private String getTitle() {
        return StringMisc.form("Process") +
                StringMisc.form(" name") +
                StringMisc.form("Cpu need", 12) +
                StringMisc.form("Block", 8) +
                StringMisc.form("Work", 8) +
                StringMisc.form("BlockT", 8) +
                StringMisc.form("Blocked", 15) +
                StringMisc.form("Status", 12) +
                StringMisc.form("Quantum received", 0);
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

    public List<GroupController> getGroupControllers() {
        return groupControllers;
    }

    public void setGroupControllers(List<GroupController> groupControllers) {
        this.groupControllers = groupControllers;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void addGroup(GroupController groupController) {
        groupControllers.add(groupController);
    }

    public int getBlockMean() {
        return blockMean;
    }

    public void setBlockMean(int blockMean) {
        this.blockMean = blockMean;
    }

    public int getBlockDeviation() {
        return blockDeviation;
    }

    public void setBlockDeviation(int blockDeviation) {
        this.blockDeviation = blockDeviation;
    }

    public int size() {
        return groupControllers.size();
    }

    public int countNotCompleted() {
        int cnt = 0;
        for (GroupController group : groupControllers) {
            if (group.getCurrentState() != ProcessState.COMPLETED)
                cnt++;
        }
        return cnt;
    }

    @Override
    public String receiveQuantum(int quantumm, int currentTime) {
        if (quantumm<=0)
            return "";
        String s = RoundRobinMultiLayer.run(nextAvailable(currentTime), quantum, currentTime);
        return s;
    }

    @Override
    public String getStatus() {
        return groupControllers.get(lastWorked).getStatus();
    }

    @Override
    public ActionOnQuantum nextAvailable(int currentTime) {
        int next = selectNextAvailable(currentTime);
        if (next == -1)
            return SystemIdle.get(this);
        return groupControllers.get(next);
    }

    private int selectNextAvailable(int currentTime) {
        if (allUnavailable(currentTime)) {
            return -1;
        } else {
            int checkCount = 0;
            while (groupControllers.get(nextCycled()).allUnavailable(currentTime) && checkCount < groupControllers.size()) {
                checkCount++;
            }
            if (checkCount >= groupControllers.size()) {
                return -1;
            }
        }
        return lastWorked;
    }

    private boolean allUnavailable(int currentTime) {
        for (GroupController group : groupControllers) {
            if (!group.allUnavailable(currentTime))
                return false;
        }
        return true;
    }

    private int nextCycled() {
        lastWorked = (lastWorked + 1) % groupControllers.size();
        return lastWorked;
    }

    public Summary getSummary() {
        List<Summary> summaries = new ArrayList<>();
        for (GroupController group : groupControllers) {
            summaries.add(group.getSummary());
        }
        return Summary.addAll(summaries);
    }
}
