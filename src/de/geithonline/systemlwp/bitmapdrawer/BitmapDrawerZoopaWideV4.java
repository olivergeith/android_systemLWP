package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaWideV4 implements IBitmapDrawer {

	private int cHeight;
	private int cWidth;
	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private int abstand = 8;
	private final float gap = 0.8f;
	private int fontSize = 150;
	private Bitmap bitmap;
	private Canvas bitmapCanvas;
	private int level = -99;

	public BitmapDrawerZoopaWideV4() {
	}

	@Override
	public void draw(final int level, final Canvas canvas) {

		// Bitmap neu berechnen wenn Level sich �ndert oder Canvas dimensions
		// anders
		if (this.level != level || canvas.getWidth() != cWidth || canvas.getHeight() != cHeight) {
			cWidth = canvas.getWidth();
			cHeight = canvas.getHeight();
			bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
			bitmapCanvas = new Canvas(bitmap);

			bogenDicke = Math.round(cWidth * 0.035f);
			skaleDicke = Math.round(cWidth * 0.14f);
			offset = Math.round(cWidth * 0.011f);
			abstand = Math.round(cWidth * 0.015f);
			fontSize = Math.round(cWidth * 0.25f);

			drawBogen(level);
			drawSegmente(level);
			drawNumber(level);
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
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(0), 180 + Math.round(level * 1.8) - 1, 2, true, Settings.getZeigerPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset), 0, 360, true, Settings.getErasurePaint());

	}

	private void drawSegmente(final int level) {

		final int segmente = 20;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;
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
			final float startwinkel = 180f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(off), startwinkel, winkelOneSegment, true, paint);
		}

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawNumber(final int level) {

		// draw percentage Number
		bitmapCanvas.drawText("" + level, cWidth / 2, cWidth / 2 - 10, Settings.getTextPaint(level, fontSize));
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

}
