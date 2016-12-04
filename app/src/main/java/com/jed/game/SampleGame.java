package com.jed.game;

import android.os.Bundle;
import android.os.PowerManager;

import com.kilobolt.framework.Screen;
import com.kilobolt.framework.implementation.AndroidGame;

public class SampleGame extends AndroidGame {
    // states:
    // 0 = loading
    // 1 = menu
    // 2 = ingame
    int state = 0;

    boolean resumed = false;
    int posx = -1;
    int posy = -1;

    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
    }
    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public int getState() {
        return state;
    }

    // cannot call this 'isResumed', android already uses that method
    @Override
    public boolean Resumed() {
        return resumed;
    }

    @Override
    public int getPosx() {
        return posx;
    }

    @Override
    public int getPosy() {
        return posy;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void setResumed(boolean resumed) {
        this.resumed = resumed;
    }

    @Override
    public void setPosx(int posx) {
        this.posx = posx;
    }

    @Override
    public void setPosy(int posy) {
        this.posy = posy;
    }

    // load data from previous instance
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) { // this should ONLY be null the first time the game is loaded
            resumed = true;
            state = savedInstanceState.getInt("state");
            if(state < 2) {
                resumed = false;
            }
            int[] values = savedInstanceState.getIntArray("values");
            if(values != null && values.length >= 2) {
                posx = values[0];
                posy = values[1];
            }
        } else if(Assets.instanceState != null) {
            resumed = true;
            state = Assets.instanceState.getInt("state");
            if (state < 2) {
                resumed = false;
            }
            int[] values = Assets.instanceState.getIntArray("values");
            if (values != null && values.length >= 2) {
                posx = values[0];
                posy = values[1];
            }
        }
    }
    // save data for next instance
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("state", state);
        outState.putIntArray("values", new int[]{posx, posy});
        Bundle instanceState = new Bundle();
        instanceState.putInt("state", state);
        instanceState.putIntArray("values", new int[]{posx, posy});
        Assets.instanceState = instanceState;
        super.onSaveInstanceState(outState);
    }
}
