package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.systemlwp.bitmapdrawer.data.Gradient;
import de.geithonline.systemlwp.bitmapdrawer.data.Outline;
import de.geithonline.systemlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.systemlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.ZeigerPart;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV6 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		maxRadius = bmpWidth / 2.2f;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.2f;
		// Radiusses

	}

	public BitmapDrawerClockV6() {
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
	public boolean supportsGlowScala() {
		return true;
	}

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
				.setOutline(new Outline(PaintProvider.getGray(192, op), strokeWidth / 2))//
				.draw(bitmapCanvas);
		// SkalaBackground
		new RingPart(center, maxRadius * 0.79f, maxRadius * 0.32f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// gegenl�ufiger
		new LevelPart(center, maxRadius * 0.79f, maxRadius * 0.75f, 100 - level, -90, -360, EZColoring.ColorOf100)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);
		// Level
		new LevelPart(center, maxRadius * 0.74f, maxRadius * 0.60f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Innen Phase
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.32f, maxRadius * 0.25f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.32f, maxRadius * 0.25f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 5, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		new RingPart(center, maxRadius * 0.31f, maxRadius * 0.25f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224, op), PaintProvider.getGray(48, op), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.31f, maxRadius * 0.25f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224, op), PaintProvider.getGray(48, op), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);
		// Zeiger
		new ZeigerPart(center, level, maxRadius * 0.80f, maxRadius * 0.26f, strokeWidth, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Fl�che
		new RingPart(center, maxRadius * 0.25f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32, op), PaintProvider.getGray(224, op), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32, op), strokeWidth))//
				.draw(bitmapCanvas);

		new SkalaLinePart(center, maxRadius * 0.88f, maxRadius * 0.82f, -90, 360)//
				.set5erRadiusAussen(maxRadius * 0.86f)//
				.set1erRadiusAussen(maxRadius * 0.83f)//
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
		final long winkel = -90 + Math.round(level * 3.6f);

		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 1.01f);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = PaintProvider.getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.5f);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
