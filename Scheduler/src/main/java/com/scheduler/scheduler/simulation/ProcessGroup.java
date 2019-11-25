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
    public String receiveQuantum(int quantum, int currentTime) {
        updateState(currentTime);
        if (getCurrentState() == ProcessState.COMPLETED || getCurrentState() == ProcessState.IO_BLOCKED)
            return "Group " + StringMisc.form(getName()) + StringMisc.form(getCurrentState().toString()) + " skips quantum";

        RoundRobinMultiLayer.run(nextAvailable(currentTime), quantum, currentTime);

        return getStatus();
    }

    public ProcessState updateState(int currentTime) {
        if (allUnavailable(currentTime)) {
            if (count(ProcessState.COMPLETED, currentTime) == processList.size()) {
                currentState = ProcessState.COMPLETED;
            } else
                currentState = ProcessState.IO_BLOCKED;
        } else currentState = ProcessState.REGISTERED;
        return currentState;
    }

    @Override
    public String getStatus() {
        String processStatus = processList.get(lastWorked).getStatus();
        String res = "Group " + StringMisc.form(getName()) + StringMisc.form(getCurrentState().toString()) + " Process " + lastWorked + " " + processStatus;
        return res;
    }

    @Override
    public ActionOnQuantum nextAvailable(int currentTime) {
        return processList.get(selectNextAvailable(currentTime));
    }

    private int selectNextAvailable(int currTime) {
        nextCycled();
        ProcessState processState = processList.get(lastWorked).updateCurrentState(currTime);
        while (processState == ProcessState.COMPLETED || processState == ProcessState.IO_BLOCKED) {
            nextCycled();
            processState = processList.get(lastWorked).updateCurrentState(currTime);
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
        for (ProcessSimulation process : processList) {
            process.updateCurrentState(currentTime);
            if (process.getCurrentState() == state) {
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

    public Summary getSummary() {
        Summary summary = new Summary();
        for (ProcessSimulation proc : processList) {
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
