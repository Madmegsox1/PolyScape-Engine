package org.polyscape.rendering.events;


import org.polyscape.event.Event;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;

public class RenderEvent extends Event<RenderEvent> {

    public final Renderer renderer;
    public final RenderEngine renderEngine;


    public RenderEvent(Renderer renderer, RenderEngine renderEngine){
        this.renderer = renderer;
        this.renderEngine = renderEngine;
    }
}
