package com.scheduler.scheduler.parser;

import com.scheduler.scheduler.simulation.ProcessGroup;
import com.parser.Handler;
import com.scheduler.scheduler.simulation.ProcessSimulation;
import com.scheduler.scheduler.simulation.Simulation;

import java.util.ArrayList;

public class SimulationHandler implements Handler<Simulation> {
    private Simulation simulation;
    private ProcessGroup group;
    private int ioBlocking;

    FieldNames currentState;

    public SimulationHandler() {
        simulation = new Simulation();
        group = new ProcessGroup();

        currentState = FieldNames.None;
    }

    @Override
    public void onTagStart(String tag) {
        switch (tag) {
            case "meandev":
                currentState = FieldNames.Meandev;
                break;
            case "standdev":
                currentState = FieldNames.Standdev;
                break;
            case "runtime":
                currentState = FieldNames.Runtime;
                break;
            case "quantum":
                currentState = FieldNames.Quantum;
                break;
            case "group":
                currentState = FieldNames.Group;
                group = new ProcessGroup();
                break;
            case "name":
                currentState = FieldNames.GroupName;
                break;
            case "process":
                currentState = FieldNames.Process;
                break;
            default:
                currentState = FieldNames.None;
        }
    }

    @Override
    public void onTagEnd(String tag) {
        switch (tag) {
            case "group":
                simulation.addGroup(group);
                break;
            case "process":
                group.addProcess(ProcessSimulation.
                        create(ioBlocking, simulation.getMeandev(), simulation.getStanddev()));
                break;
        }
    }

    @Override
    public void setAttribute(String attributeName, String value) {
    }

    @Override
    public void setTag(String information) {
        switch (currentState) {
            case Meandev:
                simulation.setMeandev(Integer.parseInt(information));
                break;
            case Standdev:
                simulation.setStanddev(Integer.parseInt(information));
                break;
            case Runtime:
                simulation.setRuntime(Integer.parseInt(information));
                break;
            case Quantum:
                simulation.setQuantum(Integer.parseInt(information));
                break;
            case Process:
                ioBlocking = Integer.parseInt(information);
                break;
            case GroupName:
                group.setName(information);
                break;
            default:
                break;
        }

        currentState = FieldNames.None;
    }

    @Override
    public Simulation getParseResult() {
        return simulation;
    }
}
