package com.magicwarrior.matrix;

import com.magicwarrior.main.Main;
import com.magicwarrior.mesh.Vertex;
import com.rits.cloning.Cloner;

public class MatrixMath {
	static public Vertex DotMultiplication(Vertex v, float[][] m) {
		Vertex outVertex = new Vertex();

		outVertex.x = (v.x * m[0][0]) + (v.y * m[1][0]) + (v.z * m[2][0]) + (v.w * m[3][0]);
		outVertex.y = (v.x * m[0][1]) + (v.y * m[1][1]) + (v.z * m[2][1]) + (v.w * m[3][1]);
		outVertex.z = (v.x * m[0][2]) + (v.y * m[1][2]) + (v.z * m[2][2]) + (v.w * m[3][2]);
		float w = v.x * m[0][3] + v.y * m[1][3] + v.z * m[2][3] + m[3][3];

		if (w != 0.0f) {
			outVertex.x /= w;
			outVertex.y /= w;
			outVertex.z /= w;
		}

		return outVertex;
	}

	static public float[][] Matrix_MakeProjection(float fFovDegrees, float fAspectRatio, float fNear, float fFar) {
		float fFovRad = 1.0f / (float) Math.tan(fFovDegrees * 0.5f / 180.0f * 3.14159f);
		float[][] matrix = new float[4][4];
		matrix[0][0] = fAspectRatio * fFovRad;
		matrix[1][1] = fFovRad;
		matrix[2][2] = fFar / (fFar - fNear);
		matrix[3][2] = (-fFar * fNear) / (fFar - fNear);
		matrix[2][3] = 1.0f;
		matrix[3][3] = 0.0f;
		return matrix;
	}

	static public float[][] Matrix_MultiplyMatrix(float[][] m1, float[][] m2) {
		float[][] matrix = new float[4][4];
		for (int c = 0; c < 4; c++)
			for (int r = 0; r < 4; r++)
				matrix[r][c] = m1[r][0] * m2[0][c] + m1[r][1] * m2[1][c] + m1[r][2] * m2[2][c] + m1[r][3] * m2[3][c];
		return matrix;
	}

	static public Vertex MultiplyVertex(float[][] m, Vertex i) {

		Vertex outVertex = new Vertex();
		outVertex.x = (i.x * m[0][0]) + (i.y * m[1][0]) + (i.z * m[2][0]) + (i.w * m[3][0]);
		outVertex.y = (i.x * m[0][1]) + (i.y * m[1][1]) + (i.z * m[2][1]) + (i.w * m[3][1]);
		outVertex.z = (i.x * m[0][2]) + (i.y * m[1][2]) + (i.z * m[2][2]) + (i.w * m[3][2]);
		outVertex.w = (i.x * m[0][3]) + (i.y * m[1][3]) + (i.z * m[2][3]) + (i.w * m[3][3]);
		return outVertex;
	}

	static public float DotProduct(Vertex v1, Vertex v2) {

		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;

	}

	static public float[][] Matrix_MakeIdentity() {
		float[][] m = new float[4][4];
		m[0][0] = 1.0f;
		m[1][1] = 1.0f;
		m[2][2] = 1.0f;
		m[3][3] = 1.0f;
		return m;

	}

	static public Vertex CrossProduct(Vertex v1, Vertex v2) {
		Vertex v = new Vertex();
		v.x = (v1.y * v2.z) - (v1.z * v2.y);
		v.y = (v1.z * v2.x) - (v1.x * v2.z);
		v.z = (v1.x * v2.y) - (v1.y * v2.x);
		return v;
	}

	static public float[][] Matrix_MakeRotationX(float fAngleRad)
	{
		float[][] matrix = new float[4][4];
		matrix[0][0] = 1.0f;
		matrix[1][1] = (float)Math.cos(fAngleRad);
		matrix[1][2] = (float)Math.sin(fAngleRad);
		matrix[2][1] = -(float)Math.sin(fAngleRad);
		matrix[2][2] = (float)Math.cos(fAngleRad);
		matrix[3][3] = 1.0f;
		return matrix;
	}

	static public float[][] Matrix_MakeRotationY(float fAngleRad)
	{
		float[][] matrix = new float[4][4];
		matrix[0][0] = (float)Math.cos(fAngleRad);
		matrix[0][2] = (float)Math.sin(fAngleRad);
		matrix[2][0] = -(float)Math.sin(fAngleRad);
		matrix[1][1] = 1.0f;
		matrix[2][2] = (float)Math.cos(fAngleRad);
		matrix[3][3] = 1.0f;
		return matrix;
	}

