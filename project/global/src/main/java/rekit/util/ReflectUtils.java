package rekit.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import org.reflections.util.ConfigurationBuilder;
import rekit.config.GameConf;

/**
 * This class contains several methods for using Java Reflections in a proper
 * way.
 *
 * @author Dominik Fuchss
 *
 */
public final class ReflectUtils {
	/**
	 * Prevent instantiation.
	 */
	private ReflectUtils() {
	}

	/**
	 * Load all implementations that shall be loaded (see {@link LoadMe}) of a
	 * class by search path (-&gt; classpath). <br>
	 * If a class wants to be loaded, the class needs a <b>default
	 * constructor</b>
	 *
	 * @param searchPath
	 *            the search path (e.g. java.lang)
	 * @param loader
	 *            the class loader
	 * @param type
	 *            the class
	 * @param <T>
	 *            the class-type
	 * @return a set of instances of the found classes
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> loadInstances(String searchPath, ClassLoader loader, Class<T> type) {
		Set<T> objects = new HashSet<>();
		for (Class<?> clazz : ReflectUtils.getClasses(searchPath, loader, type)) {
			if (Modifier.isAbstract(clazz.getModifiers()) || clazz.getAnnotation(LoadMe.class) == null) {
				continue;
			}
			try {
				Constructor<?> c = clazz.getDeclaredConstructor();
				c.setAccessible(true);
				objects.add((T) c.newInstance());
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				GameConf.GAME_LOGGER.debug(clazz.getSimpleName() + " not loaded !");
			}
		}
		return objects;

	}

	/**
	 * Get all implementations of a class by search path (-&gt; classpath) .<br>
	 *
	 * @param searchPath
	 *            the search path (e.g. java.lang)
	 * @param loader
	 *            the class loader
	 * @param type
	 *            the class
	 * @param <T>
	 *            the class-type
	 * @return a set of the found classes
	 */
	public static <T> Set<Class<? extends T>> getClasses(String searchPath, ClassLoader loader, Class<T> type) {
		return new Reflections(new ConfigurationBuilder().forPackage(searchPath).addClassLoaders(loader)).getSubTypesOf(type);
	}

	/**
	 * Get all classes which are annotated with ... by search path (-&gt;
	 * classpath).<br>
	 * No subtypes will be returned except if they are annotated too.
	 *
	 * @param searchPath
	 *            the search path (e.g. java.lang)
	 * @param annotation
	 *            the class annotation
	 * @return a set of the found classes
	 */
	public static Set<Class<?>> getClassesAnnotated(String searchPath, Class<? extends Annotation> annotation) {
		return new Reflections(searchPath).getTypesAnnotatedWith(annotation, true);
	}

	/**
	 * This annotation has to be applied to Classes which shall be loaded as
	 * implementation of a specific class and shall be instantiated.<br>
	 * If a class wants to be loaded, the class needs a <b>default
	 * constructor</b>
	 *
	 * @author Dominik Fuchss
	 * @see ReflectUtils#loadInstances(String, ClassLoader, Class)
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface LoadMe {
	}

}
