#version 120

varying vec2 TexCoord;

uniform sampler2D textureSampler;
uniform vec4 textureColor; // Add this line for the text color

void main()
{
    vec4 sampled = vec4(1.0, 1.0, 1.0, texture2D(textureSampler, gl_FragCoord).r);
    gl_FragColor = textureColor * sampled; //* sampled; // Modulate texture alpha with text color
}