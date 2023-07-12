package org.polyscape.render.model;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Madmegsox1
 * @since 09/07/2023
 */

public class Model extends BaseModel {
    private int vertexArrayID, vertexBufferID, indicesBufferID, vertexCount;

    public Model(float[] vertices, int[] indices) {
        vertexArrayID = super.createVertexArray();
        indicesBufferID = super.bindIndicesBuffer(indices);
        vertexBufferID = super.storeData(0, 3, vertices);
        vertexCount = indices.length;
        GL30.glBindVertexArray(0);
    }

    public void remove() {
        GL30.glDeleteVertexArrays(vertexArrayID);
        GL15.glDeleteBuffers(vertexBufferID);
        GL15.glDeleteBuffers(indicesBufferID);
    }

    public int getVertexArrayID() {
        return vertexArrayID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
