package com.magicwarrior.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.magicwarrior.matrixMultiplication.DotProduct;
import com.magicwarrior.mesh.Mesh;
import com.magicwarrior.mesh.Triangle;
import com.magicwarrior.mesh.Vertex;
import com.rits.cloning.Cloner;

public class Main implements Runnable {

	private Graphics g;
	private BufferStrategy bs;

	private Display display;
	private Thread thread;
	private Mesh cube = new Mesh();
	private DotProduct dotProduct;
	private boolean running = false;
	double angle1 = 0;
	double angle2 = 0;
	double angle3 = 0;
	int scaledNumber = 0;
	int translate = 0;

	float far = 100f;
	float near = 0.1f;

	float FOV = 90.0f;

	boolean left = true;

	Cloner clone;
	private String title;
	private int windowWidth, windowHeight;
	float aspectRatio = 1.2f;
	int tick = 0;

	Vertex camera = new Vertex();

	float fTheta = 0.0f;

	public Main(String title, int windowWidth, int windowHeight) {
		this.title = title;
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
	}

	public float[][] rotationZ(double angle) {
		float[][] rotation = { { (float) Math.cos(angle), (float) -Math.sin(angle), 0, 0 },
				{ (float) Math.sin(angle), (float) Math.cos(angle), 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 } };
		return rotation;
	}

	public float[][] rotationX(double angle) {
		float[][] rotation = { { 1, 0, 0, 0 }, { 0, (float) Math.cos(angle), (float) -Math.sin(angle), 0 },
				{ 0, (float) Math.sin(angle), (float) Math.cos(angle), 0 }, { 0, 0, 0, 0 } };
		return rotation;
	}

	public float[][] rotationY(double angle) {
		float[][] rotation = { { (float) Math.cos(angle), 0, (float) -Math.sin(angle), 0 }, { 0, 1, 0, 0 },
				{ (float) Math.sin(angle), 0, (float) Math.cos(angle), 0 }, { 0, 0, 0, 0 } };
		return rotation;
	}

	public float[][] translate(int x, int y, int z) {
		float[][] trans = { { 1, 0, 0, x }, { 0, 1, 0, y }, { 0, 0, 1, z }, { 0, 0, 0, 0 } };
		return trans;
	}

	float[][] projectionMatrix = new float[4][4];

	public void init() {
		clone = new Cloner();
		display = new Display(title, windowWidth, windowHeight);
		dotProduct = new DotProduct(windowWidth, windowHeight);

		{
			// SOUTH
			cube.mesh.add(new Triangle(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f));

			cube.mesh.add(new Triangle(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f));

			// EAST
			cube.mesh.add(new Triangle(1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f));
			cube.mesh.add(new Triangle(1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f));

			// NORTH
			cube.mesh.add(new Triangle(1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f));
			cube.mesh.add(new Triangle(1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f));

			// WEST
			cube.mesh.add(new Triangle(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f));
			cube.mesh.add(new Triangle(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f));

			// TOP
			cube.mesh.add(new Triangle(0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f));
			cube.mesh.add(new Triangle(0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f));

			// BOTTOM
			cube.mesh.add(new Triangle(1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f));
			cube.mesh.add(new Triangle(1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));

		}
		;

//		// SOUTH
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 0.0f));
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 0.0f));
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 0.0f));
//
//		// EAST
//
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 1.0f));
//
//		// NORTH
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 1.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 1.0f));
//
//		// WEST
//
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 0.0f));
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 0.0f));
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 0.0f));
//
//		// TOP
//
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 0.0f));
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 1.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 1.0f));
//		cube.mesh.add(new Vertex(1.0f, 1.0f, 0.0f));
//
//		// BOTTOM
//
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 1.0f));
//		cube.mesh.add(new Vertex(0.0f, 0.0f, 0.0f));
//		cube.mesh.add(new Vertex(1.0f, 0.0f, 0.0f));

		float fNear = 0.1f;
		float fFar = 1000.0f;
		float fFov = 90.0f;
		float fAspectRatio = (float) windowHeight / (float) windowWidth;
		float fFovRad = (float) (1.0f / Math.tan(fFov * 0.5f / 180.0f * Math.PI));

		projectionMatrix[0][0] = fAspectRatio * fFovRad;
		projectionMatrix[1][1] = fFovRad;
		projectionMatrix[2][2] = fFar / (fFar - fNear);
		projectionMatrix[3][2] = (-fFar * fNear) / (fFar - fNear);
		projectionMatrix[2][3] = 1.0f;
		projectionMatrix[3][3] = 0.0f;

		for (Triangle triangle : cube.mesh) {
			triangle.getVertex(0).x -= 0.5;
			triangle.getVertex(0).y -= 0.5;
			triangle.getVertex(0).z -= 0.5;
			triangle.getVertex(1).x -= 0.5;
			triangle.getVertex(1).y -= 0.5;
			triangle.getVertex(1).z -= 0.5;
			triangle.getVertex(2).x -= 0.5;
			triangle.getVertex(2).y -= 0.5;
			triangle.getVertex(2).z -= 0.5;
		}

