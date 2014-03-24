package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaWideV6 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private int abstand = 8;
	private final float gap = 0.8f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;
	private int bWidth = 0;
	private int bHeight = 0;
	private final float offsetFaktorUnten = 0.69f;

	public BitmapDrawerZoopaWideV6() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsMoveUP() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Canvas canvas) {
		// welche kantge ist schmaler?
		if (cWidth < cHeight) {
			bWidth = cWidth;
			bHeight = Math.round(bWidth * offsetFaktorUnten);
		} else {
			bWidth = cHeight;
			bHeight = Math.round(bWidth * offsetFaktorUnten);
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(bWidth * 0.035f);
		skaleDicke = Math.round(bWidth * 0.14f);
		offset = Math.round(bWidth * 0.011f);
		abstand = Math.round(bWidth * 0.015f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		drawBogen(level);
		drawSegmente(level);
		if (Settings.isShowNumber()) {
			drawNumber(level);
		}
		drawArcText(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, cHeight - bHeight - offset - Settings.getVerticalPositionOffset(isPortrait()), null);
	}

	private void drawBogen(final int level) {
		// Background
		bitmapCanvas.drawArc(getRectForOffset(offset), 157.5f, 225, true, Settings.getBackgroundPaint());
		// Level
		bitmapCanvas.drawArc(getRectForOffset(offset), 157.5f, Math.round(level * 2.25f), true, Settings.getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, Settings.getErasurePaint());
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(0), 157.5f + Math.round(level * 2.25f) - 1, 2, true, Settings.getZeigerPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset), 0, 360, true, Settings.getErasurePaint());

	}

	private void drawSegmente(final int level) {

		final int segmente = 20;
		final float winkelOneSegment = (225f - (segmente - 1) * gap) / segmente;
		final int zehner = level / 5;

		final int off = offset + bogenDicke + abstand;

		// Skala Hintergergrund einer
		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i < zehner || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else if (i == zehner) {
				paint = Settings.getZeigerPaint(level);
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 157.5f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(off), startwinkel, winkelOneSegment, true, paint);
		}

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawArcText(final int level) {
		if (Settings.isCharging && Settings.isShowChargeState()) {
			final long winkel = 90 + Math.round(level * 1.8f);

			final Path mArc = new Path();
			final RectF oval = getRectForOffset(offset + bogenDicke + abstand + skaleDicke + fontSizeArc);
			mArc.addArc(oval, winkel, 180);
			final String text = Settings.getChargingText();
			final Paint p = Settings.getTextArcPaint(level, fontSizeArc);
			p.setTextAlign(Align.CENTER);
			bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
		}
	}

	// private void drawNumber(final int level) {
	// final String text = "" + level;
	// final Paint p = Settings.getTextPaint(level, fontSize);
	// p.setTextAlign(Align.CENTER);
	// final PointF point = getTextCenterToDraw(text, getRectForOffset(0), p);
	// bitmapCanvas.drawText(text, point.x, point.y, p);
	// }

	private void drawNumber(final int level) {
		// draw percentage Number
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - Math.round(bHeight * 0.1f), Settings.getTextPaint(level, fontSize));
	}

	private static PointF getTextCenterToDraw(final String text, final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

}
