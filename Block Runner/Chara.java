package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class that describes the game character and its attributes.
 */
public class Chara {
    private float width;
    private float height;
    private float speed;
    private float ySpeed;
    private float x;
    private float y;

    private Rectangle rect;
    private Color color;

    private boolean isAllowedToFall;
    private boolean isFreeFalling;
    private boolean Crashed;
    private boolean isAllowedToJump;



    public Chara (float x, float y, float w, float h, float s) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        speed = s;
        ySpeed = -Gdx.graphics.getWidth()*0.004f;
        rect = new Rectangle(x,y,width,height);
        color = Color.TEAL;
        isAllowedToFall = true;
        isFreeFalling = false;
        Crashed = false;
        isAllowedToJump = false;
    }

    // After doing changes to a character's position, the character's rectangle needs to be updated.
    public void updateRect() {
        rect = new Rectangle(x,y,width,height);
    }


    // Getters and setters.
    public Color getColor() {
        return color;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean isAllowedToFall() {
        return isAllowedToFall;
    }

    public void setAllowedToFall(boolean allowedToFall) {
        isAllowedToFall = allowedToFall;
    }

    public boolean isFreeFalling() {
        return isFreeFalling;
    }

    public void setFreeFalling(boolean freeFalling) {
        this.isFreeFalling = freeFalling;
    }

    public boolean isCrashed() {
        return Crashed;
    }

    public void setCrashed(boolean crashed) {
        this.Crashed = crashed;
    }

    public float getYSpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public boolean isAllowedToJump() {
        return isAllowedToJump;
    }

    public void setAllowedToJump(boolean allowedToJump) {
        isAllowedToJump = allowedToJump;
    }
}
