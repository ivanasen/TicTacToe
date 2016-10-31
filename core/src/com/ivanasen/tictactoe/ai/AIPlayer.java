package com.ivanasen.tictactoe.ai;

import com.ivanasen.tictactoe.Board;
import com.ivanasen.tictactoe.Constants;

public abstract class AIPlayer {
    protected int SIZE = Constants.PlayscreenConstants.GRID_COUNT;

    protected Board board;

    protected Board.CellState mySeed;
    protected Board.CellState oppSeed;

    /** Constructor with reference to game board */
    public AIPlayer(Board board) {
        this.board = board;
    }

    /** Set/change the seed used by computer and opponent */
    public void setSeed(Board.CellState seed) {
        this.mySeed = seed;
        oppSeed = (mySeed == Board.CellState.CROSS) ? Board.CellState.CIRCLE : Board.CellState.CROSS;
    }

    public void updateBoard(Board board) {
        this.board = board;
    }

    public Board.CellState getSeed() {
        return mySeed;
    }

    /** Abstract method to get next move. Return int[2] of {row, col} */
    abstract public int[] move();
}
