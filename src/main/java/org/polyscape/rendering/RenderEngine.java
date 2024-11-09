package org.polyscape.rendering;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.pkl.core.util.Nullable;
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
    private static Renderer renderer;


    public void render(final Renderer renderer, final Display display) {
        RenderEngine.renderer = renderer;
        double time = GLFW.glfwGetTime();
        double fpsTime = time;
        int fpsOld = 0;
        fps = 0;

        final float timeStep = 1.0f / 60.0f;
        final int velocityIterations = 6;
        final int positionIterations = 2;
        double accumulator = 0.0;

        while (!renderer.shouldClose()) {
            final double currentTime = GLFW.glfwGetTime();
            deltaTime = currentTime - time;
            time = currentTime;

            accumulator += deltaTime;
            while (accumulator >= timeStep) {
                ObjectManager.setPreviousPositions();
                ObjectManager.world.step(timeStep, velocityIterations, positionIterations);
                ObjectManager.updatePosition();
                accumulator -= timeStep;
            }

            float alpha = (float) (accumulator / timeStep);
            renderer.prepare();
            Engine.getEventBus().postEvent(new RenderEvent(renderer, this, alpha));
            renderer.render(display.getWindow());
            fpsOld++;
            if (GLFW.glfwGetTime() - fpsTime >= 1.0) {
                fps = fpsOld;
                fpsOld = 0;
                fpsTime = GLFW.glfwGetTime();
            }
        }
        System.exit(0);
    }


    public static void drawLineNew(final Vector2 start, final Vector2 end, final float width, final Color color) {
        Vector2 direction = new Vector2(end.x - start.x, end.y - start.y);
        float lineLength = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        float angle = (float) Math.atan2(direction.y, direction.x);

        // Set up the transformation matrix for the line quad
        Matrix4f transformMatrix = new Matrix4f()
                .translate(start.x, start.y, 0)
                .rotateZ(angle)
                .scale(lineLength, width, 1.0f);


        renderer.shader.bind();
        renderer.shader.loadTransformMatrix(transformMatrix);
        renderer.shader.loadShapeColor(color);
        renderer.shader.loadUseTexture(false);

        float[] triangleVertices = {
                // Position          // Texture Coords
                0.0f, 0.0f,        0.0f, 0.0f,    // Bottom-left corner
                1f, 0.0f,        1.0f, 0.0f,    // Bottom-right corner
                1f,  1f,        1.0f, 1.0f,    // Top-right corner

                0.0f, 0.0f,        0.0f, 0.0f,    // Bottom-left corner
                1f,  1f,        1.0f, 1.0f,    // Top-right corner
                0.0f,  1f,        0.0f, 1.0f     // Top-left corner
        };

        GL30.glBindVertexArray(renderer.vaoId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderer.vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, triangleVertices, GL15.GL_DYNAMIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);

        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        renderer.shader.unbind();
    }

    public static void drawCircleAngleTexturedNew(final Vector2 center, final float radius, final float angle, final int segments, @Nullable Texture texture) {
        drawCircleAngleTexturedNew(center, radius, angle, segments, texture, Color.WHITE);
    }

    public static void drawCircleAngleTexturedNew(final Vector2 center, final float radius, final float angle, final int segments, @Nullable Texture texture, Color color) {
        Matrix4f transformMatrix = new Matrix4f()
                .translate(center.x, center.y, 0)
                .rotateZ(angle);


        float[] vertices = new float[(segments + 2) * 4];

        vertices[0] = 0.0f;         // x
        vertices[1] = 0.0f;         // y
        vertices[2] = 0.5f;         // tx
        vertices[3] = 0.5f;         // ty

        for (int i = 0; i <= segments; i++) {
            double theta = Math.PI * 2 * i / segments;
            float xCos = (float) Math.cos(theta);
            float yCos = (float) Math.sin(theta);

            float x = radius * xCos;
            float y = radius * yCos;

            float tx = xCos * 0.5f + 0.5f;
            float ty = yCos * 0.5f + 0.5f;

            vertices[(i + 1) * 4] = x;
            vertices[(i + 1) * 4 + 1] = y;
            vertices[(i + 1) * 4 + 2] = tx;
            vertices[(i + 1) * 4 + 3] = ty;
        }

        // Bind shader and set transformation matrix
        renderer.shader.bind();
        renderer.shader.loadTransformMatrix(transformMatrix);
        renderer.shader.loadUseTexture(false);
        renderer.shader.loadShapeColor(color);

        GL30.glBindVertexArray(renderer.vaoId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderer.vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);

        GL20.glEnableVertexAttribArray(0); // Position
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(1); // Texture coordinate
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

        if(texture != null) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            texture.bind();
        }

        renderer.shader.loadTextureSampler(0);

        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, segments + 2);

        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        renderer.shader.unbind();
        if(texture != null) {
            texture.disable();
        }
    }

    public static void drawCircleAngleNew(final Vector2 center, final float radius, final float angle, final int segments, final Color color) {
        drawCircleAngleTexturedNew(center, radius, angle, segments, null, color);
    }

    // todo rewrite this function
    public static void drawHollowCircle(final Vector2 center, final float radius, final int segments, final float width, final Color color) {
        final float[] c = Color.convertColorToFloatAlpha(color);
        glPushMatrix();
        glColor4f(c[0], c[1], c[2], c[3]);
        glLineWidth(width);
        glBegin(GL11.GL_LINE_LOOP);

        for (int i = 0; i < segments; i++) {
            double theta = 2.0f * Math.PI * i / segments;
            float x = (float) (radius * Math.cos(theta));
            float y = (float) (radius * Math.sin(theta));

            GL11.glVertex2f(center.x + x, center.y + y);
        }

        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawQuadTextureNew(final Vector2 position, final float width, final float height, final Texture texture, final Color color) {
        drawQuadTextureAngleNew(position, 0, width, height, texture, color);
    }

    public static void drawQuadTextureNew(final Vector2 position, final float width, final float height, final Texture texture) {
        drawQuadTextureAngleNew(position, 0, width, height, texture, Color.WHITE);
    }

    public static void drawQuadTextureAngleNew(final Vector2 position, float angle, final float width, final float height, final Texture texture) {
        drawQuadTextureAngleNew(position, angle, width, height, texture, Color.WHITE);
    }

    public static void drawQuadNew(final Vector2 position, final float width, final float height, final Color color) {
        drawQuadTextureAngleNew(position, 0, width, height, null, color);
    }

    public static void drawQuadAngleNew(final Vector2 position, float angle, final float width, final float height, final Color color) {
        drawQuadTextureAngleNew(position, angle, width, height, null, color);
    }

    public static void drawQuadTextureAngleNew(final Vector2 position, float angle, final float width, final float height, @Nullable Texture texture, final Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

        Matrix4f transformMatrix = new Matrix4f()
                .translate(position.x + halfWidth, position.y + halfHeight, 0)
                .rotateZ(angle)
                .translate(-halfWidth, -halfHeight, 0);

        renderer.shader.bind();
        renderer.shader.loadTransformMatrix(transformMatrix);
        renderer.shader.loadShapeColor(color);
        renderer.shader.loadUseTexture(texture != null);

        float[] vertices = {
                // Positions          // Texture Coords
                0.0f, 0.0f, 0.0f, 0.0f,       // Bottom-left
                width, 0.0f, 1.0f, 0.0f,       // Bottom-right
                width, height, 1.0f, 1.0f,     // Top-right

                0.0f, 0.0f, 0.0f, 0.0f,       // Bottom-left
                width, height, 1.0f, 1.0f,     // Top-right
                0.0f, height, 0.0f, 1.0f       // Top-left
        };

        GL30.glBindVertexArray(renderer.vaoId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderer.vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);

        if(texture != null) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            texture.bind();
        }
        renderer.shader.loadTextureSampler(0);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

        GL30.glBindVertexArray(0);
        renderer.shader.unbind();
        if(texture != null) {
            texture.disable();
        }
    }

    public static void drawQuadTextureAngleNew(final Vector2 position, float angle, final float width, final float height, final float tx, final float ty, final float tw, final float th, Texture texture, final Color color) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

        // Create a transformation matrix for the quad
        Matrix4f transformMatrix = new Matrix4f()
                .translate(position.x + halfWidth, position.y + halfHeight, 0)
                .rotateZ(angle)
                .translate(-halfWidth, -halfHeight, 0);

        renderer.shader.bind();
        renderer.shader.loadTransformMatrix(transformMatrix);
        renderer.shader.loadShapeColor(color);
        renderer.shader.loadUseTexture(texture != null);

        float[] vertices = {
                // Positions          // Texture Coords
                0.0f, 0.0f, tx, ty,       // Bottom-left
                width, 0.0f, tx + tw, ty,       // Bottom-right
                width, height, tx + tw, ty + th,     // Top-right

                0.0f, 0.0f, tx, ty,       // Bottom-left
                width, height, tx + tw, ty + th,     // Top-right
                0.0f, height, tx, ty + th       // Top-left
        };

        GL30.glBindVertexArray(renderer.vaoId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderer.vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);

        if(texture != null) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            texture.bind();
        }
        renderer.shader.loadTextureSampler(0);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

        GL30.glBindVertexArray(0);
        renderer.shader.unbind();
        if(texture != null) {
            texture.disable();
        }
    }


    public static void drawQuadTextureNew(final Vector2 position, final float width, final float height, final float tx, final float ty, final float tw, final float th, Texture texture, final Color color) {
        drawQuadTextureAngleNew(position, 0, width, height, tx, ty, tw, th, texture, color);
    }

    public static void drawQuadTextureAngleNew(final Vector2 position, float angle, final float width, final float height, final float tx, final float ty, final float tw, final float th, Texture texture) {
        drawQuadTextureAngleNew(position, angle, width, height, tx, ty, tw, th, texture, Color.WHITE);
    }

    public static void drawQuadTextureNew(final Vector2 position, final float width, final float height, final float tx, final float ty, final float tw, final float th, Texture texture) {
        drawQuadTextureAngleNew(position, 0, width, height, tx, ty, tw, th, texture, Color.WHITE);
    }

    public static void drawQuadLines(final Vector2 pos, final float width, final float height, final float lineWidth, final Color color) {
        RenderEngine.drawLineNew(pos, new Vector2(pos.x, pos.y + height), lineWidth, color);
        RenderEngine.drawLineNew(new Vector2(pos.x, pos.y + height), new Vector2(pos.x + width, pos.y + height), lineWidth, color);
        RenderEngine.drawLineNew(new Vector2(pos.x + width, pos.y + height), new Vector2(pos.x + width, pos.y), lineWidth, color);
        RenderEngine.drawLineNew(new Vector2(pos.x + width, pos.y), pos, lineWidth, color);
    }

    public static void drawWireframe(Vector2 screenPos, float screenWidth, float screenHeight, float angle) {
        Matrix4f transformMatrix = new Matrix4f()
                .translate(screenPos.x, screenPos.y, 0)
                .rotateZ(angle)
                .scale(screenWidth / 2, screenHeight / 2, 1.0f);

        renderer.shader.bind();
        renderer.shader.loadTransformMatrix(transformMatrix);
        renderer.shader.loadShapeColor(Color.GREEN);
        renderer.shader.loadUseTexture(false);

        float[] vertices = {
                -1.0f, -1.0f,  // Bottom-left
                -1.0f,  1.0f,  // Top-left
                1.0f,  1.0f,  // Top-right
                1.0f, -1.0f   // Bottom-right
        };

        GL30.glBindVertexArray(renderer.vaoId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderer.vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 2 * Float.BYTES, 0);

        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 4);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        renderer.shader.unbind();
    }


    public static float normalize(float val, float max, float min) {
        return (val - min) / (max - min);
    }
}
