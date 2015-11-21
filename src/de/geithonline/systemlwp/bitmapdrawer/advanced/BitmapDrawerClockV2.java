package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.DropShadow;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.Gradient;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.Gradient.GRAD_STYLE;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.LevelPart.LEVEL_STYLE;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.Outline;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.ZeigerPart;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV2 extends AdvancedSquareBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;

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

	// @Override
	// public boolean supportsShowRand() {
	// return true;
	// }

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {

		new LevelPart(center, maxRadius * 0.98f, maxRadius * 0.92f, level, -90, 360)//
				.setStyle(LEVEL_STYLE.normal)//
				.draw(bitmapCanvas);

		new RingPart(center, maxRadius * 0.90f, maxRadius * 0.70f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(64), strokeWidth))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.70f, maxRadius * 0.60f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(160), PaintProvider.getGray(200), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(96), strokeWidth))//
				.draw(bitmapCanvas);

		new SkalaLinePart(center, maxRadius * 0.67f, maxRadius * 0.63f, -90, 360)//
				.set5erRadius(maxRadius * 0.65f)//
				// .set1erRadius(maxRadius * 0.83f)//
				.setDicke(strokeWidth / 2)//
				.draw(bitmapCanvas);

		new RingPart(center, maxRadius * 0.58f, 0, PaintProvider.getBackgroundPaint())//
				.setOutline(new Outline(PaintProvider.getGray(192, 128), strokeWidth))//
				.draw(bitmapCanvas);

		// Zeiger
		new ZeigerPart(center, level, maxRadius * 0.87f, maxRadius * 0.73f, strokeWidth * 3, -90, 360)//
				.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.raute)//
				.draw(bitmapCanvas);

		// drawZeiger(level, maxRadius * 0.87f, maxRadius * 0.73f, Settings.isShowRand());
	}

	private void drawZeiger(final int level, final float ra, final float ri, final boolean outline) {
		final Path path = new ZeigerShapePath(center, ra, ri, zeigerdicke, -90 + level * 3.6f, ZEIGER_TYP.raute);
		final Paint paint = PaintProvider.getZeigerPaint();
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
