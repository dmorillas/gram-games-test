package co.morillas.gramgames.manager;

import co.morillas.gramgames.dao.EventDao;
import co.morillas.gramgames.event.Event;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventManagerImplTest {
    @Mock
    private EventDao eventDaoMock;

    private EventManagerImpl eventManager;

    @Before
    public void setUp() {
        initMocks(this);

        eventManager = new EventManagerImpl(eventDaoMock);
    }

    @Test
    public void addEvent_IfEventCumulative_WorksAsExcepted() {
        Event eventMock = mock(Event.class);
        when(eventMock.getType()).thenReturn("CUMULATIVE");

        eventManager.addEvent(eventMock);

        verify(eventDaoMock, times(1)).addCumulativeEvent(eventMock);
        verify(eventDaoMock, times(0)).addTimeSeriesEvent(eventMock);
    }

    @Test
    public void addEvent_IfEventTimeSeries_WorksAsExcepted() {
        Event eventMock = mock(Event.class);
        when(eventMock.getType()).thenReturn("TIME-SERIES");

        eventManager.addEvent(eventMock);

        verify(eventDaoMock, times(0)).addCumulativeEvent(eventMock);
        verify(eventDaoMock, times(1)).addTimeSeriesEvent(eventMock);
    }

    @Test
    public void addEvent_IfWrongEventType_DoesNothing() {
        Event eventMock = mock(Event.class);
        when(eventMock.getType()).thenReturn("WRONG-EVENT-TYPE");

        eventManager.addEvent(eventMock);

        verify(eventDaoMock, times(0)).addCumulativeEvent(eventMock);
        verify(eventDaoMock, times(0)).addTimeSeriesEvent(eventMock);
    }

    @Test
    public void dumpEvents_WorksAsExpected() {
        Map<String, List<Event>> events = new HashMap<>();
        events.put("key1", Arrays.asList(new Event()));
        events.put("key2", Arrays.asList(new Event(), new Event()));

        Gson gson = new Gson();
        String expectedJson = gson.toJson(events);

        when(eventDaoMock.getEventsFor("clientId")).thenReturn(events);

        String json = eventManager.dumpEvents("clientId");

        assertThat(json, is(expectedJson));
    }
}
