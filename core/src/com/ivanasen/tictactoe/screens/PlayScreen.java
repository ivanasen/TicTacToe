package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.TicTacToeMain;
import com.ivanasen.tictactoe.sprites.PlayerSymbol;
import com.ivanasen.tictactoe.sprites.Scoreboard;

import java.util.Stack;

/**
 * Created by ivan-asen on 13.09.16.
 */
public class PlayScreen implements Screen {
    private static final String TAG = PlayScreen.class.getSimpleName();
    private static final String SCOREBOARD_NAME = "scoreboard";

    private TicTacToeMain game;

    private Viewport viewport;

    private Stage stage;
    private Table gameGridTable;
    private Table table;

    private float gameGridSize;

    private Texture transparentTexture;
    private Image gameGrid;
    private Sprite backgroundSprite;
    private Image[][] gameCells;

    private Vector2 gameGridPosition;
    private Constants.CellState[][] gameTrackerArray;
    private boolean circleIsOnTurn;

    private boolean animated;
    private boolean gameEnded;

    private int moveCount;
    private Container<Table> tableContainer;
    private boolean tableNotAdded;

    private Image mainMenuBtn;
    private Scoreboard scoreboard;
    private Image restartBtn;

    private Image playerTurnCell;
    private boolean shouldResize;
    private Texture circleTurn;
    private Texture crossTurn;
    private Image undoBtn;
    private Stack<Vector2> moves;

    PlayScreen(TicTacToeMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new StretchViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        gameGridTable = new Table();
        tableContainer = new Container<>(gameGridTable);
        scoreboard = new Scoreboard();

        initGridVars();
        initGameTrackingVars();
        loadImages();

        createMainMenuButton();
        createUndoButton();
        createRestartButton();
        createHintForPlayerTurn();
        updateGameGridSize();
        createScoreBoard();
        createGameGridCells();

        stage.addActor(gameGrid);
        stage.addActor(table);
    }

    private void initGridVars() {
        animated = false;
        tableNotAdded = true;
        gameGridPosition = new Vector2();
        shouldResize = false;
    }

