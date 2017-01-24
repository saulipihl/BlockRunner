package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Class for handling UI elements for the game. A lot of texture and button handling could've been moved here from other classes.
 */
public class UiElements {
    private Skin buttonSkin;
    private Pixmap pixmap;
    private BitmapFont bfont;

    public UiElements() {
    }

    //Text button is created.
    public TextButton getTextButton(float x, float y, float width, float height, String text) {
        buttonSkin = new Skin();

        pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.ORANGE);
        pixmap.fill();
        buttonSkin.add("white", new Texture(pixmap));

        bfont=new BitmapFont();
        bfont.getData().scale(1);
        buttonSkin.add("default",bfont);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = buttonSkin.newDrawable("white", Color.ORANGE);

        textButtonStyle.font = buttonSkin.getFont("default");

        buttonSkin.add("default", textButtonStyle);

        TextButton textButton = new TextButton(text,textButtonStyle);

        textButton.setBounds(x-width/2, y, width, height);

        return textButton;
    }

    public void dispose() {
        buttonSkin.dispose();
        pixmap.dispose();
        bfont.dispose();;
    }

}