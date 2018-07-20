package co.morillas.gramgames.dao;

import co.morillas.gramgames.event.Event;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InMemoryEventDaoImplTest {

    private InMemoryEventDaoImpl inMemoryEventDaoImpl;

    @Before
    public void setUp() {
        inMemoryEventDaoImpl = new InMemoryEventDaoImpl();
    }

    @Test
    public void addCumulativeEvent_IfEventDoesNotExistAddsIt() {
        Event event = new Event("clientId", "id", 1L, "type", "key", 1);

        inMemoryEventDaoImpl.addCumulativeEvent(event);

        List<Event> cumulativeEvents = inMemoryEventDaoImpl.getCumulativeEvents();

        assertThat(cumulativeEvents.size(), is(1));
        assertThat(cumulativeEvents.get(0), is(event));
    }

    @Test
    public void addCumulativeEvent_IfEventExistsAddsValue() {
        Event initialEvent = new Event("clientId", "id", 1L, "type", "key", 1);
        Event event = new Event("clientId", "id", 1L, "type", "key", 3);
        Event expectedEvent = new Event("clientId", "id", 1L, "type", "key", 4);

        inMemoryEventDaoImpl.addCumulativeEvent(initialEvent);
        inMemoryEventDaoImpl.addCumulativeEvent(event);

        List<Event> cumulativeEvents = inMemoryEventDaoImpl.getCumulativeEvents();

        assertThat(cumulativeEvents.size(), is(1));
        assertThat(cumulativeEvents.get(0), is(expectedEvent));
    }

    @Test
    public void addTimeSeriesEvent_AlwaysAddsEvent() {
        Event event1 = new Event("clientId", "id", 1L, "type", "key", 1);
        Event event2 = new Event("clientId", "id", 1L, "type", "key", 3);

        inMemoryEventDaoImpl.addTimeSeriesEvent(event1);
        inMemoryEventDaoImpl.addTimeSeriesEvent(event2);

        List<Event> timeSeriesEvents = inMemoryEventDaoImpl.getTimeSeriesEvents();

        assertThat(timeSeriesEvents.size(), is(2));
        assertThat(timeSeriesEvents.get(0), is(event1));
        assertThat(timeSeriesEvents.get(1), is(event2));
    }

    @Test
    public void getEventsFor_WorksAsExpected() {
        Event event1 = new Event("clientId1", "id1", 1L, "type", "key", 1);
        Event event2 = new Event("clientId1", "id2", 1L, "type", "key2", 3);
        Event event3 = new Event("clientId2", "id3", 1L, "type", "key", 5);
        Event event4 = new Event("clientId1", "id4", 1L, "type", "key", 2);
        Event event5 = new Event("clientId1", "id5", 1L, "type", "key2", 3);

        inMemoryEventDaoImpl.addCumulativeEvent(event1);
        inMemoryEventDaoImpl.addCumulativeEvent(event2);
        inMemoryEventDaoImpl.addCumulativeEvent(event3);
        inMemoryEventDaoImpl.addTimeSeriesEvent(event4);
        inMemoryEventDaoImpl.addTimeSeriesEvent(event5);

        Map<String, List<Event>> expectedEvents = new HashMap<>();
        expectedEvents.put("CUMULATIVE", Arrays.asList(event1, event2));
        expectedEvents.put("TIME-SERIES", Arrays.asList(event4, event5));

        Map<String, List<Event>> events = inMemoryEventDaoImpl.getEventsFor("clientId1");

        assertThat(events.size(), is(2));
        assertThat(events, is(expectedEvents));
    }
}
