package rekit.primitives.image;

/**
 * This class represents an abstract version of an Image.
 *
 * @author Dominik Fuchss
 *
 */
public final class AbstractImage {
	/**
	 * The width.
	 */
	public final int width;
	/**
	 * The height.
	 */
	public final int height;
	/**
	 * This array contains the RGBA values of the image in this order
	 * R-&gt;G-&gt;B-&gt;A.
	 */
	public final byte[] pixels;

	/**
	 * Create an Abstract Image.
	 *
	 * @param height
	 *            the height
	 * @param width
	 *            the width
	 * @param pixels
	 *            the pixels
	 */
	public AbstractImage(int height, int width, byte[] pixels) {
		this.height = height;
		this.width = width;
		this.pixels = pixels;
	}
}
