package com.magicwarrior.IO;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.magicwarrior.main.Main;

public class Event_Listener implements MouseMotionListener, MouseListener{
	
	private int clickedX, clickedY;
	private int currentX, currentY;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		currentX = e.getX();
		currentY = e.getY();
		
		Main.yChange = currentX - clickedX;
		Main.xChange = currentY - clickedY;
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		clickedX = e.getX();
		clickedY = e.getY();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Main.currentX += Main.xChange;
		Main.currentY += Main.yChange;
	}

}
