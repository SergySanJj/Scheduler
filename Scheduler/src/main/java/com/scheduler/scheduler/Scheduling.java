package com.scheduler.scheduler;

import com.parser.StAXParser;
import com.scheduler.scheduler.parser.SimulationHandler;
import com.scheduler.scheduler.simulation.Simulation;

public class Scheduling {
    private static final String schema = "processes.xsd";

    public static void main(String[] args) {
        StAXParser<Simulation> parser = new StAXParser<>(new SimulationHandler(),schema);
        Simulation simulation = parser.parse("config.xml");

        int a=0;
        a++;
    }
}
