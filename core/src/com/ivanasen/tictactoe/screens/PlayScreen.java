package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.TicTacToeMain;

/**
 * Created by ivan-asen on 13.09.16.
 */
public class PlayScreen implements Screen {

    private final TicTacToeMain game;
    private final SpriteBatch batch;

    public PlayScreen(TicTacToeMain game) {
        this.game = game;
        this.batch = game.batch;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Constants.BACKGROUND_RED_VALUE, Constants.BACKGROUND_GREEN_VALUE,
                Constants.BACKGROUND_BLUE_VALUE, Constants.BACKGROUND_ALPHA_VALUE);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
