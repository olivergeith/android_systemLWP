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

public class BitmapDrawerTachoV4 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 5;
	private int skaleDicke = 100;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	protected Canvas bitmapCanvas;

	private int fontSizeScala = 20;

	public BitmapDrawerTachoV4() {
	}

	@Override
	public boolean supportsPointerColor() {
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

		bogenDicke = Math.round(bWidth * 0.01f);
		skaleDicke = Math.round(bWidth * 0.13f);
		fontSize = Math.round(bWidth * 0.37f);
		fontSizeArc = Math.round(bWidth * 0.04f);
		fontSizeScala = Math.round(bWidth * 0.05f);
		offset = fontSizeArc;

		drawBogen(level);
		return bitmap;
	}

	private void drawBogen(final int level) {
		final Paint randPaint = getBackgroundPaint();
		randPaint.setColor(Color.WHITE);
		randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, 360, true, getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, -Math.round(level * 3.6), true, getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, getErasurePaint());

		// Skalatext
		drawScalaText(level);

		if (Settings.isShowRand()) {
			// äußeren Rand
			randPaint.setStrokeWidth(bogenDicke);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + 2), 270, 360, true, randPaint);
		}
		// Zeiger
		final Paint zp = getZeigerPaint(level);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		bitmapCanvas.drawArc(getRectForOffset(0), 270 - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, getErasurePaint());

		// innerer Rand
		randPaint.setStyle(Style.FILL);
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 270, 360, true, randPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 0, 360, true, getErasurePaint());

		// innere Fläche
		final Paint bgPaint2 = getBackgroundPaint();
		bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 270, 360, true, bgPaint2);
	}

	private void drawScalaText(final int level) {
		final float startwinkel = 270 - Math.round(level * 3.6f);
		for (int i = 0; i < 100; i = i + 1) {
			// Zeiger
			final Paint zp = getZeigerPaint(level);
			zp.setShadowLayer(10, 0, 0, Color.BLACK);
			if (i % 10 == 0) {
				final float winkel = startwinkel - 18 + Math.round(i * 3.6f);
				final Path mArc = new Path();
				final RectF oval = getRectForOffset(offset + bogenDicke + fontSizeScala);
				mArc.addArc(oval, winkel, 36);
				final Paint p = getTextScalePaint(fontSizeScala, Align.CENTER, true);
				p.setTextAlign(Align.CENTER);
				bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
				bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke - fontSizeArc), (float) (startwinkel + i * 3.6 - 0.5f), 1f, true, zp);
			} else if (i % 5 == 0) {
				bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke - fontSizeArc * 2 / 3), (float) (startwinkel + i * 3.6 - 0.5f), 1f,
						true, zp);
			} else {
				bitmapCanvas
						.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke - fontSizeArc / 2), (float) (startwinkel + i * 3.6 - 0.5f), 1f, true, zp);
			}
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSize);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Paint p = getTextPaint(level, fontSizeArc, Align.CENTER, true, false);
		int fader = 10 - level % 10;
		if (fader == 0) {
			fader = 1;
		}
		fader = p.getAlpha() * fader / 10;
		p.setAlpha(fader);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(bogenDicke + skaleDicke + bogenDicke + fontSizeArc + bogenDicke);
		mArc.addArc(oval, 180, -180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke + fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

}
