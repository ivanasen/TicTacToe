package com.ivanasen.tictactoe;

public class Constants {

    public static final int V_HEIGHT = 1280;
    public static final int V_WIDTH = 720;

    public static class FileDirectories {
        public static final String LOGO_IMG = "main_menu_sprites/logo.png";
        public static final String PLAY_BTN_IMG = "main_menu_sprites/play_btn.png";
        public static final String SETTINGS_IMG = "main_menu_sprites/settings_btn.png";
        public static final String NO_ADS_IMG = "main_menu_sprites/no_ads.png";
        public static final String V_AI_IMG = "main_menu_sprites/player_v_ai.png";
        public static final String V_PLAYER_IMG = "main_menu_sprites/player_v_player.png";

        public static final String TRANSPARENT_IMG = "playscreen_sprites/transparent.png";
        public static final String GAME_GRID_IMG = "playscreen_sprites/grid.png";
        public static final String RESTART_BTN_IMG = "playscreen_sprites/restart_btn.png";
        public static final String RESET_BTN_IMG = "playscreen_sprites/reset_btn.png";
        public static final String SCOREBOARD_IMG = "playscreen_sprites/scoreboard.png";
        public static final String CIRCLE_TURN_IMG = "playscreen_sprites/circle_turn.png";
        public static final String CROSS_TURN_IMG = "playscreen_sprites/cross_turn.png";
        public static final String UNDO_BTN_IMG = "playscreen_sprites/undo_btn.png";

        public static final String CROSS_IMG = "cross.png";
        public static final String CIRCLE_IMG = "circle.png";
        public static final String BACKGROUND_IMG = "background.png";
        public static final String MAIN_MENU_BTN_IMG = "main_menu_btn.png";

        public static final String FONT_FILE = "fonts/Roboto-Light.ttf";
    }

    public static class Animations {
        public static final float BASE_ANIMATION_DURATION = 1;
        public static final float FADE_ANIM_DURATION = BASE_ANIMATION_DURATION / 2.5f;
        public static final float RESTART_ANIM_DURATION = BASE_ANIMATION_DURATION / 5;
        public static final float SYMBOL_ANIMATION_DURATION = 0.6f;
    }

    public static class PlayscreenConstants {
        public static final int GRID_COUNT = 3;
        public static final float BUTTON_PADDING = 16;
        public static final float GAME_GRID_IMG_PADDING = 145;
        public static final int SCORE_FONT_SIZE = 84;
        public static final float GAME_GRID_PADDING_RIGHT = 6;
        public static final int GAME_COLS = 3;
        public static final float UNDO_BUTTON_PADDING = 36;
        public static final float SCORE_PADDING_TOP = 88;
        public static final float SCORE_CONT_WIDTH = 185;
        public static final float BTN_SCALE = 1.5f;
        public static final float RESET_BTN_WIDTH = 160;
        public static final float RESET_BTN_PADDING_TOP = SCORE_PADDING_TOP + 50;
        public static final float AI_THINK_TIME = 0.8f;
        public static float BTN_COLOR_A_WHEN_PRESSED = 0.5f;
        public static final float RESET_BTN_PADDING = 25;

        public static float getGameGridSize(float viewportWidth) {
            return viewportWidth * 8f / 10f;
        }
    }
}
