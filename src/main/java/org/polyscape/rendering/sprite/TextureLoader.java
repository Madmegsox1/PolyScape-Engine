package org.polyscape.rendering.sprite;


import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.polyscape.Profile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
public final class TextureLoader {


    public static int readTexture(int w, int h, ByteBuffer buffer) {

        final IntBuffer tmp = BufferUtils.createIntBuffer(1);

        try {
            GL11.glGenTextures(tmp);
        } catch (final Exception e) {
            System.err.println("Init engine before loading textures!");
        }
        tmp.rewind();


        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        tmp.rewind();

        return tmp.get(0);
    }


    public static int readTexture(String filename) {
        filename = Profile.Textures.TEXTURE_LOCATION + filename + "." + Profile.Textures.TEXTURE_FILEFORMAT;
        final IntBuffer tmp = BufferUtils.createIntBuffer(1);

        try {
            GL11.glGenTextures(tmp);
        } catch (final Exception e) {
            System.err.println("Init engine before loading textures!");
        }
        tmp.rewind();

        try {
            final InputStream in = new FileInputStream(filename);
            final PNGDecoder decoder = new PNGDecoder(in);

            final ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

        } catch (final java.io.FileNotFoundException ex) {
            System.out.println("Error " + filename + " not found");
        } catch (final java.io.IOException e) {
            System.out.println("Error decoding " + filename);
        }

        tmp.rewind();

        return tmp.get(0);
    }

    public static BufferedImage readTextureToBufferedImage(String filename) {
        filename = Profile.Textures.TEXTURE_LOCATION + filename + "." + Profile.Textures.TEXTURE_FILEFORMAT;
        try {
            final File file = new File(filename);
            final BufferedImage img = ImageIO.read(file);

            return img;

        } catch (final Exception e) {
            System.out.println("Error reading " + filename);
        }

        return null;
    }
}
