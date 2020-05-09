package de.lostmekka.gamejam.boathell.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.lostmekka.gamejam.boathell.BoatHellGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 920;
		new LwjglApplication(new BoatHellGame(), config);
	}
}
