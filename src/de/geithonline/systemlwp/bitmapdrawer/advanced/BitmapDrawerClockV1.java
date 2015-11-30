package de.geithonline.systemlwp.bitmapdrawer.advanced;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.systemlwp.bitmapdrawer.data.Outline;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.systemlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.parts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.parts.ZeigerPart;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerClockV1 extends AdvancedBitmapDrawer {

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
		fontSizeLevel = maxRadius * 0.2f;
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
	public boolean supportsExtraLevelBars() {
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
		new LevelPart(center, maxRadius * 0.86f, maxRadius * 0.72f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setMode(EZMode.Einer)//
				.setStyle(EZStyle.sweep)//
				.draw(bitmapCanvas);
				// // Playing
				// new LevelPart(center, maxRadius * 0.70f, maxRadius * 0.65f, level, -180, 180, EZColoring.LevelColors)//
				// .configureSegemte(1f, strokeWidth / 3)//
				// .setMode(EZMode.Zehner)//
				// .setStyle(EZStyle.segmented_all)//
				// .draw(bitmapCanvas);
				// new ZeigerPart(center, level, maxRadius * 0.65f, maxRadius * 0.20f, strokeWidth * 1.5f, -180, 180, EZMode.Einer)//
				// .setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				// .setZeigerType(ZEIGER_TYP.needle)//
				// .overrideColor(Color.RED)//
				// .draw(bitmapCanvas);

		// Skalatext
		new SkalaLinePart(center, maxRadius * 0.90f, maxRadius * 0.84f, -90, 360)//
				.set5erRadiusAussen(maxRadius * 0.90f)//
				.set1erRadiusAussen(maxRadius * 0.90f)//
				.set5erRadiusInnen(maxRadius * 0.86f)//
				.set1erRadiusInnen(maxRadius * 0.88f)//
				.setDicke(strokeWidth / 2)//
				.setDicke10er(strokeWidth)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.75f, fontSizeScala, -90, 360)//
				.draw(bitmapCanvas);

		// level Numbers
		drawDualLevel(level);

		// Zeiger
		new ZeigerPart(center, level, maxRadius * 0.70f, maxRadius * 0.20f, strokeWidth * 1.5f, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.rect)//
				// .overrideColor(Color.RED)//
				.draw(bitmapCanvas);
		// einer
		new ZeigerPart(center, level, maxRadius * 0.95f, maxRadius * 0.20f, strokeWidth, -90, 360, EZMode.EinerOnly10Segmente)//
				.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.rect)//
				.draw(bitmapCanvas);
				// // zehner
				// new ZeigerPart(center, level, maxRadius * 0.80f, maxRadius * 0.20f, strokeWidth, -90, 360, EZMode.zehner)//
				// .setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
				// .setZeigerType(ZEIGER_TYP.rect)//
				// .draw(bitmapCanvas);

		// innere Fl�che
		new RingPart(center, maxRadius * 0.20f, maxRadius * 0f, new Paint())//
				.setColor(Color.WHITE)//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

	}

	@Override
	public void drawLevelNumber(final int level) {
	}

	private void drawDualLevel(final int level) {
		if (Settings.isShowExtraLevelBars()) {
			final PointF centerLi = new PointF(center.x - maxRadius * 0.25f, center.y + maxRadius * 0.38f);
			final PointF centerRe = new PointF(center.x + maxRadius * 0.25f, center.y + maxRadius * 0.38f);

			new RingPart(centerLi, maxRadius * 0.22f, 0, PaintProvider.getBackgroundPaint())//
					.setOutline(new Outline(Color.WHITE, strokeWidth / 2))//
					.draw(bitmapCanvas);
			new RingPart(centerRe, maxRadius * 0.22f, 0, PaintProvider.getBackgroundPaint())//
					.setOutline(new Outline(Color.WHITE, strokeWidth / 2))//
					.draw(bitmapCanvas);
			new LevelPart(centerLi, maxRadius * 0.20f, maxRadius * 0.15f, level, -90, 360, EZColoring.ColorOf100)//
					.setMode(EZMode.Zehner)//
					.setStyle(EZStyle.segmented_all)//
					.setSegemteAbstand(5f)//
					.setStrokeWidth(strokeWidth / 3)//
					.draw(bitmapCanvas);
			new LevelPart(centerRe, maxRadius * 0.20f, maxRadius * 0.15f, level, -90, 360, EZColoring.ColorOf100)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.setStyle(EZStyle.segmented_all)//
					.setSegemteAbstand(5f)//
					.setStrokeWidth(strokeWidth / 3)//
					.draw(bitmapCanvas);
			// LevelNumbers
			if (Settings.isShowNumber()) {
				int zehner = level / 10;
				if (level == 100) {
					zehner = 10;
				}
				final int einer = level % 10;
				drawLevelNumberCenteredInRect(bitmapCanvas, level, "" + zehner, fontSizeLevel, GeometrieHelper.getCircle(centerLi, maxRadius * 0.15f));
				drawLevelNumberCenteredInRect(bitmapCanvas, level, "" + einer, fontSizeLevel, GeometrieHelper.getCircle(centerRe, maxRadius * 0.15f));
			}
		} else {
			if (Settings.isShowNumber()) {
				drawLevelNumber(bitmapCanvas, level, fontSizeLevel * 1.5f, new PointF(center.x, center.x + maxRadius * 0.6f));
			}
		}
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
