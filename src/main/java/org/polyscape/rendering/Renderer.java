package org.polyscape.rendering;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.polyscape.Profile;
import org.polyscape.rendering.elements.Color;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;


public final class Renderer {
    private final Color backgroundColor;

    private int fbo;
    private int rbo;

    private double fpsCap = 1d / 120d;

    private double time;

    private double processedTime = 0;

    private Display display;

    public Renderer(Display display) {
        this.backgroundColor = new Color(Profile.Display.BACKGROUND_COLOR[0],
                Profile.Display.BACKGROUND_COLOR[1],
                Profile.Display.BACKGROUND_COLOR[2],
                Profile.Display.BACKGROUND_COLOR[3]);
        this.display = display;
    }

    public void init() {


        glEnable(GL11.GL_TEXTURE_2D);

        //glEnable(GL_LIGHTING);

        //glEnable(GL_LIGHT0);

        //glEnable(GL_LIGHT1);

        glShadeModel(GL_SMOOTH);

        glDisable(GL_CULL_FACE);

        glEnable(GL_MULTISAMPLE);


        glMatrixMode(GL11.GL_PROJECTION);
        glLoadIdentity();

        glOrtho(0, Profile.Display.WIDTH, Profile.Display.HEIGHT, 0, -1, 1);
        glMatrixMode(GL11.GL_MODELVIEW);
        glLoadIdentity();
        glEnable(GL_STENCIL_TEST);
        glViewport(0, 0, Profile.Display.WIDTH, Profile.Display.HEIGHT);

        glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);

        this.fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.fbo);

        this.rbo = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.rbo);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_RGBA8, Profile.Display.WIDTH, Profile.Display.HEIGHT);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, this.rbo);

        assert GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE;
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    public void prepare() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, this.fbo);
        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void render(final long window) {

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.fbo);
        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL20.glDrawBuffers(GL11.GL_BACK_LEFT);

        GL30.glBlitFramebuffer(0,
                0,
                Profile.Display.WIDTH,
                Profile.Display.HEIGHT,
                0,
                0,
                Profile.Display.WIDTH,
                Profile.Display.HEIGHT,
                GL11.GL_COLOR_BUFFER_BIT,
                GL11.GL_NEAREST);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public double getTime() {
        return System.nanoTime() / 1000000000d;
    }

    public boolean isUpdateReady() {
        double currentTime = getTime();
        double timePassed = currentTime - time;
        processedTime += timePassed;
        time = currentTime;

        while (processedTime > fpsCap) {
            processedTime -= fpsCap;
            return true;
        }

        return false;
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
        return glfwWindowShouldClose(display.getWindow());
    }
}
