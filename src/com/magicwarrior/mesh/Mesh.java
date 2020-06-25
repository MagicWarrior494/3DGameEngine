package com.magicwarrior.mesh;

import java.util.Vector;

public class Mesh {

	public Vector<Triangle> mesh = new Vector<Triangle>();
	//public Vector<Vertex> mesh = new Vector<Vertex>();
	
	public int getSize() {
		return mesh.size();
	}

}
