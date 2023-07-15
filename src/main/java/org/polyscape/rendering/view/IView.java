package org.polyscape.rendering.view;


import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;

public interface IView {

    void renderView();

    void keyEvent(KeyEvent event);

    void mouseEvent(MouseClickEvent event);
}
