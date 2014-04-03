package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Canvas;

public interface IBitmapDrawer {
	public void draw(final int level, final Canvas canvas, boolean forcedraw);

	public boolean supportsShowPointer();

	public boolean supportsPointerColor();

	public boolean supportsShowRand();
}
