package org.polyscape.ui.events;

import org.polyscape.event.Event;
import org.polyscape.ui.component.Component;

public class ComponentClickEvent extends Event<ComponentClickEvent> {

    public final double mx, my;
    public final Component component;

    public final int action;

    public ComponentClickEvent(double mx, double my, Component component, int action){
        this.mx =mx;
        this.my = my;
        this.component = component;
        this.action = action;
    }

}
