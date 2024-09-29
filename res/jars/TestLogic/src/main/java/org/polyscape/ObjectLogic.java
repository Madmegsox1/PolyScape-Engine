package org.polyscape;

import org.lwjgl.glfw.GLFW;
import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.logic.LogicLink;
import org.polyscape.logic.LogicType;
import org.polyscape.logic.objectLogic.LogicObject;
import org.polyscape.rendering.events.KeyEvent;

@LogicLink(logicType = LogicType.OBJECT, linkId = 999)
public class ObjectLogic extends LogicObject {
    @Override
    public void onLoad() {
        System.out.println("ObjectLogic.onLoad");
        IEvent<KeyEvent> a = (b) -> {
            float yv = 0;
            float xv = 0;

            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_W)) {
                yv += 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_S)) {
                yv -= 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_A)) {
                xv -= 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_D)) {
                xv += 20f;
            }

            float finalXv = xv;
            float finalYv = yv;
            object.addForce(xv, yv);
        };
        KeyEvent.addEvent(a, new EventMetadata(KeyEvent.class, 0));
    }

    @Override
    public void onUnload() {
        System.out.println("ObjectLogic.onUnload");
    }

    @Override
    public void onRender() {

    }

    @Override
    public void onPosUpdate() {

    }
}
