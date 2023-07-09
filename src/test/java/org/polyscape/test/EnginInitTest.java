package org.polyscape.test;

import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.eventbus.EventBus;
import org.polyscape.eventbus.IEvent;
import org.polyscape.render.Model;
import org.polyscape.render.Renderer;
import org.polyscape.render.Window;
import org.polyscape.render.events.KeyEvent;

public class EnginInitTest extends Engine {

    public static void main(String[] args) {
        window = new Window("Polyscape - Engin Init Test");
        window.setBackgroundColor(1f, 0f, 0f);
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

        Model model = new Model(new float[] {
                -0.5f, 0.5f, 0.0f, // top left      0
                0.5f, 0.5f, 0.0f, // top right      1
                -0.5f, -0.5f, 0.0f, // bottom left  2
                0.5f, -0.5f, 0.0f // bottom right   3
        }, new int[]{
                0,1,2,
                2,3,0
        });
        model.create();

        renderer = new Renderer();



        while (window.closeFlag()){
            if(window.isUpdateReady()) {
                window.update();
                renderer.renderModel(model);
                window.swapBuffers();
            }
        }

        model.remove();


    }

}
