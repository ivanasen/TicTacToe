package com.ivanasen.tictactoe.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ivanasen.tictactoe.Constants;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.table;

/**
 * Created by ivan-asen on 16.10.16.
 */

public class Scoreboard extends Table {

    private int xWins;
    private int oWins;

    public Scoreboard() {
        super();
        init();
    }

    private void init() {
        Texture scoreboardTexture = new Texture(Constants.SCOREBOARD_IMG);
        scoreboardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.setBackground(new SpriteDrawable(new Sprite(scoreboardTexture)));
        this.addAction(Actions.moveTo(0, -this.getHeight()));
        this.setDebug(true);
    }

    public void setCrossWins(int xWins) {
        this.xWins = xWins;
    }

    public void setCircleWins(int oWins) {
        this.oWins = oWins;
    }

    //TODO: finish
    private void drawCircleWins() {}

    private void drawCrossWins() {}
}
