package org.polyscape.test;

import org.polyscape.render.shaders.Shader;

/**
 * @author Madmegsox1
 * @since 10/07/2023
 */

public class BasicShader extends Shader {
    public BasicShader() {
        super("D:\\PolyScape\\src\\test\\java\\org\\polyscape\\test\\BasicVertexShader.glsl", "D:\\PolyScape\\src\\test\\java\\org\\polyscape\\test\\BasicFragShader.glsl");
    }

    @Override
    public void bindAllAttributes() {
        super.bindAttribute(0, "vertices");
    }
}
