package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;

public abstract class BitmapDrawer implements IBitmapDrawer {
	protected int cHeight = 0;
	protected int cWidth = 0;
	protected int level = -99;
	private Bitmap bitmap;

	public abstract Bitmap drawBitmap(final int level, final Canvas canvas);

	public abstract void drawOnCanvas(Bitmap bitmap, Canvas canvas);

	@Override
	public void draw(final int level, final Canvas canvas) {
		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		// anders
		if (this.level != level || canvas.getWidth() != cWidth || canvas.getHeight() != cHeight || bitmap == null) {
			cWidth = canvas.getWidth();
			cHeight = canvas.getHeight();
			bitmap = drawBitmap(level, canvas);
		}
		// den aktuellen level merken
		this.level = level;
		if (Settings.isDebugging()) {
			BitmapHelper.saveBitmap(bitmap, getClass().getSimpleName(), level);
		}
		drawOnCanvas(bitmap, canvas);
	}

	@Override
	public boolean supportsOrientation() {
		return false;
	}

	@Override
	public boolean supportsCenter() {
		return false;
	}

	@Override
	public boolean supportsShowPointer() {
		return false;
	}

	@Override
	public boolean supportsPointerColor() {
		return false;
	}

}
