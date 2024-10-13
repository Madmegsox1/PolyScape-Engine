package org.polyscape.rendering.shaders;

import org.joml.Matrix4f;
import org.polyscape.rendering.elements.Color;

public class RendererShader extends Shader {
    private int locationProjection;
    private int locationTransform;
    private int locationShapeColor;
    private int locationUseTexture;
    private int locationTextureSampler;

    public RendererShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    public void bindAllAttributes() {
        // Bind attribute locations for position and texture coordinates
        bindAttribute(0, "aPos");          // Vertex position attribute
        bindAttribute(1, "aTexCoord");     // Texture coordinate attribute
    }

    @Override
    protected void getAllUniforms() {
        // Retrieve uniform locations and store them in fields
        locationProjection = getUniform("projection");
        locationTransform = getUniform("transform");
        locationShapeColor = getUniform("shapeColor");
        locationUseTexture = getUniform("useTexture");
        locationTextureSampler = getUniform("textureSampler");
    }

    // Uniform Loaders specific to this shader's needs

    public void loadProjectionMatrix(Matrix4f matrix) {
        setUniformMatrix("projection", matrix);
    }

    public void loadTransformMatrix(Matrix4f matrix) {
        setUniformMatrix("transform", matrix);
    }

    public void loadShapeColor(Color color) {
        setUniform("shapeColor", color);
    }

    public void loadUseTexture(boolean useTexture) {
        setUniform("useTexture", useTexture);
    }

    public void loadTextureSampler(int sampler) {
        setUniform("textureSampler", sampler);
    }
}
