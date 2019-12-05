package com.scheduler.parser;

import com.scheduler.simulation.GroupController;
import com.parser.Handler;
import com.scheduler.simulation.ProcessController;
import com.scheduler.simulation.SimulationController;

public class SimulationHandler implements Handler<SimulationController> {
    private SimulationController simulationController;
    private GroupController group;
    private int ioBlocking;

    FieldNames currentState;

    public SimulationHandler() {
        simulationController = new SimulationController();

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
                group = new GroupController(simulationController);
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
                simulationController.addGroup(group);
                break;
            case "process":
                group.addProcess(ProcessController.
                        create(group, ioBlocking, simulationController.getMeandev(), simulationController.getStanddev(),
                                simulationController.getBlockMean(), simulationController.getBlockDeviation()));
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
                simulationController.setMeandev(Integer.parseInt(information));
                break;
            case Standdev:
                simulationController.setStanddev(Integer.parseInt(information));
                break;
            case BlockMean:
                simulationController.setBlockMean(Integer.parseInt(information));
                break;
            case BlockDeviation:
                simulationController.setBlockDeviation(Integer.parseInt(information));
                break;
            case Runtime:
                simulationController.setRuntime(Integer.parseInt(information));
                break;
            case Quantum:
                simulationController.setQuantum(Integer.parseInt(information));
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
    public SimulationController getParseResult() {
        return simulationController;
    }
}
