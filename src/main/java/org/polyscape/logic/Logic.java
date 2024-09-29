package org.polyscape.logic;


import org.polyscape.event.EventBus;

public abstract class Logic {
    private EventBus eventBus;

    public final void init(EventBus eventBus){
        this.eventBus = eventBus;
    }

    public abstract void onLoad();

    public abstract void onUnload();

}
