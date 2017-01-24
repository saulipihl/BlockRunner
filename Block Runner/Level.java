package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;


/**
 * Class that generates the levels and holds the information regarding them.
 */

public class Level {
    private ArrayList<Rectangle> floorRectangles;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<Rectangle> upRectangles;

    private boolean hasUpRectangles;
    private boolean isLeftGoal;             // Determines if the goal is situated leftwards from the character's starting position.

    private Color floorColor;
    private Color goalColor;
    private Rectangle goalRectangle;
    private Rectangle bottomRectangle;
    private Chara chara;

    // Levels consist of rectangles, and they can be located at the bottom or top - hence the floor- and uprectangles.
    public Level(int lvl, Chara ch) {
        chara = ch;
        powerUps = new ArrayList<PowerUp>();
        floorRectangles = new ArrayList<Rectangle>();
        upRectangles = new ArrayList<Rectangle>();
        generateLevel(lvl);
        floorColor = Color.GRAY;
        goalColor = Color.PINK;
    }

    // Generates the level based on the level number the player has chosen.
    private void generateLevel(int lvlNumber) {
        Rectangle rectangle = new Rectangle(-chara.getWidth(),0,chara.getWidth()*10,chara.getHeight());
        floorRectangles.add(rectangle);

        // Variable was used, because I thought it was needed for different sorts of screen resolution scales. Useless but works.
        float scaleVariable = chara.getWidth();
        switch (lvlNumber) {
            case 1:
                isLeftGoal = false;
                hasUpRectangles = false;
                floorRectangles.add(new Rectangle(scaleVariable*13.5f,0,scaleVariable*9.5f,scaleVariable));
                floorRectangles.add(new Rectangle(scaleVariable*26.5f,0,scaleVariable*5,scaleVariable));
                floorRectangles.add(new Rectangle(scaleVariable*34,0,scaleVariable*3,scaleVariable));
                floorRectangles.add(new Rectangle(scaleVariable*38.5f,0,scaleVariable*4,scaleVariable*2.5f));
                floorRectangles.add(new Rectangle(scaleVariable*45,0,scaleVariable*4,scaleVariable*4));
                floorRectangles.add(new Rectangle(scaleVariable*55,0,scaleVariable*6,scaleVariable));
                goalRectangle = new Rectangle(scaleVariable*60,scaleVariable,scaleVariable , Gdx.graphics.getHeight());
                bottomRectangle = new Rectangle(-scaleVariable*10,-scaleVariable*11,goalRectangle.getX()+scaleVariable*21,scaleVariable*11f);
                break;
            case 2:
                isLeftGoal = false;
                hasUpRectangles = true;
                floorRectangles.add(new Rectangle(scaleVariable*12.5f,0,scaleVariable*4,scaleVariable*2.5f));
                floorRectangles.add(new Rectangle(scaleVariable*19,0,scaleVariable*3,scaleVariable*4.5f));
                floorRectangles.add(new Rectangle(scaleVariable*24.5f,0,scaleVariable*4,scaleVariable*5.5f));
                floorRectangles.add(new Rectangle(scaleVariable*28.5f,0,scaleVariable,scaleVariable*6));
                upRectangles.add(new Rectangle(scaleVariable*28.5f,scaleVariable*8.6f,scaleVariable,scaleVariable*7.2f));
                floorRectangles.add(new Rectangle(scaleVariable*29.5f,0,scaleVariable*5,scaleVariable*5.5f));
                floorRectangles.add(new Rectangle(scaleVariable*34.5f,0,scaleVariable,scaleVariable*7));
                upRectangles.add(new Rectangle(scaleVariable*34.5f,scaleVariable*9.8f,scaleVariable,scaleVariable*7.2f));
                floorRectangles.add(new Rectangle(scaleVariable*38f,0,scaleVariable*5,scaleVariable*4.5f));
                floorRectangles.add(new Rectangle(scaleVariable*45,0,scaleVariable*5,scaleVariable*6));
                floorRectangles.add(new Rectangle(scaleVariable*58,0,scaleVariable*2,scaleVariable));
                goalRectangle = new Rectangle(scaleVariable*59,scaleVariable,scaleVariable, Gdx.graphics.getHeight());
                bottomRectangle = new Rectangle(-scaleVariable*10,-scaleVariable*11,goalRectangle.getX()+scaleVariable*21,scaleVariable*11);
                break;
            case 3:
                isLeftGoal = true;
                hasUpRectangles = true;
                floorRectangles.add(new Rectangle(scaleVariable*12.5f,0,scaleVariable*4,scaleVariable*2.5f));
                floorRectangles.add(new Rectangle(scaleVariable*21,0,scaleVariable*4,scaleVariable*2.5f));
                floorRectangles.add(new Rectangle(scaleVariable*28,0,scaleVariable*4,scaleVariable*5));
                floorRectangles.add(new Rectangle(scaleVariable*35,0,scaleVariable*4,scaleVariable*7.5f));
                floorRectangles.add(new Rectangle(scaleVariable*42,0,scaleVariable*2,scaleVariable*10));
                floorRectangles.add(new Rectangle(scaleVariable*47,0,scaleVariable*2,scaleVariable*12.5f));
                floorRectangles.add(new Rectangle(scaleVariable*51,0,scaleVariable*2,scaleVariable*10));
                floorRectangles.add(new Rectangle(scaleVariable*55,0,scaleVariable*2,scaleVariable*7.5f));
                floorRectangles.add(new Rectangle(scaleVariable*59,0,scaleVariable*2,scaleVariable*5));
                floorRectangles.add(new Rectangle(scaleVariable*64,0,scaleVariable*8,scaleVariable*5));
                powerUps.add(new PowerUp("REVERSE",scaleVariable*71,scaleVariable*5));
                goalRectangle = new Rectangle(-scaleVariable,scaleVariable,scaleVariable , Gdx.graphics.getHeight());
                bottomRectangle = new Rectangle(-scaleVariable*10,-scaleVariable*11,floorRectangles.get(floorRectangles.size()-1).getX()+floorRectangles.get(floorRectangles.size()-1).getWidth()+scaleVariable*20,scaleVariable*11);
        }
    }

    //Getters and setters

    public ArrayList<Rectangle> getFloorRectangles() {
        return floorRectangles;
    }
    public Color getFloorColor() {
        return floorColor;
    }

    public Color getGoalColor() {
        return goalColor;
    }

    public Rectangle getGoalRectangle() {
        return goalRectangle;
    }

    public boolean hasUpRectangles() {
        return hasUpRectangles;
    }

    public ArrayList<Rectangle> getUpRectangles() {
        return upRectangles;
    }

    public Rectangle getBottomRectangle() {
        return bottomRectangle;
    }

    public boolean isLeftGoal() {
        return isLeftGoal;
    }

    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

}
