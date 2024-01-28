package org.polyscape.rendering.sprite.action;

import org.polyscape.object.RenderProperty;

public interface IAction {
    void run(RenderProperty object) throws InterruptedException;
}
