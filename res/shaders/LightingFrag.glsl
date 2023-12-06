#version 120

uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform float lightBrightness;

uniform float lightHeight;
uniform float lightWidth;


void main() {
    // if lightBrightness is 0, then the light is off
    if (lightBrightness == 0) {
        gl_FragColor = vec4(0, 0, 0, 0);
        return;
    }

    float attenuation = 0;

    //check if gl_FragCoord is in bounds of lightLocation, lightHeight and lightWidth
    if(lightWidth > 0 && lightHeight > 0){
        if (gl_FragCoord.x < lightLocation.x + lightWidth && gl_FragCoord.x > lightLocation.x && gl_FragCoord.y < lightLocation.y + lightHeight && gl_FragCoord.y > lightLocation.y) {
            gl_FragColor = vec4(1, 1, 1, 0);
            return;
        }

        float distance = length(vec2(lightLocation.x + lightWidth / 2, lightLocation.y + lightHeight / 2) - gl_FragCoord.xy);

        attenuation = 1.0 / distance;
    }

    // get brightness
    attenuation = attenuation * lightBrightness;


    vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(lightColor, 1);

    gl_FragColor = color;
}
