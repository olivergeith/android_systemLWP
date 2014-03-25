package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaWideV1 extends BitmapDrawer {

	private int offset = 5;
	private int einerDicke = 30;
	private int zehnerDicke = 100;
	private int abstand = 8;
	private final float gap = 2f;
	private int fontSize = 150;
	private Canvas bitmapCanvas;
	private int fontSizeArc = 20;

	public BitmapDrawerZoopaWideV1() {
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

		einerDicke = Math.round(cWidth * 0.05f);
		zehnerDicke = Math.round(cWidth * 0.12f);
		offset = Math.round(cWidth * 0.011f);
		abstand = Math.round(cWidth * 0.015f);
		fontSize = Math.round(cWidth * 0.25f);
		fontSizeArc = Math.round(cWidth * 0.04f);

		drawSegmente(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, cHeight - cWidth / 2 - 5 - Settings.getVerticalPositionOffset(isPortrait()), null);
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;
		final int zehner = level / 10;
		final int einer = level % 10;

		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i < einer || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else if (i == einer) {
				paint = Settings.getZeigerPaint(level);
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 180f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(offset), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 0, 360, true, Settings.getErasurePaint());

		for (int i = 0; i < segmente; i++) {
			if (i < zehner || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else if (i == zehner) {
				paint = Settings.getZeigerPaint(level);
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 180f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke + abstand), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke + abstand + zehnerDicke), 0, 360, true, Settings.getErasurePaint());

	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		bitmapCanvas.drawText("" + level, cWidth / 2, cWidth / 2 - 10, Settings.getTextPaint(level, fontSize));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset / 2);
		mArc.addArc(oval, 200, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextArcPaint(level, fontSizeArc));
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

}
