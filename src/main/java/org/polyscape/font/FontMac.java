package org.polyscape.font;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;
import org.polyscape.Profile;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FontMac {

    public ByteBuffer atlas;
    public FontMac() throws IOException {
        ByteBuffer fontBuffer = loadFontFile("Segoe UI");
        atlas = createFontAtlas(fontBuffer, 512, 512);
    }

    public ByteBuffer loadFontFile(String fontPath) throws IOException {
        fontPath = Profile.Font.FONT_LOCATION + File.separator + fontPath + "." + Profile.Font.FONT_FILEFORMAT;
        byte[] fontData = Files.readAllBytes(Paths.get(fontPath));
        ByteBuffer fontBuffer = BufferUtils.createByteBuffer(fontData.length);
        fontBuffer.put(fontData);
        fontBuffer.flip(); // Prepare for reading
        return fontBuffer;
    }


    private ByteBuffer createFontAtlas(ByteBuffer fontBuffer, int bitmapWidth, int bitmapHeight) {
        // Create a buffer for the font atlas bitmap


        ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapHeight * bitmapHeight);

        try (STBTTPackContext pc = STBTTPackContext.malloc()) {
            // Initialize pack context
            stbtt_PackBegin(pc, bitmap, bitmapWidth, bitmapHeight, 0, 1, NULL);

            // Allocate memory for character data (ASCII 32 to 126)
            STBTTPackedchar.Buffer cdata = STBTTPackedchar.malloc(95);
            STBTTPackRange.Buffer packRange = STBTTPackRange.malloc(1);

            packRange.get(0)
                    .font_size(123.1f)
                    .first_unicode_codepoint_in_range(32)
                    .num_chars(95)
                    .chardata_for_range(cdata)
                    .array_of_unicode_codepoints(null);



            // Load font info
            STBTTFontinfo fontInfo = STBTTFontinfo.create();
            if (!stbtt_InitFont(fontInfo, fontBuffer)) {
                throw new IllegalStateException("Failed to initialize font information.");
            }

            // Pack characters to create the font atlas
            if (!stbtt_PackFontRanges(pc, fontBuffer, 0, packRange)) {
                throw new IllegalStateException("Failed to pack font ranges.");
            }

            stbtt_PackEnd(pc);

            generateGlyphs(cdata, bitmapWidth, bitmapHeight);
        }


        // 'bitmap' now contains the font atlas, and 'cdata' contains character data
        // You can upload 'bitmap' to OpenGL as a texture, and use 'cdata' for rendering
        return bitmap;
    }

    private void generateGlyphs(STBTTPackedchar.Buffer cdata, int atlasWidth, int atlasHeight) {
        STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
        float[] xpos = new float[]{0.0f}; // This will be updated to the end of the last character rendered
        float[] ypos = new float[]{0.0f}; // Initial y position
        for (int i = 32; i < 127; i++) {
            int charIndex = i - 32;
            stbtt_GetPackedQuad(cdata, atlasWidth, atlasHeight, charIndex, xpos, ypos, quad, false);

            char c = (char) i;
            float charWidth = quad.x1() - quad.x0();
            float charHeight = quad.y1() - quad.y0();


            float x = xpos[0];
            float y = ypos[0];
        }
        quad.free();
    }


}


