package com.game.model;

import android.graphics.Point;


public class Scrollable {

	protected Point position;
	protected int velocity;
	protected int width;
	protected int height;
	protected boolean isScrolledLeft;

	public Scrollable(int x, int y, int width, int height, int scrollSpeed) {
		position = new Point(x, y);
		velocity = scrollSpeed;
		this.width = width;
		this.height = height;
		isScrolledLeft = false;
	}

	public void update(float delta) {
		position.x-=velocity;
		// If the Scrollable object is no longer visible:
		if (position.x + width <= 0) {
			isScrolledLeft = true;
		}
	}

	// Reset: Should Override in subclass for more specific behavior.
	public void reset(int newX) {
		position.x = newX;
		isScrolledLeft = false;
	}

	public void stop() {
		velocity= 0;
	}

	// Getters for instance variables
	public boolean isScrolledLeft() {
		return isScrolledLeft;
	}

	public int getTailX() {
		return position.x + width;
	}

	public int getX() {
		return position.x;
	}

	public int getY() {
		return position.y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
