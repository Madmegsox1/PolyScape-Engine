#version 120

attribute vec2 aPos;
attribute vec2 aTexCoord;

varying vec2 TexCoord;

void main() {
    gl_Position = vec4(aPos, 0.0, 1.0);
    TexCoord = aTexCoord;
}
