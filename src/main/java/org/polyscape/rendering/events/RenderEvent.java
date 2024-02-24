package org.polyscape.rendering.events;


import org.polyscape.event.Event;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;

public class RenderEvent extends Event<RenderEvent> {

    public final Renderer renderer;
    public final RenderEngine renderEngine;

    public final float alpha;


    public RenderEvent(Renderer renderer, RenderEngine renderEngine, float alpha){
        this.renderer = renderer;
        this.renderEngine = renderEngine;
        this.alpha = alpha;
    }
}
