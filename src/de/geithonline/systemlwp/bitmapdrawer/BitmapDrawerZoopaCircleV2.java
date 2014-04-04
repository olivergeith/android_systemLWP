package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaCircleV2 extends BitmapDrawer {

	private int offset = 10;
	private int einerDicke = 30;
	private int zehnerDicke = 100;
	private int abstand = 8;
	private final float gap = 2f;
	private int fontSize = 150;
	private Canvas bitmapCanvas;
	private int fontSizeArc = 20;

	public BitmapDrawerZoopaCircleV2() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level) {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (cWidth < cHeight) {
			// hochkant
			setBitmapSize(cWidth, cWidth, true);
		} else {
			// quer
			setBitmapSize(cHeight, cHeight, false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bWidth * 0.05f);
		zehnerDicke = Math.round(bWidth * 0.12f);
		offset = Math.round(bWidth * 0.011f);
		abstand = Math.round(bWidth * 0.015f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		drawSegmente(level);

		return bitmap;
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
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
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
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
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke + abstand), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke + abstand + zehnerDicke), 0, 360, true, Settings.getErasurePaint());
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 180 + Math.round(level * 3.6);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + einerDicke + abstand + zehnerDicke + fontSizeArc);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = Settings.getTextPaint(level, fontSizeArc);
		p.setTextAlign(Align.CENTER);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSize);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
