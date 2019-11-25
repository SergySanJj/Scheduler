package com.scheduler.scheduler;

import com.parser.StAXParser;
import com.scheduler.scheduler.parser.SimulationHandler;
import com.scheduler.scheduler.simulation.Simulation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Scheduling {
    private static final String schema = "res/processes.xsd";

    public static void main(String[] args) {
        StAXParser<Simulation> parser = new StAXParser<>(new SimulationHandler(), schema);
        Simulation simulation = parser.parse("res/config.xml");

        FairShare fairShare = new FairShare();
        File resFile = new File("res/Summary-Results.txt");
        File procFile = new File("res/Summary-Processes.txt");
        try {
            procFile.createNewFile();
            FileOutputStream procOut = new FileOutputStream(procFile, false);
            PrintStream procStream = new PrintStream(procOut);
            fairShare.run(simulation, procStream);
            procStream.close();
            procOut.close();

            FileOutputStream resOut = new FileOutputStream(resFile, false);
            PrintStream resStream = new PrintStream(resOut);
            resStream.println("Scheduling via: Fair Share algorithm");
            resStream.println(simulation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
