package org.polyscape.event;

public interface IEvent<T> {

    void run(T event);

}
