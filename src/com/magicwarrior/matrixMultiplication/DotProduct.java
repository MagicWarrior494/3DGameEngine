package com.magicwarrior.matrixMultiplication;

import com.magicwarrior.mesh.Vertex;

public class DotProduct {
	boolean first = true;

	public DotProduct(int windowWidth, int windowHeight) {

	}

	public Vertex DotMultiplication(Vertex vertex, float[][] multMatrix) {

		Vertex outVertex = new Vertex();

		outVertex.x = (vertex.x * multMatrix[0][0]) + (vertex.y * multMatrix[0][1]) + (vertex.z * multMatrix[0][2])
				+ (vertex.w * multMatrix[0][3]);
		outVertex.y = (vertex.x * multMatrix[1][0]) + (vertex.y * multMatrix[1][1]) + (vertex.z * multMatrix[1][2])
				+ (vertex.w * multMatrix[1][3]);
		outVertex.z = (vertex.x * multMatrix[2][0]) + (vertex.y * multMatrix[2][1]) + (vertex.z * multMatrix[2][2])
				+ (vertex.w * multMatrix[2][3]);
		float w = (vertex.x * multMatrix[3][0]) + (vertex.y * multMatrix[3][1]) + (vertex.z * multMatrix[3][2])
				+ (vertex.w * multMatrix[3][3]);

		if (w != 0.0f) {
			outVertex.x /= w;
			outVertex.y /= w;
			outVertex.z /= w;
		}
		return outVertex;
	}

	public Vertex Mult(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x * value;
		outVertex.y = vertex.y * value;
		outVertex.z = vertex.z * value;
		return outVertex;
	}
	public Vertex MultX(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x * value;
		outVertex.y = vertex.y;
		outVertex.z = vertex.z;
		return outVertex;
	}
	public Vertex MultY(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x;
		outVertex.y = vertex.y * value;
		outVertex.z = vertex.z;
		return outVertex;
	}
	public Vertex MultZ(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x;
		outVertex.y = vertex.y;
		outVertex.z = vertex.z * value;
		return outVertex;
	}
	public Vertex AddX(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x + value;
		outVertex.y = vertex.y;
		outVertex.z = vertex.z;
		return outVertex;
	}
	public Vertex AddY(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x;
		outVertex.y = vertex.y + value;
		outVertex.z = vertex.z;
		return outVertex;
	}
	public Vertex AddZ(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x;
		outVertex.y = vertex.y;
		outVertex.z = vertex.z + value;
		return outVertex;
	}
}
