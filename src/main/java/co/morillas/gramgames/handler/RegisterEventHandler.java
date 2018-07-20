package co.morillas.gramgames.handler;

import co.morillas.gramgames.Logger;
import co.morillas.gramgames.event.Event;
import co.morillas.gramgames.manager.EventManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;

public class RegisterEventHandler implements HttpHandler {

    private final EventManager eventManager;

    public RegisterEventHandler(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr =  new InputStreamReader(httpExchange.getRequestBody());
        BufferedReader br = new BufferedReader(isr);

        String body = br.lines().collect(Collectors.joining(""));
        if(body == null || body.isEmpty()) {
            String message = "Bad request: event body null or is empty.";
            Logger.error(message);
            sendResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST, message);
            return;
        }

        Gson gson = new Gson();
        Event event = gson.fromJson(body, Event.class);
        if(!isValidEvent(event)) {
            String message = "Bad request: event format invalid.";
            Logger.error(message);
            sendResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST, message);
            return;
        }

        eventManager.addEvent(event);

        sendResponse(httpExchange, HttpURLConnection.HTTP_OK, "Event processed successfully");
    }

    private boolean isValidEvent(Event event) {
        return !isNullOrEmpty(event.getClientId()) &&
                !isNullOrEmpty(event.getId()) &&
                !isNullOrEmpty(event.getKey()) &&
                !isNullOrEmpty(event.getType());
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
