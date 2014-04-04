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

public class BitmapDrawerSimpleArcV3 extends BitmapDrawer {

	private int offset = 10;
	private int einerDicke = 70;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerSimpleArcV3() {
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
			setBitmapSize(cHeight, cHeight / 2, false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight,
				Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bWidth * 0.05f);
		offset = Math.round(bWidth * 0.01f);
		fontSize = Math.round(bWidth * 0.45f);
		fontSizeArc = Math.round(bWidth * 0.045f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {

		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), 180, 180,
				true, Settings.getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), 180,
				Math.round(level * 1.8), true, Settings.getBatteryPaint(level));

		// Zeiger
		if (Settings.isShowZeiger()) {
			final Paint zp = Settings.getZeigerPaint(level);
			zp.setShadowLayer(10, 0, 0, Color.BLACK);
			bitmapCanvas.drawArc(getRectForOffset(fontSizeArc),
					180 + Math.round(level * 1.8) - 1, 2, true, zp);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(
				getRectForOffset(offset + fontSizeArc + einerDicke), 0, 360,
				true, Settings.getErasurePaint());
		// Rand
		if (Settings.isShowRand()) {
			final Paint randPaint = Settings.getBackgroundPaint();
			randPaint.setColor(Color.WHITE);
			randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
			// äußeren Rand
			randPaint.setStrokeWidth(offset);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc
					+ einerDicke), 270, 360, true, randPaint);
			// innere Fläche
			final Paint bgPaint2 = Settings.getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc
					+ einerDicke), 270, 360, true, bgPaint2);
		}

	}

	@Override
	public void drawChargeStatusText(final int level) {
		final float startwinkel = 180f + level * 1.5f;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc + einerDicke);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0,
				Settings.getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		final Paint p = Settings.getNumberPaint(level, fontSize);
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - 10, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight * 2 - offset);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + einerDicke
				+ fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = Settings.getTextPaint(100, fontSizeArc, Align.CENTER,
				true, false);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
