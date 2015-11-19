package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerClockV1 extends BitmapDrawerAdvanced {

	private int offset = 5;
	private int bogenDicke = 5;
	private int skaleDicke = 100;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	protected Canvas bitmapCanvas;

	private float fontSizeScala = 20f;

	public BitmapDrawerClockV1() {
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
		skaleDicke = Math.round(bWidth * 0.35f);
		fontSize = Math.round(bWidth * 0.1f);
		fontSizeArc = Math.round(bWidth * 0.04f);
		fontSizeScala = bWidth * 0.05f;
		offset = fontSizeArc;

		drawBogen(level);
		return bitmap;
	}

	private void drawBogen(final int level) {
		final Paint randPaint = PaintProvider.getBackgroundPaint();
		randPaint.setColor(Color.WHITE);
		randPaint.setShadowLayer(20, 0, 0, Color.BLACK);
		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, 360, true, PaintProvider.getBackgroundPaint());
		// level
		// bitmapCanvas.drawArc(getRectForOffset(offset), 270, Math.round(level * 3.6), true, getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, PaintProvider.getErasurePaint());

		// Skalatext
		drawScalaText();

		if (Settings.isShowRand()) {
			// äußeren Rand
			randPaint.setStrokeWidth(bogenDicke);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + 2), 270, 360, true, randPaint);
		}
		// Zeiger
		final Paint zp = PaintProvider.getZeigerPaint(level);
		zp.setShadowLayer(20, 0, 0, Color.BLACK);
		bitmapCanvas.drawArc(getRectForOffset(0), 270 + Math.round(level * 3.6) - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, PaintProvider.getErasurePaint());

		// innerer Rand
		randPaint.setStyle(Style.FILL);
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 270, 360, true, randPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 0, 360, true, PaintProvider.getErasurePaint());

		// innere Fläche
		final Paint bgPaint2 = PaintProvider.getBackgroundPaint();
		bgPaint2.setColor(ColorHelper.darker2times(bgPaint2.getColor()));
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 270, 360, true, bgPaint2);
	}

	private void drawScalaText() {
		for (int i = 0; i < 100; i = i + 10) {
			final long winkel = 252 + Math.round(i * 3.6f);
			final Path mArc = new Path();
			final RectF oval = getRectForOffset(offset + bogenDicke + fontSizeScala);
			mArc.addArc(oval, winkel, 36);
			final Paint p = PaintProvider.getTextScalePaint(fontSizeScala, Align.CENTER, true);
			p.setTextAlign(Align.CENTER);
			bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
		}
		for (int i = 5; i < 100; i = i + 10) {
			final long winkel = 252 + Math.round(i * 3.6f);
			final Path mArc = new Path();
			final RectF oval = getRectForOffset(offset + bogenDicke + fontSizeScala);
			mArc.addArc(oval, winkel, 36);
			final Paint p = PaintProvider.getTextScalePaint(fontSizeScala * 0.75f, Align.CENTER, true);
			p.setTextAlign(Align.CENTER);
			bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
		}

		// for (int i = 0; i < 100; i = i + 10) {
		// // Zeiger
		// final Paint zp = getZeigerPaint(level);
		// zp.setShadowLayer(10, 0, 0, Color.BLACK);
		// bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke - 3 * offset), (float) (270f + i * 3.6 - 0.5f), 1f, true, zp);
		// }

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSize);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 272 + Math.round(level * 3.6f);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc - bogenDicke);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = PaintProvider.getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + bogenDicke + 3 * fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	private RectF getRectForOffset(final float offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

}
