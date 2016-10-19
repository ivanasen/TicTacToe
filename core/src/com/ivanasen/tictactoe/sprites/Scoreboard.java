package com.ivanasen.tictactoe.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.ivanasen.tictactoe.Constants;

/**
 * Created by ivan-asen on 16.10.16.
 */

public class Scoreboard extends Table {
    private static final String TAG = Scoreboard.class.getSimpleName();

    private int crossWins;
    private int circleWins;

    private boolean isCrossAdded;
    private boolean isCircleAdded;

    private Container<Label> circleWinsContainer;
    private Container<Label> crossWinsContainer;
    private Container<Image> resetBtnContainer;

    private BitmapFont font;
    private Label circleWinsLabel;
    private Label crossWinsLabel;
    private Image resetBtn;

    public Scoreboard() {
        super();
        init();
    }

    private void init() {
        Texture scoreboardTexture = new Texture(Constants.SCOREBOARD_IMG);
        scoreboardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        createFont();

        this.setBackground(new SpriteDrawable(new Sprite(scoreboardTexture)));
        this.addAction(Actions.moveTo(0, -this.getHeight()));

        isCircleAdded = false;
        isCrossAdded = false;

        crossWins = 0;
        circleWins = 0;

        createTableContainers();
        drawCrossWins();
        drawCircleWins();
    }

    private void createTableContainers() {
        createResetBtn();
        circleWinsContainer = new Container<>();
        crossWinsContainer = new Container<>();

        add(crossWinsContainer)
                .center()
                .padTop(Constants.SCORE_PADDING_TOP)
                .width(Constants.SCORE_CONT_WIDTH);
        add(resetBtn)
                .center()
                .width(Constants.RESET_BTN_WIDTH)
                .padLeft(Constants.RESET_BTN_PADDING)
                .padRight(Constants.RESET_BTN_PADDING)
                .padTop(Constants.RESET_BTN_PADDING_TOP);
        add(circleWinsContainer)
                .center()
                .padTop(Constants.SCORE_PADDING_TOP)
                .width(Constants.SCORE_CONT_WIDTH);
    }

    private void createFont() {
        FreeTypeFontGenerator fontGenerator =
                new FreeTypeFontGenerator(Gdx.files.internal(Constants.FONT_FILE));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = Constants.SCORE_FONT_SIZE;
        font = fontGenerator.generateFont(fontParameter);

        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        circleWinsLabel = new Label(Integer.toString(circleWins), style);
        circleWinsLabel.setFontScale(Constants.SCORE_FONT_SCALE);
        crossWinsLabel = new Label(Integer.toString(crossWins), style);
        crossWinsLabel.setFontScale(Constants.SCORE_FONT_SCALE);
        fontGenerator.dispose();
    }

    public void setCrossWon() {
        crossWins++;
        drawCrossWins();
    }

    public void setCircleWon() {
        circleWins++;
        drawCircleWins();
    }

    public void restart() {
        crossWins = 0;
        circleWins = 0;
        drawCrossWins();
        drawCircleWins();
    }

    private void createResetBtn() {
        Texture btnTexture = new Texture(Constants.RESET_BTN_IMG);
        btnTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        resetBtn =
                new Image(new SpriteDrawable(new Sprite(btnTexture)));
        resetBtn.setScaleY(Constants.BTN_SCALE);
        resetBtn.setTouchable(Touchable.enabled);

        resetBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetBtn.getColor().a = Constants.BTN_COLOR_A_WHEN_PRESSED;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                resetBtn.getColor().a = 1;
                restart();
            }
        });
    }

    private void drawCircleWins() {
        if (!isCircleAdded) {
            circleWinsContainer.setActor(circleWinsLabel);
            isCircleAdded = true;
        } else {
            circleWinsContainer.getActor().setText(Integer.toString(circleWins));
            circleWinsContainer.getActor().setAlignment(Align.center);
        }
    }

    private void drawCrossWins() {
        if (!isCrossAdded) {
            crossWinsContainer.setActor(crossWinsLabel);
            isCrossAdded = true;
        } else {
            crossWinsContainer.getActor().setText(Integer.toString(crossWins));
            crossWinsContainer.getActor().setAlignment(Align.center);
        }
    }

    public void dispose() {
        font.dispose();
    }

}
