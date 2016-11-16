package com.ivanasen.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ivanasen.tictactoe.screens.MainMenu;

public class TicTacToeMain extends Game {

    public final ActionResolver actionResolver;
    public SpriteBatch batch;

    public static boolean isMainMenuVisible;

    public TicTacToeMain(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        batch = new SpriteBatch();
        setScreen(new MainMenu(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
