package co.morillas.gramgames.event;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("client_id")
    private String clientId;

    @SerializedName("event_id")
    private String id;

    @SerializedName("event_timestamp")
    private long timestamp;

    @SerializedName("event_type")
    private String type;

    @SerializedName("event_key")
    private String key;

    @SerializedName("event_value")
    private int value;

    public Event() {
        this.clientId = "";
        this.id = "";
        this.timestamp = 0L;
        this.type = "";
        this.key = "";
        this.value = 0;
    }

    public Event(String clientId, String id, long timestamp, String type, String key, int value) {
        this.clientId = clientId;
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getClientId() {
        return clientId;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (timestamp != event.timestamp) return false;
        if (value != event.value) return false;
        if (clientId != null ? !clientId.equals(event.clientId) : event.clientId != null) return false;
        if (id != null ? !id.equals(event.id) : event.id != null) return false;
        if (type != null ? !type.equals(event.type) : event.type != null) return false;
        return key != null ? key.equals(event.key) : event.key == null;
    }

    @Override
    public int hashCode() {
        int result = clientId != null ? clientId.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + value;
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "clientId='" + clientId + '\'' +
                ", id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
