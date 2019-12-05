package com.scheduler.simulation;

import com.StringMisc;

import java.util.Random;

public class ProcessSimulation implements ActionOnQuantum {
    private ActionOnQuantum parent;
    private static Random random = new Random();

    private int cpuTimeNeeden;
    private int ioblocking;
    private int blockPeriod;

    private int timesBlocked;
    private ProcessState currentState;
    private int lastBlockedTime;
    private int workedAfterUnblock;

    private int cpuTotal;
    private int quantumsReceived;


    private ProcessSimulation(ActionOnQuantum parent) {
        this.parent = parent;
    }

    public static ProcessSimulation create(ActionOnQuantum parentGroup, int ioblocking, int meandev, int standdev, int blockMean, int blockDeviation) {
        ProcessSimulation processSimulation = new ProcessSimulation(parentGroup);
        processSimulation.setCpuTimeNeeden(distribute(meandev, standdev));
        processSimulation.setCurrentState(ProcessState.PENDING);
        processSimulation.setIoblocking(ioblocking);
        processSimulation.setBlockPeriod(distribute(blockMean, blockDeviation));
        processSimulation.quantumsReceived = 0;
        return processSimulation;
    }

    private static int distribute(int avg, int deviation) {
        double val = random.nextGaussian() * deviation + avg;
        if (val < 0.)
            return avg;
        return (int) Math.round(val);
    }

    @Override
    public String receiveQuantum(int quantum, int currentTime) {
        quantumsReceived++;
        if (currentState == ProcessState.PENDING) {
            currentState = ProcessState.READY;
            lastBlockedTime = currentTime;
            workedAfterUnblock = 0;
        }
        while (quantum > 0 && currentState != ProcessState.COMPLETED) {
            if (currentState == ProcessState.IO_BLOCKED) {
                if (currentTime >= lastBlockedTime + blockPeriod) {
                    currentState = ProcessState.READY;
                    lastBlockedTime = currentTime;
                    workedAfterUnblock = 0;
                }
            }

            if (cpuTotal + 1 >= cpuTimeNeeden) {
                currentState = ProcessState.COMPLETED;
                cpuTotal = cpuTimeNeeden;
                continue;
            }

            workedAfterUnblock++;
            cpuTotal++;

            if (workedAfterUnblock >= ioblocking) {
                currentState = ProcessState.IO_BLOCKED;
                lastBlockedTime = currentTime;
                timesBlocked++;
                workedAfterUnblock = 0;
                quantum--;
                currentTime++;
                if (quantum > 0)
                    parent.nextAvailable(currentTime).receiveQuantum(quantum, currentTime);
                return getStatus();
            }

            quantum--;
            currentTime++;
        }

        return getStatus();
    }

    @Override
    public String getStatus() {
        StringBuilder res = new StringBuilder();
        res.append(StringMisc.form(currentState.toString(), 12)).append(" (").
                append(StringMisc.form(cpuTimeNeeden, 10)).append(" ").
                append(StringMisc.form(ioblocking, 10)).append(" ").
                append(StringMisc.form(cpuTotal, 10)).append(" ").
                append(StringMisc.form(blockPeriod, 10)).append(" ").
                append(StringMisc.form(quantumsReceived, 10)).append(" ").
                append(StringMisc.form(timesBlocked, 10)).append(")");
        //res.append(StringMisc.form(workedAfterUnblock));
        return res.toString();
    }

    @Override
    public ActionOnQuantum nextAvailable(int currentTime) {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("   ");
        res.append(StringMisc.form(getCpuTimeNeeden()));
        res.append(StringMisc.form(getIoblocking()));
        res.append(StringMisc.form(getCpuTotal()));
        res.append(StringMisc.form(getBlockPeriod()));
        res.append(StringMisc.form(getTimesBlocked())).append(" times ");
        res.append(StringMisc.form(currentState.toString(), 12));
        res.append(StringMisc.form(quantumsReceived));

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

    public ProcessState updateCurrentState(int currentTime) {
        if (currentState == ProcessState.IO_BLOCKED && currentTime >= lastBlockedTime + blockPeriod)
            currentState = ProcessState.READY;
        return currentState;
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

    public int getBlockPeriod() {
        return blockPeriod;
    }

    public void setBlockPeriod(int blockPeriod) {
        this.blockPeriod = blockPeriod;
    }

    public int getQuantumsReceived() {
        return quantumsReceived;
    }

    public void setQuantumsReceived(int quantumsReceived) {
        this.quantumsReceived = quantumsReceived;
    }
}
