package org.polyscape.render;

import org.lwjgl.opengl.GL15;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Madmegsox1
 * @since 12/07/2023
 */

public class Material {
    private int textureId;
    public Material(String file){
        try {
            textureId = TextureLoader.getTexture("png", new FileInputStream("res/textures/" + file)).getTextureID();
        }catch (IOException e){
            System.err.println("Cannot find/load texture");
            System.exit(-1);
        }
    }

    public void remove(){
        GL15.glDeleteTextures(textureId);
    }

    public int getTextureId() {
        return textureId;
    }
}
