package org.polyscape.rendering.shaders;

import org.lwjgl.BufferUtils;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public class TextureColorShader extends Shader {

    private int textureColorUniform;


    private int vertexPos;

    private int vertexTexPos;

    public TextureColorShader() {
        super("TextColorVertex","TextColorFrag");
    }

    @Override
    public void bindAllAttributes() {
        this.vertexPos = glGetAttribLocation(this.programId, "aPos");
        this.vertexTexPos = glGetAttribLocation(this.programId, "aTexCoord");
    }

    @Override
    protected void getAllUniforms() {
        textureColorUniform = getUniform("textureColor");
    }


    public void loadTextureColor(Color c){
        loadColorAUniform(textureColorUniform, Color.convertColorToFloatAlpha(c));
    }

    public void loadVertexLocation(List<Vector2> vector2, boolean normalised){
        if(!normalised) {
            vector2 = convertPixelCoordsToNDC(vector2);
        }
        float[] floatArray = toFloatArray(vector2);
        FloatBuffer vertexLocation = BufferUtils.createFloatBuffer(floatArray.length);
        vertexLocation.put(floatArray).flip();

        glEnableVertexAttribArray(vertexPos);
        glVertexAttribPointer(vertexPos, 2, GL_FLOAT, false, 0, vertexLocation);
    }

    public void loadVertexTexPos(List<Vector2> vector2){
        float[] floatArray = toFloatArray(vector2);
        FloatBuffer vertexTexPosBuffer = BufferUtils.createFloatBuffer(floatArray.length);
        vertexTexPosBuffer.put(floatArray).flip();

        glEnableVertexAttribArray(vertexTexPos);
        glVertexAttribPointer(vertexTexPos, 2, GL_FLOAT, false, 0, vertexTexPosBuffer);
    }
}
