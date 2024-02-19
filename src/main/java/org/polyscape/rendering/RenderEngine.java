package org.polyscape.rendering;

import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.object.ObjectManager;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.RenderEvent;

import static org.lwjgl.opengl.GL11.*;

public final class RenderEngine {
    public static int fps = 0;
    public static double deltaTime = 0;


    public void render(final Renderer renderer, final Display display) {
        double time = GLFW.glfwGetTime();
        double fpsTime = time;
        int fpsOld = 0;
        fps = 0;

        final float timeStep = 1.0f / 60.0f;
        final int velocityIterations = 6;
        final int positionIterations = 2;

        while (!renderer.shouldClose()) {

            if (renderer.isUpdateReady()) {
                final double currentTime = GLFW.glfwGetTime();
                deltaTime = currentTime - time;
                time = currentTime;
                ObjectManager.world.step(timeStep, velocityIterations, positionIterations);
                renderer.prepare();
                Engine.getEventBus().postEvent(new RenderEvent(renderer, this));
                renderer.render(display.getWindow());
                fpsOld++;
                if (GLFW.glfwGetTime() - fpsTime >= 1.0) {
                    fps = fpsOld;
                    fpsOld = 0;
                    fpsTime = GLFW.glfwGetTime();
                }
            }
        }
        System.exit(0);
    }


    public static void drawLine(final Vector2 a, final Vector2 b, final float width, final Color color) {
        final float[] c = Color.convertColorToFloatAlpha(color);
        glPushMatrix();
        glColor4f(c[0], c[1], c[2], c[3]);
        glLineWidth(width);
        glBegin(GL_LINE_STRIP);

        glVertex2f(a.x, a.y);
        glVertex2f(b.x, b.y);

        glEnd();
        glPopMatrix();
    }


    public static void drawQuad(final Vector2 vector, final float width, final float height, final Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        final float[] c = Color.convertColorToFloat(color);
        glPushMatrix();
        glColor3f(c[0], c[1], c[2]);
        glTranslatef(vector.x + halfWidth, vector.y + halfHeight, 0);
        //glRotatef((float) Math.toDegrees(0), 0, 0, 1);
        glBegin(GL_QUADS);

        glVertex2f(-halfWidth, -halfHeight);
        glVertex2f(-halfWidth, halfHeight);
        glVertex2f(halfWidth, halfHeight);
        glVertex2f(halfWidth, -halfHeight);

        glEnd();

        glPopMatrix();
    }

    public static void drawQuadAngle(final Vector2 vector, final float angle, final float width, final float height, final Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        final float[] c = Color.convertColorToFloat(color);
        glPushMatrix();
        glColor3f(c[0], c[1], c[2]);
        glTranslatef(vector.x + halfWidth, vector.y + halfHeight, 0);
        glRotatef((float) Math.toDegrees(angle), 0, 0, 1);
        glBegin(GL_QUADS);

        glVertex2f(-halfWidth, -halfHeight);
        glVertex2f(-halfWidth, halfHeight);
        glVertex2f(halfWidth, halfHeight);
        glVertex2f(halfWidth, -halfHeight);

        glEnd();

        glPopMatrix();
    }

    public static void drawQuadAngleA(final Vector2 vector, float angle, final float width, final float height, final Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        final float[] c = Color.convertColorToFloatAlpha(color);

        glPushMatrix();

        glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
        glColor4f(c[0], c[1], c[2], c[3]);
        glTranslatef(vector.x + halfWidth, vector.y + halfHeight, 0);
        glRotatef((float) Math.toDegrees(angle), 0, 0, 1);
        glBegin(GL_QUADS);

        glVertex2f(-halfWidth, -halfHeight);
        glVertex2f(-halfWidth, halfHeight);
        glVertex2f(halfWidth, halfHeight);
        glVertex2f(halfWidth, -halfHeight);

        glEnd();
        glPopMatrix();

    }

    public static void drawQuadA(final Vector2 vector, final float width, final float height, final Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        final float[] c = Color.convertColorToFloatAlpha(color);

        glPushMatrix();

        glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
        glColor4f(c[0], c[1], c[2], c[3]);
        glTranslatef(vector.x + halfWidth, vector.y + halfHeight, 0);

        glBegin(GL_QUADS);

        glVertex2f(-halfWidth, -halfHeight);
        glVertex2f(-halfWidth, halfHeight);
        glVertex2f(halfWidth, halfHeight);
        glVertex2f(halfWidth, -halfHeight);

        glEnd();
        glPopMatrix();

    }

