#version 330 core

in vec2 TexCoord;                           // Interpolated texture coordinates from vertex shader
in vec2 FragPos;

out vec4 FragColor;

uniform vec4 shapeColor;                    // Color of the shape (used for non-textured shapes)
uniform sampler2D textureSampler;           // Texture sampler (for textured quads)
uniform bool useTexture;                    // Whether to use texture or color only

uniform bool wireframeHolo;
uniform bool wireframe;
uniform float wireframeWidth;
uniform vec4 wireframeColor;


void main() {

    if(wireframe) {
        float distance = min(min(FragPos.x, 1.0 - FragPos.x), min(FragPos.y, 1.0 - FragPos.y));

        if(distance < wireframeWidth){
            FragColor = wireframeColor;
            return;
        }
    } else if(wireframeHolo) {
        discard;
    }

    if (useTexture) {
        FragColor = texture(textureSampler, TexCoord) * shapeColor;
    } else {
        FragColor = shapeColor;
    }
}
