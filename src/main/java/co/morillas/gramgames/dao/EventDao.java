package co.morillas.gramgames.dao;

import co.morillas.gramgames.event.Event;

import java.util.List;
import java.util.Map;

public interface EventDao {
    void addCumulativeEvent(Event event);
    void addTimeSeriesEvent(Event event);
    List<Event> getCumulativeEvents();
    List<Event> getTimeSeriesEvents();
    Map<String, List<Event>> getEventsFor(String clientId);
}
