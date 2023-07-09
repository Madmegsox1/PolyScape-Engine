package org.polyscape.render;

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

public class Model {
    private int vertexArrayId, vertexBufferId, vertexCount, indicesBufferId;
    private float[] vertices;

    private int[] indices;

    public Model(float[] vertices, int[] indices){
        this.vertices = vertices;
        this.indices = indices;
        this.vertexCount = indices.length;
    }

    // Moves vertices to mem
    public void create(){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(this.vertices.length);
        buffer.put(this.vertices);
        buffer.flip();


        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(this.indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        vertexArrayId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vertexArrayId);

        vertexBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);

        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // <--- STATIC DRAW check back if model vertexes changes

        indicesBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        // Index is where to start in vertex array - Size is what dimension its on
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
    }

    // Removes Vertex from mem
    public void remove(){
        GL30.glDeleteVertexArrays(vertexArrayId);
        GL15.glDeleteBuffers(vertexBufferId);
        GL15.glDeleteBuffers(indicesBufferId);
    }

    public int getVertexArrayId() {
        return vertexArrayId;
    }

    public int getVertexBufferId() {
        return vertexBufferId;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
