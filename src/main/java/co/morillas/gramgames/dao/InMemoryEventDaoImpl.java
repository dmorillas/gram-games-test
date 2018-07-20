package co.morillas.gramgames.dao;

import co.morillas.gramgames.event.Event;
import co.morillas.gramgames.event.EventType;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryEventDaoImpl implements EventDao {
    private List<Event> cumulativeEvents;
    private List<Event> timeBasedEvents;

    public InMemoryEventDaoImpl() {
        cumulativeEvents = new ArrayList<>();
        timeBasedEvents = new ArrayList<>();
    }

    @Override
    public void addCumulativeEvent(Event event) {
        synchronized (cumulativeEvents) {
            Optional<Event> cumulativeEvent = cumulativeEvents
                    .stream()
                    .filter(e ->
                            e.getClientId().equals(event.getClientId()) &&
                                    e.getId().equals(event.getId()) &&
                                    e.getKey().equals(event.getKey()))
                    .findFirst();

            if (cumulativeEvent.isPresent()) {
                cumulativeEvent.get().setValue(cumulativeEvent.get().getValue() + event.getValue());
            } else {
                cumulativeEvents.add(event);
            }
        }
    }

    @Override
    public void addTimeSeriesEvent(Event event) {
        synchronized (timeBasedEvents) {
            timeBasedEvents.add(event);
        }
    }

    @Override
    public List<Event> getCumulativeEvents() {
        return cumulativeEvents;
    }

    @Override
    public List<Event> getTimeSeriesEvents() {
        return timeBasedEvents;
    }


    @Override
    public Map<String, List<Event>> getEventsFor(String clientId) {
        List<Event> cumulative = cumulativeEvents.stream()
                .filter(event -> event.getClientId().equals(clientId))
                .collect(Collectors.toList());

        List<Event> timeBased = timeBasedEvents.stream()
                .filter(event -> event.getClientId().equals(clientId))
                .collect(Collectors.toList());

        Map<String, List<Event>> events = new HashMap<>();
        events.put(EventType.CUMULATIVE_EVENT_TYPE.getName(), cumulative);
        events.put(EventType.TIME_SERIES_EVENT_TYPE.getName(), timeBased);

        return events;
    }
}
