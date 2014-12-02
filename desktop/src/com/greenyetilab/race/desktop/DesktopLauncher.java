package com.greenyetilab.race.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.greenyetilab.race.RaceGame;
import com.greenyetilab.utils.FileUtils;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        FileUtils.appName = "race";
        new LwjglApplication(new RaceGame(), config);
    }
}
