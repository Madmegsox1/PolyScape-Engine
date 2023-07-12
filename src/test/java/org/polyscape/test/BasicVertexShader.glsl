#version 400 core

in vec3 vertices;
in vec2 textCoords;

out vec2 passTextCoords;

uniform float scale;

void main() {
    gl_Position = vec4(vertices * scale, 1.0);
    passTextCoords = textCoords;
}
