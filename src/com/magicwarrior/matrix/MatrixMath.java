package com.magicwarrior.matrix;

import java.awt.Color;

import com.magicwarrior.mesh.Triangle;
import com.magicwarrior.mesh.Vertex;

public class MatrixMath {

	public Triangle clipped1;
	public Triangle clipped2;

	static public Vertex Matrix_MultiplyVector(float[][] m, Vertex i) {
		Vertex v = new Vertex();
		v.x = i.x * m[0][0] + i.y * m[1][0] + i.z * m[2][0] + i.w * m[3][0];
		v.y = i.x * m[0][1] + i.y * m[1][1] + i.z * m[2][1] + i.w * m[3][1];
		v.z = i.x * m[0][2] + i.y * m[1][2] + i.z * m[2][2] + i.w * m[3][2];
		v.w = i.x * m[0][3] + i.y * m[1][3] + i.z * m[2][3] + i.w * m[3][3];
		return v;
	}

	static public float[][] Matrix_MakeIdentity() {
		float[][] matrix = new float[4][4];
		matrix[0][0] = 1.0f;
		matrix[1][1] = 1.0f;
		matrix[2][2] = 1.0f;
		matrix[3][3] = 1.0f;
		return matrix;
	}

	static public Vertex Vector_IntersectPlane(Vertex plane_p, Vertex plane_n, Vertex lineStart, Vertex lineEnd) {
		plane_n = Vector_Normalise(plane_n);
		float plane_d = -Vector_DotProduct(plane_n, plane_p);
		float ad = Vector_DotProduct(lineStart, plane_n);
		float bd = Vector_DotProduct(lineEnd, plane_n);
		float t = (-plane_d - ad) / (bd - ad);
		Vertex lineStartToEnd = Vector_Sub(lineEnd, lineStart);
		Vertex lineToIntersect = Vector_Mul(lineStartToEnd, t);
		return Vector_Add(lineStart, lineToIntersect);
	}

