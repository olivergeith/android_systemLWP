package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;

public abstract class BitmapDrawer implements IBitmapDrawer {
	protected int cHeight = 0;
	protected int cWidth = 0;
	protected int bHeight = 0;
	protected int bWidth = 0;
	protected int level = -99;
	private Bitmap bitmap;

	public abstract Bitmap drawBitmap(final int level);

	public abstract void drawLevelNumber(final int level);

	public abstract void drawChargeStatusText(final int level);

	public abstract void drawBattStatusText();

	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		if (Settings.isCenteredBattery()) {
			canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight / 2 - bHeight / 2, null);
		} else {
			canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight - bHeight - Settings.getVerticalPositionOffset(isPortrait()), null);
		}
	}

	protected void setBitmapSize(final int w, final int h, final boolean isPortrait) {
		if (isPortrait) {
			// hochkant
			bHeight = Math.round(h * Settings.getPortraitResizeFactor());
			bWidth = Math.round(w * Settings.getPortraitResizeFactor());
		} else {
			// landscape mode
			bHeight = Math.round(h * Settings.getLandscapeResizeFactor());
			bWidth = Math.round(w * Settings.getLandscapeResizeFactor());
		}
	}

	@Override
	public void draw(final int level, final Canvas canvas, final boolean forcedraw) {
		final int h = canvas.getHeight();
		final int w = canvas.getWidth();
		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		if (this.level != level || w != cWidth || h != cHeight || bitmap == null || forcedraw) {
			cWidth = w;
			cHeight = h;
			// Memory frei geben für altes bitmap
			if (bitmap != null) {
				bitmap.recycle();
			}
			// Bitnmap neu berechnen
			bitmap = drawBitmap(level);
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

	public int getcHeight() {
		return cHeight;
	}

	public void setcHeight(final int cHeight) {
		this.cHeight = cHeight;
	}

	public int getcWidth() {
		return cWidth;
	}

	public void setcWidth(final int cWidth) {
		this.cWidth = cWidth;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	protected boolean isPortrait() {
		return cHeight > cWidth;
	}

	protected boolean isLandscape() {
		return cHeight < cWidth;
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
