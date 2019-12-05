package com.scheduler.parser;

import com.scheduler.simulation.ProcessGroup;
import com.parser.Handler;
import com.scheduler.simulation.ProcessSimulation;
import com.scheduler.simulation.Simulation;

public class SimulationHandler implements Handler<Simulation> {
    private Simulation simulation;
    private ProcessGroup group;
    private int ioBlocking;

    FieldNames currentState;

    public SimulationHandler() {
        simulation = new Simulation();

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
            case "blockMean":
                currentState = FieldNames.BlockMean;
                break;
            case "blockDeviation":
                currentState = FieldNames.BlockDeviation;
                break;
            case "runtime":
                currentState = FieldNames.Runtime;
                break;
            case "quantum":
                currentState = FieldNames.Quantum;
                break;
            case "group":
                currentState = FieldNames.Group;
                group = new ProcessGroup(simulation);
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
                        create(group, ioBlocking, simulation.getMeandev(), simulation.getStanddev(),
                                simulation.getBlockMean(), simulation.getBlockDeviation()));
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
            case BlockMean:
                simulation.setBlockMean(Integer.parseInt(information));
                break;
            case BlockDeviation:
                simulation.setBlockDeviation(Integer.parseInt(information));
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
