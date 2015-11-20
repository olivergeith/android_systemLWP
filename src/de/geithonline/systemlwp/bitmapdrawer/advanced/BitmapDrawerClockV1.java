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
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV1 extends AdvancedSquareBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;
	private float radiusSkalaOuter;
	private float radiusSkalaInner;
	private float radiusChangeText;
	private float radiusBattStatus;
	private float radiusSkalaText;

	private float radiusLevelOuter;

	private float radiusLevelInner;

	private float zeigerdicke;

	private float radiusZeigerOuter;

	private float radiusZeigerInner;
	private final PointF center = new PointF();

	private void initPrivateMembers() {

		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		zeigerdicke = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.1f;
		fontSizeLevel = maxRadius * 0.3f;
		// Radiusses
		radiusSkalaOuter = maxRadius - fontSizeArc - strokeWidth;
		radiusSkalaInner = maxRadius * 0.2f;

		radiusChangeText = maxRadius - fontSizeArc;

		radiusBattStatus = maxRadius * 0.5f;

		radiusSkalaText = radiusSkalaOuter - strokeWidth - fontSizeScala;

		radiusLevelOuter = radiusSkalaInner + 4 * strokeWidth;
		radiusLevelInner = radiusSkalaInner + strokeWidth;

		radiusZeigerOuter = maxRadius - strokeWidth;
		radiusZeigerInner = radiusSkalaInner + strokeWidth;

	}

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
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();

		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {
		// scala
		final Paint skalaPaint = PaintProvider.getBackgroundPaint();
		final Path skalaPath = new CirclePath(center, radiusSkalaOuter, radiusSkalaInner, false);
		bitmapCanvas.drawPath(skalaPath, skalaPaint);
		if (Settings.isShowRand()) {
			skalaPaint.setColor(Color.WHITE);
			skalaPaint.setStrokeWidth(strokeWidth);
			skalaPaint.setStyle(Style.STROKE);
			skalaPaint.setShadowLayer(4 * strokeWidth, 0, 0, Color.BLACK);
			bitmapCanvas.drawPath(skalaPath, skalaPaint);
		}
		// Skalatext
		drawScalaText();

		// level
		final float levelArc = level * 3.6f;
		final Path levelPath = new LevelArcPath(center, radiusLevelOuter, radiusLevelInner, -90, levelArc);
		bitmapCanvas.drawPath(levelPath, PaintProvider.getBatteryPaint(level));

		// Zeiger
		final Path zeigerPath = new ZeigerPath(center, radiusSkalaText - 2 * strokeWidth, radiusZeigerInner, zeigerdicke * 1.3f, -90 + levelArc);
		final Paint zeigerPaint = PaintProvider.getZeigerPaint(level, zeigerdicke * 2);
		bitmapCanvas.drawPath(zeigerPath, zeigerPaint);

		// einer Zeiger
		final float einerWinkel = level * 36f;
		final Path einerPath = new ZeigerPath(center, radiusZeigerOuter, radiusZeigerInner, zeigerdicke, -90 + einerWinkel);
		bitmapCanvas.drawPath(einerPath, zeigerPaint);

		// innere Fläche
		final Path centerPath = new CirclePath(center, radiusSkalaInner, 0, true);
		zeigerPaint.setColor(Color.WHITE);
		bitmapCanvas.drawPath(centerPath, zeigerPaint);

	}

	private void drawScalaText() {
		for (int i = 0; i < 100; i = i + 10) {
			final long winkel = 252 + Math.round(i * 3.6f);
			final Path mArc = new Path();
			final RectF oval = GeometrieHelper.getCircle(center, radiusSkalaText);
			mArc.addArc(oval, winkel, 36);
			final Paint p = PaintProvider.getTextScalePaint(fontSizeScala, Align.CENTER, true);
			p.setTextAlign(Align.CENTER);
			bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
		}
		for (int i = 0; i < 100; i = i + 5) {
			final long winkel = 270 + Math.round(i * 3.6f);
			final Path zeigerPath = new ZeigerPath(center, radiusSkalaOuter, radiusSkalaOuter - 2 * strokeWidth, zeigerdicke, winkel);
			final Paint zeigerPaint = PaintProvider.getZeigerPaint(level, 0f);
			bitmapCanvas.drawPath(zeigerPath, zeigerPaint);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumber(bitmapCanvas, level, fontSizeLevel, new PointF(center.x, center.x + maxRadius * 0.6f));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 272 + Math.round(level * 3.6f);

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
