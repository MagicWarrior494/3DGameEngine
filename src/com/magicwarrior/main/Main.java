package com.magicwarrior.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.magicwarrior.IO.Event_Listener;
import com.magicwarrior.IO.FileReader;
import com.magicwarrior.matrix.MatrixMath;
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
	private FileReader fileReader;
	private DotProduct dotProduct;
	private Event_Listener eventListener;
	private boolean running = false;

	public static float xChange = 0, yChange = 0;
	public static float currentX = 0, currentY = 0;

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
	static public int windowWidth, windowHeight;
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
		float[][] trans = { { 1, 0, 0, x }, { 0, 1, 0, y }, { 0, 0, 1, z }, { 0, 0, 0, 1 } };
		return trans;
	}

	public void init() {
		eventListener = new Event_Listener();
		display = new Display(title, windowWidth, windowHeight);
		dotProduct = new DotProduct(windowWidth, windowHeight);
		fileReader = new FileReader();
		Path myObj = Paths.get("C:\\Users\\MagicWarrior\\Desktop\\OBJ", "teapot.obj");
		fileReader.readFile(myObj);

		display.getCanves().addMouseListener(eventListener);
		display.getCanves().addMouseMotionListener(eventListener);
		cube = fileReader.getMesh();

	}

	public void drawVertex(Vertex vertex) {
		g.fillOval((int) vertex.x, (int) vertex.y, 5, 5);
	}

	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	public void drawTriangle(Triangle triangle) {
		Vertex v1 = triangle.getVertex(0);
		Vertex v2 = triangle.getVertex(1);
		Vertex v3 = triangle.getVertex(2);

		g.setColor(Color.BLACK);
		drawLine(v1.x, v1.y, v2.x, v2.y);
		drawLine(v2.x, v2.y, v3.x, v3.y);
		drawLine(v3.x, v3.y, v1.x, v1.y);

		int[] xPoints = { (int) v1.x, (int) v2.x, (int) v3.x };
		int[] yPoints = { (int) v1.y, (int) v2.y, (int) v3.y };

		//g.setColor(triangle.color);

		//g.fillPolygon(xPoints, yPoints, 3);

	}

	public void render() {

		bs = display.getCanves().getBufferStrategy();
		if (bs == null) {
			display.getCanves().createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();
		g.clearRect(0, 0, windowWidth, windowHeight);

		List<Triangle> triangleRaster = new ArrayList<>();

		TreeMap<Float, Triangle> triangleRasterTree = new TreeMap<>();

		for (Triangle triangle : cube.mesh) {
			Triangle projectedTriangle = new Triangle();
			Triangle scaledTriangle = new Triangle();
			Triangle translatedTriangle = new Triangle();

			translatedTriangle
					.setVertex(MatrixMath.worldUpdate(triangle.getVertex(0), angle1, angle2, angle3, 80), 0);
			translatedTriangle
					.setVertex(MatrixMath.worldUpdate(triangle.getVertex(1), angle1, angle2, angle3, 80), 1);
			translatedTriangle
					.setVertex(MatrixMath.worldUpdate(triangle.getVertex(2), angle1, angle2, angle3, 80), 2);

			Vertex normal = new Vertex();
			Vertex line1 = new Vertex();
			Vertex line2 = new Vertex();

			line1 = MatrixMath.subtractVector(translatedTriangle.getVertex(1), translatedTriangle.getVertex(0));
			line2 = MatrixMath.subtractVector(translatedTriangle.getVertex(2), translatedTriangle.getVertex(0));

			normal.x = line1.y * line2.z - line1.z * line2.y;
			normal.y = line1.z * line2.x - line1.x * line2.z;
			normal.z = line1.x * line2.y - line1.y * line2.x;

			float l = (float) Math.sqrt(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z);

			normal.x /= l;
			normal.y /= l;
			normal.z /= l;

			if (normal.x * (translatedTriangle.getVertex(0).x - camera.x)
					+ normal.y * (translatedTriangle.getVertex(0).y - camera.y)
					+ normal.z * (translatedTriangle.getVertex(0).z - camera.z) < 0.0f) {

				Vertex light_direction = new Vertex(0.0f, 0.0f, -1.0f);
				float b = (float) Math.sqrt(light_direction.x * light_direction.x
						+ light_direction.y * light_direction.y + light_direction.z * light_direction.z);

				light_direction.x /= b;
				light_direction.y /= b;
				light_direction.z /= b;

				float dp = normal.x * light_direction.x + normal.y * light_direction.y + normal.z * light_direction.z;

				projectedTriangle.setVertex(
						dotProduct.DotMultiplication(translatedTriangle.getVertex(0), MatrixMath.project()), 0);
				projectedTriangle.setVertex(
						dotProduct.DotMultiplication(translatedTriangle.getVertex(1), MatrixMath.project()), 1);
				projectedTriangle.setVertex(
						dotProduct.DotMultiplication(translatedTriangle.getVertex(2), MatrixMath.project()), 2);

				scaledTriangle.setVertex(MatrixMath.ProjectedUpdate(projectedTriangle.getVertex(0)), 0);
				scaledTriangle.setVertex(MatrixMath.ProjectedUpdate(projectedTriangle.getVertex(1)), 1);
				scaledTriangle.setVertex(MatrixMath.ProjectedUpdate(projectedTriangle.getVertex(2)), 2);

				if (dp > 1) {
					dp = 1;
				} else if (dp < 0) {
					dp = 0;
				}

				scaledTriangle.color = new Color(dp, dp, dp);
				float average = (translatedTriangle.getVertex(0).z + translatedTriangle.getVertex(1).z
						+ translatedTriangle.getVertex(2).z) / 3.0f;
				triangleRasterTree.put(average, scaledTriangle);
				//triangleRaster.add(scaledTriangle);
			}
		}

		int drawingCounter = triangleRasterTree.size();
		for (int i = 0; i < drawingCounter; i++) {
			drawTriangle(triangleRasterTree.pollLastEntry().getValue());
			//drawTriangle(triangleRaster.get(i));
		}

		left = false;
		angle1 += 0.001;
		angle2 += 0.025;
		angle3 += 0.0075;

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
				System.out.println("Running at: " + ticks + "Ticks per second");
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
