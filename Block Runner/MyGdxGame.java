package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This class starts up the game.
 */

public class MyGdxGame extends Game {
	public SpriteBatch batch;

    //Create method sets the main menu as the screen. First screen that players see and can interact with.
	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new MainMenu(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}

}