    public static void drawQuadTextureAngle(final Vector2 vector2, float angle, final float width, final float height, final Texture texture, final Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        final float[] c = Color.convertColorToFloatAlpha(color);

        glPushMatrix();
        texture.bind();
        glColor4f(c[0], c[1], c[2], c[3]);
        glTranslatef(vector2.x + halfWidth, vector2.y + halfHeight, 0);
        glRotatef(angle, 0, 0, 1);
        glTranslatef(-halfWidth, -halfHeight, 0);

        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(0, 0);

        glTexCoord2f(1, 0);
        glVertex2f(width, 0);

        glTexCoord2f(1, 1);
        glVertex2f(width, height);

        glTexCoord2f(0, 1);
        glVertex2f(0, height);

        glEnd();
        texture.disable();
        glPopMatrix();
    }

    public static void drawQuadTexture(final Vector2 vector2, final float width, final float height, final Texture texture, final Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        final float[] c = Color.convertColorToFloatAlpha(color);

        glPushMatrix();
        texture.bind();
        glColor4f(c[0], c[1], c[2], c[3]);
        glTranslatef(vector2.x + halfWidth, vector2.y + halfHeight, 0);
        glTranslatef(-halfWidth, -halfHeight, 0);

        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(0, 0);

        glTexCoord2f(1, 0);
        glVertex2f(width, 0);

        glTexCoord2f(1, 1);
        glVertex2f(width, height);

        glTexCoord2f(0, 1);
        glVertex2f(0, height);

        glEnd();
        texture.disable();
        glPopMatrix();
    }

    public static void drawQuadTextureAngle(final Vector2 vector2, float angle, final float width, final float height, final Texture texture) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        glPushMatrix();
        texture.bind();

        glColor4f(1, 1, 1, 1);
        glTranslatef(vector2.x + halfWidth, vector2.y + halfHeight, 0);
        glRotatef(angle, 0, 0, 1);
        glTranslatef(-halfWidth, -halfHeight, 0);


        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(0, 0);

        glTexCoord2f(1, 0);
        glVertex2f(width, 0);

        glTexCoord2f(1, 1);
        glVertex2f(width, height);

        glTexCoord2f(0, 1);
        glVertex2f(0, height);

