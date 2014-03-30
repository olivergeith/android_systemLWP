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
import de.geithonline.systemlwp.utils.ColorHelper;

public class InstrumentVoltV1 {

	private final int borderDicke;
	private final int dicke;
	private final Canvas bitmapCanvas;
	private final RectF position;
	private final int fontSize;
	private final int fontSizeScala;

	public InstrumentVoltV1(final int borderDicke, final int dicke, final Canvas bitmapCanvas, final RectF position) {
		super();
		this.borderDicke = borderDicke;
		this.dicke = dicke;
		this.bitmapCanvas = bitmapCanvas;
		this.position = position;

		fontSize = Math.round(position.width() * 0.15f);
		fontSizeScala = Math.round(dicke * 0.4f);

	}

	public void draw(int value) {
		final int min = 0;
		final int max = 6000;
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
		randPaint.setStrokeWidth(borderDicke);
		randPaint.setStyle(Style.STROKE);

		final Paint zp = Settings.getZeigerPaint(100);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint textPaintSkala = Settings.getTextPaint(100, fontSizeScala, Align.CENTER, true, false);
		textPaintSkala.setTextAlign(Align.CENTER);
		textPaintSkala.setAlpha(255);
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

		for (int i = min; i <= max; i = i + 100) {
			final float winkel = 135 + (i - min) * winkelProEinheit;
			if (i % 1000 == 0) {
				// Skala
				final Path mArc = new Path();
				final RectF oval = getRectForOffset(borderDicke + fontSizeScala);
				mArc.addArc(oval, winkel - 18, 36);
				bitmapCanvas.drawTextOnPath("" + i / 1000f, mArc, 0, 0, textPaintSkala);
				// striche
				bitmapCanvas.drawArc(getRectForOffset(dicke - dicke * 4 / 10), winkel - 0.5f, 1f, true, zp);
			} else if (i % 500 == 0) {
				bitmapCanvas.drawArc(getRectForOffset(dicke - dicke * 3 / 10), winkel - 0.5f, 1f, true, zp);
			} else {
				bitmapCanvas.drawArc(getRectForOffset(dicke - dicke * 2 / 10), winkel - 0.5f, 1f, true, zp);
			}
		}
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(borderDicke), 135 + voltWinkel - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(dicke), 0, 360, true, Settings.getErasurePaint());

		// Rand
		if (Settings.isShowRand()) {
			// äußeren Rand
			bitmapCanvas.drawArc(getRectForOffset(borderDicke / 2), 0, 360, true, randPaint);
			// innere Fläche
			final Paint bgPaint2 = Settings.getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(borderDicke + dicke), 270, 360, true, bgPaint2);
			// innerer Rand
			bitmapCanvas.drawArc(getRectForOffset(borderDicke + dicke), 0, 360, true, randPaint);
		}
		final String text = "" + (value / 10) / 100f;
		final PointF point = getTextCenterToDraw(text, getRectForOffset(0), textPaint);
		bitmapCanvas.drawText(text, point.x, point.y, textPaint);
		// Text unten
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(2 * borderDicke);
		mArc.addArc(oval, 135, -90);
		bitmapCanvas.drawTextOnPath("Volt", mArc, 0, 0, textPaintSkala);
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
