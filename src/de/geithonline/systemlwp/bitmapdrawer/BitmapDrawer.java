package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;

public abstract class BitmapDrawer extends ColorProvider implements IBitmapDrawer {
	protected int cHeight = 0;
	protected int cWidth = 0;
	protected int bHeight = 0;
	protected int bWidth = 0;
	protected int level = -99;
	private Bitmap bitmap;
	private boolean isDrawIcon = false;

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
		// kein resizen wenn ein icon gemalt wird!
		if (isDrawIcon) {
			bHeight = h;
			bWidth = w;
			return;
		}

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
		// Bitmap neu berechnen wenn Level sich �ndert oder Canvas dimensions
		if (this.level != level || w != cWidth || h != cHeight || bitmap == null || forcedraw) {
			cWidth = w;
			cHeight = h;
			// Memory frei geben f�r altes bitmap
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

	@Override
	public Bitmap drawIcon(final int level, final int size) {
		final int h = size;
		final int w = size;
		// Bitmap neu berechnen wenn Level sich �ndert oder Canvas dimensions
		cWidth = w;
		cHeight = h;
		isDrawIcon = true;
		final Bitmap icon = drawBitmap(level);
		isDrawIcon = false;
		drawLevelNumber(level);
		return icon;
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final int fontSize) {
		drawLevelNumberCentered(canvas, level, fontSize, false);
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final int fontSize, final boolean dropShadow) {
		final String text = "" + level;
		final Paint p = getNumberPaint(level, fontSize, Align.CENTER, true, false);
		if (dropShadow) {
			p.setShadowLayer(10, 0, 0, Color.BLACK);
		}
		final PointF point = getTextCenterToDraw(new RectF(0, 0, bWidth, bHeight), p);
		canvas.drawText(text, point.x, point.y, p);
	}

	protected void drawLevelNumberBottom(final Canvas canvas, final int level, final int fontSize) {
		final Paint p = getNumberPaint(level, fontSize, Align.CENTER, true, false);
		canvas.drawText("" + level, bWidth / 2, bHeight - Math.round(bWidth * 0.01f), p);
	}

	private static PointF getTextCenterToDraw(final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds("69", 0, 2, textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
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

	@Override
	public boolean supportsLevelStyle() {
		return false;
	}

}
