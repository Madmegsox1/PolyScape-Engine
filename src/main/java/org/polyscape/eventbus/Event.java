package org.polyscape.eventbus;

import java.util.HashMap;
import java.util.Map;

public abstract class Event<T> {

    public String name;
    private static HashMap<IEvent<?>, String> events = new HashMap<>();


    public Event(){
        this.name = this.getClass().getSimpleName();
    }

    public Event(String name){
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public void callEvents(Event<?> instance){

        for (Map.Entry<IEvent<?>, String> event: events.entrySet()){
            if(instance.name.equals(event.getValue())) {
                ((IEvent<T>) event.getKey()).run((T) instance);
            }
        }
    }

    public static void addEvent(IEvent<?> event, String eventName){
        events.put(event, eventName);
    }

}
