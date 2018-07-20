package co.morillas.gramgames.manager;

import co.morillas.gramgames.event.Event;

public interface EventManager {
    void addEvent(Event event);
    String dumpEvents(String clientId);
}
