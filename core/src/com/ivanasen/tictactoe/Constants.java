package com.ivanasen.tictactoe;

/**
 * Created by ivan-asen on 13.09.16.
 */
public class Constants {

    public static final int V_HEIGHT = 1280;
    public static final int V_WIDTH = 720;

    public static final String LOGO_IMG = "logo.png";
    public static final String PLAY_BTN_IMG = "play_btn.png";
    public static final String CROSS_IMG = "cross.png";
    public static final String CIRCLE_IMG = "circle.png";
    public static final String TRANSPARENT_IMG = "transparent.png";
    public static final String BACKGROUND_IMG = "background.png";
    public static final String GAME_GRID_IMG = "grid.png";
    public static final String MAIN_MENU_BTN_IMG = "main_menu_btn.png";
    public static final String RESTART_BTN_IMG = "restart_btn.png";
    public static final String RESET_BTN_IMG = "reset_btn.png";
    public static final String SCOREBOARD_IMG = "scoreboard.png";
    public static final String FONT_FILE = "fonts/Roboto-Light.ttf";
    public static final String CIRCLE_TURN_IMG = "circle_turn.png";
    public static final String CROSS_TURN_IMG = "cross_turn.png";
    public static final String UNDO_BTN_IMG = "undo_btn.png";

    public static final float BASE_ANIMATION_DURATION = 1;
    public static final float FADE_ANIM_DURATION = BASE_ANIMATION_DURATION / 2.5f;
    public static final float RESTART_ANIM_DURATION = BASE_ANIMATION_DURATION / 5;
    public static final float SYMBOL_ANIMATION_DURATION = 0.6f;

    public static final int GRID_COUNT = 3;
    public static final float BUTTON_PADDING = 16;
    public static final float GAME_GRID_IMG_PADDING = 145;
    public static final int SCORE_FONT_SIZE = 42;
    public static final int SCORE_FONT_SCALE = 2;
    public static final float GAME_GRID_PADDING_RIGHT = 6;
    public static final int GAME_3_COLS = 3;
    public static final float UNDO_BUTTON_PADDING = 36;
    public static final float RESTART_IMG_SIZE = 62;
    public static final float SCORE_PADDING_TOP = 88;
    public static final float SCORE_CONT_WIDTH = 185;
    public static final float BTN_SCALE = 1.5f;
    public static final float RESET_BTN_WIDTH = 160;
    public static final float RESET_BTN_PADDING_TOP = SCORE_PADDING_TOP + 50;
    public static float BTN_COLOR_A_WHEN_PRESSED = 0.5f;
    public static final float RESET_BTN_PADDING = 25;

    public enum CellState {
        BLANK, CIRCLE, CROSS
    }

    public static float getGameGridSize(float viewportWidth) {
        return viewportWidth * 8f / 10f;
    }
}
