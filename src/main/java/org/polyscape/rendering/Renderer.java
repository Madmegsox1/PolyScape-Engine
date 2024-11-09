package org.polyscape.rendering;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.polyscape.Profile;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.shaders.RendererShader;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.glGenBuffers;

public final class Renderer {
    private final Color backgroundColor;

    private int fbo;
    private int rbo;
    public int vaoId;
    public int vboId;

    public RendererShader shader;

    public Matrix4f projectionMatrix;

    private double fpsCap = 1d / 120d;

    public boolean destroy = false;

    private Display display;

    public Renderer(Display display) {
        this.backgroundColor = new Color(Profile.Display.BACKGROUND_COLOR[0],
                Profile.Display.BACKGROUND_COLOR[1],
                Profile.Display.BACKGROUND_COLOR[2],
                Profile.Display.BACKGROUND_COLOR[3]);
        this.display = display;
    }

    public void init() {

        shader = new RendererShader("RendererVertex", "RendererFrag");

        projectionMatrix = new Matrix4f().ortho(
                0.0f, Profile.Display.WIDTH,       // Left to Right
                Profile.Display.HEIGHT, 0.0f,      // Bottom to Top
                -1.0f, 1.0f);

        // Generate and bind VAO and VBO
        vaoId = GL30.glGenVertexArrays();
        vboId = GL15.glGenBuffers();

        GL30.glBindVertexArray(vaoId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);

        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        shader.create();
        shader.bind();
        shader.loadProjectionMatrix(projectionMatrix);

        shader.unbind();



        //glEnable(GL11.GL_TEXTURE_2D);
        //glShadeModel(GL_SMOOTH);

        glDisable(GL_CULL_FACE);

        glEnable(GL_MULTISAMPLE);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);

        this.fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.fbo);

        this.rbo = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.rbo);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_RGBA8, Profile.Display.WIDTH, Profile.Display.HEIGHT);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, this.rbo);

        assert GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE;

    }

    public void prepare() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (this.fbo > 0) {
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, this.fbo);
            GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
        } else {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        }
    }

    public void render(final long window) {
        if (this.fbo > 0) {
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.fbo);
            GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
            GL20.glDrawBuffers(GL11.GL_BACK_LEFT);

            GL30.glBlitFramebuffer(
                    0, 0, Profile.Display.WIDTH, Profile.Display.HEIGHT,
                    0, 0, Profile.Display.WIDTH, Profile.Display.HEIGHT,
                    GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST
            );
        }

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public double getTime() {
        return System.nanoTime() / 1000000000d;
    }

    public double getFpsCap() {
        return fpsCap;
    }

    public void setFpsCap(double fpsCap) {
        this.fpsCap = 1d / fpsCap;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor.r = color.r / 255;
        this.backgroundColor.g = color.g / 255;
        this.backgroundColor.b = color.b / 255;
        this.backgroundColor.a = color.a / 255;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(display.getWindow()) || destroy;
    }
}
