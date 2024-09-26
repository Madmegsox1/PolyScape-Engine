package org.polyscape.ui.events;

import org.polyscape.event.Event;
import org.polyscape.ui.Screen;

public class ScreenChangeEvent extends Event<ScreenChangeEvent> {

    public final Screen newScreen;
    public final Screen oldScreen;
    public final int index;

    public ScreenChangeEvent(int index, Screen newScreen, Screen oldScreen) {
        this.index = index;
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }
}
