package org.polyscape.rendering.shaders;

import org.polyscape.rendering.elements.Color;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class TextureColorShader extends Shader{

    private int textureColorUniform;

    public TextureColorShader() {
        super("TextureColorFrag",false);
    }

    @Override
    public void bindAllAttributes() {
    }

    @Override
    protected void getAllUniforms() {
        textureColorUniform = getUniform("textureColor");
    }


    public void loadTextureColor(Color c){
        loadColorAUniform(textureColorUniform, c);
    }
}
