package org.polyscape.ui.component.lable;

import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.Component;
import org.polyscape.ui.events.ComponentClickEvent;

public class Label extends Component {

    public final Component parentComponent;

    public final int offsetX, offsetY;

    public Label(int offsetX, int offsetY, String text, String id, Screen screen, Component parentComponent) {
        super(0, 0, screen.font.getWidth(text), screen.font.getHeight(text), screen, id);
        this.parentComponent = parentComponent;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.text = text;
    }


    @Override
    public void draw(RenderEvent event) {
        this.screen.font.renderText(text, new Vector2(this.parentComponent.getX() + offsetX, this.parentComponent.getY() + offsetY), this.foregroundColor);

    }

    @Override
    public void onClick(MouseClickEvent event) {

    }

    @Override
    public void onKey(KeyEvent event) {

    }

    @Override
    public void onComponentClick(ComponentClickEvent event) {

    }

    @Override
    public String getName() {
        return "Label";
    }
}
