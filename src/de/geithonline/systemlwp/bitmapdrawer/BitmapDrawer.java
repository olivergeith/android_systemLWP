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

	public abstract void drawLevelNumber(final int level);

	public abstract void drawChargeStatusText(final int level);

	public abstract void drawBattStatusText();

	public abstract void drawOnCanvas(Bitmap bitmap, Canvas canvas);

	@Override
	public void draw(final int level, final Canvas canvas, final boolean forcedraw) {
		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		// anders
		if (this.level != level || canvas.getWidth() != cWidth || canvas.getHeight() != cHeight || bitmap == null || forcedraw) {
			cWidth = canvas.getWidth();
			cHeight = canvas.getHeight();
			// Memory frei geben für altes bitmap
			if (bitmap != null) {
				bitmap.recycle();
			}
			// Bitnmap neu berechnen
			bitmap = drawBitmap(level, canvas);
			if (Settings.isShowNumber()) {
				drawLevelNumber(level);
			}
			if (Settings.isCharging && Settings.isShowChargeState()) {
				drawChargeStatusText(level);
			}
			if (Settings.isShowStatus()) {
				drawBattStatusText();
			}
		}
		// den aktuellen level merken
		this.level = level;
		if (Settings.isDebugging()) {
			BitmapHelper.saveBitmap(bitmap, getClass().getSimpleName(), level);
		}
		drawOnCanvas(bitmap, canvas);
	}

	protected boolean isPortrait() {
		return cHeight > cWidth;
	}

	protected boolean isLandscape() {
		return cHeight < cWidth;
	}

	@Override
	public boolean supportsMoveUP() {
		return false;
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

	@Override
	public boolean supportsShowRand() {
		return false;
	}

}