//		for (Vertex vertex : cube.mesh) {
//			vertex.x -= 0.5;
//			vertex.y -= 0.5;
//			vertex.z -= 0.5;
//		}

	}

	public void drawVertex(Vertex vertex) {
		g.fillOval((int) vertex.x, (int) vertex.y, 5, 5);
	}

	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	public void drawTriangle(Triangle triangle, int value) {
		Vertex v1 = triangle.getVertex(0);
		Vertex v2 = triangle.getVertex(1);
		Vertex v3 = triangle.getVertex(2);

		drawLine(v1.x, v1.y, v2.x, v2.y);
		drawLine(v2.x, v2.y, v3.x, v3.y);
		drawLine(v3.x, v3.y, v1.x, v1.y);

		int[] xPoints = { (int) v1.x, (int) v2.x, (int) v3.x };
		int[] yPoints = { (int) v1.y, (int) v2.y, (int) v3.y };
		switch (value) {
		case (0):
			g.setColor(Color.RED);
			break;
		case (1):
			g.setColor(Color.BLUE);
			break;
		case (2):
			g.setColor(Color.YELLOW);
			break;
		case (3):
			g.setColor(Color.GREEN);
			break;
		case (4):
			g.setColor(Color.GRAY);
			break;
		case (5):
			g.setColor(Color.ORANGE);
			break;
		case (6):
			g.setColor(Color.CYAN);
			break;
		default:
			g.setColor(Color.PINK);
			break;
		}
		g.fillPolygon(xPoints, yPoints, 3);

	}

	public void render() {

		bs = display.getCanves().getBufferStrategy();
		if (bs == null) {
			display.getCanves().createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();
		g.clearRect(0, 0, windowWidth, windowHeight);

		int counter = 0;
		for (Triangle triangle : cube.mesh) {
			Triangle rotationalTriangle = new Triangle();
			Triangle projectedTriangle = new Triangle();
			Triangle scaledTriangle = new Triangle();
			Triangle translatedTriangle = new Triangle();

			rotationalTriangle.setVertex(dotProduct.DotMultiplication(triangle.getVertex(0), rotationX(angle1)), 0);
			rotationalTriangle.setVertex(dotProduct.DotMultiplication(triangle.getVertex(1), rotationX(angle1)), 1);
			rotationalTriangle.setVertex(dotProduct.DotMultiplication(triangle.getVertex(2), rotationX(angle1)), 2);

			rotationalTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(0), rotationY(angle2)), 0);
			rotationalTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(1), rotationY(angle2)), 1);
			rotationalTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(2), rotationY(angle2)), 2);

			rotationalTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(0), rotationZ(angle3)), 0);
			rotationalTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(1), rotationZ(angle3)), 1);
			rotationalTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(2), rotationZ(angle3)), 2);

			translatedTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(0), translate(0, 0, 30)), 0);
			translatedTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(1), translate(0, 0, 30)), 1);
			translatedTriangle
					.setVertex(dotProduct.DotMultiplication(rotationalTriangle.getVertex(2), translate(0, 0, 30)), 2);

			Vertex normal = new Vertex();
			Vertex line1 = new Vertex();
			Vertex line2 = new Vertex();
			line1.x = translatedTriangle.getVertex(1).x - translatedTriangle.getVertex(0).x;
			line1.y = translatedTriangle.getVertex(1).y - translatedTriangle.getVertex(0).y;
			line1.z = translatedTriangle.getVertex(1).z - translatedTriangle.getVertex(0).z;

			line2.x = translatedTriangle.getVertex(2).x - translatedTriangle.getVertex(0).x;
			line2.y = translatedTriangle.getVertex(2).y - translatedTriangle.getVertex(0).y;
			line2.z = translatedTriangle.getVertex(2).z - translatedTriangle.getVertex(0).z;

			normal.x = line1.y * line2.z - line1.z * line2.y;
			normal.y = line1.z * line2.x - line1.x * line2.z;
			normal.z = line1.x * line2.y - line1.y * line2.x;

			float l = (float) Math.sqrt(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z);

			if (normal.x * (translatedTriangle.getVertex(0).x - camera.x)
					+ normal.y * (translatedTriangle.getVertex(0).y - camera.y)
					+ normal.z * (translatedTriangle.getVertex(0).z - camera.z) < 0.0) {

				projectedTriangle
						.setVertex(dotProduct.DotMultiplication(translatedTriangle.getVertex(0), projectionMatrix), 0);
				projectedTriangle
						.setVertex(dotProduct.DotMultiplication(translatedTriangle.getVertex(1), projectionMatrix), 1);
				projectedTriangle
						.setVertex(dotProduct.DotMultiplication(translatedTriangle.getVertex(2), projectionMatrix), 2);

				scaledTriangle.setVertex(dotProduct.AddX(projectedTriangle.getVertex(0), 1), 0);
				scaledTriangle.setVertex(dotProduct.AddX(projectedTriangle.getVertex(1), 1), 1);
				scaledTriangle.setVertex(dotProduct.AddX(projectedTriangle.getVertex(2), 1), 2);

				scaledTriangle.setVertex(dotProduct.AddY(scaledTriangle.getVertex(0), 1), 0);
				scaledTriangle.setVertex(dotProduct.AddY(scaledTriangle.getVertex(1), 1), 1);
				scaledTriangle.setVertex(dotProduct.AddY(scaledTriangle.getVertex(2), 1), 2);

				scaledTriangle.setVertex(dotProduct.MultX(scaledTriangle.getVertex(0), windowWidth / 2), 0);
				scaledTriangle.setVertex(dotProduct.MultX(scaledTriangle.getVertex(1), windowWidth / 2), 1);
				scaledTriangle.setVertex(dotProduct.MultX(scaledTriangle.getVertex(2), windowWidth / 2), 2);

				scaledTriangle.setVertex(dotProduct.MultY(scaledTriangle.getVertex(0), windowHeight / 2), 0);
				scaledTriangle.setVertex(dotProduct.MultY(scaledTriangle.getVertex(1), windowHeight / 2), 1);
				scaledTriangle.setVertex(dotProduct.MultY(scaledTriangle.getVertex(2), windowHeight / 2), 2);
				drawTriangle(scaledTriangle, counter);
			}
			counter++;
		}

