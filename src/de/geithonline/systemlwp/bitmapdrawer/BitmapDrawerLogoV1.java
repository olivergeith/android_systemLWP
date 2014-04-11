package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerLogoV1 extends BitmapDrawer {

	private int offset = 10;
	private final float gap = 0.6f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private int einerDicke = 70;
	private Canvas bitmapCanvas;

	public BitmapDrawerLogoV1() {
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

		offset = Math.round(bWidth * 0.021f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.05f);
		einerDicke = Math.round(bWidth * 0.10f);

		drawSegmente(level);

		return bitmap;
	}

	private void drawSegmente(final int level) {
		// Scala
		drawScala();
		// load logo
		final Bitmap bgBitmap = Settings.getDefaultLogoForDrawer(bWidth);
		// grayscale it
		final Paint bgPaint = new Paint();
		final ColorMatrix cm = new ColorMatrix();
		// final ColorMatrix cm = new ColorMatrix(new float[]
		// { 0.5f, 0.5f, 0.5f, 0, 0, //
		// 0.5f, 0.5f, 0.5f, 0, 0, //
		// 0.5f, 0.5f, 0.5f, 0, 0, //
		// 0, 0, 0, 1, 0, 0,
		// 0, 0, 0, 0, 1, 0 });
		cm.setSaturation(0);
		final ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		bgPaint.setColorFilter(f);
		// paint it
		bitmapCanvas.drawBitmap(bgBitmap, 0, 0, bgPaint);
		// overpaint it with colored version

		final BitmapShader fillBMPshader = new BitmapShader(bgBitmap, TileMode.REPEAT, TileMode.REPEAT);

		final Paint battPaint = new Paint();
		battPaint.setStyle(Paint.Style.FILL);
		battPaint.setShader(fillBMPshader);
		bitmapCanvas.drawArc(getRectForOffset(0), 270, Math.round(level * 3.6f), true, battPaint);
		// zeiger
		if (Settings.isShowZeiger()) {
			drawZeiger(level);
		}
		if (Settings.isShowRand()) {
			final Paint randPaint = getBackgroundPaint();
			randPaint.setColor(Color.WHITE);
			randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
			// äußeren Rand
			randPaint.setStrokeWidth(offset);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke), 270, 360, true, randPaint);
			// innere Fläche
			final Paint bgPaint2 = getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke), 270, 360, true, bgPaint2);
		}
		drawScalaText();

	}

	private void drawScala() {
		final Paint p = getTextScalePaint(fontSizeArc, Align.CENTER, true);
		final RectF oval = getRectForOffset(offset + fontSizeArc);
		for (int i = 0; i < 100; i = i + 5) {
			final long winkel = 252 + Math.round(i * 3.6f);
			final Path mArc = new Path();
			mArc.addArc(oval, winkel, 36);
			// bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
			bitmapCanvas.drawArc(getRectForOffset(0), (float) (270f + i * 3.6 - 0.5f), 1f, true, p);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset), 0, 360, true, getErasurePaint());
	}

	private void drawScalaText() {
		final Paint p = getTextScalePaint(fontSizeArc, Align.CENTER, true);
		final RectF oval = getRectForOffset(offset + fontSizeArc);
		for (int i = 0; i < 100; i = i + 5) {
			final long winkel = 252 + Math.round(i * 3.6f);
			final Path mArc = new Path();
			mArc.addArc(oval, winkel, 36);
			bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
		}
	}

	private void drawZeiger(final int level) {
		// Zeiger
		if (Settings.isShowZeiger()) {
			final Paint zp = getZeigerPaint(level);
			zp.setShadowLayer(10, 0, 0, Color.BLACK);
			bitmapCanvas.drawArc(getRectForOffset(0), 270 + Math.round(level * 3.6f) - 0.5f, 1f, true, zp);
		}
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final int segmente = 101;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
		final float startwinkel = 272f + level * (winkelOneSegment + gap) + gap / 2;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + fontSizeArc);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSize, false);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
