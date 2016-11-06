package com.ivanasen.tictactoe;

public class Board {

    public enum CellState {
        BLANK, CIRCLE, CROSS;


        /**
         * Compares two cell states to see if they're equal but not blank.
         *
         * @param state2 Compared cell state
         * @return True if matching and not blank
         */
        public boolean matches(CellState state2) {
            return this == state2 && state2 != BLANK;
        }


        @Override
        public String toString() {
            return super.toString();
        }
    }

    private CellState boardTrackerArray[][];

    public Board() {
        init();
    }

    public void init() {
        boardTrackerArray = new CellState
                [Constants.PlayscreenEntry.GRID_COUNT]
                [Constants.PlayscreenEntry.GRID_COUNT];
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

    /**
     * Converts an int to a cell. All cell numbers start at 0 and go top to bottom, left to right.
     * @param cellNumber Cell number to be converted
     * @return { column, row }
     */
    public static int[] intToCell(int cellNumber) {
        int cell[] = { cellNumber / 3, cellNumber % 3 };
        return cell;
    }

}

