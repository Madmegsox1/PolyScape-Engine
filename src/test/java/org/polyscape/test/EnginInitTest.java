package org.polyscape.test;

import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.eventbus.EventBus;
import org.polyscape.eventbus.IEvent;
import org.polyscape.render.Window;
import org.polyscape.render.events.KeyEvent;

public class EnginInitTest extends Engine {

    public static void main(String[] args) {
        window = new Window("Polyscape - Engin Init Test");
        eventBus = new EventBus();

        assert Window.get() != null;
        window.create(false);

        assert window.closeFlag();

        IEvent<KeyEvent> keyEvent = n -> {
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_F)){
                window.fullscreen();
            }
        };

        KeyEvent.addEvent(keyEvent, "KeyEvent");

        while (window.closeFlag()){
            window.update();


            window.swapBuffers();
        }


    }

}
