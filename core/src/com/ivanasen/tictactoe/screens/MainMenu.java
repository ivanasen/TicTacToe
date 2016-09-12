package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.TicTacToeMain;

/**
 * Created by ivan-asen on 12.09.16.
 */
public class MainMenu implements Screen {
    private static final String TAG = MainMenu.class.getSimpleName();

    private TicTacToeMain game;
    private SpriteBatch batch;

    private Stage stage;
    private Image gameLogo;
    private Image playButton;

    public MainMenu(TicTacToeMain ticTacToeMain) {
        game = ticTacToeMain;
        batch = game.batch;
    }

    @Override
    public void show() {
        stage = new Stage();

        gameLogo = new Image(new Texture(Constants.LOGO_IMG_NAME));
        playButton = new Image(new Texture(Constants.PLAY_BTN_NAME));

        final float width = Gdx.graphics.getWidth();
        final float height = Gdx.graphics.getHeight();

        gameLogo.setPosition((width - gameLogo.getWidth()) / 2,
                3 * height / 4 - gameLogo.getHeight() / 2);
        playButton.setPosition((width - playButton.getWidth()) / 2,
                height / 4 - playButton.getHeight() / 2);

//        gameLogo.getColor().a = 0;
//        gameLogo.addAction(Actions.fadeIn(Constants.MAIN_MENU_ANIMATION_DURATION));
//        playButton.getColor().a = 0;
//        playButton.addAction(Actions.fadeIn(Constants.MAIN_MENU_ANIMATION_DURATION));

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                playButton.addAction(Actions.moveBy(-(gameLogo.getWidth() + width / 2), 0,
                        Constants.MAIN_MENU_ANIMATION_DURATION, Interpolation.swing));
                gameLogo.addAction(Actions.sequence(
                        Actions.moveBy(-(gameLogo.getWidth() + width / 2), 0,
                            Constants.MAIN_MENU_ANIMATION_DURATION, Interpolation.swing),
                        Actions.run(() -> game.setScreen(new PlayScreen(game)))));
            }
        });


        stage.addActor(gameLogo);
        stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Constants.BACKGROUND_RED_VALUE, Constants.BACKGROUND_GREEN_VALUE,
                Constants.BACKGROUND_BLUE_VALUE, Constants.BACKGROUND_ALPHA_VALUE);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

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
