package com.ivanasen.tictactoe;

import java.util.Arrays;

public class Board {

    public enum CellState {
        BLANK, CIRCLE, CROSS;
    }

    private CellState boardTrackerArray[][];

    public Board() {
        init();
    }

    public void init() {
        boardTrackerArray = new CellState
                [Constants.PlayscreenConstants.GRID_COUNT]
                [Constants.PlayscreenConstants.GRID_COUNT];
        for (int i = 0; i < boardTrackerArray.length; i++) {
            for (int j = 0; j < boardTrackerArray.length; j++) {
                boardTrackerArray[i][j] = CellState.BLANK;
            }
        }
    }

    public void clearCell(int row, int col) {
        boardTrackerArray[row][col] = CellState.BLANK;
    }


    public void putMark(int row, int col, CellState cellState) {
        boardTrackerArray[row][col] = cellState;
    }

    public CellState getCell(int row, int col) {
        return boardTrackerArray[row][col];
    }

    public int getSize() {
        return boardTrackerArray.length;
    }
}

