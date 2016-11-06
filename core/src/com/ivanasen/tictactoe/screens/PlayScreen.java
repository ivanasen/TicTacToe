package com.ivanasen.tictactoe.screens;

import com.badlogic.gdx.Screen;

public abstract class PlayScreen implements Screen {

    public abstract void checkIfGameHasEnded();

    public abstract void restartGame();
}
