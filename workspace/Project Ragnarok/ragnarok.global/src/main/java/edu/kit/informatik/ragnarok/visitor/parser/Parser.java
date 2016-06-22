package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;

import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;
import edu.kit.informatik.ragnarok.visitor.Visitor;

/**
 * This interface defines a parser which will be used for parsing a String to a
 * specific Class for a {@link Visitor}
 *
 * @author Dominik Fuchß
 * @see VisitInfo
 *
 */
public interface Parser {
	/**
	 * Parse the definition to the specific class
	 * 
	 * @param obj
	 *            the Visitable Object
	 * @param field
	 *            the current field
	 * @param definition
	 *            the String definition
	 * @return {@code true} if successful, {@code false} otherwise
	 * @throws Exception
	 *             will thrown by Reflect stuff
	 *
	 */
	default boolean parse(Visitable obj, Field field, String definition) throws Exception {
		if (field == null || definition == null) {
			return false;
		}
		return true;
	}
}
