package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaWideV5 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private int abstand = 8;
	private final float gap = 0.8f;
	private int fontSize = 150;
	private Canvas bitmapCanvas;

	public BitmapDrawerZoopaWideV5() {
	}

	@Override
	public Bitmap drawBitmap(final int level, final Canvas canvas) {

		final Bitmap bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(cWidth * 0.01f);
		skaleDicke = Math.round(cWidth * 0.14f);
		offset = Math.round(cWidth * 0.011f);
		abstand = Math.round(cWidth * 0.015f);
		fontSize = Math.round(cWidth * 0.25f);

		drawSegmente(level);
		drawNumber(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, cHeight - cWidth / 2 - 5, null);
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;

		// Bogen 1
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, Settings.getBackgroundPaint());
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, Settings.getErasurePaint());

		// Skala Hintergergrund einer
		Paint paint;
		for (int i = 0; i < segmente; i++) {
			paint = Settings.getBackgroundPaint();
			final float startwinkel = 180f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset), startwinkel, winkelOneSegment, true, paint);

			final int winkel = -81 + (i * 18);
			bitmapCanvas.save();
			bitmapCanvas.rotate(winkel, cWidth / 2, cWidth / 2);
			bitmapCanvas.drawText("" + (i + 1) * 10, cWidth / 2, offset + bogenDicke + offset + skaleDicke * 2 / 3, Settings.getEraserTextPaint(fontSize / 4));
			bitmapCanvas.restore();

		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset + skaleDicke), 0, 360, true, Settings.getErasurePaint());

		// Bogen 2
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset + skaleDicke + offset), 180, 180, true, Settings.getBackgroundPaint());

		// overpaint level
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, Math.round(level * 1.8), true, Settings.getBatteryPaintSourceIn(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset + skaleDicke + offset + bogenDicke), 0, 360, true, Settings.getErasurePaint());

	}

	private void drawNumber(final int level) {

		// draw percentage Number
		bitmapCanvas.drawText("" + level, cWidth / 2, cWidth / 2 - 10, Settings.getTextPaint(level, fontSize));
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

}
