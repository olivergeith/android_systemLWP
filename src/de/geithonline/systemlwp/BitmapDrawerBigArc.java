package de.geithonline.systemlwp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class BitmapDrawerBigArc {

	private int cHeight;
	private int cWidth;
	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private int abstand = 8;
	private final float gap = 2f;
	private int fontSize = 150;
	private Bitmap bitmap;
	private Canvas bitmapCanvas;
	private int level = -99;

	public BitmapDrawerBigArc() {
	}

	public void draw(final int level, final Canvas canvas) {

		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		// anders
		if (this.level != level || canvas.getWidth() != cWidth) {
			cWidth = canvas.getWidth();
			cHeight = canvas.getHeight();
			bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
			bitmapCanvas = new Canvas(bitmap);

			bogenDicke = Math.round(cWidth * 0.035f);
			skaleDicke = Math.round(cWidth * 0.14f);
			offset = Math.round(cWidth * 0.011f);
			abstand = Math.round(cWidth * 0.015f);
			fontSize = Math.round(cWidth * 0.25f);
			final Bitmap bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
			final Canvas bitmapCanvas = new Canvas(bitmap);

			drawBogen(level);
			drawSegmente(level);
			drawZeiger(level);
		}

		canvas.drawBitmap(bitmap, 0, cHeight - cWidth / 2 - 5, null);
		this.level = level;
	}

	private void drawBogen(final int level) {
		// Background
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, Settings.getBackgroundPaint());
		// Level
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, Math.round(level * 1.8), true, Settings.getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;
		final int zehner = level / 10;

		final int off = offset + bogenDicke + abstand;

		// Skala Hintergergrund einer
		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i < zehner || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 180f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(off), startwinkel, winkelOneSegment, true, paint);
		}

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawZeiger(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;
		int zehner = level / 10;
		final int einer = level % 10;

		final int off = offset + bogenDicke + abstand;
		final int radiusDelta = skaleDicke * einer / 10;

		// Skala Hintergergrund einer
		final Paint paint = Settings.getBatteryPaint(level);
		if (zehner == 10)
			zehner = 9;
		final float startwinkel = 180f + zehner * (winkelOneSegment + gap);

		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke - radiusDelta), startwinkel, winkelOneSegment, true, paint);

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, Settings.getErasurePaint());

		// draw percentage Number
		bitmapCanvas.drawText("" + level, cWidth / 2, cWidth / 2 - 10, Settings.getTextPaint(level, fontSize));
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

}
