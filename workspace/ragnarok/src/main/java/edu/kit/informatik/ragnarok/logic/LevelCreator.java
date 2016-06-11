package edu.kit.informatik.ragnarok.logic;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.logic.gameelements.Inanimate;

public class LevelCreator {

	private GameModel model;
	
	public LevelCreator(GameModel model) {
		for (int x = 1; x < 40; x++) {
			Inanimate inanim = new Inanimate(new Vec2D(x, 7), new Vec2D(1,1), new RGB(100, 0, 0));
			model.addGameElement(inanim);
		}
	}
	
}
