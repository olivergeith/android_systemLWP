package de.geithonline.systemlwp.bitmapdrawer.outdated;

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
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawer;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerTachoV6a extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 5;
	private int levelDicke = 100;
	private int voltDicke = 100;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	protected Canvas bitmapCanvas;

	private int bWidth = 0;
	private int bHeight = 0;

	public BitmapDrawerTachoV6a() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsCenter() {
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
			bWidth = cWidth;
			bHeight = cWidth;
		} else {
			// quer
			bWidth = cHeight;
			bHeight = cHeight;
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(bWidth * 0.01f);
		levelDicke = Math.round(bWidth * 0.12f);
		voltDicke = Math.round(bWidth * 0.09f);
		fontSize = Math.round(bWidth * 0.20f);
		fontSizeArc = Math.round(bWidth * 0.04f);
		offset = Math.round(bWidth * 0.02f);

		final InstrumentV1 levelInst = new InstrumentV1(bogenDicke, levelDicke, bitmapCanvas, getRectForOffset(offset));
		final InstrumentV1 voltInst = new InstrumentV1(bogenDicke, voltDicke, bitmapCanvas, getRectForOffset(offset + levelDicke));
		final InstrumentV1 tempInst = new InstrumentV1(bogenDicke, voltDicke, bitmapCanvas, getRectForOffset(offset + levelDicke
				+ voltDicke));

		// Skalatext
		// drawLevelScala(level, offset, levelDicke);
		levelInst.draw(level, 0, 100, false, " %", fontSizeArc, 10, 1);
		voltInst.draw(Settings.battVoltage, 2000, 5000, false, " Volt", fontSizeArc, 1000, 1000);
		tempInst.draw(Settings.battTemperature, 0, 600, false, " °C", fontSizeArc, 100, 10);
		// drawVoltageScala(Settings.battVoltage, offset + levelDicke,
		// voltDicke);
		// drawTemperatureScala(Settings.battTemperature, offset + levelDicke +
		// voltDicke, voltDicke);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		// draw mittig
		if (Settings.isCenteredBattery()) {
			canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight / 2 - bHeight / 2, null);
		} else {
			// draw unten
			if (cWidth < cHeight) {
				// unten
				canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight - bHeight, null);
			} else {
				// links
				canvas.drawBitmap(bitmap, cWidth - bWidth, cHeight / 2 - bHeight / 2, null);
			}
		}
	}

	private void drawLevelScala(final int level, final int rand, final int dicke) {
		final Paint randPaint = Settings.getBackgroundPaint();
		randPaint.setColor(Color.WHITE);
		randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint zp = Settings.getZeigerPaint(100);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint textPaint = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setAlpha(255);
		// scala
		bitmapCanvas.drawArc(getRectForOffset(rand), 0, 360, true, Settings.getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(rand), 135, Math.round(level * 2.7f), true, Settings.getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(rand + dicke), 0, 360, true, Settings.getErasurePaint());

		for (int i = 0; i <= 100; i = i + 1) {
			final float winkel = 135 + i * 2.7f;
			if (i % 10 == 0) {
				// Skala
				final Path mArc = new Path();
				final RectF oval = getRectForOffset(rand + bogenDicke + fontSizeArc);
				mArc.addArc(oval, winkel - 18, 36);
				bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, textPaint);
				// striche
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 4 / 10), winkel - 0.5f, 1f, true, zp);
			} else if (i % 5 == 0) {
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 3 / 10), winkel - 0.5f, 1f, true, zp);
			} else {
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 2 / 10), winkel - 0.5f, 1f, true, zp);
			}
		}
		// Rand
		if (Settings.isShowRand()) {
			// äußeren Rand
			randPaint.setStrokeWidth(bogenDicke);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(rand + 2), 0, 360, true, randPaint);
		}
		// Text unten
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(rand + 2 * bogenDicke);
		mArc.addArc(oval, 135, -90);
		bitmapCanvas.drawTextOnPath("Battery-Level " + (level) + " %", mArc, 0, 0, textPaint);
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(rand - bogenDicke), 135 + Math.round(level * 2.7f) - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(rand + dicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawVoltageScala(int volt, final int rand, final int dicke) {
		final int min = 1000;
		final int max = 5000; // millivolt
		final int span = max - min;
		final float winkelProMillivolt = 270f / span;

		if (volt < min) {
			volt = min;
		}
		if (volt > max) {
			volt = max;
		}
		final Paint randPaint = Settings.getBackgroundPaint();
		randPaint.setColor(Color.WHITE);
		randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint zp = Settings.getZeigerPaint(100);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint textPaint = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setAlpha(255);
		// scala
		bitmapCanvas.drawArc(getRectForOffset(rand), 270, 360, true, Settings.getBackgroundPaint());
		// level
		final float voltWinkel = (volt - min) * winkelProMillivolt;
		// bitmapCanvas.drawArc(getRectForOffset(rand), 135, voltWinkel, true,
		// Settings.getBatteryPaint(volt));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(rand + dicke), 0, 360, true, Settings.getErasurePaint());

		for (int i = min; i <= max; i = i + 100) {
			final float winkel = 135 + (i - min) * winkelProMillivolt;
			if (i % 1000 == 0) {
				// Skala
				final Path mArc = new Path();
				final RectF oval = getRectForOffset(rand + bogenDicke + fontSizeArc);
				mArc.addArc(oval, winkel - 18, 36);
				bitmapCanvas.drawTextOnPath("" + i / 1000 + ".0", mArc, 0, 0, textPaint);
				// striche
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 4 / 10), winkel - 0.75f, 1.5f, true, zp);
			} else if (i % 500 == 0) {
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 3 / 10), winkel - 0.75f, 1.5f, true, zp);
			} else {
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 2 / 10), winkel - 0.75f, 1.5f, true, zp);
			}
		}
		// Rand
		if (Settings.isShowRand()) {
			// äußeren Rand
			randPaint.setStrokeWidth(bogenDicke);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(rand + 2), 0, 360, true, randPaint);
		}
		// Text unten
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(rand + 2 * bogenDicke);
		mArc.addArc(oval, 135, -90);
		bitmapCanvas.drawTextOnPath("" + (float) (volt / 10) / 100 + " Volt", mArc, 0, 0, textPaint);
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(rand - bogenDicke), 135 + voltWinkel - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(rand + dicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawTemperatureScala(int temp, final int rand, final int dicke) {
		final int min = 0;
		final int max = 600; // zehntel°C
		final int span = max - min;
		final float winkelProMillivolt = 270f / span;

		if (temp < min) {
			temp = min;
		}
		if (temp > max) {
			temp = max;
		}
		final Paint randPaint = Settings.getBackgroundPaint();
		randPaint.setColor(Color.WHITE);
		randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint zp = Settings.getZeigerPaint(100);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		final Paint textPaint = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setAlpha(255);
		// scala
		bitmapCanvas.drawArc(getRectForOffset(rand), 270, 360, true, Settings.getBackgroundPaint());
		// level
		final float tempWinkel = (temp - min) * winkelProMillivolt;
		// bitmapCanvas.drawArc(getRectForOffset(rand), 135, tempWinkel, true,
		// Settings.getBatteryPaint(temp));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(rand + dicke), 0, 360, true, Settings.getErasurePaint());

		for (int i = min; i <= max; i = i + 10) {
			final float winkel = 135 + (i - min) * winkelProMillivolt;
			if (i % 100 == 0) {
				// Skala
				final Path mArc = new Path();
				final RectF oval = getRectForOffset(rand + bogenDicke + fontSizeArc);
				mArc.addArc(oval, winkel - 18, 36);
				bitmapCanvas.drawTextOnPath("" + i / 10, mArc, 0, 0, textPaint);
				// striche
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 4 / 10), winkel - 0.75f, 1.5f, true, zp);
			} else if (i % 50 == 0) {
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 3 / 10), winkel - 0.75f, 1.5f, true, zp);
			} else {
				bitmapCanvas.drawArc(getRectForOffset(rand + dicke - dicke * 2 / 10), winkel - 0.75f, 1.5f, true, zp);
			}
		}
		// Rand
		if (Settings.isShowRand()) {
			// äußeren Rand
			randPaint.setStrokeWidth(bogenDicke);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(rand + 2), 0, 360, true, randPaint);
		}
		// Text unten
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(rand + 2 * bogenDicke);
		mArc.addArc(oval, 135, -90);
		bitmapCanvas.drawTextOnPath("" + temp / 10f + " °C", mArc, 0, 0, textPaint);
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(rand - bogenDicke), 135 + tempWinkel - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(rand + dicke), 0, 360, true, Settings.getErasurePaint());
	}

	@Override
	public void drawLevelNumber(final int level) {
		final String text = "" + level;
		final Paint p = Settings.getNumberPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		final PointF point = getTextCenterToDraw(text, getRectForOffset(0), p);
		bitmapCanvas.drawText(text, point.x, point.y, p);
	}

	private static PointF getTextCenterToDraw(final String text, final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 135 + Math.round(level * 2.2f);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(bogenDicke + levelDicke);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = Settings.getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		// do nothing
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

}
