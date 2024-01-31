package org.polyscape.ui.component;

import org.polyscape.Profile;
import org.polyscape.object.RenderProperty;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.ui.IScreen;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public abstract class Component extends RenderProperty implements IComponent {
    protected Vector2 pos;

    protected Color backgroundColor;

    protected Color foregroundColor;

    protected Color accent;

    protected IScreen screen;

    public Component(int x, int y, int width, int height, IScreen screen) {
        this.screen = screen;
        this.pos = new Vector2(x, y);
        this.width = width;
        this.height = height;

        // todo theme manager etc
        this.backgroundColor = Profile.UiThemes.Dark.background;
        this.foregroundColor = Profile.UiThemes.Dark.foreground;
        this.accent = Profile.UiThemes.Dark.accent;
    }

    public Vector2 getPosition(){
        return pos;
    }

    public void setPosition(int x, int y) {
        this.pos = new Vector2(x, y);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
