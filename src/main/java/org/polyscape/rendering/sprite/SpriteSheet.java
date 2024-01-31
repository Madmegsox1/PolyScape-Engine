package org.polyscape.rendering.sprite;

import org.lwjgl.system.MemoryUtil;
import org.polyscape.Profile;
import org.polyscape.rendering.elements.Texture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SpriteSheet {

    private String fileName;

    private int chunkWidth, chunkHeight;

    public int width, height;

    public int rows, cols;

    private int chunks;

    private BufferedImage imageBuffer;

    private ArrayList<Texture> textures;


    public SpriteSheet(String fileName, int width, int height) {
        try {
            this.fileName = fileName;
            this.chunkWidth = width;
            this.chunkHeight = height;

            final InputStream in = new FileInputStream(Profile.Textures.TEXTURE_LOCATION + fileName + "." + Profile.Textures.TEXTURE_FILEFORMAT);
            this.imageBuffer = ImageIO.read(in);

            this.width = imageBuffer.getWidth();
            this.height = imageBuffer.getHeight();

            this.rows = (this.width / this.chunkWidth);
            this.cols = (this.height / this.chunkHeight);

            this.chunks = rows * cols;

            textures = new ArrayList<>(chunks);
            loadSprites();
        }catch (Exception ignored){}
    }

    public Texture getTexture(int index){
        return textures.get(index);
    }

    public void loadSprites() {
        BufferedImage imgs[] = new BufferedImage[chunks];
        int count = 0;

        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                int type = imageBuffer.getType();
                if (type == 0) {
                    type = 5;
                }

                imgs[count] = new BufferedImage(this.chunkWidth, this.chunkHeight, type);
                Graphics2D gr = imgs[count].createGraphics();


                gr.drawImage(this.imageBuffer, 0, 0, chunkWidth, chunkHeight, chunkWidth * x, chunkHeight * y,
                        chunkWidth * x + chunkWidth, chunkHeight * y + chunkHeight, null);
                gr.dispose();
                count++;
            }
        }

        for (BufferedImage bufferedImage : imgs) {
            Texture texture = new Texture(this.chunkWidth, this.chunkHeight, toByteBuffer(bufferedImage));
            textures.add(texture);
        }

    }

    private static ByteBuffer toByteBuffer(BufferedImage image){


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



}
