package org.polyscape.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.ResizeWindowEvent;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;


public final class Display {
    private String title;
    private GLFWErrorCallback errorCallback;
    private long window;

    public Display(final String title)
    {
        this.title = title;
    }


    public static Vector2 getMousePosition(long display){
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(display, posX, posY);
        double mouseX = posX.get(0);
        double mouseY = posY.get(0);

        return new Vector2(mouseX, mouseY);
    }

    public static Vector2 getWorldMousePosition(long display, Vector2 cameraPosition, float scale){
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(display, posX, posY);
        double mouseX = posX.get(0);
        double mouseY = posY.get(0);

        double worldX = mouseX / scale;
        double worldY = mouseY / scale;

        worldX -= cameraPosition.x / scale;
        worldY -= cameraPosition.y / scale;

        return new Vector2((float)worldX, (float)worldY);
    }
    public void destroyWindow(){
        glfwDestroyWindow(window);
    }

    public void init(boolean aa)
    {
        // Error callback.
        glfwSetErrorCallback(this.errorCallback = GLFWErrorCallback.createPrint(System.err));

        // GLFW initialize
        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }




        // Window config
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Check if user is on MacOS if not the disable this hint
        glfwWindowHint(GLFW.GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW.GLFW_FALSE);

        if(aa) {
            glfwWindowHint(GLFW_SAMPLES, 4);
        }

        // Window creation
        this.window = GLFW.glfwCreateWindow(Profile.Display.WIDTH, Profile.Display.HEIGHT, this.title, NULL, NULL);

        // Make fullscreen
        // this.window = glfwCreateWindow(Constants.Display.WIDTH,
        // Constants.Display.HEIGHT, this.title, ,
        // NULL);
        glfwMakeContextCurrent(this.window);
        GL.createCapabilities();


        if (this.window == NULL)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetMouseButtonCallback(window, ((window1, button, action, mods) -> Engine.getEventBus().postEvent(new MouseClickEvent(button, action, window))));
        glfwSetKeyCallback(window, ((window1, key, scancode, action, mods) -> Engine.getEventBus().postEvent(new KeyEvent(key, action, window))));


        glfwSetWindowAspectRatio(this.window, Profile.Display.ASPECT_RATIO_NUMERATOR, Profile.Display.ASPECT_RATIO_DENOMINATOR);

        glfwSwapInterval(1);
        glfwShowWindow(this.window);

        org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback(this.window, resizeWindow);

    }

    private static GLFWFramebufferSizeCallback resizeWindow = new GLFWFramebufferSizeCallback()
    {
        @Override
        public void invoke(final long window, final int width, final int height)
        {
            // Keep ratio when resizing window
            //final int newHeight = (int) (width / 1.777777777777778D);

            resizeWindow(width, height, true, window);
            Engine.getEventBus().postEvent(new ResizeWindowEvent(window, width, height));
        }
    };

    public void resizeWindow(int width, int height){
        resizeWindow(width, height, false, window);
    }

    public static void resizeWindow(int width, int height, boolean callback, long window){
        Profile.Display.HEIGHT = height;
        Profile.Display.WIDTH = width;

        if(!callback){
            glfwSetWindowSize(window, width, height);
        }

        Engine.getRenderer().init();
    }

    public GLFWErrorCallback getErrorCallback()
    {
        return this.errorCallback;
    }

    public long getWindow()
    {
        return this.window;
    }


    public void setTitle(final String title)
    {
        this.title = title;
        glfwSetWindowTitle(this.window, title);
    }



}
