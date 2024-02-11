package org.polyscape.ui.component;

import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.events.ComponentClickEvent;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public interface IComponent {
    void draw(RenderEvent event);

    void onClick(MouseClickEvent event);

    void onKey(KeyEvent event);

    void onComponentClick(ComponentClickEvent event);

    String getName();

}
