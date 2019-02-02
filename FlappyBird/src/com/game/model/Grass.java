package com.game.model;

public class Grass extends Scrollable {

    
    public Grass(int x, int y, int width, int height, int scrollSpeed) {
        super(x, y, width, height, scrollSpeed);
    }
    public void onRestart(float x, float scrollSpeed) {
        position.x = (int) x;
        velocity = (int) scrollSpeed;
    }

}

