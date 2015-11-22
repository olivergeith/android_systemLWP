package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.DropShadow;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.EZColoring;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.EZMode;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.EZStyle;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.Gradient;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.Gradient.GRAD_STYLE;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.Outline;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.ZeigerPart;
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

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.50f;
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
	public boolean supportsLevelStyle() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {

		new LevelPart(center, maxRadius * 0.98f, maxRadius * 0.92f, level, -90, 360, EZColoring.LevelColors)//
				.configureSegemte(1f, strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(EZMode.Einer)//
				.draw(bitmapCanvas);

		// new LevelPart(center, maxRadius * 0.98f, maxRadius * 0.92f, level, -90, 360)//
		// .setStyle(Settings.getLevelStyle())//
		// .configureSegemte(20, 1.0f, strokeWidth / 3)//
		// .draw(bitmapCanvas);

		new RingPart(center, maxRadius * 0.90f, maxRadius * 0.70f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(200), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(64), strokeWidth))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.70f, maxRadius * 0.60f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(180), GRAD_STYLE.top2bottom))//
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
		new ZeigerPart(center, level, maxRadius * 0.87f, maxRadius * 0.73f, strokeWidth * 3, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.raute)//
				.draw(bitmapCanvas);
		// Timer
		new LevelPart(center, maxRadius * 0.56f, maxRadius * 0.48f, level, 60, -90, EZColoring.Custom)//
				.setColor(Settings.getScaleColor())//
				.setStyle(EZStyle.segmented_all)//
				.setMode(EZMode.EinerOnly10Segmente)//
				.configureSegemte(3f, strokeWidth / 3)//
				.draw(bitmapCanvas);
		new LevelPart(center, maxRadius * 0.56f, maxRadius * 0.48f, level, 120, 90, EZColoring.Custom)//
				.setColor(Settings.getScaleColor())//
				.setStyle(EZStyle.segmented_all)//
				.setMode(EZMode.Zehner)//
				.configureSegemte(3f, strokeWidth / 3)//
				.draw(bitmapCanvas);

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
