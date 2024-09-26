package org.polyscape.rendering.sprite;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.system.MemoryUtil;
import org.polyscape.Profile;
import org.polyscape.rendering.elements.Texture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SpriteSheet {

    private String fileName;

    private int spriteSheetId = 0;

    private int chunkWidth, chunkHeight;

    public int width, height;

    public int rows, cols;

    private int chunks;

    private BufferedImage imageBuffer;

    private ArrayList<Texture> textures;

    private final Texture masterTexture;

    public SpriteSheet(String fileName, int width, int height) {
        this.masterTexture = new Texture(fileName);

        try {
            this.fileName = fileName;
            this.chunkWidth = width;
            this.chunkHeight = height;


            final InputStream in = new FileInputStream(Profile.Textures.TEXTURE_LOCATION + fileName + "." + Profile.Textures.TEXTURE_FILEFORMAT);
            this.imageBuffer = ImageIO.read(in);

            this.width = imageBuffer.getWidth();
            this.height = imageBuffer.getHeight();

            this.rows = (this.height / this.chunkHeight);
            this.cols = (this.width / this.chunkWidth);

            this.chunks = rows * cols;

            textures = new ArrayList<>(chunks);
            loadSprites();
        } catch (Exception e) {
            System.err.println("Failed to load spritesheet: " + fileName);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public Texture getTexture(int index) {
        return textures.get(index);
    }


    public void loadSprites() {

        BufferedImage[] imgs = new BufferedImage[chunks];
        int count = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int type = imageBuffer.getType();
                if (type == 0) {
                    type = 5;
                }

                imgs[count] = new BufferedImage(this.chunkWidth, this.chunkHeight, type);
                int x0 = chunkWidth * x;
                int y0 = chunkHeight * y;
                int w = chunkWidth;
                int h = chunkHeight;

                // Ensure we don't exceed the image boundaries
                w = Math.min(w, imageBuffer.getWidth() - x0);
                h = Math.min(h, imageBuffer.getHeight() - y0);

                // Get the pixel data from the original image
                Raster raster = imageBuffer.getRaster().createChild(x0, y0, w, h, 0, 0, null);

                // Set the pixel data to the new chunk image
                imgs[count].setData(raster);

                count++;
            }
        }

        for (BufferedImage bufferedImage : imgs) {
            Texture texture = new Texture(this.chunkWidth, this.chunkHeight, toByteBuffer(bufferedImage));
            textures.add(texture);
        }

    }

    private static ByteBuffer toByteBuffer(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();


        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);


        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                int pixel = pixels[i * width + j];

                buffer.put((byte) ((pixel >> 16) & 0xFF));

                buffer.put((byte) ((pixel >> 8) & 0xFF));

                buffer.put((byte) (pixel & 0xFF));

                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();
        return buffer;
    }


    public Texture getMasterTexture() {
        return masterTexture;
    }

    public int getChunkWidth() {
        return chunkWidth;
    }

    public int getChunkHeight() {
        return chunkHeight;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getSpriteSheetId() {
        return spriteSheetId;
    }

    public void setSpriteSheetId(int spriteSheetId) {
        this.spriteSheetId = spriteSheetId;
    }
}
