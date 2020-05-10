package com.magicwarrior.mesh;

public class Vertex {
	
	public float x, y, z, w;
	
	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
	}
	
	public Vertex() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 1;
	}

}