        glEnd();
        texture.disable();
        glPopMatrix();
    }


    public static void drawQuadTexture(final Vector2 vector2, final float width, final float height, final Texture texture) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        glPushMatrix();
        texture.bind();

        glColor4f(1, 1, 1, 1);
        glTranslatef(vector2.x + halfWidth, vector2.y + halfHeight, 0);
        glTranslatef(-halfWidth, -halfHeight, 0);


        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(0, 0);

        glTexCoord2f(1, 0);
        glVertex2f(width, 0);

        glTexCoord2f(1, 1);
        glVertex2f(width, height);

        glTexCoord2f(0, 1);
        glVertex2f(0, height);

        glEnd();
        texture.disable();
        glPopMatrix();
    }

    public static void drawQuadTextureAngle(final Vector2 vector2, float angle, final float width, final float height, final float tx, final float ty, final float tw, final float th, final Texture texture) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

        glPushMatrix();
        texture.bind();
        glColor4f(1, 1, 1, 1);
        glTranslatef(vector2.x + halfWidth, vector2.y + halfHeight, 0);
        glRotatef(angle, 0, 0, 1);
        glTranslatef(-halfWidth, -halfHeight, 0);

        glBegin(GL_QUADS);

        glTexCoord2f(tx, ty);
        glVertex2f(0, 0);

        glTexCoord2f(tx + tw, ty);
        glVertex2f(width, 0);

        glTexCoord2f(tx + tw, ty + th);
        glVertex2f(width, height);

        glTexCoord2f(tx, ty + th);
        glVertex2f(0, height);

        glEnd();
        texture.disable();
        glPopMatrix();
    }

    public static void drawQuadTexture(final Vector2 vector2, final float width, final float height, final float tx, final float ty, final float tw, final float th, final Texture texture) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

        glPushMatrix();
        texture.bind();
        glColor4f(1, 1, 1, 1);
        glTranslatef(vector2.x + halfWidth, vector2.y + halfHeight, 0);
        glTranslatef(-halfWidth, -halfHeight, 0);

        glBegin(GL_QUADS);

        glTexCoord2f(tx, ty);
        glVertex2f(0, 0);

        glTexCoord2f(tx + tw, ty);
        glVertex2f(width, 0);

        glTexCoord2f(tx + tw, ty + th);
        glVertex2f(width, height);

        glTexCoord2f(tx, ty + th);
        glVertex2f(0, height);

        glEnd();
        texture.disable();
        glPopMatrix();
    }

    public static void drawQuadTextureAngle(final Vector2 vector2, float angle, final float width, final float height, final float tx, final float ty, final float tw, final float th, final Texture texture, final Color color) {
        final float[] c = Color.convertColorToFloatAlpha(color);
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        texture.bind();
        glPushMatrix();

        glColor4f(c[0], c[1], c[2], c[3]);
        glTranslatef(vector2.x + halfWidth, vector2.y + halfHeight, 0);
        glRotatef(angle, 0, 0, 1);
        glTranslatef(-halfWidth, -halfHeight, 0);
        glBegin(GL_QUADS);

        glTexCoord2f(tx, ty);
        glVertex2f(0, 0);

        glTexCoord2f(tx + tw, ty);
        glVertex2f(width, 0);

        glTexCoord2f(tx + tw, ty + th);
        glVertex2f(width, height);

        glTexCoord2f(tx, ty + th);
        glVertex2f(0, height);

        glEnd();
        texture.disable();
        glPopMatrix();
    }

    public static void drawQuadTexture(final Vector2 vector2, final float width, final float height, final float tx, final float ty, final float tw, final float th, final Texture texture, final Color color) {
        final float[] c = Color.convertColorToFloatAlpha(color);
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        texture.bind();
        glPushMatrix();

        glColor4f(c[0], c[1], c[2], c[3]);
        glTranslatef(vector2.x + halfWidth, vector2.y + halfHeight, 0);
        glTranslatef(-halfWidth, -halfHeight, 0);
        glBegin(GL_QUADS);

        glTexCoord2f(tx, ty);
        glVertex2f(0, 0);

        glTexCoord2f(tx + tw, ty);
        glVertex2f(width, 0);

        glTexCoord2f(tx + tw, ty + th);
        glVertex2f(width, height);

        glTexCoord2f(tx, ty + th);
        glVertex2f(0, height);

        glEnd();
        texture.disable();
        glPopMatrix();
    }

    public static void drawQuadLines(final Vector2 pos, final float width, final float height, final float lineWidth, final Color color) {
        RenderEngine.drawLine(pos, new Vector2(pos.x, pos.y + height), lineWidth, color);
        RenderEngine.drawLine(new Vector2(pos.x, pos.y + height), new Vector2(pos.x + width, pos.y + height), lineWidth, color);
        RenderEngine.drawLine(new Vector2(pos.x + width, pos.y + height), new Vector2(pos.x + width, pos.y), lineWidth, color);
        RenderEngine.drawLine(new Vector2(pos.x + width, pos.y), pos, lineWidth, color);
    }

    public static void drawWireframe(Vector2 screenPos, float screenWidth, float screenHeight, float angle) {
        final float[] c = Color.convertColorToFloatAlpha(Color.GREEN);
        glPushMatrix();
        glColor4f(c[0], c[1], c[2], c[3]);
        // Translate to the object's position on the screen
        glTranslatef(screenPos.x, screenPos.y, 0);
        // Rotate around the center of the object
        glRotatef((float) Math.toDegrees(angle), 0, 0, 1);

        // Draw a wireframe rectangle to represent the object
        glBegin(GL_LINE_LOOP);
        glVertex2f(-screenWidth / 2, -screenHeight / 2);
        glVertex2f(-screenWidth / 2, screenHeight / 2);
        glVertex2f(screenWidth / 2, screenHeight / 2);
        glVertex2f(screenWidth / 2, -screenHeight / 2);
        glEnd();

        glPopMatrix();
    }


    public static float normalize(float val, float max, float min) {
        return (val - min) / (max - min);
    }
}
