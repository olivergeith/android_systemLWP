package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Typeface;
import de.geithonline.systemlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.systemlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.systemlwp.bitmapdrawer.data.Gradient;
import de.geithonline.systemlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.systemlwp.bitmapdrawer.data.Outline;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.systemlwp.bitmapdrawer.parts.ArchPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.MultimeterSkalaPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.MultimeterZeigerPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.systemlwp.bitmapdrawer.parts.ZeigerPart;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerRotatingV2 extends AdvancedBitmapDrawer {

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
		fontSizeScala = maxRadius * 0.07f;
		fontSizeLevel = maxRadius * 0.15f;
	}

	public BitmapDrawerRotatingV2() {
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
	public boolean supportsShowPointer() {
		return true;
	}

	@Override
	public boolean supportsExtraLevelBars() {
		return true;
	}

	@Override
	public boolean supportsGlowScala() {
		return true;
	}

	@Override
	public boolean supportsVoltmeter() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {

		// SkalaBackground
		new RingPart(center, maxRadius * 0.95f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.95f, maxRadius * 0.85f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);
		// Level
		new LevelPart(center, maxRadius * 0.83f, maxRadius * 0.60f, level, -225, 270, EZColoring.LevelColors)//
				.setSegemteAbstand(0.75f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Rotierendes Feld f�r Levelnummer
		new ZeigerPart(center, level, maxRadius * 0.98f, maxRadius * 0.70f, 25, -225, 270, EZMode.Einer)//
				.setZeigerType(ZEIGER_TYP.inward_triangle)//
				.setDropShadow(new DropShadow(strokeWidth * 2, Color.BLACK))//
				.draw(bitmapCanvas);
		new ZeigerPart(center, level, maxRadius * 0.98f, maxRadius * 0.70f, 25, -225, 270, EZMode.Einer)//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.setZeigerType(ZEIGER_TYP.inward_triangle)//
				.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
				.draw(bitmapCanvas);

		// Skala
		new SkalaLinePart(center, maxRadius * 0.58f, maxRadius * 0.52f, -225, 270)//
				.set5erRadiusAussen(maxRadius * 0.58f)//
				.set1erRadiusAussen(maxRadius * 0.58f)//
				.set5erRadiusInnen(maxRadius * 0.54f)//
				.set1erRadiusInnen(maxRadius * 0.56f)//
				.setDicke(strokeWidth / 2)//
				.setDraw100(true)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.45f, fontSizeScala, -225, 270)//
				// .setFontsize5er(fontSizeScala * 0.75f)//
				.setDraw100(true)//
				.draw(bitmapCanvas);

		// SkalaGlow
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 30, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		// Innen Phase
		new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);

		// Extra Level Bars
		if (Settings.isShowExtraLevelBars()) {
			final PointF center2 = new PointF(center.x, center.y + maxRadius * 0.95f);
			final float w = 90;
			new LevelPart(center2, maxRadius * 0.43f, maxRadius * 0.40f, level, -90 - w / 2, w, EZColoring.ColorOf100)//
					.setSegemteAbstand(1f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_alpha)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.draw(bitmapCanvas);
			new LevelPart(center2, maxRadius * 0.39f, maxRadius * 0.33f, level, -90 - w / 2, w, EZColoring.ColorOf100)//
					.setSegemteAbstand(1f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_alpha)//
					.setMode(EZMode.Zehner)//
					.draw(bitmapCanvas);
		}
		// Voltmeter
		if (Settings.isShowVoltmeter()) {
			final PointF center2 = new PointF(center.x, center.y + maxRadius * 0.95f);
			MultimeterSkalaPart.getDefaultVoltmeterPart(center2, maxRadius * 0.50f, maxRadius * 0.45f, -135, 90)//
					.setFontAttributes(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala))//
					.setFontRadius(maxRadius * 0.52f)//
					.setLineRadius(maxRadius * 0.45f)//
					.draw(bitmapCanvas);
			MultimeterZeigerPart.getDefaultVoltmeterPart(center2, Settings.getBattVoltage(), maxRadius * 0.50f, maxRadius * 0.05f, -135, 90)//
					.setDicke(strokeWidth)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);
			new ArchPart(center, maxRadius * 0.99f, maxRadius * 0.80f, 75, 30, new Paint())//
					.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
					.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);
			new TextOnCirclePart(center, maxRadius * 0.92f, 90, fontSizeArc, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.CENTER)//
					.invert(true)//
					.draw(bitmapCanvas, Settings.getBattVoltage() + " Volt");

		}

		if (Settings.isShowZeiger()) {
			// Zeiger
			new ZeigerPart(center, level, maxRadius * 0.65f, maxRadius * 0.16f, strokeWidth, -225, 270, EZMode.Einer)//
					.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
					.draw(bitmapCanvas);
		}
		// Innen Fl�che
		new RingPart(center, maxRadius * 0.15f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);
	}

	@Override
	public void drawLevelNumber(final int level) {
		final float winkel = -226 + level * 2.7f;
		new TextOnCirclePart(center, maxRadius * 0.85f, winkel, fontSizeLevel, PaintProvider.getNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.setDropShadow(new DropShadow(strokeWidth * 2, 0, strokeWidth / 2, Color.BLACK))//
				.draw(bitmapCanvas, "" + level);

	}

	@Override
	public void drawChargeStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.35f, -90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText() {
		new TextOnCirclePart(center, maxRadius * 0.62f, -90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
