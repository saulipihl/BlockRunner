package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;


/**
 * The class that handles the main game logic and acts as a screen for the gameplay.
 */

public class GameScreen implements Screen, GestureDetector.GestureListener, InputProcessor {
    MyGdxGame game;
    private Stage stage;
    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    private Level level;
    private Chara character;
    private gameState state;
    private UiElements ui;

    private long fallStart;             //Used to avoid colliding just after dropping from the edge.
    private long jumpStart;             //Used to determine the length of the jump.
    private long wonTimer;              //Used to determine the time until the screen changes to main menu screen after beating a level.

    private int hitPowerUpIndex;        //If level has multiple power ups, this determines which one is hit.
    private int lvlNumber;
    private final float scaleVariable;  //Used, because thought would help in scaling for different screen resolutions. Useless, but still works.

    private boolean jumping;
    private boolean notAtGoal;
    private boolean fallTimeNotStarted;

    private Button retryButton;
    private Button continueButton;
    private Button menuButton;

    private Texture paused;
    private Texture gameOverTexture;
    private Texture youWonTexture;
    private Image pauseButton;


    public GameScreen(final MyGdxGame game, int lvl) {
        this.game = game;
        lvlNumber = lvl;
        renderer = new ShapeRenderer();
        scaleVariable = Gdx.graphics.getWidth()*0.08f;
        character = new Chara(scaleVariable/2f, scaleVariable*2, scaleVariable, scaleVariable, scaleVariable*0.09f);
        level = new Level(lvlNumber, character);

        ui = new UiElements();

        fallTimeNotStarted = true;
        jumping = false;
        notAtGoal = true;

        fallStart = 0;
        hitPowerUpIndex = 0;
        jumpStart = 0;
        wonTimer = 0;
        camera = new OrthographicCamera(Gdx.graphics.getWidth() * 1.3f, Gdx.graphics.getHeight() * 1.3f);
        camera.update();

        stage = new Stage();
        state = gameState.RUNNING;


        gameOverTexture = new Texture("gameOverText.png");
        youWonTexture = new Texture("won.png");
        paused = new Texture("paused.png");

        //Initialization of buttons.
        retryButton = ui.getTextButton(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() * 0.5f, Gdx.graphics.getWidth() * 0.48f, Gdx.graphics.getHeight() * 0.14f, "RETRY");
        retryButton.setVisible(false);
        retryButton.setDisabled(true);
        retryButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new GameScreen(game,lvlNumber));
                dispose();
                return;
            }
        });
        continueButton = ui.getTextButton(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() * 0.5f, Gdx.graphics.getWidth() * 0.48f, Gdx.graphics.getHeight() * 0.14f, "CONTINUE");
        continueButton.setVisible(false);
        continueButton.setDisabled(true);
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                state = gameState.RUNNING;
                continueButton.setVisible(false);
                continueButton.setDisabled(true);
                menuButton.setDisabled(true);
                menuButton.setVisible(false);
                pauseButton.setVisible(true);
            }
        });
        Texture pauseBu = new Texture("pauseButton.jpg");
        pauseButton = new Image(pauseBu);
        pauseButton.setX(Gdx.graphics.getWidth()*0.9f);
        pauseButton.setY(Gdx.graphics.getHeight()*0.85f);
        pauseButton.setWidth(Gdx.graphics.getHeight() * 0.14f);
        pauseButton.setHeight(Gdx.graphics.getHeight() * 0.14f);

        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                state = gameState.PAUSED;
                menuButton.setDisabled(false);
                menuButton.setVisible(true);
            }
        });
        menuButton = ui.getTextButton(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() * 0.3f, Gdx.graphics.getWidth() * 0.48f, Gdx.graphics.getHeight() * 0.14f, "MENU");
        menuButton.setDisabled(true);
        menuButton.setVisible(false);
        menuButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MainMenu(game));
                dispose();
                return;
            }
        });


        //Two inputs: stage (buttons) and gesture detector (touching the screen).
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);


        stage.addActor(retryButton);
        stage.addActor(pauseButton);
        stage.addActor(continueButton);
        stage.addActor(menuButton);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    //Enum for game state.
    public enum gameState {
        RUNNING,
        PAUSED,
        OVER
    }

    @Override
    public void render(float delta) {

        // if gameState.RUNNING == TRUE, render the whole game.
        if (state == gameState.RUNNING) {
            if(powerUpHit()){
                PowerUp power = level.getPowerUps().get(hitPowerUpIndex);
                power.activate(power.getName(), character);
            }
            if (notAtGoal) {
                wonTimer = 0;
            }

            // Character's X coordinate is updated by its speed.
            if (character.getX() + character.getWidth() < level.getGoalRectangle().getX() && !level.isLeftGoal() && !character.isCrashed()) {
                character.setX(character.getX() + character.getSpeed());
            }

            // Same as the previous if statement but for levels with goal at left side.
            if (character.getX() > level.getGoalRectangle().getX()+level.getGoalRectangle().getWidth() && level.isLeftGoal() && !character.isCrashed()) {
                character.setX(character.getX() + character.getSpeed());
            }

            // Checks if the player has reached goal.
            if (!level.isLeftGoal() && character.getX() + character.getWidth() >= level.getGoalRectangle().getX()) {
                if (wonTimer == 0) {
                    wonTimer = TimeUtils.millis();
                    notAtGoal = false;
                    character.setX(level.getGoalRectangle().getX()-character.getWidth());
                }
                if (TimeUtils.timeSinceMillis(wonTimer) >= 2000) {
                    game.setScreen(new MainMenu(game));
                    dispose();
                    return;
                }
            }

            // -||- for the left sided goal.
            if (level.isLeftGoal() && character.getX() <= level.getGoalRectangle().getX()+character.getWidth()) {
                if (wonTimer == 0) {
                    wonTimer = TimeUtils.millis();
                    notAtGoal = false;
                    character.setX(level.getGoalRectangle().getX()+level.getGoalRectangle().getWidth());
                }
                if (TimeUtils.timeSinceMillis(wonTimer) >= 2000) {
                    game.setScreen(new MainMenu(game));
                    dispose();
                    return;
                }
            }
            character.updateRect();


            for (int i = 0; i < level.getFloorRectangles().size(); i++) {
                // Checks if the player loses.
                if (characterCrashes(character, level.getFloorRectangles().get(i))) {
                    state = gameState.OVER;
                    menuButton.setDisabled(false);
                    menuButton.setVisible(true);
                    pauseButton.setVisible(false);
                }
                // Checks if the falling is stopped.
                if (rectsShareAnX(character.getRect(), level.getFloorRectangles().get(i)) && !character.isCrashed()) {
                    if ((level.getFloorRectangles().get(i).contains(character.getX(), character.getY() -  6) || level.getFloorRectangles().get(i).contains(character.getX()+character.getWidth(), character.getY() - 6)) && !jumping) {
                        character.setySpeed(0);
                        fallTimeNotStarted = true;
                        character.setY(level.getFloorRectangles().get(i).getY() + level.getFloorRectangles().get(i).getHeight() + 1);
                        character.setAllowedToFall(false);
                    }
                }

                // Checks if the player is falling and updates its Y coordinate.
                if (character.isFreeFalling() && character.isAllowedToFall()) {
                    if(fallTimeNotStarted) {
                        fallStart = TimeUtils.millis();
                        fallTimeNotStarted = false;
                    }
                    character.setY(character.getY() - scaleVariable*0.13f);
                    character.setySpeed(-scaleVariable*0.13f);
                    character.setAllowedToFall(false);
                }

                character.updateRect();
            }
            if (!character.isFreeFalling()) {
                character.setAllowedToFall(true);
            }


             //Jumping logic
            if (jumping) {
                character.setAllowedToFall(false);
                character.setY(character.getY() + scaleVariable*0.13f);
                character.setySpeed(scaleVariable*0.13f);
                character.updateRect();

            } else {
                character.setAllowedToFall(true);
                character.setAllowedToJump(false);
                for (int j = 0; j < level.getFloorRectangles().size(); j++) {
                    if (character.getRect().overlaps(new Rectangle(level.getFloorRectangles().get(j).getX() - 6, level.getFloorRectangles().get(j).getY() - 6, level.getFloorRectangles().get(j).getWidth() + 12, level.getFloorRectangles().get(j).getHeight() + 12))) {
                        character.setAllowedToJump(true);
                    }
                }
            }

            // Cancels the jumping if time since the start of jump exceeds the wanted amount.
            if (TimeUtils.timeSinceMillis(jumpStart) > 400) {
                jumping = false;
            }

            character.updateRect();

            //Makes the camera to follow the character.
            camera.position.set(character.getX() + character.getWidth() * 0.5f, character.getY() + character.getHeight()*1.2f, 0);
            camera.update();
            renderer.setProjectionMatrix(camera.combined);

            // Everything is drawn.
            draw();
        }

        stage.act(delta);
        stage.draw();

        // If game is paused, render only pause menu.
        if (state == gameState.PAUSED) {

            pauseButton.setVisible(false);
            continueButton.setVisible(true);
            continueButton.setDisabled(false);

            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            game.batch.begin();
            game.batch.draw(paused, Gdx.graphics.getWidth()*0.31f,Gdx.graphics.getHeight()*0.7f,Gdx.graphics.getWidth()*0.39f,Gdx.graphics.getHeight()*0.16f);
            game.batch.end();

            stage.act(delta);
            stage.draw();

        }

        // After crashing into an obstacle, the render methods proceeds here. The game is lost.
        if (state == gameState.OVER) {
            retryButton.setVisible(true);
            retryButton.setDisabled(false);

            character.setSpeed(0);

            draw();
            stage.act(delta);
            stage.draw();
        }
    }


    // Draws everything on the screen.
    private void draw() {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!character.isCrashed()) {
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(level.getGoalColor());
            renderer.rect(level.getGoalRectangle().getX(), level.getGoalRectangle().getY(), level.getGoalRectangle().getWidth(), level.getGoalRectangle().getHeight());
            renderer.setColor(level.getFloorColor());
            for (int k = 0; k < level.getFloorRectangles().size(); k++) {
                renderer.rect(level.getFloorRectangles().get(k).getX(), level.getFloorRectangles().get(k).getY(), level.getFloorRectangles().get(k).getWidth(), level.getFloorRectangles().get(k).getHeight());
            }

            if (level.hasUpRectangles()) {
                for (int k = 0; k < level.getUpRectangles().size(); k++) {
                    renderer.rect(level.getUpRectangles().get(k).getX(), level.getUpRectangles().get(k).getY(),level.getUpRectangles().get(k).getWidth(),level.getUpRectangles().get(k).getHeight());
                }
            }

            renderer.setColor(character.getColor());

            renderer.rect(character.getX(), character.getY(), character.getWidth(), character.getHeight());
            renderer.setColor(Color.RED);
            renderer.rect(level.getBottomRectangle().getX(),level.getBottomRectangle().getY(),level.getBottomRectangle().getWidth(),level.getBottomRectangle().getHeight());
            for(int k = 0; k < level.getPowerUps().size(); k++) {
                renderer.setColor(level.getPowerUps().get(k).getColor());
                renderer.rect(level.getPowerUps().get(k).getX(),level.getPowerUps().get(k).getY(),level.getPowerUps().get(k).getWidth(),level.getPowerUps().get(k).getHeight());
            }
            renderer.end();
        }

        game.batch.begin();
        if (state == gameState.RUNNING) {
            if (!notAtGoal) {
                game.batch.draw(youWonTexture, Gdx.graphics.getWidth()*0.15f, Gdx.graphics.getHeight() * 0.42f, Gdx.graphics.getWidth()*0.70f, Gdx.graphics.getHeight()*0.23f);
            }
        }
        if (state == gameState.OVER) {
            game.batch.draw(gameOverTexture,Gdx.graphics.getWidth()*0.31f,Gdx.graphics.getHeight()*0.7f,Gdx.graphics.getWidth()*0.39f,Gdx.graphics.getHeight()*0.16f);
        }
        game.batch.end();
    }

    // Handles the power up logic after colliding with one.
    private boolean powerUpHit() {
        boolean hit = false;
        Rectangle charaRect = new Rectangle(character.getRect().getX(),character.getRect().getY(), character.getRect().getWidth(),character.getRect().getHeight());
        for (int k = 0; k < level.getPowerUps().size(); k++) {
            Rectangle powerRect = new Rectangle(level.getPowerUps().get(k).getX(),level.getPowerUps().get(k).getY(),level.getPowerUps().get(k).getWidth(),level.getPowerUps().get(k).getHeight());
            if (charaRect.getY() < powerRect.getY()+powerRect.getHeight()){
                if (charaRect.getX() + character.getWidth() < powerRect.getX()) {
                    if (charaRect.getX()+charaRect.getWidth()+10 >= powerRect.getX()) {
                        hit = true;
                        hitPowerUpIndex = k;
                    }
                }
            }
        }
        return hit;
    }

    // Handles the different situations when the character can crash with an object.
    private boolean characterCrashes(Chara character, Rectangle rect) {
        boolean crash = false;
        Rectangle charaRect = new Rectangle(character.getRect().getX(),character.getRect().getY(), character.getRect().getWidth(),character.getRect().getHeight());

        if (charaRect.getY() < rect.getY()+rect.getHeight() && TimeUtils.timeSinceMillis(fallStart) >= 20){
            if (charaRect.getX() + character.getWidth() < rect.getX()) {
                if (charaRect.getX()+charaRect.getWidth()+10 >= rect.getX()) {
                    character.setCrashed(true);
                    character.setX(rect.getX()-character.getWidth());
                    crash = true;
                }
            }
        }
        if (level.isLeftGoal() && TimeUtils.timeSinceMillis(fallStart) >= 20 && character.getSpeed()<0) {
            if (charaRect.getY() < rect.getY()+rect.getHeight()){
                if (charaRect.getX() > rect.getX()+rect.getWidth()) {
                    if (charaRect.getX()-10 <= rect.getX()+rect.getWidth()) {
                        character.setCrashed(true);
                        crash = true;
                        character.setX(rect.getX()+rect.getWidth());
                        character.updateRect();
                    }
                }
            }
        }
        if (level.hasUpRectangles()) {
            for(int x = 0; x < level.getUpRectangles().size(); x++) {
                Rectangle upRect = new Rectangle(level.getUpRectangles().get(x).getX(), level.getUpRectangles().get(x).getY(), level.getUpRectangles().get(x).getWidth(), level.getUpRectangles().get(x).getHeight());
                if (charaRect.getY() + charaRect.getHeight() > upRect.getY()) {
                    if (charaRect.getX() + character.getWidth() < upRect.getX()) {
                        if (charaRect.getX() + charaRect.getWidth() + 10 >= upRect.getX()) {
                            character.setCrashed(true);
                            crash = true;
                        }
                    }
                }
                if (jumping) {
                    if (charaRect.getY() + charaRect.getHeight() < upRect.getY()) {
                        if (charaRect.getX() + charaRect.getWidth() > upRect.getX() && charaRect.getX() < upRect.getX()+upRect.getWidth()) {
                            if (charaRect.getY() + charaRect.getHeight() + character.getYSpeed() > upRect.getY()) {
                                character.setCrashed(true);
                                crash = true;
                            }
                        }
                    }
                }
            }
        }
        if (character.isFreeFalling()) {

                if (charaRect.getX() + character.getWidth() < rect.getX()) {
                    if (charaRect.getY() - character.getYSpeed() < rect.getY()+ rect.getHeight() && character.getX()+charaRect.getWidth()+10 >rect.getX()) {

                        character.setCrashed(true);
                        crash = true;

                    }
                }

        }
        if (!character.isFreeFalling()) {
            if (charaRect.getY() < rect.getY()+rect.getHeight()){
                if (charaRect.getX() + character.getWidth() < rect.getX()) {
                    if (charaRect.getX()+charaRect.getWidth()+10 >= rect.getX()) {
                        character.setCrashed(true);
                        character.setX(rect.getX()-character.getWidth());
                        crash = true;
                    }
                }
            }
        }
        if (character.getY() <= level.getBottomRectangle().getY()+level.getBottomRectangle().getHeight()) {
            crash = true;
            character.setCrashed(true);
        }
        return crash;
    }


    // Checks if two rectangles share an X coordinate.
    private boolean rectsShareAnX(Rectangle rect, Rectangle rectangle) {
        boolean share = false;
        if (rect.getX() > rectangle.getX() && rect.getX() < rectangle.getX() + rectangle.getWidth()) {
            share = true;
        }
        if (rect.getX() + rect.getWidth() > rectangle.getX() && rect.getX() + rect.getWidth() < rectangle.getX() + rectangle.getWidth()) {
            share = true;
        }
        if (share) {
            character.setFreeFalling(false);
        } else {
            character.setFreeFalling(true);
        }

        return share;
    }


    @Override
    public void dispose() {
        pauseButton.remove();
        gameOverTexture.dispose();
        youWonTexture.dispose();
        paused.dispose();
        stage.dispose();
        renderer.dispose();
        ui.dispose();
        game = null;
        lvlNumber = 0;
        character = null;
        camera = null;
        level = null;
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override

    // Player touches the screen; if character is allowed to jump, jump starts.
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (character.getX() + character.getWidth() != level.getGoalRectangle().getX() && character.getX() != level.getGoalRectangle().getX()+level.getGoalRectangle().getWidth()) {
            if (character.isAllowedToJump()) {
                character.setAllowedToJump(false);
                jumping = true;
                jumpStart = TimeUtils.millis();
            }
        }
        return false;
    }

    // When player doesn't press the screen, falling is started.
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        jumping = false;
        character.setAllowedToFall(true);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }



    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        return false;
    }


    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}

