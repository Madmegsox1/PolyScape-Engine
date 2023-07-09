package org.polyscape.render;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.polyscape.Engine;
import org.polyscape.render.events.KeyEvent;
import org.polyscape.render.events.MouseClickEvent;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;

public class Window {

    private int WIDTH = 1920;
    private int HEIGHT = 1080;

    private String Title = "Untitled Window";

    private static Window INSTANCE;

    private long windowId;

    private long monitorId;

    private boolean fullscreen;
    public Window(){
        Window.INSTANCE = this;
    }

    public Window(int width, int height, String title){
        this.WIDTH = width;
        this.HEIGHT = height;
        this.Title = title;
        Window.INSTANCE = this;
    }

    public Window(String title){
        this.Title = title;
        Window.INSTANCE = this;
    }

    public static Window get(){
        if(INSTANCE == null){
            return new Window();
        }

        return Window.INSTANCE;
    }

    public void create(boolean fullScreen){

        this.fullscreen = fullScreen;

        if(!GLFW.glfwInit()){
            System.err.println("GLFW Has not been initialised");
            System.exit(-1);
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        windowId = GLFW.glfwCreateWindow(WIDTH, HEIGHT, Title, (this.fullscreen) ? glfwGetPrimaryMonitor() : 0,0);

        monitorId = glfwGetPrimaryMonitor();
        if(windowId == 0){
            System.err.println("GLFW Has failed to create window");
            System.exit(-1);
        }



        glfwMakeContextCurrent(windowId);
        GL.createCapabilities();

        glfwSetMouseButtonCallback(windowId, ((window1, button, action, mods) -> Engine.getEventBus().postEvent(new MouseClickEvent(button, action, windowId))));
        glfwSetKeyCallback(windowId, ((window1, key, scancode, action, mods) -> Engine.getEventBus().postEvent(new KeyEvent(key, action, windowId))));

        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitorId);

        if(this.fullscreen) {
            glfwSetWindowMonitor(windowId, monitorId, 0, 0, WIDTH, HEIGHT, vidMode.refreshRate());
        }
        if(!this.fullscreen){
            glfwSetWindowMonitor(windowId, 0, 0, 0, WIDTH, HEIGHT, vidMode.refreshRate());
            GLFW.glfwSetWindowPos(windowId, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);
        }

        glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowId);
    }

    public void fullscreen(){
        GLFWVidMode vidMode = glfwGetVideoMode(monitorId);
        if(!this.fullscreen) {
            this.fullscreen = true;
            glfwSetWindowMonitor(windowId, monitorId, 0, 0, WIDTH, HEIGHT, vidMode.refreshRate());
            glfwMakeContextCurrent(windowId);
        }
        else{
            this.fullscreen = false;
            glfwSetWindowMonitor(windowId, 0, 0, 0, WIDTH, HEIGHT, vidMode.refreshRate());
            GLFW.glfwSetWindowPos(windowId, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);
            glfwMakeContextCurrent(windowId);
        }
    }

    public boolean closeFlag(){
        return !GLFW.glfwWindowShouldClose(windowId);
    }

    public void update(){
        GLFW.glfwPollEvents();
    }

    public void swapBuffers(){
        GLFW.glfwSwapBuffers(windowId);
    }
}
