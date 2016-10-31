package com.ivanasen.tictactoe.ai;

import com.ivanasen.tictactoe.Board;

/**
 * Created by ivan-asen on 22.10.16 in TicTacToe.
 */

public class AIPLayerEasy extends AIPlayer {
    // Moves {row, col} in order of preferences. {0, 0} at top-left corner
    private int[][] preferredMoves = {
            {1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2},
            {0, 1}, {1, 0}, {1, 2}, {2, 1}};

    /**
     * constructor
     */
    public AIPLayerEasy(Board board) {
        super(board);
    }

    /**
     * Search for the first empty cell, according to the preferences
     * Assume that next move is available, i.e., not gameover
     *
     * @return int[2] of {row, col}
     */
    @Override
    public int[] move() {
        for (int[] move : preferredMoves) {
            if (board.getCell(move[0], move[1]) == Board.CellState.BLANK) {
                return move;
            }
        }

        return null;
    }

}
