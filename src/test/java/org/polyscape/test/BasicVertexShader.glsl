#version 400 core

in vec3 vertices;

out vec4 color;

void main() {
    gl_Position = vec4(vertices, 1.0);
    color = vec4(vertices.x + 0.5, 0.0, vertices.y + 0.5, 1.0);
}
