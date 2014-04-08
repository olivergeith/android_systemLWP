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

public class BitmapDrawerTachoV1 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 5;
	private int skaleDicke = 100;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	protected Canvas bitmapCanvas;
	private int fontSizeScala = 20;

	public BitmapDrawerTachoV1() {
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
			setBitmapSize(cWidth, cWidth / 2, true);
		} else {
			// quer
			setBitmapSize(cWidth, cWidth / 2, false);
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(bWidth * 0.01f);
		skaleDicke = Math.round(bWidth * 0.13f);
		fontSize = Math.round(bWidth * 0.30f);
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
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, Math.round(level * 1.8), true, getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, getErasurePaint());

		// Skalatext
		drawScalaText();

		if (Settings.isShowRand()) {
			// äußeren Rand
			randPaint.setStrokeWidth(bogenDicke);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + 2), 180, 180, true, randPaint);
		}

		// Zeiger
		final Paint zp = getZeigerPaint(level);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		bitmapCanvas.drawArc(getRectForOffset(0), 180 + Math.round(level * 1.8) - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, getErasurePaint());

		// innerer Rand
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 180, 180, true, randPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 0, 360, true, getErasurePaint());

		// innere Fläche
		final Paint bgPaint2 = getBackgroundPaint();
		bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 180, 180, true, bgPaint2);
	}

	private void drawScalaText() {
		long winkel = 180 + Math.round(level * 1.8f);
		for (int i = 10; i < 100; i = i + 10) {
			// final int winkel = -81 + (i * 18);
			winkel = 171 + Math.round(i * 1.8f);
			final Path mArc = new Path();
			final RectF oval = getRectForOffset(offset + bogenDicke + fontSizeScala);
			mArc.addArc(oval, winkel, 18);
			final Paint p = getTextPaint(i, fontSizeScala, Align.CENTER, true, true);
			p.setTextAlign(Align.CENTER);
			bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
		}
		final Paint zp = getZeigerPaint(level);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		for (int i = 10; i < 100; i = i + 10) {
			// Zeiger
			bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke - fontSizeArc / 2), (float) (180f + i * 1.8 - 0.5f), 1f, true, zp);
		}

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberBottom(bitmapCanvas, level, fontSize);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 182 + Math.round(level * 1.8f);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc - 4);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke + fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
