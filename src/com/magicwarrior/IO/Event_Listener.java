package com.magicwarrior.IO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Event_Listener implements KeyListener, MouseMotionListener {

	private static boolean[] keys = new boolean[256];
	public static boolean forward, backwards, left, right, shift, space, z, x, c, j;
	public static float Yaw, xMotion;

	private static void updateKeys() {
		forward = keys[KeyEvent.VK_W];
		backwards = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_SPACE];
		shift = keys[KeyEvent.VK_SHIFT];
		z = keys[KeyEvent.VK_Z];
		x = keys[KeyEvent.VK_X];
		c = keys[KeyEvent.VK_C];
		j = keys[KeyEvent.VK_J];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		updateKeys();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		updateKeys();

	}

	@Override
	public void mouseDragged(MouseEvent e) {
//		System.out.println(e.getX());
		xMotion = Yaw - e.getX();
		Yaw = e.getX();
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
