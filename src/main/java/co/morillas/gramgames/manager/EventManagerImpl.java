package co.morillas.gramgames.manager;

import co.morillas.gramgames.Logger;
import co.morillas.gramgames.dao.EventDao;
import co.morillas.gramgames.event.Event;
import co.morillas.gramgames.event.EventType;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class EventManagerImpl implements EventManager {
    private final EventDao eventDao;

    public EventManagerImpl(final EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void addEvent(Event event) {
        String type = event.getType();

        if(type.equals(EventType.CUMULATIVE_EVENT_TYPE.getName())) {
            eventDao.addCumulativeEvent(event);
            return;
        }

        if(type.equals(EventType.TIME_SERIES_EVENT_TYPE.getName())) {
            eventDao.addTimeSeriesEvent(event);
            return;
        }

        Logger.error(String.format("received event with wrong type: %s. Ignoring it.", type));
        return;
    }

    public String dumpEvents(String clientId) {
        Map<String, List<Event>> events = eventDao.getEventsFor(clientId);

        Gson gson = new Gson();
        String json = gson.toJson(events);

        return json;
    }
}
