package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaWideV2 implements IBitmapDrawer {

	private int cHeight;
	private int cWidth;
	private int offset = 5;
	private int einerDicke = 30;
	private int zehnerDicke = 100;
	private int abstand = 8;
	private final float gap = 2f;
	private int fontSize = 150;
	private Bitmap bitmap;
	private Canvas bitmapCanvas;
	private int level = -99;

	public BitmapDrawerZoopaWideV2() {
	}

	@Override
	public void draw(final int level, final Canvas canvas) {

		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		// anders
		if (this.level != level || canvas.getWidth() != cWidth || canvas.getHeight() != cHeight) {
			cWidth = canvas.getWidth();
			cHeight = canvas.getHeight();
			bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
			bitmapCanvas = new Canvas(bitmap);

			einerDicke = Math.round(cWidth * 0.05f);
			zehnerDicke = Math.round(cWidth * 0.18f);
			offset = Math.round(cWidth * 0.011f);
			abstand = Math.round(cWidth * 0.015f);
			fontSize = Math.round(cWidth * 0.25f);

			drawSegmente(level);
		}

		canvas.drawBitmap(bitmap, 0, cHeight - cWidth / 2 - 5, null);
		this.level = level;
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;
		final int zehner = level / 10;

		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i <= zehner || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 180f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(offset), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + zehnerDicke), 0, 360, true, Settings.getErasurePaint());

		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(0), 180 + Math.round(level * 1.8) - 1, 2, true, Settings.getZeigerPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + zehnerDicke + offset), 0, 360, true, Settings.getErasurePaint());

		// draw percentage Number
		bitmapCanvas.drawText("" + level, cWidth / 2, cWidth / 2 - 10, Settings.getTextPaint(level, fontSize));

	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

}
