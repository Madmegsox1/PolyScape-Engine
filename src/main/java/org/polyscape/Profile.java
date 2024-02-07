package org.polyscape;

import org.polyscape.rendering.elements.Color;

public final class Profile {
    public final static class Display
    {
        public static int WIDTH = 1280;
        public static int HEIGHT = 720;
        public static int ASPECT_RATIO_NUMERATOR = 16;
        public static int ASPECT_RATIO_DENOMINATOR = 9;

        public static float Z_NEAR = 0.01f;

        public static float Z_FAR = 1000f;

        private final static int BACKGROUND_RED = 255;
        private final static int BACKGROUND_GREEN = 0;
        private final static int BACKGROUND_BLUE = 0;
        private final static int BACKGROUND_ALPHA = 0;
        public static float[] BACKGROUND_COLOR = new float[]{BACKGROUND_RED/255f, BACKGROUND_GREEN/255f, BACKGROUND_BLUE/255f, BACKGROUND_ALPHA/255f};

    }

    public final static class ObjectSettings{
        public static final float BaseVelocityDecay = 0.99f;
    }

    public final static class Textures
    {

        public static final String TEXTURE_LOCATION = "res/textures/";
        public static final String TEXTURE_FILEFORMAT = "png";

    }

    public final static class Font{
        public static final String FONT_LOCATION = "res/fonts/";
        public static final String FONT_FILEFORMAT = "ttf";
    }

    public final static class Shaders{
        public static final String SHADER_LOCATION = "res/shaders/";
        public static final String SHADER_FILEFORMAT = "glsl";
    }

    public final static class Sound
    {

        public static final String SOUND_LOCATION = "res/sound/";
        public static final String SOUND_FILEFORMAT = "wav";

    }

    public final static class Colors
    {
        public static Color navyBlue = new Color(32, 44, 57);
        public static Color lighterNavyBlue = new Color(40, 56, 69);
        public static Color tealSand = new Color(184, 176, 141);
        public static Color lighterTealSand = new Color(242, 212, 146);
        public static Color funnyOrange = new Color(242, 149, 89);
        public static Color black = new Color(0, 0, 0);
        public static Color white = new Color(255, 255, 255);
        public static Color grey = new Color(100, 100, 100);
    }

    public final static class UiThemes
    {
        public final static class Dark {
            public static Color background = new Color(61, 61, 61);
            public static Color foreground = Colors.white;

            public static Color accent = new Color(175, 255, 178);

        }
    }
}
