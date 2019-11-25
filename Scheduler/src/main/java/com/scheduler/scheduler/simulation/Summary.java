package com.scheduler.scheduler.simulation;

import com.StringMisc;

import java.util.List;

public class Summary {
    private int totCpuNeed = 0;
    private int totFinished = 0;
    private int totWorked = 0;
    private int totQuantums = 0;
    private int totProcesses = 0;


    public int getTotCpuNeed() {
        return totCpuNeed;
    }

    public void addTotCpuNeed(int totCpuNeed) {
        this.totCpuNeed += totCpuNeed;
    }

    public int getTotFinished() {
        return totFinished;
    }

    public void addTotFinished(int totFinished) {
        this.totFinished += totFinished;
    }

    public int getTotWorked() {
        return totWorked;
    }

    public void addTotWorked(int totWorked) {
        this.totWorked += totWorked;
    }

    public int getTotQuantums() {
        return totQuantums;
    }

    public void addTotQuantums(int totQuantums) {
        this.totQuantums += totQuantums;
    }

    public int getTotProcesses() {
        return totProcesses;
    }

    public void addTotProcesses(int totProcesses) {
        this.totProcesses += totProcesses;
    }

    public static Summary addAll(List<Summary> list) {
        Summary res = new Summary();
        for (Summary summary : list) {
            res.addTotCpuNeed(summary.getTotCpuNeed());
            res.addTotFinished(summary.getTotFinished());
            res.addTotQuantums(summary.getTotQuantums());
            res.addTotWorked(summary.getTotWorked());
            res.addTotProcesses(summary.getTotProcesses());
        }
        return res;
    }

    public String getTitle() {
        StringBuilder res = new StringBuilder();
        res.append(StringMisc.form("Total", 16));
        res.append(StringMisc.form("Cpu Need", 16));
        res.append(StringMisc.form("Cpu Worked", 16));
        res.append(StringMisc.form("Quantum got", 16));
        res.append(StringMisc.form("Processes", 16));
        res.append(StringMisc.form("Finished", 16));
        return res.toString();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(StringMisc.form("", 16));
        res.append(StringMisc.form(totCpuNeed, 16));
        res.append(StringMisc.form(totWorked, 16));
        res.append(StringMisc.form(totQuantums, 16));
        res.append(StringMisc.form(totProcesses, 16));
        res.append(StringMisc.form(totFinished, 16));

        return res.toString();
    }
}
