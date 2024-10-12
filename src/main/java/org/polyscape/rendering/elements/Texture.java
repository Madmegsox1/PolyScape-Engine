package org.polyscape.rendering.elements;

import org.lwjgl.opengl.GL11;
import org.polyscape.rendering.sprite.TextureLoader;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public final class Texture {
    public final static Map<Integer, String> loadedTextures = new HashMap<>();
    private int texture;

    public Texture(final String textureName) {
        boolean textureIsLoaded = false;

        for (final Integer textureId : loadedTextures.keySet()) {
            final String loadedTexture = loadedTextures.get(textureId);

            if (loadedTexture.equals(textureName)) {
                textureIsLoaded = true;
                this.texture = textureId;

                break;
            }
        }

        if (!textureIsLoaded) {
            this.loadTexture(textureName);
        }
    }

    public Texture(int w, int h, ByteBuffer buffer){
        loadTexture(w, h, buffer);
    }

    public Texture(int textureId){
        this.texture = textureId;
    }

    private void loadTexture(final String textureName) {
        this.texture = TextureLoader.readTexture(textureName);
        loadedTextures.put(this.texture, textureName);
    }

    // TODO add texture ID to this so we can keep track of loaded sprite sheet textures to prevent mem leak
    private void loadTexture(int w, int h, ByteBuffer buffer){
        this.texture = TextureLoader.readTexture(w, h, buffer);
    }

    public int getTexture() {
        return this.texture;
    }

    public void bind() {
        glEnable(GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getTexture());
    }


    public void disable() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }


    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Texture) {
            final Texture otherMat = (Texture) obj;
            return otherMat.texture == this.texture;
        }

        return false;
    }
}
