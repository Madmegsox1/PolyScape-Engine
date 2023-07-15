package org.polyscape.event;

public final class EventBus {

    public void postEvent(Event<?> event){
        event.callEvents(event);
    }

}
