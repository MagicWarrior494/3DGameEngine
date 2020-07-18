package com.magicwarrior.mesh;

import java.util.Vector;

public class Mesh {

	public Vector<Triangle> mesh = new Vector<Triangle>();
	
	public Mesh(Triangle t1, Triangle t2) {
		mesh.add(t1);
		mesh.add(t2);
	}
	public Mesh() {
		
	}

	public int getSize() {
		return mesh.size();
	}

}
