package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.time.TimeDependency;

/**
 *
 * This class can decorate all {@link GuiElement GuiElements} so that they will
 * be deleted after a specific time
 *
 */
public class TimeDecorator extends GuiElement {
	/**
	 * The decorated GuiElement
	 */
	private GuiElement element;
	/**
	 * The timer
	 */
	private TimeDependency timer;

	/**
	 * Create a TimeDecorator
	 * 
	 * @param scene
	 *            the scene
	 * @param element
	 *            the decorated element
	 * @param timer
	 *            the timer
	 */
	public TimeDecorator(Scene scene, GuiElement element, TimeDependency timer) {
		super(scene);
		this.element = element;
		this.timer = timer;
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.timer.removeTime(deltaTime);
		if (this.timer.timeUp()) {
			this.visible = false;
			this.getScene().removeGuiElement(this);
		}

		this.element.logicLoop(deltaTime);
	}

	@Override
	public void internalRender(Field f) {
		this.element.internalRender(f);
	}

}
