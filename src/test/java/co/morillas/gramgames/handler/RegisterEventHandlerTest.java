package co.morillas.gramgames.handler;

import co.morillas.gramgames.event.Event;
import co.morillas.gramgames.manager.EventManager;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.*;
import java.net.HttpURLConnection;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class RegisterEventHandlerTest {

    @Mock
    private EventManager eventManagerMock;

    @Mock
    private HttpExchange httpExchangeMock;

    private RegisterEventHandler registerEventHandler;

    @Before
    public void setUp() {
        initMocks(this);

        registerEventHandler = new RegisterEventHandler(eventManagerMock);
    }

    @Test
    public void handle_IfBodyEmptyReturnsError() throws IOException {
        when(httpExchangeMock.getRequestBody()).thenReturn(new ByteArrayInputStream("".getBytes()));

        String response = "Bad request: event body null or is empty.";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        registerEventHandler.handle(httpExchangeMock);

        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }

    @Test
    public void handle_IfEventWithNoClientIdReturnsError() throws IOException {
        String event = "{\"event_id\" : \"...\", \"event_timestamp\" : 1, \"event_type\" : \"...\", \"event_key\" : \"...\", \"event_value\" : 1 }";
        when(httpExchangeMock.getRequestBody()).thenReturn(new ByteArrayInputStream(event.getBytes()));

        String response = "Bad request: event format invalid.";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        registerEventHandler.handle(httpExchangeMock);

        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }

    @Test
    public void handle_IfEventWithNoEventIdReturnsError() throws IOException {
        String event = "{ \"client_id\" : \"...\", \"event_timestamp\" : 1, \"event_type\" : \"...\", \"event_key\" : \"...\", \"event_value\" : 1 }";
        when(httpExchangeMock.getRequestBody()).thenReturn(new ByteArrayInputStream(event.getBytes()));

        String response = "Bad request: event format invalid.";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        registerEventHandler.handle(httpExchangeMock);

        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }

    @Test
    public void handle_IfEventWithNoEventKeyReturnsError() throws IOException {
        String event = "{ \"client_id\" : \"...\", \"event_id\" : \"...\", \"event_timestamp\" : 1, \"event_type\" : \"...\", \"event_value\" : 1 }";
        when(httpExchangeMock.getRequestBody()).thenReturn(new ByteArrayInputStream(event.getBytes()));

        String response = "Bad request: event format invalid.";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        registerEventHandler.handle(httpExchangeMock);

        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }

    @Test
    public void handle_IfEventWithNoEventTypeReturnsError() throws IOException {
        String event = "{ \"client_id\" : \"...\", \"event_id\" : \"...\", \"event_timestamp\" : 1, \"event_key\" : \"...\", \"event_value\" : 1 }";
        when(httpExchangeMock.getRequestBody()).thenReturn(new ByteArrayInputStream(event.getBytes()));

        String response = "Bad request: event format invalid.";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        registerEventHandler.handle(httpExchangeMock);

        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }

    @Test
    public void handle_IfEventCorrectReturnsOk() throws IOException {
        String event = "{ \"client_id\" : \"...\", \"event_id\" : \"...\", \"event_timestamp\" : 1, \"event_type\" : \"...\", \"event_key\" : \"...\", \"event_value\" : 1 }";
        when(httpExchangeMock.getRequestBody()).thenReturn(new ByteArrayInputStream(event.getBytes()));

        String response = "Event processed successfully";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        registerEventHandler.handle(httpExchangeMock);

        verify(eventManagerMock, times(1)).addEvent(any(Event.class));
        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }
}
