package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ivanasen.tictactoe.Board;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.TicTacToeMain;
import com.ivanasen.tictactoe.ai.AI;
import com.ivanasen.tictactoe.sprites.PlayerSymbol;

import java.util.Random;

public class PlayScreenAI extends PlayScreen {

    private boolean aiThinking;
    private AI aiPlayer;
    private Board.CellState playerSeed;

    PlayScreenAI(TicTacToeMain game, AI aiPlayer) {
        super(game);
        this.aiPlayer = aiPlayer;
    }

    @Override
    protected void createHintForPlayerTurn() {
        if (playerTurnCell == null) {
            table.row();
            playerTurnCell = new Image();
            table.add(playerTurnCell).colspan(Constants.PlayscreenEntry.GAME_COLS).expandX();
        }

        Texture t;
        if (aiOnTurn) {
            t = (aiPlayer.getSeed() == Board.CellState.CIRCLE) ? circleTurn : crossTurn;
        } else {
            t = (playerSeed == Board.CellState.CIRCLE) ? circleTurn : crossTurn;
        }

        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        table.getCell(playerTurnCell).getActor()
                .setDrawable(new SpriteDrawable(new Sprite(t)));
    }

    @Override
    protected void processPlayerTurn(int row, int col) {
        if (row < 0 || col < 0)
            return;

        board.putMark(row, col, (aiOnTurn ?
                aiPlayer.getSeed() : playerSeed));
        turnCount++;
        moves.push(new Vector2(row, col));
        drawCurrentPlayerSymbol(gameCells[row][col]);
        gameCells[row][col].setTouchable(Touchable.disabled);
        aiOnTurn = !aiOnTurn;
        createHintForPlayerTurn();

        gridTable.setTouchable(aiOnTurn ? Touchable.disabled : Touchable.enabled);
    }

    @Override
    protected void drawCurrentPlayerSymbol(Image image) {
        SpriteDrawable symbol;
        if (aiOnTurn) {
            Texture t = new Texture((aiPlayer.getSeed() == Board.CellState.CIRCLE) ?
                    Constants.FileDirectories.CIRCLE_IMG : Constants.FileDirectories.CROSS_IMG);
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            symbol = new PlayerSymbol(t);
        } else {
            Texture t = new Texture((playerSeed == Board.CellState.CIRCLE) ?
                    Constants.FileDirectories.CIRCLE_IMG : Constants.FileDirectories.CROSS_IMG);
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            symbol = new PlayerSymbol(t);
        }
        image.getColor().a = 0;

        image.setDrawable(symbol);
        PlayerSymbol.animateSymbol(image, this);
    }

    @Override
    protected void update(float delta) {
        super.update(delta);

        if (!gameEnded && !aiThinking) {
            if (aiOnTurn) {
                playAiTurn();
            } else {
                gridTable.setTouchable(Touchable.enabled);
            }
        }
    }

    @Override
    void undoLastMove() {
        if (moves.empty())
            return;

        aiOnTurn = !aiOnTurn;
        createHintForPlayerTurn();
        turnCount--;
        Vector2 lastMove = moves.pop();
        clearCell((int) lastMove.x, (int) lastMove.y);
    }

    private void playAiTurn() {
        if (turnCount >= 9)
            return;

        int[] move = aiPlayer.getMove(board, turnCount);
        aiThinking = true;
        gridTable.addAction(Actions.sequence(
                Actions.delay(Constants.PlayscreenEntry.AI_THINK_TIME),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        aiThinking = false;
                        if (aiOnTurn) {
                            processPlayerTurn(move[0], move[1]);
                        }

                    }
                })
        ));
    }

    @Override
    protected void initGameTrackingVars() {
        super.initGameTrackingVars();

        if (playerSeed == null) {
            Preferences prefs = Gdx.app.getPreferences(Constants.SettingsEntry.PREFERENCES_NAME);
            String seed = prefs.getString(Constants.SettingsEntry.PLAYER_SYMBOL);
            playerSeed = seed.equals(Board.CellState.CIRCLE.toString()) ?
                    Board.CellState.CIRCLE : Board.CellState.CROSS;

            //randomise who will start the game
            Random r = new Random();
            aiOnTurn = r.nextBoolean();
        }

        aiPlayer.setSeed((playerSeed == Board.CellState.CIRCLE) ?
                Board.CellState.CROSS : Board.CellState.CIRCLE);
        aiThinking = false;
    }
}
