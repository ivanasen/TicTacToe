package com.ivanasen.tictactoe.ai;

import com.ivanasen.tictactoe.Board;

import java.util.ArrayList;

public abstract class AI {

    private Board.CellState seed;

    public abstract int[] getMove(Board board, int turns);

    protected static ArrayList<Integer> getEmptyCells(Board board) {
        ArrayList<Integer> emptyCells = new ArrayList<>(9);

        for (int i = 0; i < 9; i++) {
            if (board.getCell(i / 3, i % 3) == Board.CellState.BLANK) {
                emptyCells.add(i);
            }
        }

        return emptyCells;
    }

    protected static int[] predictWin(Board board) {
        int[] winner;
        int[] cells = new int[3];
        int xWin = -1, oWin = -1;

        //Test columns
        for (int c = 0; (xWin == -1 || oWin == -1) && c < 3; c++ ) {
            for (int r = 0; r < 3; r++) {
                cells[r] = c * 3 + r;
            }

            winner = winsNextTurn(board, cells[0], cells[1], cells[2]);
            if (winner != null) {
                if (winner[0] == Board.CellState.CROSS.ordinal()) {
                    xWin = winner[1];
                } else if (winner[0] == Board.CellState.CIRCLE.ordinal()) {
                    oWin = winner[1];
                }
            }
        }

        //Test rows
        for (int r = 0; (xWin == -1 || oWin == -1) && r < 3; r++) {
            for (int c = 0; c < 3; c++ ) {
                cells[c] = c * 3 + r;
            }

            winner = winsNextTurn(board, cells[0], cells[1], cells[2]);
            if (winner != null) {
                if (winner[0] == Board.CellState.CROSS.ordinal()) {
                    xWin = winner[1];
                } else if (winner[0] == Board.CellState.CIRCLE.ordinal()) {
                    oWin = winner[1];
                }
            }
        }

        //Test diagonals
        if (xWin == -1 || oWin == -1) {
            winner = winsNextTurn(board, 0, 4, 8);

            if (winner != null) {
                if (winner[0] == Board.CellState.CROSS.ordinal()) {
                    xWin = winner[1];
                }
                else if (winner[0] == Board.CellState.CIRCLE.ordinal()) {
                    oWin = winner[1];
                }
            }
        }
        if (xWin == -1 || oWin == -1) {
            winner = winsNextTurn(board, 2, 4, 6);

            if (winner != null) {
                if (winner[0] == Board.CellState.CROSS.ordinal()) {
                    xWin = winner[1];
                }
                else if (winner[0] == Board.CellState.CIRCLE.ordinal()) {
                    oWin = winner[1];
                }
            }
        }

        int[] winningMoves = { xWin, oWin };
        return winningMoves;
    }

    private static int[] winsNextTurn(Board board, int cell1, int cell2, int cell3) {
        final int PLAYER = 0, CELL = 1;
        int[] winner = new int[2];

        Board.CellState cs1 = board.getCell(Board.intToCell(cell1)[0], Board.intToCell(cell1)[1]);
        Board.CellState cs2 = board.getCell(Board.intToCell(cell2)[0], Board.intToCell(cell2)[1]);
        Board.CellState cs3 = board.getCell(Board.intToCell(cell3)[0], Board.intToCell(cell3)[1]);

        if (cs1.matches(cs2) && cs3 == Board.CellState.BLANK) {
            winner[PLAYER] = cs1.ordinal();
            winner[CELL]   = cell3;
            return winner;
        }

        if (cs2.matches(cs3) && cs1 == Board.CellState.BLANK) {
            winner[PLAYER] = cs2.ordinal();
            winner[CELL]   = cell1;
            return winner;
        }

        if (cs1.matches(cs3) && cs2 == Board.CellState.BLANK) {
            winner[PLAYER] = cs1.ordinal();
            winner[CELL]   = cell2;
            return winner;
        }

        return null;
    }

    public void setSeed(Board.CellState cellState) {
        this.seed = cellState;
    }

    public Board.CellState getSeed() {
        return seed;
    }
}