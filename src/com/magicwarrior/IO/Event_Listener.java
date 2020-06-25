package com.magicwarrior.IO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Event_Listener implements KeyListener {
	
	private static boolean[] keys = new boolean[256];
	public static boolean forward, backwards, left, right, shift, space;

	private static void updateKeys() {
		forward = keys[KeyEvent.VK_W];
		backwards = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_SPACE];
		shift = keys[KeyEvent.VK_SHIFT];
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
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
