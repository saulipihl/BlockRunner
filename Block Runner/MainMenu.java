package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Main menu, where player can check the rules for the game and choose the level wanted to be played.
 */

public class MainMenu implements Screen {
    private MyGdxGame game;
    private Stage stage;
    private UiElements ui;

    private Button levelSelection;
    private Button rulesButton;

    private LevelButtonHandler handler;

    private Texture menuText;
    private Texture rules;
    private Image backButton;

    private boolean rulesOn;
    private boolean backOn;


    public MainMenu(final MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        ui = new UiElements();

        handler = new LevelButtonHandler(stage, game);

        rulesOn = false;
        backOn = false;

        menuText = new Texture("menu.png");
        rules = new Texture("ohjeet.png");
        Texture backBut = new Texture("backButton.jpg");
        backButton = new Image(backBut);
        backButton.setX(Gdx.graphics.getWidth()*0.9f);
        backButton.setY(Gdx.graphics.getHeight()*0.85f);
        backButton.setWidth(Gdx.graphics.getHeight() * 0.14f);
        backButton.setHeight(Gdx.graphics.getHeight() * 0.14f);
        backButton.setVisible(false);

        levelSelection = ui.getTextButton(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() * 0.5f, Gdx.graphics.getWidth() * 0.48f, Gdx.graphics.getHeight() * 0.14f, "LEVEL SELECTION");
        rulesButton = ui.getTextButton(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() * 0.3f, Gdx.graphics.getWidth() * 0.48f, Gdx.graphics.getHeight() * 0.14f, "RULES");

        stage.addActor(levelSelection);
        stage.addActor(rulesButton);
        stage.addActor(backButton);


        //Listeners for the different buttons.
        rulesButton.addListener(new ClickListener() {
                                    public void clicked(InputEvent event, float x, float y) {
                                        levelSelection.setDisabled(true);
                                        levelSelection.setVisible(false);
                                        rulesButton.setDisabled(true);
                                        rulesButton.setVisible(false);
                                        rulesOn = true;
                                        backOn = true;
                                        backButton.setVisible(true);
                                    }
                                }
        );


        levelSelection.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                levelSelection.setDisabled(true);
                levelSelection.setVisible(false);
                rulesButton.setDisabled(true);
                rulesButton.setVisible(false);
                handler.showLevelButtons();
                backButton.setVisible(true);
                backOn = true;
                }
            }
        );

        backButton.addListener(new ClickListener() {
                                       public void clicked(InputEvent event, float x, float y) {
                                           game.setScreen(new MainMenu(game));
                                           dispose();
                                           return;
                                       }
                                   }
        );


    }

    //Things to be rendered such as drawing of textures.
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(menuText, Gdx.graphics.getWidth()*0.15f, Gdx.graphics.getHeight()*0.68f,Gdx.graphics.getWidth()*0.7f, Gdx.graphics.getHeight()*0.3f);

        if (rulesOn) {
            game.batch.draw(rules,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        }

        if (backOn) {
            backButton.draw(game.batch,1);
        }
        game.batch.end();

        stage.act(delta);
        stage.draw();

    }

    public void dispose() {
        stage.dispose();
        menuText.dispose();
        rules.dispose();
        ui.dispose();
        game = null;

        handler.dispose();
        levelSelection.remove();
        rulesButton.remove();
        handler = null;
        backButton.remove();

    }

    public void show(){
    }

    public void hide() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void resize(int a, int b) {

    }
}

