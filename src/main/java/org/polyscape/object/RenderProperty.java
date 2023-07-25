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

    protected boolean isTextured;

    protected Texture texture;

    protected Color baseColor;

    protected boolean shadered;

    protected Shader shader;

    public void loadShaderPropertys(){

    }
}