	public int Triangle_ClipAgainstPlane(Vertex plane_p, Vertex plane_n, Triangle in_tri) {
		// Make sure plane normal is indeed normal
		plane_n = Vector_Normalise(plane_n);
		// Create two temporary storage arrays to classify points either side of plane
		// If distance sign is positive, point lies on "inside" of plane
		Vertex[] inside_points = new Vertex[3];
		Vertex[] outside_points = new Vertex[3];
		
		int nInsidePointCount = 0;
		int nOutsidePointCount = 0;

		// Get signed distance of each point in triangle to plane
		float d0 = dist(in_tri.getVertex(0), plane_n, plane_p);
		float d1 = dist(in_tri.getVertex(1), plane_n, plane_p);
		float d2 = dist(in_tri.getVertex(2), plane_n, plane_p);

		if (d0 >= 0) {
			inside_points[nInsidePointCount++] = in_tri.getVertex(0);
		} else {
			outside_points[nOutsidePointCount++] = in_tri.getVertex(0);
		}
		if (d1 >= 0) {
			inside_points[nInsidePointCount++] = in_tri.getVertex(1);
		} else {
			outside_points[nOutsidePointCount++] = in_tri.getVertex(1);
		}
		if (d2 >= 0) {
			inside_points[nInsidePointCount++] = in_tri.getVertex(2);
		} else {
			outside_points[nOutsidePointCount++] = in_tri.getVertex(2);
		}

		// Now classify triangle points, and break the input triangle into
		// smaller output triangles if required. There are four possible
		// outcomes...

		if (nInsidePointCount == 0) {
			// All points lie on the outside of plane, so clip whole triangle
			// It ceases to exist
			clipped1 = null;
			clipped2 = null;
			return 0; // No returned triangles are valid
		}

		if (nInsidePointCount == 3) {
			// All points lie on the inside of plane, so do nothing
			// and allow the triangle to simply pass through
			clipped1 = in_tri;
			clipped1.color = Color.red;
			clipped2 = null;
			
			return 1; // Just the one returned original triangle is valid
		}

		if (nInsidePointCount == 1 && nOutsidePointCount == 2) {
			// The inside point is valid, so keep that...
			clipped1 = new Triangle();
			clipped2 = null;
			clipped1.setVertex(inside_points[0], 0);

			// but the two new points are at the locations where the
			// original sides of the triangle (lines) intersect with the plane
			clipped1.setVertex(Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]), 1);
			clipped1.setVertex(Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[1]), 2);

			clipped1.color = Color.red;

			return 1; // Return the newly formed single triangle
		}

		if (nInsidePointCount == 2 && nOutsidePointCount == 1) {
			// Triangle should be clipped. As two points lie inside the plane,
			// the clipped triangle becomes a "quad". Fortunately, we can
			// represent a quad with two new triangles

			// Copy appearance info to new triangles

			// The first triangle consists of the two ianside points and a new
			// point determined by the location where one side of the triangle
			// intersects with the plane
			clipped1 = new Triangle();
			clipped2 = new Triangle();
			
			clipped1.color = Color.red;
			clipped2.color = Color.BLUE;
			
			clipped1.setVertex(inside_points[0], 0);
			clipped1.setVertex(inside_points[1], 1);
			clipped1.setVertex(Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]), 2);

			// The second triangle is composed of one of he inside points, a
			// new point determined by the intersection of the other side of the
			// triangle and the plane, and the newly created point above
			clipped2.setVertex(inside_points[1], 0);
			clipped2.setVertex(clipped1.getVertex(2), 1);
			clipped2.setVertex(Vector_IntersectPlane(plane_p, plane_n, inside_points[1], outside_points[0]), 2);

			return 2; // Return two newly formed triangles which form a quad
		} else {
			return 0;
		}
	}

	static public float dist(Vertex p, Vertex plane_n, Vertex plane_p) {

		return (plane_n.x * p.x + plane_n.y * p.y + plane_n.z * p.z - Vector_DotProduct(plane_n, plane_p));
	}

	static public float[][] Matrix_MakeRotationX(float fAngleRad) {
		float[][] matrix = new float[4][4];
		matrix[0][0] = 1.0f;
		matrix[1][1] = (float) Math.cos(fAngleRad);
		matrix[1][2] = (float) Math.sin(fAngleRad);
		matrix[2][1] = (float) -Math.sin(fAngleRad);
		matrix[2][2] = (float) Math.cos(fAngleRad);
		matrix[3][3] = 1.0f;
		return matrix;
	}

	static public float[][] Matrix_MakeRotationY(float fAngleRad) {
		float[][] matrix = new float[4][4];
		matrix[0][0] = (float) Math.cos(fAngleRad);
		matrix[0][2] = (float) Math.sin(fAngleRad);
		matrix[2][0] = (float) -Math.sin(fAngleRad);
		matrix[1][1] = 1.0f;
		matrix[2][2] = (float) Math.cos(fAngleRad);
		matrix[3][3] = 1.0f;
		return matrix;
	}

	static public float[][] Matrix_MakeRotationZ(float fAngleRad) {
		float[][] matrix = new float[4][4];
		matrix[0][0] = (float) Math.cos(fAngleRad);
		matrix[0][1] = (float) Math.sin(fAngleRad);
		matrix[1][0] = (float) -Math.sin(fAngleRad);
		matrix[1][1] = (float) Math.cos(fAngleRad);
		matrix[2][2] = 1.0f;
		matrix[3][3] = 1.0f;
		return matrix;
	}

	static public float[][] Matrix_MakeTranslation(float x, float y, float z) {
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

	static public float[][] Matrix_PointAt(Vertex pos, Vertex target, Vertex up) {
		// Calculate new forward direction
		Vertex newForward = Vector_Sub(target, pos);
		newForward = Vector_Normalise(newForward);

		// Calculate new Up direction
		Vertex a = Vector_Mul(newForward, Vector_DotProduct(up, newForward));
		Vertex newUp = Vector_Sub(up, a);
		newUp = Vector_Normalise(newUp);

		// New Right direction is easy, its just cross product
		Vertex newRight = Vector_CrossProduct(newUp, newForward);

		// Construct Dimensioning and Translation Matrix
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
		matrix[3][0] = pos.x;
		matrix[3][1] = pos.y;
		matrix[3][2] = pos.z;
		matrix[3][3] = 1.0f;
		return matrix;

	}

	static public float[][] Matrix_QuickInverse(float[][] m) // Only for Rotation/Translation Matrices
	{
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
		matrix[3][0] = -(m[3][0] * matrix[0][0] + m[3][1] * matrix[1][0] + m[3][2] * matrix[2][0]);
		matrix[3][1] = -(m[3][0] * matrix[0][1] + m[3][1] * matrix[1][1] + m[3][2] * matrix[2][1]);
		matrix[3][2] = -(m[3][0] * matrix[0][2] + m[3][1] * matrix[1][2] + m[3][2] * matrix[2][2]);
		matrix[3][3] = 1.0f;
		return matrix;
	}

	static public Vertex Vector_Add(Vertex v1, Vertex v2) {
		return new Vertex(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	static public Vertex Vector_Sub(Vertex v1, Vertex v2) {
		return new Vertex(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	static public Vertex Vector_Mul(Vertex v1, float k) {
		return new Vertex(v1.x * k, v1.y * k, v1.z * k);
	}

	static public Vertex Vector_Div(Vertex v1, float k) {
		return new Vertex(v1.x / k, v1.y / k, v1.z / k);
	}

	static public float Vector_DotProduct(Vertex v1, Vertex v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	static public float Vector_Length(Vertex v) {
		return (float) Math.sqrt(Vector_DotProduct(v, v));
	}

	static public Vertex Vector_Normalise(Vertex v) {
		float l = Vector_Length(v);
		return new Vertex(v.x / l, v.y / l, v.z / l);
	}

	static public Vertex Vector_CrossProduct(Vertex v1, Vertex v2) {
		Vertex v = new Vertex();
		v.x = v1.y * v2.z - v1.z * v2.y;
		v.y = v1.z * v2.x - v1.x * v2.z;
		v.z = v1.x * v2.y - v1.y * v2.x;
		return v;
	}
}