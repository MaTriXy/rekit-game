package ragnarok.logic.scene;

import ragnarok.logic.GameModel;
import ragnarok.logic.level.LevelManager;

/**
 * This class realizes a LevelScene for BossRush levels.
 */
final class BossRushScene extends LevelScene {
	/**
	 * Create a new BossRush Scene.
	 *
	 * @param model
	 *            the model
	 */
	private BossRushScene(GameModel model) {
		super(model, LevelManager.getBossRushLevel());
	}

	/**
	 * Create method of the scene.
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options
	 * @return a new arcade scene.
	 */
	public static Scene create(GameModel model, String[] options) {
		return new BossRushScene(model);
	}

}
