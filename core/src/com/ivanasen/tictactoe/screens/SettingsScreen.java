package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ivanasen.tictactoe.Board;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.TicTacToeMain;

import static com.ivanasen.tictactoe.Constants.Difficulty;
import static com.ivanasen.tictactoe.Constants.Difficulty.EASY;
import static com.ivanasen.tictactoe.Constants.Difficulty.HARD;
import static com.ivanasen.tictactoe.Constants.Difficulty.MEDIUM;

class SettingsScreen implements Screen, InputProcessor {

    private final TicTacToeMain game;
    private SpriteBatch batch;
    private StretchViewport viewport;
    private Stage stage;
    private Table table;
    private Table difficultyTable;

    private Texture background;
    private Image settingsBackground;
    private Image circle;
    private Image cross;
    private TextureAtlas difficultyAtlas;

    private Image mainMenuBtn;
    private TextureRegion[] easyBtnTexture;
    private TextureRegion[] mediumBtnTexture;
    private TextureRegion[] hardBtnTexture;

    private Image easyBtn;
    private Image mediumBtn;
    private Image hardBtn;

    private Preferences preferences;
    private boolean shouldMarkSettings;

    SettingsScreen(TicTacToeMain game) {
        this.game = game;
        this.batch = game.batch;
    }

    @Override
    public void show() {
        TicTacToeMain.isMainMenuVisible = false;
        preferences = Gdx.app.getPreferences(Constants.SettingsEntry.PREFERENCES_NAME);
        viewport = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);

        stage = new Stage(viewport);

        InputMultiplexer multiplexer = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(multiplexer);

        table = new Table();
        table.setFillParent(true);

        difficultyTable = new Table();

        shouldMarkSettings = true;

