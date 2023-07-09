package org.polyscape.render;


import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.polyscape.Engine;
import org.polyscape.render.events.KeyEvent;
import org.polyscape.render.events.MouseClickEvent;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private int WIDTH = 1920;
    private int HEIGHT = 1080;

    private String title = "Untitled Window";

    private static Window INSTANCE;

    private long windowId;

    private long monitorId;

    private double fpsCap = 1d/120;

    private double time;

    private double processedTime = 0;

    private boolean fullscreen;

    private Vector3f backgroundColor = new Vector3f(0f,0f,0f);
    public Window(){
        Window.INSTANCE = this;
        this.time = getTime();
    }

    public Window(int width, int height, String title){
        this.WIDTH = width;
        this.HEIGHT = height;
        this.title = title;
        Window.INSTANCE = this;
        this.time = getTime();
    }

    public Window(String title){
        this.title = title;
        Window.INSTANCE = this;
        this.time = getTime();
    }

    public static Window get(){
        if(INSTANCE == null){
            return new Window();
        }

        return Window.INSTANCE;
    }

    public void create(boolean fullScreen){

        this.fullscreen = fullScreen;

        if(!glfwInit()){
            System.err.println("GLFW Has not been initialised");
            System.exit(-1);
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        windowId = glfwCreateWindow(WIDTH, HEIGHT, title, (this.fullscreen) ? glfwGetPrimaryMonitor() : 0,0);

        monitorId = glfwGetPrimaryMonitor();
        if(windowId == 0){
            System.err.println("GLFW Has failed to create window");
            System.exit(-1);
        }



        glfwMakeContextCurrent(windowId);
        GL.createCapabilities();

        glfwSetMouseButtonCallback(windowId, ((window1, button, action, mods) -> Engine.getEventBus().postEvent(new MouseClickEvent(button, action, windowId))));
        glfwSetKeyCallback(windowId, ((window1, key, scancode, action, mods) -> Engine.getEventBus().postEvent(new KeyEvent(key, action, windowId))));

        GLFWVidMode vidMode = glfwGetVideoMode(monitorId);

        if(this.fullscreen) {
            glfwSetWindowMonitor(windowId, monitorId, 0, 0, WIDTH, HEIGHT, vidMode.refreshRate());
        }
        if(!this.fullscreen){
            glfwSetWindowMonitor(windowId, 0, 0, 0, WIDTH, HEIGHT, vidMode.refreshRate());
            glfwSetWindowPos(windowId, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);
        }

        glfwSwapInterval(1);
        glfwShowWindow(windowId);
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
            glfwSetWindowPos(windowId, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);
            glfwMakeContextCurrent(windowId);
        }
    }

    public double getTime(){
        return System.nanoTime() / 1000000000d;
    }

    public boolean closeFlag(){
        return !glfwWindowShouldClose(windowId);
    }

    public void close(){
        glfwTerminate();
    }

    public void update(){
        GL11.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        glfwPollEvents();
    }

    public boolean isUpdateReady(){
        double currentTime = getTime();
        double timePassed = currentTime - time;
        processedTime += timePassed;
        time = currentTime;

        while (processedTime > fpsCap){
            processedTime -= fpsCap;
            return true;
        }

        return false;
    }

    public void swapBuffers(){
        glfwSwapBuffers(windowId);
    }


    public double getFpsCap() {
        return fpsCap;
    }

    public void setFpsCap(double fpsCap) {
        this.fpsCap = 1d/fpsCap;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public String getTitle() {
        return title;
    }

    public long getWindowId() {
        return windowId;
    }

    public long getMonitorId() {
        return monitorId;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(this.windowId, this.title);
    }

    public Vector3f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(float r, float g, float b) {
        this.backgroundColor = new Vector3f(r, g, b);
    }
}
