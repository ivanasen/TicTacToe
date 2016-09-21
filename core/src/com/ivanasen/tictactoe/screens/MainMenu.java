package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.TicTacToeMain;

/**
 * Created by ivan-asen on 12.09.16.
 */
public class MainMenu implements Screen {
    private static final String TAG = MainMenu.class.getSimpleName();

    private TicTacToeMain game;
    private SpriteBatch batch;
    private Viewport viewport;

    private Stage stage;
    private Image gameLogo;
    private Image playButton;

    private boolean isAnimating;

    public MainMenu(TicTacToeMain ticTacToeMain) {
        game = ticTacToeMain;
        batch = game.batch;
    }

    @Override
    public void show() {
        stage = new Stage();
        viewport = new ScreenViewport();

        gameLogo = new Image(new Texture(Constants.LOGO_IMG));
        playButton = new Image(new Texture(Constants.PLAY_BTN_IMG));

        isAnimating = false;

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float width = viewport.getCamera().viewportWidth;
                super.clicked(event, x, y);
                isAnimating = true;

                playButton.addAction(
                        Actions.moveBy(
                                -(gameLogo.getWidth() + width / 2),
                                0,
                                Constants.BASE_ANIMATION_DURATION,
                                Interpolation.swingIn));
                gameLogo.addAction(
                        Actions.sequence(
                                Actions.moveBy(
                                        -(gameLogo.getWidth() + width / 2),
                                        0,
                                        Constants.BASE_ANIMATION_DURATION,
                                        Interpolation.swingIn),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        game.setScreen(new PlayScreen(game));
                                        MainMenu.this.dispose();
                                    }
                                })
                        )
                );
            }
        });


        stage.addActor(gameLogo);
        stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(Constants.BACKGROUND_RED_VALUE, Constants.BACKGROUND_GREEN_VALUE,
                Constants.BACKGROUND_BLUE_VALUE, Constants.BACKGROUND_ALPHA_VALUE);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    private void update(float delta) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        final float width = viewport.getCamera().viewportWidth;
        final float height = viewport.getCamera().viewportHeight;

        float aspectRatioLogo = gameLogo.getHeight() / gameLogo.getWidth();
        gameLogo.setWidth(width / 1.2f);
        gameLogo.setHeight(aspectRatioLogo * gameLogo.getWidth());
        float aspectRatioButton = playButton.getHeight() / playButton.getWidth();
        playButton.setWidth(width / 2f);
        playButton.setHeight(aspectRatioButton * playButton.getWidth());

        if (!isAnimating) {
            gameLogo.setPosition((width - gameLogo.getWidth()) / 2,
                    3 * height / 4 - gameLogo.getHeight() / 2);
            playButton.setPosition((width - playButton.getWidth()) / 2,
                    height / 4 - playButton.getHeight() / 2);
        }

        stage.act();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        stage.dispose();
    }
}
