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
import de.geithonline.systemlwp.bitmapdrawer.parts.ArchPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.systemlwp.bitmapdrawer.parts.ZeigerPart;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerAsymetricV1 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;

	private final PointF center = new PointF();
	private final PointF center2 = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		center2.x = bmpWidth / 2;
		center2.y = bmpHeight * 0.57f;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.07f;
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.2f;
		// Radiusses

	}

	public BitmapDrawerAsymetricV1() {
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

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.95f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.94f, maxRadius * 0.0f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(128), PaintProvider.getGray(16), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
		// SkalaBackground
		new RingPart(center2, maxRadius * 0.65f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(Color.WHITE, strokeWidth / 2))//
				.draw(bitmapCanvas);
		// Skala
		new SkalaLinePart(center2, maxRadius * 0.70f, maxRadius * 0.65f, -90, 360)//
				.set5erRadiusAussen(maxRadius * 0.68f)//
				.set1erRadiusAussen(maxRadius * 0.66f)//
				.setDicke(strokeWidth / 2)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center2, maxRadius * 0.70f, fontSizeScala, -90, 360)//
				.setFontsize5er(fontSizeScala * 0.75f)//
				.draw(bitmapCanvas);
		// phase
		new RingPart(center2, maxRadius * 0.64f, maxRadius * 0.59f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(224), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth / 3))//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center2, maxRadius * 0.58f, maxRadius * 0.48f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);
		// Zeiger
		new ZeigerPart(center2, level, maxRadius * 0.64f, maxRadius * 0.0f, strokeWidth, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Phase
		if (Settings.isShowGlowScala()) {
			new RingPart(center2, maxRadius * 0.2f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center2, maxRadius * 0.2f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 5, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		new RingPart(center2, maxRadius * 0.2f, maxRadius * 0.15f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(224), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth / 3))//
				.draw(bitmapCanvas);
		// Innen Fl�che
		new RingPart(center2, maxRadius * 0.14f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

		// Timer
		new ArchPart(center, maxRadius * 0.93f, maxRadius * 0.81f, -201, 92, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new LevelPart(center, maxRadius * 0.91f, maxRadius * 0.83f, level, -200, 90, EZColoring.ColorOf100)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(EZStyle.segmented_all)//
				.setMode(EZMode.Zehner)//
				.draw(bitmapCanvas);
		new ArchPart(center, maxRadius * 0.93f, maxRadius * 0.81f, 21, -92, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new LevelPart(center, maxRadius * 0.91f, maxRadius * 0.83f, level, 20, -90, EZColoring.ColorOf100)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(EZStyle.segmented_all)//
				.setMode(EZMode.EinerOnly10Segmente)//
				.draw(bitmapCanvas);

		new ArchPart(center, maxRadius * 0.93f, maxRadius * 0.70f, -108, 36, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
	}

	@Override
	public void drawLevelNumber(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.74f, -90, fontSizeLevel, PaintProvider.getNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, "" + level);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.74f, -70, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText() {
		new TextOnCirclePart(center, maxRadius * 0.74f, -110, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.RIGHT)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
