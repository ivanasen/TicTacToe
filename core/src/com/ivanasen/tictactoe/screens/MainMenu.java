package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.ivanasen.tictactoe.ai.AI;
import com.ivanasen.tictactoe.ai.EasyAI;
import com.ivanasen.tictactoe.ai.HardAI;
import com.ivanasen.tictactoe.ai.MediumAI;

public class MainMenu implements Screen {
    private static final String TAG = MainMenu.class.getSimpleName();

    private TicTacToeMain game;
    private SpriteBatch batch;
    private Viewport viewport;

    private Stage stage;
    private Image gameLogo;
    private Image settingsBtn;
    private Sprite backgroundSprite;
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

        loadImages();

        isAnimating = false;

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Runnable playScreenRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Preferences prefs = Gdx.app.getPreferences(Constants.SettingsEntry.PREFERENCES_NAME);
                        String difficulty = prefs.getString(Constants.SettingsEntry.DIFFICULTY);
                        AI ai;

                        if ( difficulty.equals(Constants.Difficulty.EASY.toString()) ) {
                            ai = new EasyAI();
                        } else if ( difficulty.equals(Constants.Difficulty.HARD.toString()) ) {
                            ai = new HardAI();
                        } else {
                            ai = new MediumAI();
                        }
                        game.setScreen(new PlayScreenTwoPlayers(game));
//                        game.setScreen(new PlayScreenAI(game, ai));
                        MainMenu.this.dispose();
                    }
                };

                playExitAnimationAndRunAction(playScreenRunnable);
            }
        });

        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Runnable settingsRunnable = new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new SettingsScreen(game));
                        MainMenu.this.dispose();
                    }
                };
                playExitAnimationAndRunAction(settingsRunnable);
            }
        });

        stage.addActor(gameLogo);
        stage.addActor(playButton);
        stage.addActor(settingsBtn);

        playEnterAnimaton();

        Gdx.input.setInputProcessor(stage);
    }

    private void playEnterAnimaton() {
        gameLogo.getColor().a = 0;
        playButton.getColor().a = 0;

        gameLogo.addAction(Actions.fadeIn(Constants.Animations.FADE_ANIM_DURATION));
        playButton.addAction(Actions.fadeIn(Constants.Animations.FADE_ANIM_DURATION));
        settingsBtn.addAction(Actions.fadeIn(Constants.Animations.FADE_ANIM_DURATION));
    }

    private void playExitAnimationAndRunAction(Runnable runnable) {
        isAnimating = true;
        float height = viewport.getCamera().viewportHeight;
        Vector2 objectMovement = new Vector2(0, (gameLogo.getHeight() + height / 2));

        gameLogo.addAction(
                Actions.parallel(
                        Actions.sequence(
                                Actions.moveBy(
                                        objectMovement.x,
                                        objectMovement.y,
                                        Constants.Animations.BASE_ANIMATION_DURATION),
                                Actions.run(runnable)
                        ),
                        Actions.fadeOut(Constants.Animations.BASE_ANIMATION_DURATION)
                )
        );
        playButton.addAction(
                Actions.parallel(
                        Actions.moveBy(
                                objectMovement.x,
                                -objectMovement.y,
                                Constants.Animations.BASE_ANIMATION_DURATION),
                        Actions.fadeOut(Constants.Animations.BASE_ANIMATION_DURATION)
                ));
        settingsBtn.addAction(
                Actions.parallel(
                        Actions.moveBy(
                                objectMovement.x,
                                objectMovement.y,
                                Constants.Animations.BASE_ANIMATION_DURATION),
                        Actions.fadeOut(Constants.Animations.BASE_ANIMATION_DURATION)
                ));
    }

    private void loadImages() {
        Texture background = new Texture(Constants.FileDirectories.BACKGROUND_IMG);
        backgroundSprite = new Sprite(background);

        Texture logo = new Texture(Constants.FileDirectories.LOGO_IMG);
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture playBtn = new Texture(Constants.FileDirectories.PLAY_BTN_IMG);
        playBtn.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture settingsTexture = new Texture(Constants.FileDirectories.SETTINGS_IMG);
        settingsTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        gameLogo = new Image(logo);
        playButton = new Image(playBtn);
        settingsBtn = new Image(settingsTexture);
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

        settingsBtn.setSize(width / 9, width / 9);

        if (!isAnimating) {
            gameLogo.setPosition((width - gameLogo.getWidth()) / 2,
                    3 * height / 4 - gameLogo.getHeight() / 2);
            playButton.setPosition((width - playButton.getWidth()) / 2,
                    height / 4 - playButton.getHeight() / 2);
            settingsBtn.setPosition(stage.getWidth() - settingsBtn.getWidth() * 1.3f,
                    stage.getHeight() - settingsBtn.getHeight() * 1.3f);
        }

        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
