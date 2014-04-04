package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaCircleV3 extends BitmapDrawer {
	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private int abstand = 8;
	private final float gap = 2f;
	private int fontSize = 150;
	private Canvas bitmapCanvas;
	private int fontSizeArc = 20;

	public BitmapDrawerZoopaCircleV3() {

	}

	private Bitmap initDimensions() {
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

		bogenDicke = Math.round(bWidth * 0.035f);
		skaleDicke = Math.round(bWidth * 0.14f);
		offset = Math.round(bWidth * 0.02f);
		abstand = Math.round(bWidth * 0.015f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		return bitmap;
	}

	@Override
	public Bitmap drawBitmap(final int level) {
		final Bitmap bitmap = initDimensions();
		drawBogen(level);
		drawSegmente(level);
		drawZeiger(level);

		return bitmap;
	}

	private void drawBogen(final int level) {
		// Background
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, 360, true, Settings.getBackgroundPaint());
		// Level
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, Math.round(level * 3.6), true, Settings.getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, Settings.getErasurePaint());
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final int segmente = 101;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
		final float startwinkel = 270f + level * (winkelOneSegment + gap) + gap / 2;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + bogenDicke);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextPaint(level, fontSizeArc));
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
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
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(off), startwinkel, winkelOneSegment, true, paint);
		}

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawZeiger(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
		int zehner = level / 10;
		final int einer = level % 10;

		final int off = offset + bogenDicke + abstand;

		// Skala Hintergergrund einer
		final Paint paint = Settings.getBatteryPaint(level);
		if (zehner == 10) {
			zehner = 9;
		}
		final float startwinkel = 270f + zehner * (winkelOneSegment + gap) + gap / 2;

		// Fader
		int fader = einer;
		if (fader == 0) {
			fader = 1;
		}
		fader = paint.getAlpha() * fader / 10;
		paint.setAlpha(fader);
		// erstmal Hintergrund weglöschen
		// bitmapCanvas.drawArc(getRectForOffset(off), startwinkel,
		// winkelOneSegment, true, Settings.getErasurePaint());
		// dann neu zeichnen
		bitmapCanvas.drawArc(getRectForOffset(off), startwinkel, winkelOneSegment, true, paint);

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, Settings.getErasurePaint());
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberinCenterofBitmap(bitmapCanvas, level, fontSize);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
