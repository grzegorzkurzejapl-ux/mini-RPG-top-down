package com.myrpg.game;

import com.badlogic.gdx.Game;

public class MyRpgGame extends Game {
    @Override
    public void create() {
        Assets.load();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}
