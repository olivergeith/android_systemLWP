package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerSimpleArcV1 extends BitmapDrawer {

	private int offset = 10;
	private int einerDicke = 70;
	private int zehnerDicke = 70;
	private final float gap = 0.6f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerSimpleArcV1() {
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
	public boolean supportsShowRand() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level) {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (cWidth < cHeight) {
			// hochkant
			setBitmapSize(cWidth, cWidth / 2, true);
		} else {
			// quer
			setBitmapSize(cWidth, cWidth / 2, false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bWidth * 0.03f);
		zehnerDicke = Math.round(bWidth * 0.12f);
		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.20f);
		fontSizeArc = Math.round(bWidth * 0.045f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {

		final int segmente = 100;
		final float winkelOneSegment = (180f - (segmente - 0) * gap) / segmente;

		Paint paint;
		final Paint bgPaint = getBackgroundPaint();
		final Paint btPaint = getBatteryPaint(100);
		for (int i = 0; i < segmente; i++) {
			if (i < level || level == 100) {
				paint = bgPaint;
			} else {
				paint = btPaint;
			}
			final float startwinkel = 180f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke), 0, 360, true, getErasurePaint());

		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset), 180, 180, true, getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset), 180, Math.round(level * 1.8), true, getBatteryPaint(level));

		// Skalatext
		drawScalaText();

		// Zeiger
		final Paint zp = getZeigerPaint(level);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		bitmapCanvas.drawArc(getRectForOffset(fontSizeArc), 180 + Math.round(level * 1.8) - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 0, 360, true, getErasurePaint());
		// Rand
		if (Settings.isShowRand()) {
			final Paint randPaint = getBackgroundPaint();
			randPaint.setColor(Color.WHITE);
			randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
			// äußeren Rand
			randPaint.setStrokeWidth(offset);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 180, 180, true, randPaint);
			// innere Fläche
			final Paint bgPaint2 = getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 180, 180, true, bgPaint2);
		}

	}

	private void drawScalaText() {
		final Paint p = getTextScalePaint(fontSizeArc, Align.CENTER, true);
		final RectF oval = getRectForOffset(offset + fontSizeArc + einerDicke + offset + fontSizeArc);
		for (int i = 0; i <= 100; i = i + 10) {
			if (i > 0 && i < 100) {
				final long winkel = 180 - 18 + Math.round(i * 1.8f);
				final Path mArc = new Path();
				mArc.addArc(oval, winkel, 36);
				bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
			}
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke / 2), (float) (180f + i * 1.8 - 0.5f), 1f, true, p);
		}
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final int segmente = 100;
		final float winkelOneSegment = (180f - (segmente - 0) * gap) / segmente;
		final float startwinkel = 182f + level * (winkelOneSegment + gap) + gap / 2;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke + fontSizeArc);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberBottom(bitmapCanvas, level, fontSize);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight * 2 - offset);
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
