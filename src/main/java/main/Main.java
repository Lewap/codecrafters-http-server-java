package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.MyServer;

import java.time.Instant;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    public static final String STARTUPTIME = Instant.now().toString();

    public static void main(String[] args) {

        Thread shutdownHook = new Thread(() -> logger.info("Shutting down..."));
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        logger.info("Server startup");
        MyServer server = new MyServer(args);
        server.start(4221);

    }
}
