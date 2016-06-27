package edu.kit.informatik.ragnarok.logic.parallax;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;

public class HeapElementCloud extends HeapElement {

	public HeapElementCloud(HeapLayer parent, Vec pos, Vec size, RGBAColor col) {
		super(parent, pos, size, col);
		// R, G and B channel must be same for clouds
		if (col != null) {
			this.col = new RGBAColor(col.red, col.red, col.red, col.alpha);
		}
	}

	@Override
	public void render(Field f) {
		f.drawCircle(this.getPos(), this.getSize(), this.col);
	}

	@Override
	public HeapElement clone(HeapLayer parent, Vec pos, Vec size, RGBAColor col) {
		return new HeapElementCloud(parent, pos, size, col);
	}

}
