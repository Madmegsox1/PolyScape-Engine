package org.polyscape.rendering.shaders;

import org.polyscape.Profile;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

/**
 * @author Madmegsox1
 * @since 18/07/2023
 */

public class LightingShader extends Shader{

    private int lightLocationUniformId;
    private int lightColorUniformId;
    private int lightBrightnessUniformId;

    public LightingShader() {
        super("LightingFrag", false);
    }

    @Override
    public void bindAllAttributes() {

    }

    @Override
    protected void getAllUniforms() {
        lightLocationUniformId = getUniform("lightLocation");
        lightBrightnessUniformId = getUniform("lightBrightness");
        lightColorUniformId = getUniform("lightColor");

    }

    public void loadLightColor(Color color){
        super.loadColorUniform(lightColorUniformId, color);
    }

    public void loadLightLocation(Vector2 vector2){
        super.loadVectorUniform(lightLocationUniformId, new Vector2(vector2.x, Profile.Display.HEIGHT - vector2.y));
    }

    public void loadLightBrightness(float brightness){
        super.loadFloatUniform(lightBrightnessUniformId, brightness);
    }

}
