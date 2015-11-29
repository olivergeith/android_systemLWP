package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import de.geithonline.systemlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.systemlwp.bitmapdrawer.data.Gradient;
import de.geithonline.systemlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.systemlwp.bitmapdrawer.data.Outline;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.systemlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.systemlwp.bitmapdrawer.parts.ZeigerPart;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerRotatingV1 extends AdvancedBitmapDrawer {

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
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.25f;
	}

	public BitmapDrawerRotatingV1() {
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
		final int op = 224;

		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.90f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32, op), PaintProvider.getGray(96, op), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128, op), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Mittlerer ring
		new RingPart(center, maxRadius * 0.65f, maxRadius * 0.50f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(96, op), PaintProvider.getGray(32, op), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(192, op), strokeWidth / 2))//
				.draw(bitmapCanvas);

		new SkalaLinePart(center, maxRadius * 0.55f, maxRadius * 0.51f, -90, 360)//
				.set5erRadiusAussen(maxRadius * 0.54f)//
				.set1erRadiusAussen(maxRadius * 0.52f)//
				.setDicke(strokeWidth / 2)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.57f, fontSizeScala, -90, 360)//
				// .setFontsize5er(fontSizeScala * 0.75f)//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.88f, maxRadius * 0.67f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Innen Phase (with white dropshadow)
		new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224, op), PaintProvider.getGray(32, op), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);
		// Zeiger
		new ZeigerPart(center, level, maxRadius * 0.65f, maxRadius * 0.16f, strokeWidth, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		if (Settings.isShowTimer()) {
			// Level
			new LevelPart(center, maxRadius * 0.48f, maxRadius * 0.35f, level, 85, -170, EZColoring.ColorOf100)//
					.setSegemteAbstand(1f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_alpha)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.draw(bitmapCanvas);
			new ZeigerPart(center, level, maxRadius * 0.48f, maxRadius * 0.16f, strokeWidth, 85, -170, EZMode.EinerOnly10Segmente)//
					.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
					.overrideColor(Color.WHITE)//
					.draw(bitmapCanvas);
			new LevelPart(center, maxRadius * 0.48f, maxRadius * 0.35f, level, 95, 170, EZColoring.ColorOf100)//
					.setSegemteAbstand(1f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_alpha)//
					.setMode(EZMode.Zehner)//
					.draw(bitmapCanvas);
			new ZeigerPart(center, level, maxRadius * 0.48f, maxRadius * 0.16f, strokeWidth, 95, 170, EZMode.Zehner)//
					.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
					.overrideColor(Color.WHITE)//
					.draw(bitmapCanvas);

		}

		// Innen Fl�che
		new RingPart(center, maxRadius * 0.15f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192, op), PaintProvider.getGray(32, op), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32, op), strokeWidth))//
				.draw(bitmapCanvas);

	}

	@Override
	public void drawLevelNumber(final int level) {
		final float winkel = -90 + level * 3.6f;
		new TextOnCirclePart(center, maxRadius * 0.68f, winkel, fontSizeLevel, PaintProvider.getNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.setDropShadow(new DropShadow(strokeWidth * 2, 0, strokeWidth / 2, Color.BLACK))//
				.draw(bitmapCanvas, "" + level);

	}

	@Override
	public void drawChargeStatusText(final int level) {
		final float winkel = 90 + level * 3.6f;
		new TextOnCirclePart(center, maxRadius * 0.70f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText() {
		final float winkel = 90 + level * 3.6f;
		new TextOnCirclePart(center, maxRadius * 0.80f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
