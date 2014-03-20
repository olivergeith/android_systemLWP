package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Canvas;

public interface IBitmapDrawer {
	public void draw(final int level, final Canvas canvas);

	public boolean supportsOrientation();

	public boolean supportsMoveUP();

	public boolean supportsCenter();

	public boolean supportsShowPointer();

	public boolean supportsPointerColor();
}
