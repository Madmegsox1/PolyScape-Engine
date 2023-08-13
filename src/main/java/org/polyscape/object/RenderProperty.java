package org.polyscape.object;

import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.shaders.Shader;

/**
 * @author Madmegsox1
 * @since 25/07/2023
 */

public abstract class RenderProperty {

    protected int width, height;

    protected boolean isTextured = false;

    protected Texture texture;

    protected Color baseColor = Color.WHITE;

    protected boolean shadered = false;

    protected Shader shader;

    protected boolean wireframe = false;

    protected boolean wireframeTextured = false;

    public void loadShaderProperty(){

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

    public void setTexture(Texture texture) {
        this.texture = texture;
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
