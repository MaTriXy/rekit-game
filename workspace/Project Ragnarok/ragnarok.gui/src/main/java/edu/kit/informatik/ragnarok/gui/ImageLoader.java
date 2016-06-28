package edu.kit.informatik.ragnarok.gui;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * This class helps to load images from the resources
 *
 * @author Dominik Fuchß
 *
 *
 */
public class ImageLoader {
	/**
	 * The cache
	 */
	private final static Map<String, Image> CACHE = new HashMap<>();
	/**
	 * The Device needed for creation of {@link Image Images}
	 */
	private static final Device DEVICE = Display.getDefault();
	/**
	 * The loader Object for the Resource loading
	 */
	private static final Object LOADER = new Object();

	/**
	 * Get the {@link Image} from the resources by name<br>
	 * <b>IMPORTANT: Do NOT dispose the images</b>
	 *
	 * @param src
	 *            the path relative to /images/
	 *
	 * @return the Image
	 */
	public synchronized static Image get(String src) {
		if (!ImageLoader.CACHE.containsKey(src)) {
			InputStream res = ImageLoader.LOADER.getClass().getResourceAsStream("/images/" + src);
			ImageLoader.CACHE.put(src, new Image(ImageLoader.DEVICE, res));
		}
		return ImageLoader.CACHE.get(src);
	}
}
