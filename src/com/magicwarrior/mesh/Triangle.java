package com.magicwarrior.mesh;

import java.awt.Color;

public class Triangle {

	public Vertex[] triangle = new Vertex[3];
	public Color color;

	int sideNumber;

	public Triangle(float v1X, float v1Y, float v1Z, float v2X, float v2Y, float v2Z, float v3X, float v3Y, float v3Z,
			int sideNumber) {
		triangle[0] = new Vertex(v1X, v1Y, v1Z);
		triangle[1] = new Vertex(v2X, v2Y, v2Z);
		triangle[2] = new Vertex(v3X, v3Y, v3Z);
		this.sideNumber = sideNumber;
	}

	public Triangle() {
		triangle[0] = new Vertex(0, 0, 0);
		triangle[1] = new Vertex(0, 0, 0);
		triangle[2] = new Vertex(0, 0, 0);
	}

	public Triangle(String v1, String v2, String v3) {
		String[] v1P = v1.split(" ");
		String[] v2P = v2.split(" ");
		String[] v3P = v3.split(" ");
		
		triangle[0] = new Vertex(v1P[1], v1P[2], v1P[3]);
		triangle[1] = new Vertex(v2P[1], v2P[2], v2P[3]);
		triangle[2] = new Vertex(v3P[1], v3P[2], v3P[3]);
	}

	public Vertex getVertex(int value) {
		return triangle[value];
	}

	public void setVertex(Vertex vertex, int value) {
		triangle[value] = vertex;
	}

	public int getSideNumber() {
		return sideNumber;
	}

}
