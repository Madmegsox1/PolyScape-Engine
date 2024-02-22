package org.polyscape.font;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;
import org.polyscape.Profile;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBTruetype.*;

public class FontMac {

    public ByteBuffer atlas;

    public Texture texture;
    public float scale;

    private STBTTPackedchar.Buffer cdata;

    public float fontSize = 47;

    private final HashMap<Character, Glyph> glyphs;

    public FontMac(String fontName, float fontSize) {
        glyphs = new HashMap<>();
        try {
            this.fontSize = fontSize;
            ByteBuffer fontBuffer = loadFontFile(fontName);
            atlas = createFontAtlas(fontBuffer, 512, 512);
        } catch (IOException e) {
            System.err.println("Failed to load font: " + fontName);
        }

        generateGlyphs();
    }

    public FontMac(String fontName){
        this(fontName, 47);
    }

    private ByteBuffer loadFontFile(String fontPath) throws IOException {
        fontPath = Profile.Font.FONT_LOCATION + File.separator + fontPath + "." + Profile.Font.FONT_FILEFORMAT;
        byte[] fontData = Files.readAllBytes(Paths.get(fontPath));
        ByteBuffer fontBuffer = BufferUtils.createByteBuffer(fontData.length);
        fontBuffer.put(fontData);
        fontBuffer.flip();
        return fontBuffer;
    }


    private ByteBuffer createFontAtlas(ByteBuffer fontBuffer, int bitmapWidth, int bitmapHeight) {
        cdata = STBTTPackedchar.malloc(95);
        STBTTFontinfo stb_font_info = STBTTFontinfo.create();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapHeight * bitmapWidth);
            STBTTPackContext stp_pack_ctx = STBTTPackContext.malloc(stack);
            stbtt_InitFont(stb_font_info, fontBuffer);
            stbtt_PackBegin(stp_pack_ctx, bitmap, bitmapWidth, bitmapHeight, 0, 4);
            stbtt_PackSetOversampling(stp_pack_ctx, 1, 1);
            stbtt_PackFontRange(stp_pack_ctx, fontBuffer, 0, fontSize, 32, cdata);
            stbtt_PackEnd(stp_pack_ctx);

            scale = stbtt_ScaleForPixelHeight(stb_font_info, fontSize);

            ByteBuffer colorBitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight * 4);
            /* Converts bitmap to a RGBA bitmap instead of a Alpha Bitmap*/
            for (int i = 0; i < bitmapWidth * bitmapHeight; i++){
                byte alpha = bitmap.get(i);
                colorBitmap.put((byte) 0xff); // R
                colorBitmap.put((byte) 0xff); // G
                colorBitmap.put((byte) 0xff); // B
                colorBitmap.put(alpha); // A
            }

            colorBitmap.flip();

            int id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmapWidth, bitmapHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, colorBitmap);

            this.texture = new Texture(id);

            bitmap.clear();
            fontBuffer.clear();

            return colorBitmap;
        }
    }

    public int getWidth(String text){
        int size = 0;
        for (char c : text.toCharArray()){
            size += (int) glyphs.get(c).advance;
        }
        return size;
    }

    public int getHeight(String text) {
        int maxHeight = 0;
        for (char c : text.toCharArray()) {
            var g = glyphs.get(c);
            if (g.height > maxHeight) {
                maxHeight = (int) g.height;
            }
        }
        return maxHeight;
    }

    public void renderText(String text, Vector2 vector,Color color){
        for (char c : text.toCharArray()){
            Glyph g = glyphs.get(c);
            Vector2 newPoint = new Vector2(vector.x + g.x, vector.y + g.y);
            RenderEngine.drawQuadTexture(newPoint, g.width, g.height, g.uvx, g.uvy, g.uvWidth, g.uvHeight, this.texture, color);

            vector.addToVect(g.advance, 0);
        }
    }

    public void renderText(String text, Vector2 vector){
        this.renderText(text, vector,Color.BLACK);
    }

    private void generateGlyphs() {
        for (int i = 32; i < 127; i++) {
            int charIndex = i - 32;

            STBTTPackedchar info = this.cdata.get(charIndex);

            char c = (char) i;

            float charX = info.xoff();
            float charY = info.yoff();

            float width = info.xoff2() - info.xoff();
            float height = info.yoff2() - info.yoff();

            float uvX = info.x0() / 512f;
            float uvY = info.y0() / 512f;

            float uvWidth = (info.x1() - info.x0()) / 512f;
            float uvHeight = (info.y1() - info.y0()) / 512f;

            Glyph glyph = new Glyph(width, height, charX, charY, uvX, uvY, uvWidth, uvHeight, info.xadvance());
            glyphs.put(c, glyph);
        }
    }


}


