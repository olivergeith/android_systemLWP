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

public class BitmapDrawerAokpCircleV1 extends BitmapDrawer {

	private int bWidth = 0;
	private int bHeight = 0;
	private int offset = 10;
	private int einerDicke = 70;
	private int abstand = 8;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerAokpCircleV1() {
	}

	@Override
	public boolean supportsCenter() {
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
		abstand = Math.round(bWidth * 0.02f);
		einerDicke = Math.round(bWidth * 0.15f);
		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(cWidth * 0.04f);

		drawSegmente(level);
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
		final int segmente = 12;
		final float winkelOneSegment = 15;

		// Backgroundpaint alpha erh�hren falls unter 32...sonst sieht man ja
		// nix ;-)
		final Paint paint = Settings.getBackgroundPaint();
		if (paint.getAlpha() < 32) {
			paint.setAlpha(32);
		}
		// Zahnrad hintergrund herstellen
		for (int i = 0; i < segmente; i++) {
			final float startwinkel = 270f + 7.5f + i * (winkelOneSegment + 15);
			bitmapCanvas.drawArc(getRectForOffset(offset), startwinkel, winkelOneSegment, true, Settings.getBackgroundPaint());
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + abstand), 0, 360, true, Settings.getErasurePaint());
		// Background
		bitmapCanvas.drawArc(getRectForOffset(offset + abstand), 270, 360, true, Settings.getBackgroundPaint());
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 0, 360, true, Settings.getErasurePaint());

		// overpaint level
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, Math.round(level * 3.6), true, Settings.getBatteryPaintSourceIn(level));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 270 + Math.round(level * 3.6);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + einerDicke + fontSizeArc);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextArcPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
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
