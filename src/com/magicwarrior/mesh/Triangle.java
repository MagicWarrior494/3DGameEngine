package com.magicwarrior.mesh;

public class Triangle{
	
	public Vertex[] triangle = new Vertex[3];
	
	
	public Triangle(float v1X, float v1Y, float v1Z, float v2X, float v2Y, float v2Z, float v3X, float v3Y, float v3Z) {
		triangle[0] = new Vertex(v1X, v1Y, v1Z);
		triangle[1]= new Vertex(v2X, v2Y, v2Z);
		triangle[2] = new Vertex(v3X, v3Y, v3Z);
	}
	
	public Triangle() {
		triangle[0] = new Vertex(0,0,0);
		triangle[1] = new Vertex(0,0,0);
		triangle[2] = new Vertex(0,0,0);
	}
	
	public Vertex getVertex(int value) {
		return triangle[value];
	}
	public void setVertex(Vertex vertex, int value) {
		triangle[value] = vertex;
	}
	
}
