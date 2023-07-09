package org.polyscape.eventbus;

public final class EventBus {
    public void postEvent(Event<?> event){
        event.callEvents(event);
    }
}
