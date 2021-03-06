package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaWideV6 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private int abstand = 8;
	private final float gap = 0.8f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;
	private final float offsetFaktorUnten = 0.69f;

	public BitmapDrawerZoopaWideV6() {
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
			setBitmapSize(cWidth, Math.round(cWidth * offsetFaktorUnten), true);
		} else {
			// quer
			setBitmapSize(cHeight, Math.round(cHeight * offsetFaktorUnten), false);
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(bWidth * 0.035f);
		skaleDicke = Math.round(bWidth * 0.14f);
		offset = Math.round(bWidth * 0.011f);
		abstand = Math.round(bWidth * 0.015f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		drawBogen(level);
		drawSegmente(level);
		return bitmap;
	}

	private void drawBogen(final int level) {
		// Background
		bitmapCanvas.drawArc(getRectForOffset(offset), 157.5f, 225, true, getBackgroundPaint());
		// Level
		bitmapCanvas.drawArc(getRectForOffset(offset), 157.5f, Math.round(level * 2.25f), true, getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, getErasurePaint());
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(0), 157.5f + Math.round(level * 2.25f) - 1, 2, true, getZeigerPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset), 0, 360, true, getErasurePaint());

	}

	private void drawSegmente(final int level) {

		final int segmente = 20;
		final float winkelOneSegment = (225f - (segmente - 1) * gap) / segmente;
		final int zehner = level / 5;

		final int off = offset + bogenDicke + abstand;

		// Skala Hintergergrund einer
		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i < zehner || level == 100) {
				paint = getBatteryPaint(level);
			} else if (i == zehner) {
				paint = getZeigerPaint(level);
			} else {
				paint = getBackgroundPaint();
			}
			final float startwinkel = 157.5f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(off), startwinkel, winkelOneSegment, true, paint);
		}

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, getErasurePaint());
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 90 + Math.round(level * 1.8f);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + bogenDicke + abstand + skaleDicke + fontSizeArc);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = getTextPaint(level, fontSizeArc);
		p.setTextAlign(Align.CENTER);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - Math.round(bHeight * 0.1f), getNumberPaint(level, fontSize));
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
