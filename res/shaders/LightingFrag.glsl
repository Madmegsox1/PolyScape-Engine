#version 120

uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform float lightBrightness;

void main() {
    // if lightBrightness is 0, then the light is off
    if (lightBrightness == 0) {
        gl_FragColor = vec4(0, 0, 0, 0);
        return;
    }


    float distance = length(lightLocation - gl_FragCoord.xy);
    float attenuation = 1.0 / distance;

    // get brightness
    attenuation = attenuation * lightBrightness;


    vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(lightColor, 1);

    gl_FragColor = color;
}
