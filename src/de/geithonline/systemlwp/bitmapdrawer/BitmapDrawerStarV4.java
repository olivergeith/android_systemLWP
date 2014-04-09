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

public class BitmapDrawerStarV4 extends BitmapDrawer {

	private int offset = 10;
	private int einerDicke = 70;
	private int outerRadius = 8;
	private int innerRadius = 8;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;
	private final int anzahlZacken = 50;

	public BitmapDrawerStarV4() {
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
			setBitmapSize(cWidth, cWidth, true);
		} else {
			// quer
			setBitmapSize(cHeight, cHeight, false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);
		offset = Math.round(bWidth * 0.01f);
		einerDicke = Math.round(bWidth * 0.19f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		outerRadius = bWidth / 2 - offset - fontSizeArc - fontSizeArc;
		innerRadius = Math.round(outerRadius - einerDicke * 0.5f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {
		final Paint bgPaint = getBackgroundPaint();
		final Paint btPaint = getBatteryPaint(100);
		// ausserer rahmen 1
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, -Math.round((100 - level) * 3.6), true, btPaint);
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, Math.round((level) * 3.6), true, bgPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(fontSizeArc), 0, 360, true, getErasurePaint());
		// ausserer rahmen 2
		bitmapCanvas.drawArc(getRectForOffset(fontSizeArc), 270, -Math.round((100 - level) * 3.6), true, bgPaint);
		bitmapCanvas.drawArc(getRectForOffset(fontSizeArc), 270, Math.round((level) * 3.6), true, btPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), 0, 360, true, getErasurePaint());

		final Point center = new Point(bWidth / 2, bHeight / 2);
		bitmapCanvas.drawPath(new StarPath(anzahlZacken, center, outerRadius, innerRadius), bgPaint);
		// overpaint level
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), 270, Math.round(level * 3.6), true, getBatteryPaintSourceIn(level));
		// Zeiger
		if (Settings.isShowZeiger()) {
			final Paint zp = getZeigerPaint(level);
			zp.setShadowLayer(10, 0, 0, Color.BLACK);
			bitmapCanvas.drawArc(getRectForOffset(0), 270 + Math.round(level * 3.6) - 1, 2, true, zp);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 0, 360, true, getErasurePaint());
		// Rand
		if (Settings.isShowRand()) {
			final Paint randPaint = getBackgroundPaint();
			randPaint.setColor(Color.WHITE);
			randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
			// äußeren Rand
			randPaint.setStrokeWidth(offset);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 270, 360, true, randPaint);
			// innere Fläche
			final Paint bgPaint2 = getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 270, 360, true, bgPaint2);
		}

	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 272 + Math.round(level * 3.6);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + fontSizeArc);
		mArc.addArc(oval, winkel, 180);
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
		final RectF oval = getRectForOffset(2 * offset + einerDicke);
		mArc.addArc(oval, 180, -180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
