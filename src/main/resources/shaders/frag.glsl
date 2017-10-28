#version 120

uniform sampler2D sampler;
uniform vec3 tint;

varying vec2 tex_coords;

void main() {
    gl_FragColor = texture2D(sampler, tex_coords) * vec4(tint, 1.0);
}