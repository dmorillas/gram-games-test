package co.morillas.gramgames.handler;

import co.morillas.gramgames.manager.EventManager;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class DumpEventsHandlerTest {

    @Mock
    private EventManager eventManagerMock;

    @Mock
    private HttpExchange httpExchangeMock;

    private DumpEventsHandler dumpEventsHandler;

    @Before
    public void setUp() {
        initMocks(this);

        dumpEventsHandler = new DumpEventsHandler(eventManagerMock);
    }

    @Test
    public void handle_IfNoClientIdInUrlReturnsError() throws Exception {
        String response = "Bad request: clientId is invalid.";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(httpExchangeMock.getRequestURI()).thenReturn(new URI("/events"));
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        dumpEventsHandler.handle(httpExchangeMock);

        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }

    @Test
    public void handle_IfWrongClientIdParameterInUrlReturnsError() throws Exception {
        String response = "Bad request: clientId is invalid.";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(httpExchangeMock.getRequestURI()).thenReturn(new URI("/events?clientId="));
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        dumpEventsHandler.handle(httpExchangeMock);

        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }

    @Test
    public void handle_WorksAsExpected() throws Exception {
        String response = "this is a list of manager";
        OutputStream outputStreamMock = mock(OutputStream.class);
        when(eventManagerMock.dumpEvents(any(String.class))).thenReturn(response);
        when(httpExchangeMock.getRequestURI()).thenReturn(new URI("/events?clientId=3"));
        when(httpExchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        dumpEventsHandler.handle(httpExchangeMock);

        verify(httpExchangeMock, times(1)).sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
        verify(outputStreamMock, times(1)).write(response.getBytes());
    }
}