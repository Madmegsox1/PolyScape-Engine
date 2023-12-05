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

    //check if gl_FragCoord is in bounds of lightLocation, lightHeight and lightWidth
    if (gl_FragCoord.x < lightLocation.x + lightWidth && gl_FragCoord.x > lightLocation.x && gl_FragCoord.y < lightLocation.y + lightHeight && gl_FragCoord.y > lightLocation.y) {
        gl_FragColor = vec4(1, 1, 1, 0);
        return;
    }


    // find the closes corner of the light
    // 1 = top left, 2 = top right, 3 = bottom left, 4 = bottom right
    float distance = 0;
    if (gl_FragCoord.x < lightLocation.x && gl_FragCoord.y < lightLocation.y) {
        distance = length(vec2(lightLocation.x, lightLocation.y + lightHeight)  - gl_FragCoord.xy);
    } else if (gl_FragCoord.x > lightLocation.x + lightWidth && gl_FragCoord.y < lightLocation.y) {
        distance = length(vec2(lightLocation.x + lightWidth, lightLocation.y + lightHeight) - gl_FragCoord.xy);
    } else if (gl_FragCoord.x < lightLocation.x && gl_FragCoord.y > lightLocation.y + lightHeight) {
        distance = length(lightLocation - gl_FragCoord.xy);
    } else if (gl_FragCoord.x > lightLocation.x + lightWidth && gl_FragCoord.y > lightLocation.y + lightHeight) {
        distance = length(vec2(lightLocation.x + lightWidth, lightLocation.y)  - gl_FragCoord.xy);
    } else{
        distance = length(lightLocation - gl_FragCoord.xy);
    }

    float attenuation = 1.0 / distance;

    // get brightness
    attenuation = attenuation * lightBrightness;


    vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(lightColor, 1);

    gl_FragColor = color;
}
