package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.DropShadow;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.LevelPart.LEVEL_STYLE;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.Outline;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.ZeigerPart;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV1 extends AdvancedSquareBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

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
		fontSizeScala = maxRadius * 0.1f;
		fontSizeLevel = maxRadius * 0.3f;
		// Radiusses

		radiusChangeText = maxRadius - fontSizeArc;
		radiusBattStatus = maxRadius * 0.5f;
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

		new RingPart(center, maxRadius * 0.90f, 0, PaintProvider.getBackgroundPaint())//
				.setOutline(new Outline(Color.WHITE, strokeWidth))//
				.draw(bitmapCanvas);

		// Skalatext
		new SkalaLinePart(center, maxRadius * 0.90f, maxRadius * 0.86f, -90, 360)//
				.set5erRadius(maxRadius * 0.90f)//
				.setDicke(strokeWidth / 2)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.75f, fontSizeScala, -90, 360)//
				.draw(bitmapCanvas);

		// level
		new LevelPart(center, maxRadius * 0.30f, maxRadius * 0.20f, level, -90, 360)//
				.setStyle(LEVEL_STYLE.normal)//
				.draw(bitmapCanvas);

		// Zeiger
		new ZeigerPart(center, level, maxRadius * 0.70f, maxRadius * 0.20f, strokeWidth * 1.5f, -90, 360)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		new ZeigerPart(center, level, maxRadius * 0.95f, maxRadius * 0.20f, strokeWidth, -90, 360)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.setEinerZeiger(true)//
				.draw(bitmapCanvas);

		// innere Fläche
		new RingPart(center, maxRadius * 0.20f, maxRadius * 0f, new Paint())//
				.setColor(Color.WHITE)//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

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
