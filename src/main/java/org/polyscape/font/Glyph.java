package org.polyscape.font;

public final class Glyph {

    public final float width;
    public final float height;
    public final float x;
    public final float y;
    public final float uvx;
    public final float uvy;
    public final float uvWidth;
    public final float uvHeight;

    public final float advance;

    /**
     * Creates a font Glyph.
     *
     * @param width   Width of the Glyph
     * @param height  Height of the Glyph
     * @param x       X coordinate on the font texture
     * @param y       Y coordinate on the font texture
     */
    public Glyph(float width, float height, float x, float y, float uvx, float uvy, float uvWidth, float uvHeight, float advance) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.uvx = uvx;
        this.uvy = uvy;
        this.uvWidth = uvWidth;
        this.uvHeight = uvHeight;
        this.advance = advance;
    }
}
