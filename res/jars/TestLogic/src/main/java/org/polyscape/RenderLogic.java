package org.polyscape;

import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.logic.Logic;
import org.polyscape.logic.LogicLink;
import org.polyscape.logic.LogicType;
import org.polyscape.object.BaseObject;
import org.polyscape.object.FluidObject;
import org.polyscape.object.ObjectManager;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

@LogicLink(logicType = LogicType.BASE)
public class RenderLogic extends Logic {
    public static AtomicReference<Vector2> startDrag = new AtomicReference<>();
    public static AtomicReference<Float> translateX = new AtomicReference<>();
    public static AtomicReference<Float> translateY = new AtomicReference<>();

    @Override
    public void onLoad() {
        long window = Engine.getDisplay().getWindow();
        BaseObject object = ObjectManager.getObject(999);

        IEvent<MouseClickEvent> clickEvent = e -> {
            if (e.action == 0 && e.key == 2) {
                ObjectManager.clearObjects();
            }
        };

        IEvent<RenderEvent> renderEvent = e -> {
            if(startDrag.get() != null) {
                Vector2 v = Display.getMousePosition(window);
                RenderEngine.drawQuadA(startDrag.get(), v.x - startDrag.get().x, v.y - startDrag.get().y, new Color(0, 0, 0, 100));
            }

            Vector2 vector = object.getInterpolatedPosition(e.alpha);
            float playerX = vector.x;
            float playerY = vector.y;

            // Assuming screenWidth and screenHeight are the dimensions of your window
            float halfWidth = Profile.Display.WIDTH / 2.0f;
            float halfHeight = Profile.Display.HEIGHT / 2.0f;

            // Calculate the translation needed to keep the player at the center
            translateX.set(-playerX + halfWidth);
            translateY.set(-playerY + halfHeight);

            // Apply this translation to the view matrix or directly using OpenGL's legacy functions
            glLoadIdentity(); // Load the identity matrix to reset transformations
            glPushMatrix();
            glTranslatef(translateX.get(), translateY.get(), 0.0f);
            //glEnable(GL_CULL_FACE);
            ObjectManager.renderObjects(e.alpha);
            //glDisable(GL_CULL_FACE);
            glPopMatrix();
        };
        RenderEvent.addEvent(renderEvent, new EventMetadata(RenderEvent.class, 0));
        MouseClickEvent.addEvent(clickEvent, new EventMetadata(MouseClickEvent.class, 0));
    }

    @Override
    public void onUnload() {

    }
}
