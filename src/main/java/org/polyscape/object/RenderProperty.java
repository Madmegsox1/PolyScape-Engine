package org.polyscape.object;

import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.shaders.Shader;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.rendering.sprite.action.ActionInvoker;
import org.polyscape.rendering.sprite.action.IAction;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Madmegsox1
 * @since 25/07/2023
 */

public abstract class RenderProperty {

    protected int width, height;

    protected boolean isTextured = false;

    protected Texture texture;

    protected SpriteSheet spriteSheet;

    protected Color baseColor = Color.WHITE;

    protected boolean shadered = false;

    protected Shader shader;

    protected boolean wireframe = false;

    protected boolean wireframeTextured = false;

    protected ConcurrentHashMap<String, IAction> actionMap = new ConcurrentHashMap<>();

    public void loadShaderProperty(){

    }

    public void addAction(String actionName ,IAction action){
        actionMap.put(actionName, action);
    }

    public void runAction(String actionName){
        boolean running = ActionInvoker.runningActions.getOrDefault(actionName, false);
        if(!running) {
            IAction action = actionMap.get(actionName);
            ActionInvoker invoker = new ActionInvoker(action, actionName, this);
            invoker.start();
        }
    }

    public boolean isTextured() {
        return isTextured;
    }

    public void setTextured(boolean textured) {
        isTextured = textured;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setSpriteSheet(SpriteSheet sheet){
        this.spriteSheet = sheet;
    }
    public void setTexture(Texture texture) {
        this.texture = texture;
        if(this.texture != null){
            this.isTextured = true;
        }
    }

    public void setTexture(int index){
        this.texture = spriteSheet.getTexture(index);
        if(this.texture != null){
            this.isTextured = true;
        }
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor = baseColor;
    }

    public boolean isShadered() {
        return shadered;
    }

    public void setShadered(boolean shadered) {
        this.shadered = shadered;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public boolean isWireframeTextured() {
        return wireframeTextured;
    }

    public void setWireframeTextured(boolean wireframeTextured) {
        this.wireframeTextured = wireframeTextured;
    }
}