        loadSprites();
        loadEnterAnimation();
        stage.addActor(table);
    }

    private void createMainMenuButton() {
        Texture btnTexture = new Texture(Constants.FileDirectories.MAIN_MENU_BTN_IMG);
        btnTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuBtn = new Image(btnTexture);
        mainMenuBtn.setTouchable(Touchable.enabled);

        mainMenuBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainMenuBtn.getColor().a = Constants.PlayscreenEntry.BTN_COLOR_A_WHEN_PRESSED;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                playExitAnimation();
            }
        });

        table.add(mainMenuBtn)
                .padLeft(Constants.PlayscreenEntry.BUTTON_PADDING)
                .top()
                .left()
                .expandX()
                .colspan(Constants.SettingsEntry.DIFF_COLSPAN)
                .row();
    }

    private void playExitAnimation() {
        Action backToMainMenu = Actions.run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });

        stage.addAction(Actions.sequence(
                Actions.fadeOut(Constants.Animations.FADE_ANIM_DURATION),
                backToMainMenu));
    }

    private void loadEnterAnimation() {
        circle.getColor().a = 0;
        cross.getColor().a = 0;
        settingsBackground.getColor().a = 0;

        circle.addAction(Actions.fadeIn(Constants.Animations.FADE_ANIM_DURATION));
        cross.addAction(Actions.fadeIn(Constants.Animations.FADE_ANIM_DURATION));
        settingsBackground.addAction(Actions.fadeIn(Constants.Animations.FADE_ANIM_DURATION));
    }

    private void loadSprites() {
        loadBackground();
        loadSettingsBackground();
        createMainMenuButton();
        loadCrossAndCircle();
        loadDifficulties();
    }

    private void loadDifficulties() {
        difficultyAtlas = new TextureAtlas(Constants.SettingsEntry.DIFFICULTY_PACK);

        easyBtnTexture = new TextureRegion[]{
                difficultyAtlas.findRegion("btn_easy"),
                difficultyAtlas.findRegion("btn_easy_active")
        };
        mediumBtnTexture = new TextureRegion[]{
                difficultyAtlas.findRegion("btn_medium"),
                difficultyAtlas.findRegion("btn_medium_active")
        };
        hardBtnTexture = new TextureRegion[]{
                difficultyAtlas.findRegion("btn_hard"),
                difficultyAtlas.findRegion("btn_hard_active")
        };

        easyBtn = new Image(easyBtnTexture[0]);
        mediumBtn = new Image(mediumBtnTexture[0]);
        hardBtn = new Image(hardBtnTexture[0]);

        easyBtn.scaleBy(Constants.SettingsEntry.DIFF_SCALE);
        mediumBtn.scaleBy(Constants.SettingsEntry.DIFF_SCALE);
        hardBtn.scaleBy(Constants.SettingsEntry.DIFF_SCALE);

        easyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                easyBtn.setDrawable(new TextureRegionDrawable(easyBtnTexture[1]));
                mediumBtn.setDrawable(new TextureRegionDrawable(mediumBtnTexture[0]));
                hardBtn.setDrawable(new TextureRegionDrawable(hardBtnTexture[0]));

                preferences.putString(
                        Constants.SettingsEntry.DIFFICULTY,
                        EASY.toString());
                preferences.flush();
            }
        });

        mediumBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mediumBtn.setDrawable(new TextureRegionDrawable(mediumBtnTexture[1]));
                easyBtn.setDrawable(new TextureRegionDrawable(easyBtnTexture[0]));
                hardBtn.setDrawable(new TextureRegionDrawable(hardBtnTexture[0]));

                preferences.putString(
                        Constants.SettingsEntry.DIFFICULTY,
                        Difficulty.MEDIUM.toString());
                preferences.flush();
            }
        });

        hardBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hardBtn.setDrawable(new TextureRegionDrawable(hardBtnTexture[1]));
                easyBtn.setDrawable(new TextureRegionDrawable(easyBtnTexture[0]));
                mediumBtn.setDrawable(new TextureRegionDrawable(mediumBtnTexture[0]));

                preferences.putString(
                        Constants.SettingsEntry.DIFFICULTY,
                        Difficulty.HARD.toString());

                preferences.flush();
            }
        });


        difficultyTable.add(easyBtn);
        difficultyTable.add(mediumBtn)
                .padRight(Constants.SettingsEntry.DIFF_PADDING_HORIZONTAL_RIGHT)
                .padLeft(Constants.SettingsEntry.DIFF_PADDING_HORIZONTAL_LEFT);
        difficultyTable.add(hardBtn);

        table.add(difficultyTable)
                .colspan(Constants.SettingsEntry.DIFF_COLSPAN)
                .padBottom(Constants.SettingsEntry.DIFF_PADDING_BOTTOM)
                .padRight(Constants.SettingsEntry.DIFF_PADDING_HORIZONTAL)
                .expand();
    }

    private void loadBackground() {
        background = new Texture(Constants.FileDirectories.BACKGROUND_IMG);
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void loadCrossAndCircle() {
        Texture circleTexture = new Texture(Constants.FileDirectories.CIRCLE_IMG);
        Texture crossTexture = new Texture(Constants.FileDirectories.CROSS_IMG);
        circleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        crossTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        circle = new Image(new SpriteDrawable(new Sprite(circleTexture)));
        cross = new Image(new SpriteDrawable(new Sprite(crossTexture)));

        circle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                markSetting(circle, cross);
                preferences.putString(
                        Constants.SettingsEntry.PLAYER_SYMBOL,
                        Board.CellState.CIRCLE.toString());
                preferences.flush();
            }
        });

        cross.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                markSetting(cross, circle);
                preferences.putString(
                        Constants.SettingsEntry.PLAYER_SYMBOL,
                        Board.CellState.CROSS.toString());
                preferences.flush();
            }
        });

        float cellSize = Constants.SettingsEntry.PLAYER_SYMBOL_SIZE;
        float paddingBottom = Constants.SettingsEntry.PLAYER_SYMBOL_PADDING_BOTTOM;
        float paddingTop = Constants.SettingsEntry.PLAYER_SYMBOL_PADDING_TOP;

        table.add(circle).size(cellSize).expandX().padBottom(paddingBottom).padTop(paddingTop);
        table.add(cross).size(cellSize).expandX().padBottom(paddingBottom).padTop(paddingTop);
        table.row();
    }

    private void markSetting(Image trueSetting, Image falseSetting) {
        trueSetting.addAction(Actions.alpha(1, Constants.Animations.SETTINGS_ANIMATION_DURATION));
        falseSetting.addAction(Actions.alpha(
                Constants.SettingsEntry.COLOR_A_WHEN_FALSE,
                Constants.Animations.SETTINGS_ANIMATION_DURATION));
    }

    private void loadSettingsBackground() {
        Texture bckTexture = new Texture(Constants.SettingsEntry.BACKGROUND);
        bckTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Sprite backgroundSprite = new Sprite(bckTexture);
        settingsBackground = new Image(backgroundSprite);
        stage.addActor(settingsBackground);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, stage.getWidth() * 2, stage.getHeight() * 2);
        batch.end();

        stage.draw();
    }

    private void update(float delta) {
        if (shouldMarkSettings) {
            if (circle.hasParent() && cross.hasParent()) {
                String playerSetting = preferences.getString(Constants.SettingsEntry.PLAYER_SYMBOL);
                if (playerSetting.equals(Board.CellState.CIRCLE.toString())) {
                    markSetting(circle, cross);
                } else {
                    markSetting(cross, circle);
                }
            }

            String difficultySetting = preferences.getString(Constants.SettingsEntry.DIFFICULTY);
            if (difficultySetting.equals(EASY.toString())) {
                easyBtn.setDrawable(new TextureRegionDrawable(easyBtnTexture[1]));
                mediumBtn.setDrawable(new TextureRegionDrawable(mediumBtnTexture[0]));
                hardBtn.setDrawable(new TextureRegionDrawable(hardBtnTexture[0]));
            } else if (difficultySetting.equals(MEDIUM.toString())) {
                mediumBtn.setDrawable(new TextureRegionDrawable(mediumBtnTexture[1]));
                easyBtn.setDrawable(new TextureRegionDrawable(easyBtnTexture[0]));
                hardBtn.setDrawable(new TextureRegionDrawable(hardBtnTexture[0]));
            } else if (difficultySetting.equals(HARD.toString())) {
                hardBtn.setDrawable(new TextureRegionDrawable(hardBtnTexture[1]));
                easyBtn.setDrawable(new TextureRegionDrawable(easyBtnTexture[0]));
                mediumBtn.setDrawable(new TextureRegionDrawable(mediumBtnTexture[0]));
            } else {
                easyBtn.setDrawable(new TextureRegionDrawable(easyBtnTexture[1]));
                mediumBtn.setDrawable(new TextureRegionDrawable(mediumBtnTexture[0]));
                hardBtn.setDrawable(new TextureRegionDrawable(hardBtnTexture[0]));
            }
        }

        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getCamera().update();
    }

    @Override
    public void dispose() {
        difficultyAtlas.dispose();
        stage.dispose();
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
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK) {
            dispose();
            game.setScreen(new MainMenu(game));
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
