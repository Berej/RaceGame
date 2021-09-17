package com.racegame.tbvm.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.racegame.tbvm.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RaceGame";
		config.height = 720;
		config.width = 1280;
		new LwjglApplication(new Main(), config);
	}
}
