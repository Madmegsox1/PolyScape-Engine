package org.polyscape.event;

public record EventMetadata(Class<?> c, Integer priority) {
    public EventMetadata {
        if (priority == null) priority = 0;
    }
}
