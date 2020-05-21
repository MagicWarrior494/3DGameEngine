package com.magicwarrior.matrix;

import com.magicwarrior.main.Main;
import com.magicwarrior.mesh.Vertex;
import com.rits.cloning.Cloner;

public class MatrixMath {

	static public Vertex DotMultiplication(Vertex vertex, float[][] multMatrix) {

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

	static public Vertex worldUpdate(Vertex object, double aX, double aY, double aZ, int mZ) {
		Vertex vertex = new Vertex();
		Cloner clone = new Cloner();
		vertex = DotMultiplication(object, rotationX(aX));
		vertex = DotMultiplication(vertex, rotationY(aY));
		vertex = DotMultiplication(vertex, rotationZ(aZ));
		vertex.z = clone.deepClone(vertex.z + mZ);
		return vertex;

	}

	static public float[][] rotationZ(double angle) {
		float[][] rotation = { { (float) Math.cos(angle), (float) -Math.sin(angle), 0, 0 },
				{ (float) Math.sin(angle), (float) Math.cos(angle), 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 } };
		return rotation;
	}

	static public float[][] rotationX(double angle) {
		float[][] rotation = { { 1, 0, 0, 0 }, { 0, (float) Math.cos(angle), (float) -Math.sin(angle), 0 },
				{ 0, (float) Math.sin(angle), (float) Math.cos(angle), 0 }, { 0, 0, 0, 0 } };
		return rotation;
	}

	static public float[][] rotationY(double angle) {
		float[][] rotation = { { (float) Math.cos(angle), 0, (float) -Math.sin(angle), 0 }, { 0, 1, 0, 0 },
				{ (float) Math.sin(angle), 0, (float) Math.cos(angle), 0 }, { 0, 0, 0, 0 } };
		return rotation;
	}

	static public float[][] translate(int x, int y, int z) {
		float[][] trans = { { 1, 0, 0, x }, { 0, 1, 0, y }, { 0, 0, 1, z }, { 0, 0, 0, 1 } };
		return trans;
	}

	static public Vertex subtractVector(Vertex v1, Vertex v2) {
		Vertex o = new Vertex();
		o.x = v1.x - v2.x;
		o.y = v1.y - v2.y;
		o.z = v1.z - v2.z;
		return o;
	}
	
	static public float[][] project() {
		float[][] projectionMatrix = new float[4][4];
		float fNear = 0.1f;
		float fFar = 1000.0f;
		float fFov = 90.0f;
		float fAspectRatio = (float) Main.windowHeight / (float) Main.windowWidth;
		float fFovRad = (float) (1.0f / Math.tan(fFov * 0.5f / 180.0f * Math.PI));

		projectionMatrix[0][0] = fAspectRatio * fFovRad;
		projectionMatrix[1][1] = fFovRad;
		projectionMatrix[2][2] = fFar / (fFar - fNear);
		projectionMatrix[3][2] = (-fFar * fNear) / (fFar - fNear);
		projectionMatrix[2][3] = 1.0f;
		projectionMatrix[3][3] = 0.0f;

		return projectionMatrix;
	}

	static public Vertex ProjectedUpdate(Vertex vertex) {
		Vertex o = new Vertex();
		o = vertex;
		o = AddX(o, 1);
		o = AddY(o, 1);
		o = MultX(o, Main.windowWidth / 2);
		o = MultY(o, Main.windowHeight / 2);
		return o;
	}

	static public Vertex Mult(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x * value;
		outVertex.y = vertex.y * value;
		outVertex.z = vertex.z * value;
		return outVertex;
	}

	static public Vertex MultX(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x * value;
		outVertex.y = vertex.y;
		outVertex.z = vertex.z;
		return outVertex;
	}

	static public Vertex MultY(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x;
		outVertex.y = vertex.y * value;
		outVertex.z = vertex.z;
		return outVertex;
	}

	static public Vertex MultZ(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x;
		outVertex.y = vertex.y;
		outVertex.z = vertex.z * value;
		return outVertex;
	}

	static public Vertex AddX(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x + value;
		outVertex.y = vertex.y;
		outVertex.z = vertex.z;
		return outVertex;
	}

	static public Vertex AddY(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x;
		outVertex.y = vertex.y + value;
		outVertex.z = vertex.z;
		return outVertex;
	}

	static public Vertex AddZ(Vertex vertex, int value) {
		Vertex outVertex = new Vertex();
		outVertex.x = vertex.x;
		outVertex.y = vertex.y;
		outVertex.z = vertex.z + value;
		return outVertex;
	}

}
