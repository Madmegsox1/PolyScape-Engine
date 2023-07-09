package org.polyscape.eventbus;

public interface IEvent<T> {
    void run(T event);
}
