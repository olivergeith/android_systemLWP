package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerSimpleCircleV3 extends BitmapDrawer {

	private int offset = 10;
	private int einerDicke = 70;
	private int zehnerDicke = 70;
	private final float gap = 0.6f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerSimpleCircleV3() {
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

		einerDicke = Math.round(bWidth * 0.03f);
		zehnerDicke = Math.round(bWidth * 0.10f);
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
			if (i <= level || level == 100) {
				paint = Settings.getBackgroundPaint();
			} else {
				paint = Settings.getBatteryPaint(100);
			}
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke), 0, 360, true, Settings.getErasurePaint());

		for (int i = 0; i < segmente; i++) {
			if (i < level || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else if (i == level) {
				if (Settings.isShowZeiger()) {
					paint = Settings.getZeigerPaint(level);
				} else {
					paint = Settings.getBatteryPaint(level);
				}
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 0, 360, true,
				Settings.getErasurePaint());
		if (Settings.isShowRand()) {
			final Paint randPaint = Settings.getBackgroundPaint();
			randPaint.setColor(Color.WHITE);
			randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
			// äußeren Rand
			randPaint.setStrokeWidth(offset);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 270, 360, true, randPaint);
			// innere Fläche
			final Paint bgPaint2 = Settings.getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 270, 360, true, bgPaint2);
		}

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
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		final String text = "" + level;
		final Paint p = Settings.getNumberPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		final PointF point = getTextCenterToDraw(text, getRectForOffset(0), p);
		bitmapCanvas.drawText(text, point.x, point.y, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	private static PointF getTextCenterToDraw(final String text, final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
