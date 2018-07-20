package co.morillas.gramgames.event;

public enum EventType {
    CUMULATIVE_EVENT_TYPE("CUMULATIVE"),
    TIME_SERIES_EVENT_TYPE("TIME-SERIES");

    private String name;

    EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
