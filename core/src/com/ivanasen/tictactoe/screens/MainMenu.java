package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
    private Sprite backgroundSprite;
    private Image playButton;

    private boolean isAnimating;

    public MainMenu(TicTacToeMain ticTacToeMain) {
        game = ticTacToeMain;
        batch = game.batch;
    }

    @Override
    public void show() {
        Gdx.app.log(TAG, "mainMenu created");
        stage = new Stage();
        viewport = new ScreenViewport();

        loadImages();

        isAnimating = false;

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                playExitAnimationAndStartGame();
            }
        });


        stage.addActor(gameLogo);
        stage.addActor(playButton);

        playEnterAnimaton();

        Gdx.input.setInputProcessor(stage);
    }

    private void playEnterAnimaton() {
        gameLogo.getColor().a = 0;
        playButton.getColor().a = 0;

        gameLogo.addAction(Actions.fadeIn(Constants.FADE_ANIM_DURATION));
        playButton.addAction(Actions.fadeIn(Constants.FADE_ANIM_DURATION));
    }

    private void playExitAnimationAndStartGame() {
        isAnimating = true;
        float height = viewport.getCamera().viewportHeight;
        Vector2 objectMovement = new Vector2(0, (gameLogo.getHeight() + height / 2));

        Runnable playScreenRunnable = new Runnable() {
            @Override
            public void run() {
                game.setScreen(new PlayScreen(game));
                MainMenu.this.dispose();
            }
        };
        gameLogo.addAction(
                Actions.parallel(
                        Actions.sequence(
                                Actions.moveBy(
                                        objectMovement.x,
                                        objectMovement.y,
                                        Constants.BASE_ANIMATION_DURATION),
                                Actions.run(playScreenRunnable)
                        ),
                        Actions.fadeOut(Constants.BASE_ANIMATION_DURATION)
                )
        );
        playButton.addAction(
                Actions.parallel(
                        Actions.moveBy(
                                objectMovement.x,
                                -objectMovement.y,
                                Constants.BASE_ANIMATION_DURATION),
                        Actions.fadeOut(Constants.BASE_ANIMATION_DURATION)
                ));
    }

    private void loadImages() {
        Texture background = new Texture(Constants.BACKGROUND_IMG);
        backgroundSprite = new Sprite(background);

        Texture logo = new Texture(Constants.LOGO_IMG);
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture playBtn = new Texture(Constants.PLAY_BTN_IMG);
        playBtn.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        gameLogo = new Image(logo);
        playButton = new Image(playBtn);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();

        stage.draw();
    }

    private void update(float delta) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        final float width = viewport.getCamera().viewportWidth;
        final float height = viewport.getCamera().viewportHeight;

        backgroundSprite.setSize(width, height);

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

        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