//		for (Vertex vertex : cube.mesh) {
//			Vertex projectedVertex = new Vertex();
//			Vertex rotationalVertex = new Vertex();
//			Vertex translatedVertex = new Vertex();
//			Vertex scaledVertex = new Vertex();
//			
//			rotationalVertex = clone.deepClone(dotProduct.DotMultiplication(vertex, rotationX(angle1)));
//			rotationalVertex = clone.deepClone(dotProduct.DotMultiplication(rotationalVertex, rotationY(angle2)));
//			
//			translatedVertex = clone.deepClone(dotProduct.DotMultiplication(rotationalVertex, translate(0, 0, 30)));
//			
//			projectedVertex = clone.deepClone(dotProduct.DotMultiplication(translatedVertex, projectionMatrix));
//			
//			scaledVertex = clone.deepClone(dotProduct.AddX(projectedVertex, 1));
//			scaledVertex = clone.deepClone(dotProduct.AddY(scaledVertex, 1));
//			
//			scaledVertex = clone.deepClone(dotProduct.MultX(scaledVertex, windowWidth/2));
//			scaledVertex = clone.deepClone(dotProduct.MultY(scaledVertex, windowHeight/2));
//
//			drawVertex(scaledVertex);
//
//		}
		left = false;
		angle1 += 0.025;
		angle2 += 0.05;
		angle3 += 0.0375;
//		translate = 1000;
//		if (left) {
//			scaledNumber += 10;
//		} else if (!left) {
//			scaledNumber -= 10;
//		}
//
//		if (scaledNumber > 400) {
//			left = false;
//		} else if (scaledNumber < 25) {
//			left = true;
//		}
		bs.show();
		g.dispose();
	}

	public void update() {

	}

	public void startLoop() {

		int fps = 30;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		long ticks = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				/////////////////////////////////////

				render();

				/////////////////////////////////////
				delta--;
				ticks++;
			}
			if (timer >= 1000000000) {
				timer = 0;
				// System.out.println("Running at: " + ticks + "Ticks per second");
				ticks = 0;
			}
		}
	}

	@Override
	public void run() {
		init();
		startLoop();
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
