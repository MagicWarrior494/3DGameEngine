package com.magicwarrior.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;

import com.magicwarrior.IO.Event_Listener;
import com.magicwarrior.IO.FileReader;
import com.magicwarrior.matrix.MatrixMath;
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
	private Event_Listener eventListener;
	private boolean running = false;

	public static float xChange = 0, yChange = 0;
	public static float currentX = 0, currentY = 0;

	float angle1 = 0;
	float angle2 = 0;
	float angle3 = 180 * ((float) Math.PI / 180);

	float fYaw;

	int scaledNumber = 0;
	int translate = 0;
	boolean pressedKey = false;
	float far = 100f;
	float near = 0.1f;
	boolean flipped = false;
	float FOV = 90.0f;
	Cloner clone = new Cloner();
	boolean left = true;

	private String title;
	static public int windowWidth, windowHeight;
	float aspectRatio = 1.2f;
	int tick = 0;

	Vertex vCamera = new Vertex();
	Vertex vUp, vTarget, vLookDir = new Vertex(), vCameraRay, light_direction, vOffsetView, vForward;

	float[][] matRotZ = new float[4][4], matRotX = new float[4][4], matRotY = new float[4][4],
			matTrans = new float[4][4], matWorld = new float[4][4], matCamera = new float[4][4],
			matView = new float[4][4], matProj, matCameraRot = new float[4][4];

	float fTheta = 0.0f;

	public Main(String title, int windowWidth, int windowHeight) {
		this.title = title;
		Main.windowHeight = windowHeight;
		Main.windowWidth = windowWidth;
	}

	public void init() {
		eventListener = new Event_Listener();
		display = new Display(title, windowWidth, windowHeight);
		fileReader = new FileReader();
		Path myObj = Paths.get("C:\\Users\\MagicWarrior\\Desktop\\OBJ", "untitled.obj");
		fileReader.readFile(myObj);

		display.getFrame().addKeyListener(eventListener);
		display.getCanves().addMouseMotionListener(eventListener);
		cube = fileReader.getMesh();

	}

	public void drawVertex(Vertex vertex) {
		g.setColor(Color.PINK);
		vOffsetView = new Vertex(1, 1, 0);
		
		vertex = MatrixMath.Matrix_MultiplyVector(matWorld, vertex);
		vertex = MatrixMath.Matrix_MultiplyVector(matView, vertex);
		vertex = MatrixMath.Matrix_MultiplyVector(matProj, vertex);
		vertex = MatrixMath.Vector_Div(vertex, vertex.w);
		vertex = MatrixMath.Vector_Add(vertex, vOffsetView);
		vertex.x *= 0.5f * (float) windowWidth;
		vertex.y *= 0.5f * (float) windowHeight;
		
		g.fillOval((int) vertex.x, (int) vertex.y, 5, 5);
		System.out.println("Drawing dot at: " + vertex.x + " " + vertex.y);
	}

	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	public void drawTriangle(Triangle triangle) {
		Vertex v1 = triangle.getVertex(0);
		Vertex v2 = triangle.getVertex(1);
		Vertex v3 = triangle.getVertex(2);

//		g.setColor(Color.BLACK);
//		drawLine(v1.x, v1.y, v2.x, v2.y);
//		drawLine(v2.x, v2.y, v3.x, v3.y);
//		drawLine(v3.x, v3.y, v1.x, v1.y);

		int[] xPoints = { (int) v1.x, (int) v2.x, (int) v3.x };
		int[] yPoints = { (int) v1.y, (int) v2.y, (int) v3.y };

		g.setColor(triangle.color);

		g.fillPolygon(xPoints, yPoints, 3);

	}

	public void render() {

		bs = display.getCanves().getBufferStrategy();
		if (bs == null) {
			display.getCanves().createBufferStrategy(3);
			return;
		}

		matProj = MatrixMath.Matrix_MakeProjection(90.0f, windowWidth / windowHeight, 0.1f, 1000.0f);

		matRotZ = MatrixMath.Matrix_MakeRotationZ(angle3);
		matRotX = MatrixMath.Matrix_MakeRotationX(angle1);

		matTrans = MatrixMath.Matrix_MakeTranslation(0.0f, 0.0f, 4.0f);

		matWorld = MatrixMath.Matrix_MakeIdentity();
		matWorld = MatrixMath.Matrix_MultiplyMatrix(matRotZ, matRotX);
		matWorld = MatrixMath.Matrix_MultiplyMatrix(matWorld, matTrans);

		vUp = new Vertex(0, 1, 0);
		vTarget = new Vertex(0, 0, 1);
		matCameraRot = MatrixMath.Matrix_MakeRotationY(fYaw);
		vLookDir = MatrixMath.Matrix_MultiplyVector(matCameraRot, vTarget);
		vTarget = MatrixMath.Vector_Add(vCamera, vLookDir);

		matCamera = MatrixMath.Matrix_PointAt(vCamera, vTarget, vUp);

		// Make view matrix from camera
		matView = MatrixMath.Matrix_QuickInverse(matCamera);

		TreeMap<Float, Triangle> triangleRasterTree = new TreeMap<>();

		for (Triangle tri : cube.mesh) {
			Triangle triProjected = new Triangle();
			Triangle triTransformed = new Triangle();
			Triangle triViewed = new Triangle();

			triTransformed.setVertex(MatrixMath.Matrix_MultiplyVector(matWorld, tri.getVertex(0)), 0);
			triTransformed.setVertex(MatrixMath.Matrix_MultiplyVector(matWorld, tri.getVertex(1)), 1);
			triTransformed.setVertex(MatrixMath.Matrix_MultiplyVector(matWorld, tri.getVertex(2)), 2);

			Vertex normal = new Vertex();
			Vertex line1 = new Vertex();
			Vertex line2 = new Vertex();

			line1 = MatrixMath.Vector_Sub(triTransformed.getVertex(1), triTransformed.getVertex(0));
			line2 = MatrixMath.Vector_Sub(triTransformed.getVertex(2), triTransformed.getVertex(0));

			normal = MatrixMath.Vector_CrossProduct(line1, line2);
			normal = MatrixMath.Vector_Normalise(normal);

			vCameraRay = MatrixMath.Vector_Sub(triTransformed.getVertex(0), vCamera);

			if (MatrixMath.Vector_DotProduct(normal, vCameraRay) < 0.0f) {
//				light_direction = new Vertex((float)Math.cos(fYaw), vCamera.y, (float)Math.sin(fYaw));
				light_direction = new Vertex(0,0,-1);

				light_direction = MatrixMath.Vector_Normalise(light_direction);

				float dp = Math.max(0.1f, MatrixMath.Vector_DotProduct(light_direction, normal));

				triTransformed.color = new Color(dp, dp, dp);

				triViewed.setVertex(MatrixMath.Matrix_MultiplyVector(matView, triTransformed.getVertex(0)), 0);
				triViewed.setVertex(MatrixMath.Matrix_MultiplyVector(matView, triTransformed.getVertex(1)), 1);
				triViewed.setVertex(MatrixMath.Matrix_MultiplyVector(matView, triTransformed.getVertex(2)), 2);
				triViewed.color = triTransformed.color;

				Triangle newTri1 = new Triangle();
				Triangle newTri2 = new Triangle();
				int loopCounter = MatrixMath.Triangle_ClipAgainstPlane(new Vertex(0.0f, 0.0f, 0.2f),
						new Vertex(0.0f, 0.0f, 1.0f), triViewed, newTri1, newTri2, pressedKey);

				Mesh newTriangles = new Mesh();
				newTriangles.mesh.add(newTri1);
				newTriangles.mesh.add(newTri2);

				for (int n = 0; n < loopCounter; n++) {
					Triangle testTri = new Triangle();

					triProjected.setVertex(
							MatrixMath.Matrix_MultiplyVector(matProj, newTriangles.mesh.get(n).getVertex(0)), 0);
					triProjected.setVertex(
							MatrixMath.Matrix_MultiplyVector(matProj, newTriangles.mesh.get(n).getVertex(1)), 1);
					triProjected.setVertex(
							MatrixMath.Matrix_MultiplyVector(matProj, newTriangles.mesh.get(n).getVertex(2)), 2);
					triProjected.color = newTriangles.mesh.get(n).color;

					triProjected.setVertex(
							MatrixMath.Vector_Div(triProjected.getVertex(0), triProjected.getVertex(0).w), 0);
					triProjected.setVertex(
							MatrixMath.Vector_Div(triProjected.getVertex(1), triProjected.getVertex(1).w), 1);
					triProjected.setVertex(
							MatrixMath.Vector_Div(triProjected.getVertex(2), triProjected.getVertex(2).w), 2);

					vOffsetView = new Vertex(1, 1, 0);
					triProjected.setVertex(MatrixMath.Vector_Add(triProjected.getVertex(0), vOffsetView), 0);
					triProjected.setVertex(MatrixMath.Vector_Add(triProjected.getVertex(1), vOffsetView), 1);
					triProjected.setVertex(MatrixMath.Vector_Add(triProjected.getVertex(2), vOffsetView), 2);

					triProjected.getVertex(0).x *= 0.5f * (float) windowWidth;
					triProjected.getVertex(0).y *= 0.5f * (float) windowHeight;
					triProjected.getVertex(1).x *= 0.5f * (float) windowWidth;
					triProjected.getVertex(1).y *= 0.5f * (float) windowHeight;
					triProjected.getVertex(2).x *= 0.5f * (float) windowWidth;
					triProjected.getVertex(2).y *= 0.5f * (float) windowHeight;

					float average = (triProjected.getVertex(0).z + triProjected.getVertex(1).z
							+ triProjected.getVertex(2).z) / 3.0f;

					while (triangleRasterTree.containsKey(average)) {
						average += 0.0005f;
					}

					testTri = clone.deepClone(triProjected);
					triangleRasterTree.put(average, testTri);

				}
			}
		}

		int drawingCounter = triangleRasterTree.size();

		g = bs.getDrawGraphics();
		g.clearRect(0, 0, windowWidth, windowHeight);

		for (int i = 0; i < drawingCounter; i++) {
			drawTriangle(triangleRasterTree.pollLastEntry().getValue());
		}
//		drawVertex(light_direction);

		left = false;
//		angle1 += 0.01;
//		angle2 += 0.025;
//		angle3 += 0.0375;

		bs.show();
		g.dispose();
	}

	public void update() {

		vForward = MatrixMath.Vector_Mul(vLookDir, 0.1f);

		if (Event_Listener.forward) {
			vCamera = MatrixMath.Vector_Add(vCamera, vForward);
		} else if (Event_Listener.backwards) {
			vCamera = MatrixMath.Vector_Sub(vCamera, vForward);
		}
		if (Event_Listener.left) {
			fYaw += Math.PI/64;
			System.out.println(fYaw);
		} else if (Event_Listener.right) {
			fYaw -= Math.PI/64;
			System.out.println(fYaw);
		}
		if (Event_Listener.shift) {
			vCamera.y += 0.1f;
		} else if (Event_Listener.space) {
			vCamera.y -= 0.1f;
		}
		if (Event_Listener.x) {
			vCamera = MatrixMath.Vector_Add(vCamera, MatrixMath.Vector_CrossProduct(vUp, vForward));
		} else if (Event_Listener.z) {
			vCamera = MatrixMath.Vector_Sub(vCamera, MatrixMath.Vector_CrossProduct(vUp, vForward));
		}
		if (Event_Listener.j) {
			pressedKey = !pressedKey;
			System.out.println(pressedKey);
		}
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
				update();
				/////////////////////////////////////
				delta--;
				ticks++;
			}
			if (timer >= 1000000000) {
				timer = 0;
//				System.out.println("Running at: " + ticks + "Ticks per second");
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
		thread = new Thread(this, "3dGameEngine");
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