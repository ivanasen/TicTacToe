package com.ivanasen.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ivanasen.tictactoe.screens.MainMenu;

public class TicTacToeMain extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        //TODO: use assetManager to load resources asynchronously
        batch = new SpriteBatch();
        setScreen(new MainMenu(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
