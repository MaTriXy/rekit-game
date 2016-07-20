package edu.kit.informatik.ragnarok.controller;

import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.logic.Model;

/**
 * This Interface defines the Controller for the MVC
 *
 * @author Dominik Fuchß
 *
 */
public interface Controller {
	/**
	 * Get the controller
	 *
	 * @param model
	 *            the model
	 * @return the controller
	 */
	static Controller getController(Model model, View view) {
		return new ControllerImpl(model, view);
	}

	/**
	 * Start the Controller
	 *
	 * @param view
	 *            the view to attach all controls
	 */
	void start();

}
