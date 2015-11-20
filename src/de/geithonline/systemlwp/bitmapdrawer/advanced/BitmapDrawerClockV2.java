package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.CirclePath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.LevelArcPath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerPath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV2 extends AdvancedSquareBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;
	private float radiusChangeText;
	private float radiusBattStatus;
	private float zeigerdicke;

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// sonstiges
		zeigerdicke = maxRadius * 0.1f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.1f;
		fontSizeLevel = maxRadius * 0.6f;
		// Radiusses

		radiusChangeText = maxRadius * 0.72f;// - fontSizeArc;
		radiusBattStatus = maxRadius * 0.5f;

	}

	public BitmapDrawerClockV2() {
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
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {
		drawLevelRing(level, maxRadius * 0.98f, maxRadius * 0.92f, Settings.isShowRand());
		drawGradientRing(maxRadius * 0.90f, maxRadius * 0.70f, PaintProvider.getGray(64), PaintProvider.getGray(192), PaintProvider.getGray(32), true);
		drawGradientRing(maxRadius * 0.70f, maxRadius * 0.60f, PaintProvider.getGray(96), PaintProvider.getGray(160), PaintProvider.getGray(200), true);
		drawScalaLines(maxRadius * 0.67f, maxRadius * 0.63f, 5);
		// drawColoredRing(maxRadius * 0.70f, maxRadius * 0.60f, Color.WHITE, PaintProvider.getGray(32), true);
		drawBackgroundRing(maxRadius * 0.58f, 0, PaintProvider.getGray(192, 128), true);
		drawZeiger(level, maxRadius * 0.87f, maxRadius * 0.73f, Settings.isShowRand());
	}

	private void drawScalaText(final float radius) {
		for (int i = 0; i < 100; i = i + 10) {
			final long winkel = 252 + Math.round(i * 3.6f);
			final Path mArc = new Path();
			final RectF oval = GeometrieHelper.getCircle(center, radius);
			mArc.addArc(oval, winkel, 36);
			final Paint p = PaintProvider.getTextScalePaint(fontSizeScala, Align.CENTER, true);
			p.setTextAlign(Align.CENTER);
			bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
		}
	}

	private void drawScalaLines(final float ra, final float ri, final int step) {
		final Paint paint = PaintProvider.getZeigerPaint(100, 0f);
		for (int i = 0; i < 100; i = i + step) {
			final long winkel = 270 + Math.round(i * 3.6f);
			final Path path = new ZeigerPath(center, ra, ri, strokeWidth, winkel);
			bitmapCanvas.drawPath(path, paint);
		}
	}

	private void drawZeiger(final int level, final float ra, final float ri, final boolean outline) {
		final Path path = new ZeigerShapePath(center, ra, ri, zeigerdicke, -90 + level * 3.6f, ZEIGER_TYP.raute);
		final Paint paint = PaintProvider.getZeigerPaint(level, strokeWidth * 2);
		bitmapCanvas.drawPath(path, paint);
		if (outline) {
			paint.setColor(Color.DKGRAY);
			paint.setAlpha(255);
			paint.setStrokeWidth(strokeWidth / 2);
			paint.setStyle(Style.STROKE);
			paint.setShadowLayer(0, 0, 0, Color.BLACK);
			bitmapCanvas.drawPath(path, paint);
		}
	}

	private void drawLevelRing(final int level, final float ra, final float ri, final boolean outline) {
		final float levelWinkel = level * 3.6f;
		final Path path = new LevelArcPath(center, ra, ri, -90, levelWinkel);
		final Paint paint = PaintProvider.getBatteryPaint(level);
		bitmapCanvas.drawPath(path, paint);
		if (outline) {
			paint.setAlpha(255);
			paint.setStrokeWidth(strokeWidth);
			paint.setStyle(Style.STROKE);
			bitmapCanvas.drawPath(path, paint);
		}
	}

	private void drawBackgroundRing(final float ra, final float ri, final int outlineColor, final boolean outline) {
		final Paint paint = PaintProvider.getBackgroundPaint();
		boolean filled = true;
		if (ri > 0) {
			filled = false;
		}
		final Path path = new CirclePath(center, ra, ri, filled);
		bitmapCanvas.drawPath(path, paint);

		if (outline) {
			paint.setColor(outlineColor);
			paint.setStrokeWidth(strokeWidth / 2);
			paint.setStyle(Style.STROKE);
			bitmapCanvas.drawPath(path, paint);
		}
	}

	private void drawGradientRing(final float ra, final float ri, final int cOutline, final int topColor, final int bottomColor, final boolean outline) {
		boolean filled = true;
		if (ri > 0) {
			filled = false;
		}
		final Path path = new CirclePath(center, ra, ri, filled);
		final Paint paint = PaintProvider.getGradientRingPaint(GeometrieHelper.getCircle(center, ra), topColor, bottomColor);
		bitmapCanvas.drawPath(path, paint);
		paint.setShader(null);
		if (outline) {
			paint.setColor(cOutline);
			paint.setStrokeWidth(strokeWidth);
			paint.setStyle(Style.STROKE);
			bitmapCanvas.drawPath(path, paint);
		}
	}

	private void drawColoredRing(final float ra, final float ri, final int color, final int cOutline, final boolean outline) {
		final Path path = new CirclePath(center, ra, ri, false);
		final Paint paint = PaintProvider.getBackgroundPaint();
		paint.setColor(color);
		bitmapCanvas.drawPath(path, paint);
		if (outline) {
			paint.setColor(cOutline);
			paint.setStrokeWidth(strokeWidth);
			paint.setStyle(Style.STROKE);
			bitmapCanvas.drawPath(path, paint);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 276 + Math.round(level * 3.6f);

		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, radiusChangeText);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = PaintProvider.getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, radiusBattStatus);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
