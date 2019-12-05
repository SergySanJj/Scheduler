package com.scheduler.simulation;

import com.StringMisc;
import com.scheduler.RoundRobinMultiLayer;

import java.util.ArrayList;
import java.util.List;

public class GroupController implements ActionOnQuantum {
    private ActionOnQuantum parent;
    private List<ProcessController> processList;
    private String name;

    private ProcessState currentState = ProcessState.READY;

    private int lastWorked = 0;

    public GroupController(ActionOnQuantum parent) {
        processList = new ArrayList<>();
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        int i = 0;
        for (ProcessController process : processList) {
            res.append("Process ").append(StringMisc.form(i)).append(" ").append(process.toString()).append("\n");
            i++;
        }

        return res.toString();
    }

    @Override
    public String receiveQuantum(int quantum, int currentTime) {
        updateState(currentTime);

        String s = RoundRobinMultiLayer.run(nextAvailable(currentTime), quantum, currentTime);

        return s;
    }

    public ProcessState updateState(int currentTime) {
        if (allUnavailable(currentTime)) {
            if (count(ProcessState.COMPLETED, currentTime) == processList.size()) {
                currentState = ProcessState.COMPLETED;
            } else
                currentState = ProcessState.IO_BLOCKED;
        } else currentState = ProcessState.READY;
        return currentState;
    }

    @Override
    public String getStatus() {
        String res = "Group " + StringMisc.form(getName())  + " Process " + lastWorked + " ";
        return res;
    }

    @Override
    public ActionOnQuantum nextAvailable(int currentTime) {
        int next = selectNextAvailable(currentTime);
        if (next==-1)
            return parent;
        return processList.get(next);
    }

    private int selectNextAvailable(int currTime) {
        nextCycled();
        ProcessState processState = processList.get(lastWorked).updateCurrentState(currTime);
        int tried = 0;
        while (processState == ProcessState.COMPLETED || processState == ProcessState.IO_BLOCKED && tried < processList.size()) {
            nextCycled();
            processState = processList.get(lastWorked).updateCurrentState(currTime);
            tried++;
        }
        if (tried>=processList.size()){
            return -1;
        }
        return lastWorked;
    }

    private int nextCycled() {
        lastWorked = (lastWorked + 1) % processList.size();
        return lastWorked;
    }

    public boolean allUnavailable(int currentTime) {
        return count(ProcessState.COMPLETED, currentTime) +
                count(ProcessState.IO_BLOCKED, currentTime) == processList.size();
    }

    private int count(ProcessState state, int currentTime) {
        int res = 0;
        for (ProcessController process : processList) {
            process.updateCurrentState(currentTime);
            if (process.getCurrentState() == state) {
                res++;
            }
        }
        return res;
    }

    public List<ProcessController> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessController> processList) {
        this.processList = processList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProcess(ProcessController processController) {
        processList.add(processController);
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

    public Summary getSummary() {
        Summary summary = new Summary();
        for (ProcessController proc : processList) {
            summary.addTotWorked(proc.getCpuTotal());
            summary.addTotQuantums(proc.getQuantumsReceived());
            summary.addTotCpuNeed(proc.getCpuTimeNeeden());
            summary.addTotProcesses(1);
            if (proc.getCurrentState() == ProcessState.COMPLETED)
                summary.addTotFinished(1);
        }
        return summary;
    }
}
