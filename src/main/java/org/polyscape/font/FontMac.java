package org.polyscape.font;

import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.NkFont;
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
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FontMac {

    public ByteBuffer atlas;

    public Texture texture;
    public float scale;
    public int ascent;
    public int descent;
    public int baseline;

    public STBTTPackedchar.Buffer cdata;

    public float fontSize = 60;

    public FontMac() throws IOException {
        ByteBuffer fontBuffer = loadFontFile("Segoe UI");
        atlas = createFontAtlas2(fontBuffer, 512, 512);
    }

    public ByteBuffer loadFontFile(String fontPath) throws IOException {
        fontPath = Profile.Font.FONT_LOCATION + File.separator + fontPath + "." + Profile.Font.FONT_FILEFORMAT;
        byte[] fontData = Files.readAllBytes(Paths.get(fontPath));
        ByteBuffer fontBuffer = BufferUtils.createByteBuffer(fontData.length);
        fontBuffer.put(fontData);
        fontBuffer.flip(); // Prepare for reading
        return fontBuffer;
    }


    private ByteBuffer createFontAtlas2(ByteBuffer fontBuffer, int bitmapWidth, int bitmapHeight) {
        cdata = STBTTPackedchar.malloc(95);
        STBTTFontinfo stb_font_info = STBTTFontinfo.create();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapHeight * bitmapHeight);
            STBTTPackContext stp_pack_ctx = STBTTPackContext.malloc(stack);
            stbtt_InitFont(stb_font_info, fontBuffer);
            stbtt_PackBegin(stp_pack_ctx, bitmap,  bitmapWidth, bitmapHeight, 0, 4);
            stbtt_PackSetOversampling(stp_pack_ctx, 1, 1);
            stbtt_PackFontRange(stp_pack_ctx, fontBuffer, 0, fontSize, 32, cdata);
            stbtt_PackEnd(stp_pack_ctx);

            scale = stbtt_ScaleForPixelHeight(stb_font_info, fontSize);

            int id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            int[] swizzles = { GL_ONE, GL_ONE, GL_ONE, GL_RED };
            glTexParameteriv(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_RGBA, swizzles);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, bitmapWidth, bitmapHeight, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);


            this.texture = new Texture(id);
            IntBuffer ascent = stack.ints(0);
            IntBuffer descent = stack.ints(0);
            stbtt_GetFontVMetrics(stb_font_info, ascent, descent, null);
            this.ascent = ascent.get();
            this.descent = descent.get();
            this.baseline = (int)(this.ascent * this.scale);

            return bitmap;
        }
    }

    public void generateGlyphs(float sx, float sy) {
        float x = sx;
        for (int i = 32; i < 127; i++) {
            int charIndex = i - 32;

            STBTTPackedchar info = this.cdata.get(charIndex);

            char c = (char) i;

            float charX = x + info.xoff();
            float charY = sy + info.yoff();

            float width = info.xoff2() - info.xoff();
            float height = info.yoff2() - info.yoff();

            float uvX = info.x0() / 512f;
            float uvY = info.y0() / 512f;

            float uvWidth = (info.x1() - info.x0()) / 512f;
            float uvHeight = (info.y1() - info.y0()) / 512f;

            RenderEngine.drawQuadTexture(new Vector2(charX, charY), width, height, uvX, uvY, uvWidth, uvHeight, this.texture);

            x += info.xadvance();
        }
    }


}


