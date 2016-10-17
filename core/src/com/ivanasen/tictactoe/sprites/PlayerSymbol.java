package com.ivanasen.tictactoe.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ivanasen.tictactoe.Constants;
import com.ivanasen.tictactoe.screens.PlayScreen;

/**
 * Created by ivan-asen on 02.10.16.
 */

public class PlayerSymbol extends SpriteDrawable {

    public PlayerSymbol(Texture playerSymbol) {
        super(new Sprite(playerSymbol));
    }

    public static void animateSymbol(Image image, PlayScreen screen) {
        float size = image.getWidth();
        float x = size / 2;
        float y = size / 2;

        image.setOrigin(x, y);

        float scaleX = image.getScaleX();
        float scaleY = image.getScaleY();

        image.scaleBy(-scaleX, -scaleY);

        Action fadeInAction = Actions.fadeIn(Constants.SYMBOL_ANIMATION_DURATION);
        Action scaleAction = Actions.scaleTo(scaleX, scaleY,
                Constants.SYMBOL_ANIMATION_DURATION, Interpolation.elasticOut);
        Action checkPlayerHasWonAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                screen.checkIfGameHasEnded();
            }
        });
        image.addAction(Actions.sequence(Actions.parallel(fadeInAction, scaleAction),
                checkPlayerHasWonAction));
    }
}
