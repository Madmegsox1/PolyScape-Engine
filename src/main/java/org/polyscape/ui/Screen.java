package org.polyscape.ui;

import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.component.Component;
import org.polyscape.ui.events.ComponentClickEvent;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public abstract class Screen implements IScreen {

    protected ArrayList<Component> components;
    public FontMac font;

    public Object model;

    protected ScreenManager manager;

    public Screen(){
        components = new ArrayList<>();
        //onLoad();
    }


    public abstract void render(RenderEvent event);

    public abstract void click(MouseClickEvent event);

    public abstract void key(KeyEvent event);

    public void model(){}


    @Override
    public void draw(RenderEvent event) {
        render(event);
        renderComponents(event);
    }

    @Override
    public void onClick(MouseClickEvent event) {
        click(event);
        clickComponents(event);
    }

    @Override
    public void onKey(KeyEvent event) {
        key(event);
        keyComponents(event);
    }

    public Component getComponentById(String id) {
        for (Component c : components) {
            if (c.getComponentId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void addComponent(Component component) {
        components.add(component);
    }


    public <T> T getModel(){
        if(model == null) return null;
        return (T) model;
    }
    private void renderComponents(RenderEvent event){
        for (Component c : components){
            if(!c.hidden) {
                c.draw(event);
            }
        }
    }

    protected void setFont(FontMac font){
        this.font = font;
    }

    private void clickComponents(MouseClickEvent event){
        for (Component c : components){
            if(c.hidden) continue;
            if(c.inBounds(event.mX, event.mY)){
                final ComponentClickEvent componentClickEvent = new ComponentClickEvent(event.mX, event.mY, c, event.action);

                Engine.getEventBus().postEvent(componentClickEvent);
                c.onComponentClick(componentClickEvent);
            }

            c.onClick(event);
        }
    }

    private void keyComponents(KeyEvent event){
        for (Component c : components){
            if(c.hidden) continue;
            c.onKey(event);
        }
    }

    public int getCenterX(){
        return Profile.Display.WIDTH / 2;
    }


    public void clearComponents(){
        this.components.clear();
    }



}
