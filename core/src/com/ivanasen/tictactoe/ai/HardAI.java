package com.ivanasen.tictactoe.ai;

import com.ivanasen.tictactoe.Board;

public class HardAI extends AI {

    @Override
    public int[] getMove(Board board, int turns) {
        if (board.getCell(1, 1) == Board.CellState.BLANK) {
            return Board.intToCell(4);
        }

        if (turns >= 3) {
            final int X_WIN = 0, O_WIN = 1;
            int[] winner = predictWin(board);

            if (getSeed() == Board.CellState.CIRCLE) {
                if (winner[O_WIN] != -1) {
                    return Board.intToCell(winner[O_WIN]);
                } else if (winner[X_WIN] != -1) {
                    return Board.intToCell(winner[X_WIN]);
                }
            } else if (getSeed() == Board.CellState.CROSS) {
                if (winner[X_WIN] != -1) {
                    return Board.intToCell(winner[X_WIN]);
                } else if (winner[O_WIN] != -1) {
                    return Board.intToCell(winner[O_WIN]);
                }
            }
        }

        final int[] corners = { 0, 6, 2, 8 };
        int[] currentCell;
        for (int i = 0; i < 4; i++) {
            currentCell = Board.intToCell(corners[i]);
            if (board.getCell(currentCell[0], currentCell[1]) == Board.CellState.BLANK) {
                return currentCell;
            }
        }

        for (int i = 1; i < 8; i += 2) {
            currentCell = Board.intToCell(i);
            if (board.getCell(currentCell[0], currentCell[1]) == Board.CellState.BLANK) {
                return currentCell;
            }
        }

        return null;
    }
}