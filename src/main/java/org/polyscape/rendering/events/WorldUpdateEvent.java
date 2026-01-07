package org.polyscape.rendering.events;

import org.polyscape.event.Event;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;

public class WorldUpdateEvent extends Event<WorldUpdateEvent> {
    public final Renderer renderer;
    public final RenderEngine renderEngine;

    public final double deltaTime;
    public final double accumulator;

    public WorldUpdateEvent(Renderer renderer, RenderEngine renderEngine, double deltaTime, double accumulator){
        this.renderer = renderer;
        this.renderEngine = renderEngine;
        this.deltaTime = deltaTime;
        this.accumulator = accumulator;
    }

}
