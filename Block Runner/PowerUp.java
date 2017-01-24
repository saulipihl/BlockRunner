package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/**
 * Class that handles the power ups. This version only has one power up but more could be added quite easily.
 */

public class PowerUp {
    private float x;
    private float y;
    private float width;
    private float height;
    private String name;
    private Color color;

    public PowerUp (String power, float ax, float yy) {
        name = power;
        x = ax;
        y = yy;
        width = 0;
        height = 0;
        initialize(power);
    }

    // Every power up has different kind of appearance - this method initializes different types of power ups.
    private void initialize(String power) {
        if (power.equals("REVERSE")) {
            color = Color.ORANGE;
            width = Gdx.graphics.getWidth()*0.08f;
            height = Gdx.graphics.getHeight();
        }
    }

    // Called when character interacts with the power up.
    public void activate (String name, Chara charac) {
        if (name.equals("REVERSE")) {
            charac.setSpeed(charac.getSpeed()*(-1));
        }
    }

    // Getters and setters,
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }


}
