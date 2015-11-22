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
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.Outline;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.ZeigerPart;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV4 extends AdvancedSquareBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;
		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.1f;
		fontSizeLevel = maxRadius * 0.2f;

	}

	public BitmapDrawerClockV4() {
	}

	@Override
	public boolean supportsPointerColor() {
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
		new RingPart(center, maxRadius * 0.90f, 0, PaintProvider.getBackgroundPaint())//
				.setOutline(new Outline(Color.WHITE, strokeWidth))//
				.draw(bitmapCanvas);

		// level ring
		new LevelPart(center, maxRadius * 0.82f, maxRadius * 0.72f, level, -90, 360, EZColoring.LevelColors)//
				.configureSegemte(1f, strokeWidth / 3)//
				.setMode(EZMode.Einer)//
				.setStyle(EZStyle.segmented_onlyactive)//
				.draw(bitmapCanvas);

		// Gegenl�ufiger Level
		new LevelPart(center, maxRadius * 0.88f, maxRadius * 0.84f, 100 - level, -90, -360, EZColoring.LevelColors)//
				.configureSegemte(1f, strokeWidth / 3)//
				.setMode(EZMode.Einer)//
				.setStyle(EZStyle.segmented_onlyactive)//
				.draw(bitmapCanvas);

		// Timer
		final float maxScala = 120f;
		final float ri = maxRadius * 0.60f;
		new LevelPart(center, maxRadius * 0.70f, ri, level, -225, maxScala, EZColoring.Colorfull)//
				.configureSegemte(1.5f, strokeWidth / 3)//
				.setMode(EZMode.Fuenfer)//
				.setStyle(EZStyle.segmented_all)//
				.draw(bitmapCanvas);
		new ZeigerPart(center, level, ri, maxRadius * 0.20f, strokeWidth * 1.0f, -225, maxScala, EZMode.Fuenfer)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.rect)//
				.draw(bitmapCanvas);

		new LevelPart(center, maxRadius * 0.70f, ri, level, 45, -maxScala, EZColoring.ColorOf100)//
				.configureSegemte(1.5f, strokeWidth / 3)//
				.setMode(EZMode.EinerOnly9Segment)//
				.setStyle(EZStyle.segmented_all)//
				.draw(bitmapCanvas);
		new ZeigerPart(center, level, ri, maxRadius * 0.20f, strokeWidth * 1.0f, 45, -maxScala, EZMode.EinerOnly9Segment)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.rect)//
				.draw(bitmapCanvas);

		// Skalatext
		new SkalaLinePart(center, maxRadius * 0.90f, maxRadius * 0.86f, -90, 360)//
				.set5erRadius(maxRadius * 0.90f)//
				.setDicke(strokeWidth / 2)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.75f, fontSizeScala, -90, 360)//
				.setFontsize5er(fontSizeScala * 0.75f)//
				.draw(bitmapCanvas);

		// Zeiger
		new ZeigerPart(center, level, maxRadius * 0.78f, maxRadius * 0.20f, strokeWidth * 1.5f, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.rect)//
				// .overrideColor(Color.RED)//
				.draw(bitmapCanvas);

		// innere Fl�che
		new RingPart(center, maxRadius * 0.20f, maxRadius * 0f, new Paint())//
				.setColor(Color.WHITE)//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumber(bitmapCanvas, level, fontSizeLevel * 1.5f, new PointF(center.x, center.x + maxRadius * 0.55f));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 272 + Math.round(level * 3.6f);

		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius - fontSizeArc);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = PaintProvider.getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.4f);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}