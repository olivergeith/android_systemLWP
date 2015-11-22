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
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.TimerType;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.ZeigerPart;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV5 extends AdvancedSquareBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;
	private float radiusBattStatus;

	private final PointF center = new PointF();

	private TimerType type = TimerType.Without;

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.2f;
		// Radiusses

		radiusBattStatus = maxRadius * 0.42f;

	}

	public BitmapDrawerClockV5() {
	}

	public BitmapDrawerClockV5(final TimerType type) {
		this.type = type;
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
		final float sweep = 270;
		final float startWinkel = -225;
		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.75f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32, op), PaintProvider.getGray(224, op), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128, op), strokeWidth))//
				.draw(bitmapCanvas);
		// SkalaBackground
		new RingPart(center, maxRadius * 0.74f, maxRadius * 0f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.71f, maxRadius * 0.60f, level, startWinkel, sweep, EZColoring.LevelColors)//
				.configureSegemte(1.0f, strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				// .setDropShadow(new DropShadow(strokeWidth * 1, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Phase
		new RingPart(center, maxRadius * 0.32f, maxRadius * 0.25f, new Paint())//
				.setColor(Color.BLACK)//
				.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getZeigerColor()))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.32f, maxRadius * 0.25f, new Paint())//
				.setColor(Color.BLACK)//
				.setDropShadow(new DropShadow(strokeWidth * 5, Settings.getZeigerColor()))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.31f, maxRadius * 0.25f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224, op), PaintProvider.getGray(48, op), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);
		// Zeiger
		new ZeigerPart(center, level, maxRadius * 0.80f, maxRadius * 0.26f, strokeWidth, startWinkel, sweep, EZMode.Einer)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Fläche
		new RingPart(center, maxRadius * 0.25f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32, op), PaintProvider.getGray(224, op), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32, op), strokeWidth))//
				.draw(bitmapCanvas);

		new SkalaLinePart(center, maxRadius * 0.82f, maxRadius * 0.76f, startWinkel, sweep)//
				.set5erRadius(maxRadius * 0.80f)//
				.set1erRadius(maxRadius * 0.77f)//
				.setDraw100(true)//
				.setDicke(strokeWidth / 2)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.84f, fontSizeScala, startWinkel, sweep)//
				.setDraw100(true)//
				.setFontsize5er(fontSizeScala * 0.75f)//
				.draw(bitmapCanvas);

		if (type.equals(TimerType.Timer)) {
			// ##############################################
			// Playing
			new LevelPart(center, maxRadius * 0.71f, maxRadius * 0.66f, level, startWinkel - 5, -80, EZColoring.ColorOf100)//
					.configureSegemte(1.5f, strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all)//
					.setMode(EZMode.Zehner)//
					.draw(bitmapCanvas);

			new LevelPart(center, maxRadius * 0.64f, maxRadius * 0.60f, level, startWinkel - 5, -80, EZColoring.ColorOf100)//
					.configureSegemte(1.5f, strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.draw(bitmapCanvas);
			new ZeigerPart(center, level, maxRadius * 0.66f, maxRadius * 0.26f, strokeWidth, startWinkel - 5, -80, EZMode.Zehner)//
					.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
					.overrideColor(Color.WHITE)//
					.draw(bitmapCanvas);
			new ZeigerPart(center, level, maxRadius * 0.60f, maxRadius * 0.26f, strokeWidth, startWinkel - 5, -80, EZMode.EinerOnly10Segmente)//
					.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
					.overrideColor(Color.WHITE)//
					.draw(bitmapCanvas);

			// ##############################################
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel, new DropShadow(strokeWidth * 3, strokeWidth * 2, strokeWidth * 2, Color.BLACK));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = -225 + Math.round(level * 2.7f);

		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.92f);
		mArc.addArc(oval, winkel - 90, 180);
		final String text = Settings.getChargingText();
		final Paint p = PaintProvider.getTextPaint(level, fontSizeArc);
		p.setTextAlign(Align.CENTER);
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
