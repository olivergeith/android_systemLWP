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

public class BitmapDrawerSimpleCircleV1 extends BitmapDrawer {

	private int bWidth = 0;
	private int bHeight = 0;
	private int offset = 10;
	private int einerDicke = 70;
	private final float gap = 0.6f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerSimpleCircleV1() {
	}

	@Override
	public boolean supportsCenter() {
		return true;
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsShowPointer() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Canvas canvas) {
		// welche kantge ist schmaler?
		if (cWidth < cHeight) {
			bWidth = cWidth;
			bHeight = cWidth;
		} else {
			bWidth = cHeight;
			bHeight = cHeight;
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bWidth * 0.15f);
		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(cWidth * 0.04f);

		drawSegmente(level);
		if (Settings.isShowNumber()) {
			drawNumber(level);
		}
		drawArcText(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		// draw mittig
		if (Settings.isCenteredBattery()) {
			canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight / 2 - bHeight / 2, null);
		} else {
			// draw unten
			if (cWidth < cHeight) {
				// unten
				canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight - bHeight, null);
			} else {
				// links
				canvas.drawBitmap(bitmap, cWidth - bWidth, cHeight / 2 - bHeight / 2, null);
			}
		}
	}

	private void drawSegmente(final int level) {

		final int segmente = 101;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;

		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i < level || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else if (i == level) {
				if (Settings.isShowZeiger()) {
					paint = Settings.getZeigerPaint(level);
				} else {
					paint = Settings.getBatteryPaint(level);
				}
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawArcText(final int level) {
		if (Settings.isCharging && Settings.isShowChargeState()) {
			final int segmente = 101;
			final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
			final float startwinkel = 270f + level * (winkelOneSegment + gap) + gap / 2;

			final Path mArc = new Path();
			final RectF oval = getRectForOffset(offset + einerDicke + fontSizeArc);
			mArc.addArc(oval, startwinkel, 180);
			final String text = Settings.getChargingText();
			bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextArcPaint(level, fontSizeArc));
		}
	}

	private void drawNumber(final int level) {
		final String text = "" + level;
		final Paint p = Settings.getTextPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		final PointF point = getTextCenterToDraw(text, getRectForOffset(0), p);
		bitmapCanvas.drawText(text, point.x, point.y, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	private static PointF getTextCenterToDraw(final String text, final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

}
