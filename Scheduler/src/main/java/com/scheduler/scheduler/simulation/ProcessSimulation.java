package com.scheduler.scheduler.simulation;

import com.StringMisc;

import java.util.Random;

public class ProcessSimulation implements ActionOnQuantum {
    private static Random random = new Random();

    private int cpuTimeNeeden;
    private int ioblocking;

    private int timesBlocked;
    private ProcessState currentState;

    private int cpuTotal;

    private int leftFromPrevious;


    private ProcessSimulation() {
    }

    public static ProcessSimulation create(int ioblocking, int meandev, int standdev) {
        ProcessSimulation processSimulation = new ProcessSimulation();
        processSimulation.setCpuTimeNeeden(distribute(meandev, standdev));
        processSimulation.setCurrentState(ProcessState.PENDING);
        processSimulation.setIoblocking(ioblocking);
        return processSimulation;
    }

    private static int distribute(int avg, int deviation) {
        double val = random.nextGaussian() * deviation + avg;
        return (int) Math.round(val);
    }

    @Override
    public String receiveQuantum(int quantum) {
        if (currentState == ProcessState.PENDING || currentState == ProcessState.IO_BLOCKED) {
            currentState = ProcessState.REGISTERED;
        }
        if (cpuTotal + quantum >= cpuTimeNeeden)
            quantum = cpuTimeNeeden - cpuTotal;
        cpuTotal += quantum;


        int blockTimes = (quantum + leftFromPrevious) / ioblocking;

        timesBlocked += blockTimes;
        leftFromPrevious = quantum + leftFromPrevious - blockTimes * ioblocking;

        if (cpuTotal >= cpuTimeNeeden) {
            cpuTotal = cpuTimeNeeden;
            timesBlocked = cpuTotal / ioblocking;
            currentState = ProcessState.COMPLETED;
            return getStatus();
        } else if (leftFromPrevious == 0)
            currentState = ProcessState.IO_BLOCKED;

        return getStatus();
    }

    @Override
    public String getStatus() {
        StringBuilder res = new StringBuilder();
        res.append(StringMisc.form(currentState.toString(), 12)).append(" (").
                append(StringMisc.form(cpuTimeNeeden, 5)).append(" ").
                append(StringMisc.form(ioblocking, 5)).append(" ").
                append(StringMisc.form(cpuTotal, 5)).append(" ").
                append(StringMisc.form(timesBlocked, 4)).append(")");
        return res.toString();
    }

    @Override
    public ActionOnQuantum nextAvailable() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("   ");
        res.append(StringMisc.form(getCpuTimeNeeden()));
        res.append(StringMisc.form(getIoblocking()));
        res.append(StringMisc.form(getCpuTotal()));
        res.append(StringMisc.form(getTimesBlocked())).append(" times ");
        res.append(StringMisc.form(currentState.toString()));

        return res.toString();
    }

    public int getCpuTimeNeeden() {
        return cpuTimeNeeden;
    }

    public void setCpuTimeNeeden(int cpuTimeNeeden) {
        this.cpuTimeNeeden = cpuTimeNeeden;
    }

    public int getIoblocking() {
        return ioblocking;
    }

    public void setIoblocking(int ioblocking) {
        this.ioblocking = ioblocking;
    }

    public int getTimesBlocked() {
        return timesBlocked;
    }

    public void setTimesBlocked(int timesBlocked) {
        this.timesBlocked = timesBlocked;
    }

    public ProcessState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ProcessState currentState) {
        this.currentState = currentState;
    }

    public int getCpuTotal() {
        return cpuTotal;
    }

    public void setCpuTotal(int cpuTotal) {
        this.cpuTotal = cpuTotal;
    }
}
