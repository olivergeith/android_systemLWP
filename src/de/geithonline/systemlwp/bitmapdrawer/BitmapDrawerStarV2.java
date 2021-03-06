package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.StarPath;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerStarV2 extends BitmapDrawer {

	private int offset = 10;
	private int einerDicke = 70;
	private int outerRadius = 8;
	private int innerRadius = 8;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;
	private final int anzahlZacken = 32;

	public BitmapDrawerStarV2() {
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

		einerDicke = Math.round(bWidth * 0.15f);
		outerRadius = bWidth / 2 - offset;
		innerRadius = Math.round(outerRadius - einerDicke * 0.7f);
		offset = Math.round(bWidth * 0.01f);
		fontSize = Math.round(bWidth * 0.30f);
		fontSizeArc = Math.round(bWidth * 0.04f);
		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {
		final Paint bgPaint = getBackgroundPaint();
		// bgPaint.setStyle(Style.STROKE);
		// bgPaint.setStrokeWidth(10);
		// bgPaint.setColor(Color.RED);

		final Point center = new Point(bWidth / 2, bHeight);
		bitmapCanvas.drawPath(new StarPath(anzahlZacken, center, outerRadius, innerRadius), bgPaint);
		// overpaint level
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, Math.round(level * 1.8), true, getBatteryPaintSourceIn(level));
		// Zeiger
		if (Settings.isShowZeiger()) {
			final Paint zp = getZeigerPaint(level);
			zp.setShadowLayer(10, 0, 0, Color.BLACK);
			bitmapCanvas.drawArc(getRectForOffset(0), 180 + Math.round(level * 1.8) - 1, 2, true, zp);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 0, 360, true, getErasurePaint());
		// Rand
		if (Settings.isShowRand()) {
			final Paint randPaint = getBackgroundPaint();
			randPaint.setColor(Color.WHITE);
			randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
			// �u�eren Rand
			randPaint.setStrokeWidth(offset);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 270, 360, true, randPaint);
			// innere Fl�che
			final Paint bgPaint2 = getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 270, 360, true, bgPaint2);
		}

	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 180 + Math.round(level * 1.3);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(einerDicke);
		mArc.addArc(oval, winkel, 180);
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
		final RectF oval = getRectForOffset(offset + einerDicke + fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
