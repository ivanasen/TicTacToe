package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.TicTacToeMain;

/**
 * Created by ivan-asen on 13.09.16.
 */
public class PlayScreen implements Screen {
    private static final String TAG = PlayScreen.class.getSimpleName();

    private TicTacToeMain game;
    private SpriteBatch batch;
    private Viewport viewport;
    private Image gameGrid;

    private Stage stage;
    private Table table;

    private float gameGridSize;
    private Vector2 gameGridPosition;
    private Texture transparentTexture;

    private Image[][] gameCells;
    private short[][] gameTrackerArray;

    private boolean circleIsOnTurn;
    private boolean isNotAnimated;
    private float viewportWidth;
    private float viewportHeight;
    private Stage gameGridStage;
    private boolean isAnimating;
    private boolean gameEnded;

    private int moveCount;

    PlayScreen(TicTacToeMain game) {
        this.game = game;
        this.batch = game.batch;
    }

    @Override
    public void show() {
        moveCount = 0;
        circleIsOnTurn = true;
        viewport = new ScreenViewport(new OrthographicCamera());

        gameGrid = new Image(new Texture(Constants.GAME_GRID_IMG));
        gameGridPosition = new Vector2();

        transparentTexture = new Texture(Constants.TRANSPARENT_IMG);
        stage = new Stage();
        gameGridStage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();

        gameCells = new Image[Constants.GRID_COUNT][Constants.GRID_COUNT];
        gameTrackerArray = new short[Constants.GRID_COUNT][Constants.GRID_COUNT];

        isNotAnimated = true;
        isAnimating = false;

        updateGameGridSize();

        setUpGameGridCells();
        gameGridStage.addActor(gameGrid);
        stage.addActor(table);
    }

    private void setUpGameGridCells() {
        for (int i = 0; i < gameCells.length; i++) {
            table.row();
            for (int j = 0; j < gameCells[0].length; j++) {
                gameCells[i][j] = new Image(transparentTexture);
                table.add(gameCells[i][j]).fill().expand();

                final int finalI = i;
                final int finalJ = j;
                gameCells[i][j].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        moveCount++;
                        gameTrackerArray[finalI][finalJ] = circleIsOnTurn ?
                                Constants.CIRCLE_CELL_STATE : Constants.CROSS_CELL_STATE;

                        PlayScreen.this.drawCurrentPlayerSymbol(gameCells[finalI][finalJ]);
                        circleIsOnTurn = !circleIsOnTurn;

                        gameCells[finalI][finalJ].setTouchable(Touchable.disabled);
                        Gdx.app.log(TAG, "clicked");
                    }
                });

            }
        }
    }

    private void drawCurrentPlayerSymbol(Image image) {
        SpriteDrawable symbol;
        if (circleIsOnTurn)
            symbol = new SpriteDrawable(new Sprite(new Texture(Constants.CIRCLE_IMG)));
        else
            symbol = new SpriteDrawable(new Sprite(new Texture(Constants.CROSS_IMG)));

        image.getColor().a = 0;
        image.setDrawable(symbol);
        animateSymbol(image);
    }

    private void animateSymbol(Image image) {
        float size = image.getWidth();
        float x = size / 2;
        float y = size / 2;

        image.setOrigin(x, y);

        float scaleX = image.getScaleX();
        float scaleY = image.getScaleY();

        image.scaleBy(-scaleX, -scaleY);

        Action fadeInAction = Actions.fadeIn(Constants.SYMBOL_ANIMATION_DURATION);
        Action scaleAction = Actions.scaleTo(scaleX, scaleY,
                Constants.SYMBOL_ANIMATION_DURATION, Interpolation.elasticOut);
        Action checkPlayerHasWonAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                checkIfGameHasEnded();
            }
        });
        image.addAction(Actions.sequence(Actions.parallel(fadeInAction, scaleAction),
                checkPlayerHasWonAction));
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(Constants.BACKGROUND_RED_VALUE, Constants.BACKGROUND_GREEN_VALUE,
                Constants.BACKGROUND_BLUE_VALUE, Constants.BACKGROUND_ALPHA_VALUE);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        gameGridStage.draw();
    }

    private void update(float delta) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        if (isNotAnimated) {
            gameGrid.addAction(
                    Actions.moveTo(
                            gameGridPosition.x,
                            gameGridPosition.y,
                            Constants.BASE_ANIMATION_DURATION,
                            Interpolation.swingOut)
                    );
            isNotAnimated = false;
        }

        stage.act(delta);
        gameGridStage.act(delta);
    }

    private void checkIfGameHasEnded() {
        for (int row = 0; row < gameTrackerArray.length; row++) {
            for (int col = 0; col < gameTrackerArray[0].length; col++) {
                short currentCell = gameTrackerArray[row][col];

                if (currentCell == Constants.BLANK_CELL_STATE)
                    continue;

                //check column
                for (int i = 0; i < Constants.GRID_COUNT; i++) {
                    if (gameTrackerArray[row][i] != currentCell)
                        break;
                    if (i == Constants.GRID_COUNT - 1) {
                        gameEnded = true;
                        declareGameEnded();
                        break;
                    }
                }
                //check row
                for (int i = 0; i < Constants.GRID_COUNT; i++) {
                    if (gameTrackerArray[i][col] != currentCell)
                        break;
                    if (i == Constants.GRID_COUNT - 1) {
                        gameEnded = true;
                        declareGameEnded();
                        break;
                    }
                }
                //check diagonal
                if (row == col) {
                    for (int i = 0; i < Constants.GRID_COUNT; i++) {
                        if (gameTrackerArray[i][i] != currentCell)
                            break;
                        if (i == Constants.GRID_COUNT - 1) {
                            gameEnded = true;
                            declareGameEnded();
                            break;
                        }
                    }
                }
                //check anti-diagonal
                for (int i = 0; i < Constants.GRID_COUNT; i++) {
                    if (gameTrackerArray[i][(Constants.GRID_COUNT - 1) - i] != currentCell)
                        break;
                    if (i == Constants.GRID_COUNT - 1) {
                        gameEnded = true;
                        declareGameEnded();
                        break;
                    }
                }

                if (gameEnded || moveCount >= 9) {
                    declareGameEnded();
                    break;
                }
            }
        }
    }

    private void declareGameEnded() {
        moveCount = 0;
        gameEnded = false;
        cleanTrackerArray();
        table.clear();
        setUpGameGridCells();
    }

    private void cleanTrackerArray() {
        for (int i = 0; i < gameTrackerArray.length; i++) {
            for (int j = 0; j < gameTrackerArray[0].length; j++) {
                gameTrackerArray[i][j] = Constants.BLANK_CELL_STATE;
            }
        }
    }

    private void updateGameGridSize() {
        viewportWidth = viewport.getCamera().viewportWidth;
        viewportHeight = viewport.getCamera().viewportHeight;

        gameGridSize = viewportWidth * 8f / 10f;
        gameGridPosition.x = (viewportWidth - gameGridSize) / 2f;
        gameGridPosition.y = (viewportHeight - gameGridSize) / 2f;

        gameGrid.setWidth(gameGridSize);
        gameGrid.setHeight(gameGridSize);
        table.setBounds(gameGridPosition.x, gameGridPosition.y,
                gameGridSize, gameGridSize);

        if (isNotAnimated)
            gameGrid.setPosition(
                    viewportWidth,
                    gameGridPosition.y);
        else
            gameGrid.setPosition(gameGridPosition.x, gameGridPosition.y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        updateGameGridSize();
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
        gameGridStage.dispose();
    }
}
