package com.scheduler;

import com.parser.StAXParser;
import com.scheduler.parser.SimulationHandler;
import com.scheduler.simulation.SimulationController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scheduling {
    private static final String schema = "res/processes.xsd";
    private static final String defaultConfig = "res/defaultConfig.xml";
    private static String config;
    private static SimulationController simulationController;

    public static void main(String[] args) {
        tryLoadConfig(args);

        SchedulerAlgorithm schedulerAlgorithm = new FairShare();

        File resFile = new File("res/Summary-Results.txt");
        File procFile = new File("res/Summary-Processes.txt");
        try {
            procFile.createNewFile();
            FileOutputStream procOut = new FileOutputStream(procFile, false);
            PrintStream procStream = new PrintStream(procOut);

            // Algorithm
            schedulerAlgorithm.run(simulationController, procStream);

            procStream.close();
            procOut.close();

            FileOutputStream resOut = new FileOutputStream(resFile, false);
            PrintStream resStream = new PrintStream(resOut);
            resStream.println("Scheduling via: Fair Share algorithm");
            resStream.println(simulationController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tryLoadConfig(String[] args) {
        if (args.length != 1) {
            Logger.getLogger("main").log(Level.INFO, "Args are empty or have more than 1 arg");
            loadDefaoultConfig();
        } else {
            File f = new File(args[0]);
            if (!(f.exists())) {
                Logger.getLogger("main").log(Level.INFO, "Scheduling: error, file '" + f.getName() + "' does not exist.");
                loadDefaoultConfig();
            }
            if (!(f.canRead())) {
                Logger.getLogger("main").log(Level.INFO, "Scheduling: error, read of " + f.getName() + " failed.");
                loadDefaoultConfig();
            }
            config = args[0];
        }

        StAXParser<SimulationController> parser = new StAXParser<>(new SimulationHandler(), schema);
        simulationController = parser.parse(config);
        if (simulationController == null){
            Logger.getLogger("main").log(Level.INFO, "File " + config + " can't be parsed" );
            loadDefaoultConfig();
            simulationController = parser.parse(defaultConfig);}
    }

    private static void loadDefaoultConfig() {
        Logger.getLogger("main").log(Level.INFO, "Loading default config");
        config = defaultConfig;
    }
}
