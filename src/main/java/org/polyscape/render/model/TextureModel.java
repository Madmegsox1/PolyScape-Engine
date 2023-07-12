package org.polyscape.render.model;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.polyscape.render.Material;

/**
 * @author Madmegsox1
 * @since 12/07/2023
 */

public class TextureModel extends BaseModel {

    private int vertexArrayID, vertexBufferID, indicesBufferID, vertexCount, textureCoordsBufferId;
    private Material material;

    public TextureModel(float[] vertices, float[] textureCoords,int[] indices, String file) {
        vertexArrayID = super.createVertexArray();
        indicesBufferID = super.bindIndicesBuffer(indices);
        vertexBufferID = super.storeData(0, 3, vertices);
        vertexCount = indices.length;
        textureCoordsBufferId = super.storeData(1, 2, textureCoords);
        GL30.glBindVertexArray(0);
        material = new Material(file);
    }

    public void remove() {
        GL30.glDeleteVertexArrays(vertexArrayID);
        GL15.glDeleteBuffers(vertexBufferID);
        GL15.glDeleteBuffers(textureCoordsBufferId);
        GL15.glDeleteBuffers(indicesBufferID);
        material.remove();
    }

    public Material getMaterial() {
        return material;
    }

    public int getVertexArrayID() {
        return vertexArrayID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
