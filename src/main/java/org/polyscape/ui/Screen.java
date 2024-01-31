package org.polyscape.ui;

import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.component.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public abstract class Screen implements IScreen {

    protected CopyOnWriteArrayList<Component> components;

    public Screen(){
        components = new CopyOnWriteArrayList<>();
        onLoad();
    }


    public abstract void render(RenderEvent event);

    public abstract void click(MouseClickEvent event);

    public abstract void key(KeyEvent event);


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

    public void addComponent(Component component) {
        components.add(component);
    }


    private void renderComponents(RenderEvent event){
        for (Component c : components){
            c.draw(event);
        }
    }

    private void clickComponents(MouseClickEvent event){
        for (Component c : components){
            c.onClick(event);
        }
    }

    private void keyComponents(KeyEvent event){
        for (Component c : components){
            c.onKey(event);
        }
    }



}
