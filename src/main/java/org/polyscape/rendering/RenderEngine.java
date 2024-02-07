package org.polyscape.rendering;

import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.shaders.Shader;
import org.polyscape.rendering.shaders.TextureColorShader;

import static org.lwjgl.opengl.GL11.*;
public final class RenderEngine {
    public static int fps = 0;
    public static double deltaTime = 0;

    private static TextureColorShader textureColorShader;

    public RenderEngine(){
        textureColorShader = new TextureColorShader();
        textureColorShader.create();
    }

    public void render(final Renderer renderer, final Display display) {
        double time = GLFW.glfwGetTime();
        double fpsTime = time;
        int fpsOld = 0;
        fps = 0;
        while (!renderer.shouldClose()) {

            if(renderer.isUpdateReady()) {
                final double currentTime = GLFW.glfwGetTime();
                deltaTime = currentTime - time;
                time = currentTime;
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


    public static void drawLine(final Vector2 a, final Vector2 b, final float width, final Color color){
        final float[] c = Color.convertColorToFloatAlpha(color);
        glColor4f(c[0], c[1], c[2], c[3]);
        glLineWidth(width);
        glBegin(GL_LINE_STRIP);

        glVertex2f(a.x, a.y);
        glVertex2f(b.x, b.y);

        glEnd();
    }


    public static void drawQuad(final Vector2 vector, final float width, final float height, final Color color){

        final float[] c = Color.convertColorToFloat(color);
        glColor3f(c[0], c[1], c[2]);
        glBegin(GL_QUADS);

        glVertex2f(vector.x, vector.y);
        glVertex2f(vector.x, vector.y + height);
        glVertex2f(vector.x + width, vector.y + height);
        glVertex2f(vector.x + width ,vector.y);

        glEnd();
    }

    public static void drawQuadA(final Vector2 vector, final float width, final float height, final Color color){

        final float[] c = Color.convertColorToFloatAlpha(color);
        glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
        glColor4f(c[0], c[1], c[2], c[3]);
        glBegin(GL_QUADS);

        glVertex2f(vector.x, vector.y);
        glVertex2f(vector.x, vector.y + height);
        glVertex2f(vector.x + width, vector.y + height);
        glVertex2f(vector.x + width ,vector.y);

        glEnd();
    }

    public static void drawQuadTexture(final Vector2 vector2, final float width, final float height, final Texture texture, final Color color){
        texture.bind();
        final float[] c = Color.convertColorToFloatAlpha(color);
        glColor4f(c[0], c[1], c[2], c[3]);
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(vector2.x, vector2.y);

        glTexCoord2f(1, 0);
        glVertex2f(vector2.x + width ,vector2.y);

        glTexCoord2f(1, 1);
        glVertex2f(vector2.x + width, vector2.y + height);

        glTexCoord2f(0, 1);
        glVertex2f(vector2.x, vector2.y + height);

        glEnd();
        texture.disable();
    }

    public static void drawQuadTexture(final Vector2 vector2, final float width, final float height, final Texture texture){
        texture.bind();

        glColor4f(1, 1, 1, 1);
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(vector2.x, vector2.y);

        glTexCoord2f(1, 0);
        glVertex2f(vector2.x + width ,vector2.y);

        glTexCoord2f(1, 1);
        glVertex2f(vector2.x + width, vector2.y + height);

        glTexCoord2f(0, 1);
        glVertex2f(vector2.x, vector2.y + height);

        glEnd();
        texture.disable();
    }


    public static void drawQuadTexture(final Vector2 vector2, final float width, final float height, final float tx, final float ty, final float tw, final float th,final Texture texture){
        texture.bind();
        glColor4f(1,1,1,1);
        glBegin(GL_QUADS);

        glTexCoord2f(tx, ty);
        glVertex2f(vector2.x, vector2.y);

        glTexCoord2f(tx + tw, ty);
        glVertex2f(vector2.x + width ,vector2.y);

        glTexCoord2f(tx + tw, ty + th);
        glVertex2f(vector2.x + width, vector2.y + height);

        glTexCoord2f(tx, ty + th);
        glVertex2f(vector2.x, vector2.y + height);

        glEnd();
        texture.disable();
    }

    public static void drawQuadTexture(final Vector2 vector2, final float width, final float height, final float tx, final float ty, final float tw, final float th,final Texture texture, final Color color){
        //textureColorShader.bind();

        //textureColorShader.loadTextureColor(color);
        texture.bind();
        final float[] c = Color.convertColorToFloatAlpha(color);
        glColor4f(c[0], c[1], c[2], c[3]);
        glBegin(GL_QUADS);

        glTexCoord2f(tx, ty);
        glVertex2f(vector2.x, vector2.y);

        glTexCoord2f(tx + tw, ty);
        glVertex2f(vector2.x + width ,vector2.y);

        glTexCoord2f(tx + tw, ty + th);
        glVertex2f(vector2.x + width, vector2.y + height);

        glTexCoord2f(tx, ty + th);
        glVertex2f(vector2.x, vector2.y + height);

        glEnd();
        texture.disable();
       // textureColorShader.unbind();
    }

    public static float normalize(float val, float max, float min){
        return (val - min)/(max - min);
    }
}
