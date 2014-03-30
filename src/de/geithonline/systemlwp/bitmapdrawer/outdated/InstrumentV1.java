package de.geithonline.systemlwp.bitmapdrawer.outdated;

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

public class InstrumentV1 {

	private final int borderDicke;
	private final int dicke;
	private final Canvas bitmapCanvas;
	private final RectF position;

	public InstrumentV1(final int borderDicke, final int dicke, final Canvas bitmapCanvas, final RectF position) {
		super();
		this.borderDicke = borderDicke;
		this.dicke = dicke;
		this.bitmapCanvas = bitmapCanvas;
		this.position = position;
	}

	public void draw(int value, final int min, final int max, final boolean centerText, final String einheit, final int fontSize,
			final int deviderSkala, final float deviderEinheit) {
		final int span = max - min;
		final float winkelProEinheit = 270f / span;

		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		final Paint randPaint = Settings.getBackgroundPaint();
		randPaint.setColor(Color.WHITE);
		randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint zp = Settings.getZeigerPaint(100);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint textPaint = Settings.getTextPaint(100, fontSize, Align.CENTER, true, false);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setAlpha(255);
		// scala
		bitmapCanvas.drawArc(getRectForOffset(0), 270, 360, true, Settings.getBackgroundPaint());
		// level
		final float voltWinkel = (value - min) * winkelProEinheit;
		// bitmapCanvas.drawArc(getRectForOffset(rand), 135, voltWinkel, true,
		// Settings.getBatteryPaint(volt));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(dicke), 0, 360, true, Settings.getErasurePaint());

		for (int i = min; i <= max; i = i + deviderSkala / 10) {
			final float winkel = 135 + (i - min) * winkelProEinheit;
			if (i % deviderSkala == 0) {
				// Skala
				final Path mArc = new Path();
				final RectF oval = getRectForOffset(borderDicke + fontSize);
				mArc.addArc(oval, winkel - 18, 36);
				bitmapCanvas.drawTextOnPath("" + (int) (i / deviderEinheit), mArc, 0, 0, textPaint);
				// striche
				bitmapCanvas.drawArc(getRectForOffset(dicke - dicke * 4 / 10), winkel - 0.5f, 1f, true, zp);
			} else if (i % (deviderSkala / 2) == 0) {
				bitmapCanvas.drawArc(getRectForOffset(dicke - dicke * 3 / 10), winkel - 0.5f, 1f, true, zp);
			} else {
				bitmapCanvas.drawArc(getRectForOffset(dicke - dicke * 2 / 10), winkel - 0.5f, 1f, true, zp);
			}
		}
		// Rand
		if (Settings.isShowRand()) {
			// äußeren Rand
			randPaint.setStrokeWidth(borderDicke);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(2), 0, 360, true, randPaint);
		}
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(borderDicke), 135 + voltWinkel - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(dicke), 0, 360, true, Settings.getErasurePaint());
		if (centerText) {
			final String text = "" + value / deviderEinheit + einheit;
			final PointF point = getTextCenterToDraw(text, getRectForOffset(0), textPaint);
			bitmapCanvas.drawText(text, point.x, point.y, textPaint);
		} else {
			// Text unten
			final Path mArc = new Path();
			final RectF oval = getRectForOffset(2 * borderDicke);
			mArc.addArc(oval, 135, -90);
			bitmapCanvas.drawTextOnPath("" + value / deviderEinheit + einheit, mArc, 0, 0, textPaint);
		}
	}

	private static PointF getTextCenterToDraw(final String text, final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(position.left + offset, position.top + offset, position.right - offset, position.bottom - offset);
	}
}
