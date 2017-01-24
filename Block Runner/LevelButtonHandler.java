package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * Handles the initialization of the level buttons in the main menu.
 */

public class LevelButtonHandler {
    private ArrayList<Button> levelButtons;
    private Stage stage;
    private MyGdxGame game;
    private Skin buttonSkin;
    private Pixmap pixmap;
    private BitmapFont bfont;

    public LevelButtonHandler(Stage stag, MyGdxGame gam) {

        stage = stag;
        game = gam;
        levelButtons = new ArrayList<Button>();
        initializeButtons();
    }

    //Buttons are initialized
    private void initializeButtons() {
        buttonSkin = new Skin();

        pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fill();
        buttonSkin.add("white", new Texture(pixmap));

        bfont=new BitmapFont();
        bfont.getData().scale(1);
        buttonSkin.add("default",bfont);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = buttonSkin.newDrawable("white", Color.ORANGE);

        textButtonStyle.font = buttonSkin.getFont("default");

        buttonSkin.add("default", textButtonStyle);
        for(int i = 1; i < 4; i++) {
            Button level = new Button();
            switch (i){
                case 1:
                    level = new TextButton("Level 1",textButtonStyle);
                    level.setBounds(Gdx.graphics.getWidth()*0.15f, Gdx.graphics.getHeight()*0.2f, Gdx.graphics.getWidth()*0.20f, Gdx.graphics.getWidth()*0.20f);

                    break;
                case 2:
                    level = new TextButton("Level 2",textButtonStyle);
                    level.setBounds(Gdx.graphics.getWidth()*0.40f, Gdx.graphics.getHeight()*0.2f, Gdx.graphics.getWidth()*0.20f,Gdx.graphics.getWidth()*0.20f);

                    break;
                case 3:
                    level = new TextButton("Level 3",textButtonStyle);
                    level.setBounds(Gdx.graphics.getWidth()*0.65f, Gdx.graphics.getHeight()*0.2f, Gdx.graphics.getWidth()*0.20f, Gdx.graphics.getWidth()*0.20f);
                    break;
            }
            final int tmp = i;

            //Click listener for the buttons -> Starts the game.
            level.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game, tmp));
                }
            }
            );
            level.setVisible(false);
            level.setDisabled(true);
            stage.addActor(level);
            levelButtons.add(level);

        }
    }

    //Level buttons will be visible and interactable.
    public void showLevelButtons() {
        for(int i = 0; i < 3; i++) {
            levelButtons.get(i).setVisible(true);
            levelButtons.get(i).setDisabled(false);
        }
    }

    public void dispose() {
        buttonSkin.dispose();
        pixmap.dispose();
        bfont.dispose();
    }
}
