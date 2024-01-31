package org.polyscape.ui;

import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public interface IScreen {
    void draw(RenderEvent event);

    void onLoad();

    void onClick(MouseClickEvent event);

    void onKey(KeyEvent event);
}
