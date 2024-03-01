package org.polyscape.ui.component;

import org.polyscape.Profile;
import org.polyscape.object.RenderProperty;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.ui.IScreen;
import org.polyscape.ui.Screen;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public abstract class Component extends RenderProperty implements IComponent {
    protected Vector2 pos;

    public Color backgroundColor;

    public Color foregroundColor;

    public Color accent;

    public Color accent2;

    protected Screen screen;

    protected String text;

    public String componentId;

    public boolean hidden;

    public Component(int x, int y, int width, int height, Screen screen, String compId) {
        this.screen = screen;
        this.pos = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.componentId = compId;
        this.hidden = false;

        // todo theme manager etc
        this.backgroundColor = Profile.UiThemes.Dark.background;
        this.foregroundColor = Profile.UiThemes.Dark.foreground;
        this.accent = Profile.UiThemes.Dark.accent;
        this.accent2 = Profile.UiThemes.Dark.accent2;
    }

    public Vector2 getPosition(){
        return pos;
    }

    public void setPosition(int x, int y) {
        this.pos = new Vector2(x, y);
    }

    public float getX(){
        return this.pos.x;
    }

    public float getY(){
        return this.pos.y;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean inBounds(double bx, double by){
        return (bx >= pos.x && bx <= pos.x + width) && (by >= pos.y && by <= pos.y + height);
    }

}
