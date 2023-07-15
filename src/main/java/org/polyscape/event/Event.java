package org.polyscape.event;

import java.util.HashMap;

public abstract class Event<T> {

    public String name;
    private static HashMap<IEvent<?>, Class<?>> events = new HashMap<>();


    public Event(){
        this.name = this.getClass().getSimpleName();
    }

    public Event(String name){
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public void callEvents(Object instance){

        for (IEvent<?> event : events.keySet()){
            if(instance.getClass().getSimpleName().equals(events.get(event).getSimpleName())) {
                ((IEvent<T>) event).run((T) instance);
            }
        }
    }

    public static void addEvent(IEvent<?> event, Class<?> c){
        events.put(event, c);
    }







}
