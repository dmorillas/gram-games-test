package co.morillas.gramgames;

import co.morillas.gramgames.dao.EventDao;
import co.morillas.gramgames.dao.InMemoryEventDaoImpl;
import co.morillas.gramgames.handler.DumpEventsHandler;
import co.morillas.gramgames.handler.RegisterEventHandler;
import co.morillas.gramgames.manager.EventManager;
import co.morillas.gramgames.manager.EventManagerImpl;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {
    private static final int SERVER_LISTENING_PORT = 8080;

    public static void main(String[] args) throws IOException {
        EventDao eventDao = new InMemoryEventDaoImpl();
        EventManager eventManager = new EventManagerImpl(eventDao);

        Logger.info("Starting the Events Server");
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_LISTENING_PORT), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext("/event", new RegisterEventHandler(eventManager));
        Logger.info("Added endpoint '/event'");

        server.createContext("/events", new DumpEventsHandler(eventManager));
        Logger.info("Added endpoint '/events'");

        server.start();
        Logger.info("Server started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.info("Stopping server");
            server.stop(0);
        }));
    }
}