	static public float[][] Matrix_MakeRotationZ(float fAngleRad)
	{
		float[][] matrix = new float[4][4];
		matrix[0][0] = (float)Math.cos(fAngleRad);
		matrix[0][1] = (float)Math.sin(fAngleRad);
		matrix[1][0] = -(float)Math.sin(fAngleRad);
		matrix[1][1] = (float)Math.cos(fAngleRad);
		matrix[2][2] = 1.0f;
		matrix[3][3] = 1.0f;
		return matrix;
	}
	
	static public float[][] Matrix_MakeTranslation(float x, float y, float z)
	{
		float[][] matrix = new float[4][4];
		matrix[0][0] = 1.0f;
		matrix[1][1] = 1.0f;
		matrix[2][2] = 1.0f;
		matrix[3][3] = 1.0f;
		matrix[3][0] = x;
		matrix[3][1] = y;
		matrix[3][2] = z;
		return matrix;
	}

	static public float[][] translate(float f, float g, float h) {
		float[][] trans = { { 1, 0, 0, f }, { 0, 1, 0, g }, { 0, 0, 1, h }, { 0, 0, 0, 0 } };
		return trans;
	}

	static public Vertex subtractVector(Vertex v1, Vertex v2) {
		return new Vertex(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	static public float[][] project() {
		float[][] projectionMatrix = new float[4][4];
		float fNear = 0.1f;
		float fFar = 1000.0f;
		float fFov = 90.0f;
		float fAspectRatio = (float) Main.windowHeight / (float) Main.windowWidth;
		float fFovRad = (float) (1.0f / Math.tan(fFov * 0.5f / 180.0f * (float) Math.PI));

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

	static public float[][] PointAt(Vertex pos, Vertex target, Vertex up) {

		// Calculate new forward direction
		Vertex newForward = Sub(target, pos);
		newForward = Normalise(newForward);

		// Calculate new Up direction
		Vertex a = Mult(newForward, DotProduct(up, newForward));
		Vertex newUp = Sub(up, a);
		newUp = Normalise(newUp);

		Vertex newRight = CrossProduct(newUp, newForward);

		float[][] matrix = new float[4][4];

		matrix[0][0] = newRight.x;
		matrix[0][1] = newRight.y;
		matrix[0][2] = newRight.z;
		matrix[0][3] = 0.0f;
		matrix[1][0] = newUp.x;
		matrix[1][1] = newUp.y;
		matrix[1][2] = newUp.z;
		matrix[1][3] = 0.0f;
		matrix[2][0] = newForward.x;
		matrix[2][1] = newForward.y;
		matrix[2][2] = newForward.z;
		matrix[2][3] = 0.0f;
		matrix[2][0] = pos.x;
		matrix[2][1] = pos.y;
		matrix[2][2] = pos.z;
		matrix[2][3] = 1.0f;

		return matrix;
	}

	static public float[][] QuickInverse(float[][] m) {

		float[][] matrix = new float[4][4];
		matrix[0][0] = m[0][0];
		matrix[0][1] = m[1][0];
		matrix[0][2] = m[2][0];
		matrix[0][3] = 0.0f;
		matrix[1][0] = m[0][1];
		matrix[1][1] = m[1][1];
		matrix[1][2] = m[2][1];
		matrix[1][3] = 0.0f;
		matrix[2][0] = m[0][2];
		matrix[2][1] = m[1][2];
		matrix[2][2] = m[2][2];
		matrix[2][3] = 0.0f;
		matrix[3][0] = -((m[3][0] * matrix[0][0]) + (m[3][1] * matrix[1][0]) + (m[3][2] * matrix[2][0]));
		matrix[3][1] = -((m[3][0] * matrix[0][1]) + (m[3][1] * matrix[1][1]) + (m[3][2] * matrix[2][1]));
		matrix[3][2] = -((m[3][0] * matrix[0][2]) + (m[3][1] * matrix[1][2]) + (m[3][2] * matrix[2][2]));
		matrix[3][3] = 1.0f;
		return matrix;
	}

	static public Vertex Mult(Vertex v, float value) {

		return new Vertex(v.x * value, v.y * value, v.z * value);

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
		return new Vertex(vertex.x, vertex.y, vertex.z + value);
	}

	static public Vertex VertexAdd(Vertex v1, Vertex v2) {
		return new Vertex(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	static public Vertex Sub(Vertex v1, Vertex v2) {
		return new Vertex(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	static public Vertex VertexDiv(Vertex v, float k) {
		return new Vertex(v.x / k, v.y / k, v.z / k);
	}

	static public Vertex Normalise(Vertex v) {
		float l = Length(v);
		return new Vertex(v.x / l, v.y / l, v.z / l);
	}

	static public float Length(Vertex v) {

		return (float) Math.sqrt(DotProduct(v, v));
	}

}
