package com.jed.game;

import com.kilobolt.framework.Screen;
import com.kilobolt.framework.implementation.AndroidGame;

public class SampleGame extends AndroidGame {
    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
    }
    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }
}
