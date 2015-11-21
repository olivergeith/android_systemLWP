package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
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
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.ZeigerPart;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV3 extends AdvancedSquareBitmapDrawer {

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
		zeigerdicke = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.3f;
		// Radiusses

		radiusChangeText = maxRadius * 0.70f;// - fontSizeArc;
		radiusBattStatus = maxRadius * 0.42f;

	}

	public BitmapDrawerClockV3() {
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
		final int op = 224;

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.80f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32, op), PaintProvider.getGray(192, op), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(64, op), strokeWidth))//
				.draw(bitmapCanvas);
		// SkalaBackground
		new RingPart(center, maxRadius * 0.79f, maxRadius * 0.40f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.79f, maxRadius * 0.70f, level, -90, 360)//
				.setStyle(LEVEL_STYLE.segmented_onlyactive)//
				.setColorful(true)//
				.configureSegemte(20, 2f, strokeWidth / 2)//
				.draw(bitmapCanvas);
				// new LevelPart(center, maxRadius * 0.55f, maxRadius * 0.40f, level, -90, 360)//
				// .setStyle(LEVEL_STYLE.normal)//
				// .draw(bitmapCanvas);
				// new LevelPart(center, maxRadius * 0.79f, maxRadius * 0.40f, level, -90, 360)//
				// .setStyle(LEVEL_STYLE.normal)//
				// .draw(bitmapCanvas);

		// Innen Phase (with white dropshadow)
		new RingPart(center, maxRadius * 0.40f, maxRadius * 0.35f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224, op), PaintProvider.getGray(32, op), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);
		// Zeiger
		new ZeigerPart(center, level, maxRadius * 0.85f, maxRadius * 0.36f, strokeWidth, -90, 360)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Fläche
		new RingPart(center, maxRadius * 0.35f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32, op), PaintProvider.getGray(192, op), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32, op), strokeWidth))//
				.draw(bitmapCanvas);

		new SkalaLinePart(center, maxRadius * 0.88f, maxRadius * 0.82f, -90, 360)//
				.set5erRadius(maxRadius * 0.86f)//
				.set1erRadius(maxRadius * 0.83f)//
				.setDicke(strokeWidth / 2)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.90f, fontSizeScala, -90, 360)//
				.setFontsize5er(fontSizeScala * 0.75f)//
				.draw(bitmapCanvas);

		// drawScalaLines(maxRadius * 0.85f, maxRadius * 0.82f, 5);
		// drawScalaText(maxRadius * 0.90f);
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
