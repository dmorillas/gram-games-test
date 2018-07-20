# INTRODUCTION

This is a technical test I had to do once. The idea was to develop a server with a simple REST API without using any kind of framework.

# WHAT?

- The server must have to entry points:
    - one which will allow to add new events to the system.
    - another to get all the events for a given user.
    
- The kind of storage to use will be memory structures.

- All the events have the same JSON format:
```
    {
        "client_id" : "",
        "event_id" : "",
        "event_timestamp" : 123145,
        "event_type" : ["CUMULATIVE" | "TIME-SERIES"],
        "event_key" : "",
        "event_value" : 1
    }
```

- There are two type of events:
    - **CUMULATIVE**: data will be stored as added to the previous ones
    - **TIME-SERIES**: all events must be stored individually
    
