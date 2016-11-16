package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ivanasen.tictactoe.Board;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.TicTacToeMain;
import com.ivanasen.tictactoe.sprites.PlayerSymbol;

public class PlayScreenTwoPlayers extends PlayScreen {

    private boolean circleOnTurn;

    public PlayScreenTwoPlayers(TicTacToeMain game) {
        super(game);
    }

    @Override
    void undoLastMove() {
        if (moves.empty())
            return;

        circleOnTurn = !circleOnTurn;
        createHintForPlayerTurn();
        turnCount--;
        Vector2 lastMove = moves.pop();
        clearCell((int) lastMove.x, (int) lastMove.y);
    }

    @Override
    protected void createHintForPlayerTurn() {
        if (playerTurnCell == null) {
            table.row();
            playerTurnCell = new Image();
            table.add(playerTurnCell).colspan(Constants.PlayscreenEntry.GAME_COLS).expandX();
        }

        Texture t = circleOnTurn ? circleTurn : crossTurn;
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        table.getCell(playerTurnCell).getActor()
                .setDrawable(new SpriteDrawable(new Sprite(t)));

    }

    @Override
    protected void processPlayerTurn(int row, int col) {
        if (row < 0 || col < 0)
            return;

        board.putMark(row, col, (circleOnTurn ?
                Board.CellState.CIRCLE : Board.CellState.CROSS));
        turnCount++;
        moves.push(new Vector2(row, col));
        drawCurrentPlayerSymbol(gameCells[row][col]);
        gameCells[row][col].setTouchable(Touchable.disabled);
        circleOnTurn = !circleOnTurn;
        createHintForPlayerTurn();
    }

    @Override
    protected void drawCurrentPlayerSymbol(Image image) {
        SpriteDrawable symbol;

        Texture t = new Texture(circleOnTurn ?
                Constants.FileDirectories.CIRCLE_IMG : Constants.FileDirectories.CROSS_IMG);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        symbol = new PlayerSymbol(t);

        image.getColor().a = 0;
        image.setDrawable(symbol);
        PlayerSymbol.animateSymbol(image, this);
    }
}
