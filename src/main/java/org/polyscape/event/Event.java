package org.polyscape.event;

import java.util.*;

public abstract class Event<T> {

    public String name;
    private static HashMap<IEvent<?>, EventMetadata> events = new HashMap<>();


    public Event(){
        this.name = this.getClass().getSimpleName();
    }

    public Event(String name){
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public void callEvents(Object instance){
        for (IEvent<?> event : events.keySet()){
            if(instance.getClass().getSimpleName().equals(events.get(event).c().getSimpleName())) {
                ((IEvent<T>) event).run((T) instance);
            }
        }
    }

    public static void addEvent(IEvent<?> event, EventMetadata metadata){
        events.put(event, metadata);

        List<Map.Entry<IEvent<?>, EventMetadata> > list =
                new LinkedList<>(events.entrySet());

        list.sort(Comparator.comparing(o -> (o.getValue().priority())));

        HashMap<IEvent<?>, EventMetadata> temp = new LinkedHashMap<>();
        for (Map.Entry<IEvent<?>, EventMetadata> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        events = temp;
    }
}
