package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerSimpleCircleV1 extends BitmapDrawer {

	private int offset = 10;
	private int einerDicke = 70;
	private final float gap = 0.6f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerSimpleCircleV1() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsShowPointer() {
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

		einerDicke = Math.round(bWidth * 0.15f);
		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {

		final int segmente = 101;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;

		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i < level || level == 100) {
				paint = getBatteryPaint(level);
			} else if (i == level) {
				if (Settings.isShowZeiger()) {
					paint = getZeigerPaint(level);
				} else {
					paint = getBatteryPaint(level);
				}
			} else {
				paint = getBackgroundPaint();
			}
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke), 0, 360, true, getErasurePaint());
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final int segmente = 101;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
		final float startwinkel = 270f + level * (winkelOneSegment + gap) + gap / 2;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + einerDicke + fontSizeArc);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, getTextPaint(level, fontSizeArc));
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
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
