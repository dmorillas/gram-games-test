package co.morillas.gramgames.handler;

import co.morillas.gramgames.Logger;
import co.morillas.gramgames.manager.EventManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DumpEventsHandler implements HttpHandler {

    private final EventManager eventManager;

    public DumpEventsHandler(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> parameters = getParameters(httpExchange.getRequestURI().getQuery());
        String clientId = parameters.get("clientId");

        if(isNullOrEmpty(clientId)) {
            String message = "Bad request: clientId is invalid.";
            Logger.error(message);
            sendResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST, message);
            return;
        }

        String response = eventManager.dumpEvents(clientId);
        sendResponse(httpExchange, HttpURLConnection.HTTP_OK, response);
    }

    private Map<String, String> getParameters(String query) {
        if(isNullOrEmpty(query)) {
            return new HashMap<>();
        }

        return Stream.of(query.split("&"))
                .map(parameter -> parameter.split("="))
                .collect(Collectors.toMap(p -> p[0], p -> p.length > 1 ? p[1]: ""));
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private void sendResponse(HttpExchange httpExchange, int httpCode, String response) throws IOException {
        httpExchange.sendResponseHeaders(httpCode, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}