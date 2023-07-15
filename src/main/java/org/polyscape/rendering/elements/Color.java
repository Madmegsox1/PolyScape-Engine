package org.polyscape.rendering.elements;

public final class Color {
    public float r;
    public float g;
    public float b;
    public float a;

    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float r, float g, float b)
    {
        this(r, g, b, 255);
    }

    public Color()
    {
        this(255, 255, 255, 255);
    }

    public Color(int value)
    {
        int r = (value & 0x00FF0000) >> 16;
        int g = (value & 0x0000FF00) >> 8;
        int b = (value & 0x000000FF);
        int a = (value & 0xFF000000) >> 24;

        if (a < 0)
        {
            a += 256;
        }
        if (a == 0)
        {
            a = 255;
        }

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static Color BLACK = new Color(0,0,0);
    public static Color WHITE = new Color(255,255,255);
    public static Color RED = new Color(255, 0 ,0);
    public static Color GREEN = new Color(0, 255 ,0);
    public static Color BLUE = new Color(0, 0 ,255);


    public static float[] convertColorToFloat(final Color c){
        final float[] colors = new float[3];
        colors[0] = c.r/255f;
        colors[1] = c.g/255f;
        colors[2] = c.b/255f;
        return colors;
    }

    public static float[] convertColorToFloatAlpha(final Color c){
        final float[] colors = new float[4];
        colors[0] = c.r/255f;
        colors[1] = c.g/255f;
        colors[2] = c.b/255f;
        colors[3] = c.a/255f;
        return colors;
    }
}
