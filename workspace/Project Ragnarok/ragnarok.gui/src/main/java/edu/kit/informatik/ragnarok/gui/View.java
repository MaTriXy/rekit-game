package edu.kit.informatik.ragnarok.gui;

import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.util.InputHelper;

/**
 * This Interface defines the View for the MVC
 *
 * @author Dominik Fuchß
 *
 */
public interface View {
	/**
	 * Get the view
	 *
	 * @param model
	 *            the model
	 * @return the view
	 */
	static View getView(Model model) {
		return new GameView(model);
	}

	/**
	 * Start the view
	 */
	void start();

	/**
	 * Attach a {@link InputHelper}
	 * 
	 * @param inputHelper
	 *            the inputHelper
	 */
	void attachMe(InputHelper inputHelper);
}