    private void createUndoButton() {
        Texture btnTexture = new Texture(Constants.UNDO_BTN_IMG);
        btnTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        undoBtn = new Image(btnTexture);
        undoBtn.setTouchable(Touchable.enabled);

        undoBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                undoBtn.getColor().a = Constants.BTN_COLOR_A_WHEN_PRESSED;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                undoBtn.getColor().a = 1;
                undoLastMove();
            }
        });

        undoBtn.setScale(Constants.BTN_SCALE);
        table.add(undoBtn).padTop(Constants.UNDO_BUTTON_PADDING).padLeft(Constants.UNDO_BUTTON_PADDING).top().right().expandX();
    }

    private void undoLastMove() {
        if (moves.empty())
            return;

        circleIsOnTurn = !circleIsOnTurn;
        createHintForPlayerTurn();
        moveCount--;
        Vector2 lastMove = moves.pop();
        clearCell((int) lastMove.x, (int) lastMove.y);
    }

    private void clearCell(int i, int j) {
        gameTrackerArray[i][j] = Constants.CellState.BLANK;
        gameCells[i][j].setDrawable(new SpriteDrawable(new Sprite(transparentTexture)));
        gameCells[i][j].setTouchable(Touchable.enabled);
    }

    private void createHintForPlayerTurn() {
        table.row();

        if (playerTurnCell == null && circleIsOnTurn) {
            playerTurnCell = new Image(circleTurn);
            table.add(playerTurnCell).colspan(Constants.GAME_3_COLS).expandX();
        } else if (playerTurnCell == null) {
            playerTurnCell = new Image(crossTurn);
            table.add(playerTurnCell).colspan(Constants.GAME_3_COLS).expandX();
        } else if (circleIsOnTurn) {
            table.getCell(playerTurnCell).getActor()
                    .setDrawable(new SpriteDrawable(new Sprite(circleTurn)));
        } else {
            table.getCell(playerTurnCell).getActor()
                    .setDrawable(new SpriteDrawable(new Sprite(crossTurn)));
        }
    }

    private void createScoreBoard() {
        table.row();
        table.add(scoreboard).colspan(Constants.GAME_3_COLS).bottom().expandX();
    }

    private void createRestartButton() {
        Texture btnTexture = new Texture(Constants.RESTART_BTN_IMG);
        btnTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        restartBtn = new Image(btnTexture);
        restartBtn.setTouchable(Touchable.enabled);

        restartBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                restartBtn.getColor().a = Constants.BTN_COLOR_A_WHEN_PRESSED;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                restartBtn.getColor().a = 1;
                playRestartAnimation();
                scoreboard.restart();
            }
        });

        table.add(restartBtn).pad(Constants.BUTTON_PADDING).top().right().expandX();
        table.row();
    }

    private void loadImages() {
        backgroundSprite = new Sprite(new Texture(Constants.BACKGROUND_IMG));

        Texture grid = new Texture(Constants.GAME_GRID_IMG);
        grid.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gameGrid = new Image(grid);
        gameCells = new Image[Constants.GRID_COUNT][Constants.GRID_COUNT];
        transparentTexture = new Texture(Constants.TRANSPARENT_IMG);

        circleTurn = new Texture(Constants.CIRCLE_TURN_IMG);
        crossTurn = new Texture(Constants.CROSS_TURN_IMG);
        circleTurn.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        crossTurn.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void createMainMenuButton() {
        Texture btnTexture = new Texture(Constants.MAIN_MENU_BTN_IMG);
        btnTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuBtn = new Image(btnTexture);
        mainMenuBtn.setTouchable(Touchable.enabled);

        mainMenuBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainMenuBtn.getColor().a = Constants.BTN_COLOR_A_WHEN_PRESSED;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                playExitAnimation();
            }
        });

        table.add(mainMenuBtn).padLeft(Constants.BUTTON_PADDING).top().left().expandX();
    }

    private void createGameGridCells() {
        for (int i = 0; i < gameCells.length; i++) {
            gameGridTable.row();
            for (int j = 0; j < gameCells[0].length; j++) {
                gameCells[i][j] = new Image(transparentTexture);
                gameGridTable.add(gameCells[i][j]).fill().expand();
                gameTrackerArray[i][j] = Constants.CellState.BLANK;

                final int finalI = i;
                final int finalJ = j;
                gameCells[i][j].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        moveCount++;
                        moves.push(new Vector2(finalI, finalJ));
                        gameTrackerArray[finalI][finalJ] = circleIsOnTurn ?
                                Constants.CellState.CIRCLE : Constants.CellState.CROSS;
                        gameCells[finalI][finalJ].setTouchable(Touchable.disabled);
                        PlayScreen.this.drawCurrentPlayerSymbol(gameCells[finalI][finalJ]);
                        circleIsOnTurn = !circleIsOnTurn;
                        createHintForPlayerTurn();
                    }
                });
            }
        }
    }

    private void drawCurrentPlayerSymbol(Image image) {
        SpriteDrawable symbol;
        if (circleIsOnTurn) {
            Texture circle = new Texture(Constants.CIRCLE_IMG);
            circle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            symbol = new PlayerSymbol(circle);
        } else {
            Texture cross = new Texture(Constants.CROSS_IMG);
            cross.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            symbol = new PlayerSymbol(cross);
        }
        image.getColor().a = 0;

        image.setDrawable(symbol);
        PlayerSymbol.animateSymbol(image, this);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        backgroundSprite.draw(game.batch);
        game.batch.end();

        stage.draw();
    }

    private void update(float delta) {
        if (!animated) {
            playEnterAnimation();
            animated = true;
        }

        backgroundSprite.setSize(viewport.getScreenWidth(), viewport.getScreenHeight());
        stage.act(delta);
    }

    private void playEnterAnimation() {
        scoreboard.getColor().a = 0;
        gameGrid.getColor().a = 0;
        restartBtn.getColor().a = 0;
        mainMenuBtn.getColor().a = 0;
        playerTurnCell.getColor().a = 0;
        undoBtn.getColor().a = 0;

        gameGrid.addAction(Actions.fadeIn(Constants.BASE_ANIMATION_DURATION));
        restartBtn.addAction(Actions.fadeIn(Constants.BASE_ANIMATION_DURATION));
        mainMenuBtn.addAction(Actions.fadeIn(Constants.BASE_ANIMATION_DURATION));
        playerTurnCell.addAction(Actions.fadeIn(Constants.BASE_ANIMATION_DURATION));
        undoBtn.addAction(Actions.fadeIn(Constants.BASE_ANIMATION_DURATION));
        scoreboard.addAction(Actions.parallel(Actions.moveTo(0, 0,
                Constants.BASE_ANIMATION_DURATION, Interpolation.exp10Out),
                Actions.fadeIn(Constants.BASE_ANIMATION_DURATION)));
    }

    private void playExitAnimation() {
        Action backToMainMenu = Actions.run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });

        for (Image[] gameCell : gameCells) {
            for (Image image : gameCell) {
                image.addAction(Actions.fadeOut(Constants.FADE_ANIM_DURATION));
            }
        }
        mainMenuBtn.addAction(Actions.sequence(
                Actions.fadeOut(Constants.FADE_ANIM_DURATION), backToMainMenu));
        gameGrid.addAction(Actions.fadeOut(Constants.FADE_ANIM_DURATION));
        restartBtn.addAction(Actions.fadeOut(Constants.FADE_ANIM_DURATION));
        playerTurnCell.addAction(Actions.fadeOut(Constants.FADE_ANIM_DURATION));
        undoBtn.addAction(Actions.fadeOut(Constants.FADE_ANIM_DURATION));

        scoreboard.addAction(Actions.parallel(
                Actions.moveTo(0,
                        -scoreboard.getHeight(),
                        Constants.BASE_ANIMATION_DURATION,
                        Interpolation.linear),
                Actions.fadeOut(Constants.FADE_ANIM_DURATION)));
    }

    public void checkIfGameHasEnded() {
        for (int row = 0; row < gameTrackerArray.length; row++) {
            for (int col = 0; col < gameTrackerArray[0].length; col++) {
                Constants.CellState currentCell = gameTrackerArray[row][col];

                if (currentCell == Constants.CellState.BLANK || currentCell == null)
                    continue;

                for (int i = 0; i < Constants.GRID_COUNT; i++) {
                    if (gameTrackerArray[row][i] != currentCell)
                        break;
                    if (i == Constants.GRID_COUNT - 1) {
                        gameEnded = true;
                        declareGameEnded(currentCell);
                        return;
                    }
                }

                for (int i = 0; i < Constants.GRID_COUNT; i++) {
                    if (gameTrackerArray[i][col] != currentCell)
                        break;
                    if (i == Constants.GRID_COUNT - 1) {
                        gameEnded = true;
                        declareGameEnded(currentCell);
                        return;
                    }
                }

                if (row == col) {
                    for (int i = 0; i < Constants.GRID_COUNT; i++) {
                        if (gameTrackerArray[i][i] != currentCell)
                            break;
                        if (i == Constants.GRID_COUNT - 1) {
                            gameEnded = true;
                            declareGameEnded(currentCell);
                            return;
                        }
                    }
                }

                for (int i = 0; i < Constants.GRID_COUNT; i++) {
                    if (gameTrackerArray[i][(Constants.GRID_COUNT - 1) - i] != currentCell)
                        break;
                    if (i == Constants.GRID_COUNT - 1) {
                        gameEnded = true;
                        declareGameEnded(currentCell);
                        return;
                    }
                }

                if (gameEnded && moveCount >= 9) {
                    declareGameEnded(currentCell);
                    return;
                }

                if (moveCount >= 9) {
                    declareGameEnded(Constants.CellState.BLANK);
                    return;
                }
            }
        }
    }

    private void declareGameEnded(Constants.CellState cellState) {
        if (cellState == Constants.CellState.CIRCLE) {
            scoreboard.setCircleWon();
        } else if (cellState == Constants.CellState.CROSS) {
            scoreboard.setCrossWon();
        }

        playRestartAnimation();
    }

    private void initGameTrackingVars() {
        gameTrackerArray = new Constants.CellState[Constants.GRID_COUNT][Constants.GRID_COUNT];
        circleIsOnTurn = !circleIsOnTurn;
        moveCount = 0;
        gameEnded = false;
        moves = new Stack<>();
    }

    private void restartGame() {
        initGameTrackingVars();
        gameGridTable.clear();
        createHintForPlayerTurn();
        createGameGridCells();
    }

    private void playRestartAnimation() {
        for (int i = 0; i < gameCells.length; i++) {
            Image[] gameCell = gameCells[i];
            for (int j = 0; j < gameCell.length; j++) {
                if (j == gameCell.length - 1 && i == gameCell.length - 1) {
                    gameCell[j].addAction(Actions.sequence(
                            Actions.fadeOut(Constants.RESTART_ANIM_DURATION),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    restartGame();
                                }
                            })));
                } else {
                    gameCell[j].addAction(Actions.fadeOut(Constants.FADE_ANIM_DURATION));
                }
            }
        }
    }

    private void updateGameGridSize() {
        float viewportWidth = stage.getCamera().viewportWidth;
        float viewportHeight = stage.getCamera().viewportHeight;
        gameGridSize = Constants.getGameGridSize(viewportWidth);
        gameGridPosition.x = (viewportWidth - gameGridSize) / 2f - Constants.GAME_GRID_PADDING_RIGHT;
        gameGridPosition.y = (viewportHeight - gameGridSize) / 2f + Constants.GAME_GRID_IMG_PADDING;

        gameGrid.setSize(gameGridSize, gameGridSize);

        if (tableNotAdded) {
            table.row();
            table.add(tableContainer).width(gameGridSize).height(gameGridSize)
                    .colspan(Constants.GAME_3_COLS).expand().center();
            tableNotAdded = false;
        } else {
            table.getCell(tableContainer).width(gameGridSize).height(gameGridSize)
                    .expand().center();
        }

        gameGrid.setPosition(gameGridPosition.x, gameGridPosition.y);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "resize");
        if (shouldResize) {
            stage.getCamera().update();
            updateGameGridSize();
        } else
            shouldResize = true;
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
        transparentTexture.dispose();
        scoreboard.dispose();
        circleTurn.dispose();
        crossTurn.dispose();
    }
}
