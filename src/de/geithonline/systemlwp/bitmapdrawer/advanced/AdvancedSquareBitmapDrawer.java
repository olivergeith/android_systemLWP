package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;

public abstract class AdvancedSquareBitmapDrawer implements IBitmapDrawer {
	private int displayHeight = 0;
	private int displayWidth = 0;
	private Bitmap bitmap;
	protected Canvas bitmapCanvas;
	protected int bmpHeight = 0;
	protected int bmpWidth = 0;
	protected int level = -99;
	private boolean isDrawIcon = false;

	public abstract Bitmap drawBitmap(final int level, Bitmap Bitmap);

	public abstract void drawLevelNumber(final int level);

	public abstract void drawChargeStatusText(final int level);

	public abstract void drawBattStatusText();

	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		if (Settings.isCenteredBattery()) {
			canvas.drawBitmap(bitmap, displayWidth / 2 - bmpWidth / 2, displayHeight / 2 - bmpHeight / 2, null);
		} else {
			canvas.drawBitmap(bitmap, displayWidth / 2 - bmpWidth / 2, displayHeight - bmpHeight - Settings.getVerticalPositionOffset(isPortrait()), null);
		}
	}

	protected Bitmap initSquareBitmap() {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (isPortrait()) {
			// hochkant
			setBitmapSize(displayWidth, displayWidth, true);
		} else {
			// quer
			setBitmapSize(displayHeight, displayHeight, false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
		return bitmap;
	}

	protected void setBitmapSize(final int w, final int h, final boolean isPortrait) {
		// kein resizen wenn ein icon gemalt wird!
		if (isDrawIcon) {
			bmpHeight = h;
			bmpWidth = w;
			return;
		}

		if (isPortrait) {
			// hochkant
			bmpHeight = Math.round(h * Settings.getPortraitResizeFactor());
			bmpWidth = Math.round(w * Settings.getPortraitResizeFactor());
		} else {
			// landscape mode
			bmpHeight = Math.round(h * Settings.getLandscapeResizeFactor());
			bmpWidth = Math.round(w * Settings.getLandscapeResizeFactor());
		}
	}

	@Override
	public void draw(final int level, final Canvas canvas, final boolean forcedraw) {
		final int h = canvas.getHeight();
		final int w = canvas.getWidth();
		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		if (this.level != level || w != displayWidth || h != displayHeight || bitmap == null || forcedraw) {
			displayWidth = w;
			displayHeight = h;
			// Memory frei geben für altes bitmap
			if (bitmap != null) {
				bitmap.recycle();
			}
			// Bitnmap neu berechnen
			bitmap = initSquareBitmap();
			bitmapCanvas = new Canvas(bitmap);
			bitmap = drawBitmap(level, bitmap);
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
		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		displayWidth = w;
		displayHeight = h;
		isDrawIcon = true;
		Bitmap icon = initSquareBitmap();
		bitmapCanvas = new Canvas(icon);
		icon = drawBitmap(level, icon);
		isDrawIcon = false;
		drawLevelNumber(level);
		return icon;
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final float fontSize) {
		drawLevelNumberCentered(canvas, level, fontSize, false);
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final float fontSize, final boolean dropShadow) {
		final String text = "" + level;
		final Paint p = PaintProvider.getNumberPaint(level, fontSize, Align.CENTER, true, false);
		if (dropShadow) {
			p.setShadowLayer(10, 0, 0, Color.BLACK);
		}
		final PointF point = getTextCenterToDraw(new RectF(0, 0, bmpWidth, bmpHeight), p);
		canvas.drawText(text, point.x, point.y, p);
	}

	protected void drawLevelNumberBottom(final Canvas canvas, final int level, final float fontSize) {
		final Paint p = PaintProvider.getNumberPaint(level, fontSize, Align.CENTER, true, false);
		canvas.drawText("" + level, bmpWidth / 2, bmpHeight - Math.round(bmpWidth * 0.01f), p);
	}

	protected void drawLevelNumber(final Canvas canvas, final int level, final float fontSize, final PointF position) {
		final Paint p = PaintProvider.getNumberPaint(level, fontSize, Align.CENTER, true, false);
		canvas.drawText("" + level, position.x, position.y, p);
	}

	protected void drawLevelNumberCenteredInRect(final Canvas canvas, final int level, final String txt, final float fontSize, final RectF rect) {
		final Paint p = PaintProvider.getNumberPaint(level, fontSize, Align.CENTER, true, false);
		final PointF position = getTextCenterToDraw(rect, p);
		canvas.drawText(txt, position.x, position.y, p);
	}

	private static PointF getTextCenterToDraw(final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds("69", 0, 2, textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

	public int getcHeight() {
		return displayHeight;
	}

	public void setcHeight(final int cHeight) {
		displayHeight = cHeight;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	protected boolean isPortrait() {
		return displayHeight > displayWidth;
	}

	protected boolean isLandscape() {
		return displayHeight < displayWidth;
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
