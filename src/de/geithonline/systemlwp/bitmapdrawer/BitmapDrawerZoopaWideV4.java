package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaWideV4 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private int abstand = 8;
	private final float gap = 0.8f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerZoopaWideV4() {
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

		final Bitmap bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(cWidth * 0.035f);
		skaleDicke = Math.round(cWidth * 0.14f);
		offset = Math.round(cWidth * 0.011f);
		abstand = Math.round(cWidth * 0.015f);
		fontSize = Math.round(cWidth * 0.25f);
		fontSizeArc = Math.round(cWidth * 0.04f);

		drawBogen(level);
		drawSegmente(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, cHeight - cWidth / 2 - 5 - Settings.getVerticalPositionOffset(isPortrait()), null);
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

	@Override
	public void drawChargeStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset / 2);
		mArc.addArc(oval, 200, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextArcPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		bitmapCanvas.drawText("" + level, cWidth / 2, cWidth / 2 - 10, Settings.getTextPaint(level, fontSize));
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
