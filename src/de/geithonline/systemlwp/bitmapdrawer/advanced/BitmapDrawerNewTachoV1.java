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
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.HalfArchPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaLinePart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.SkalaTextPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.ZeigerPart;
import de.geithonline.systemlwp.bitmapdrawer.enums.BitmapRatio;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerNewTachoV1 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;

	private float maxRadius;

	private final PointF center = new PointF();

	private float fontSizeScala;

	@Override
	protected BitmapRatio getBitmapRatio() {
		return BitmapRatio.RECTANGULAR;
	}

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.1f;
		fontSizeLevel = maxRadius * 0.4f;
	}

	public BitmapDrawerNewTachoV1() {
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

		// SkalaBackground
		if (Settings.isShowRand()) {
			new HalfArchPart(center, maxRadius * 0.95f, maxRadius * 0.7f, PaintProvider.getBackgroundPaint())//
					.setOutline(new Outline(Color.WHITE, strokeWidth / 2))//
					.setUndercut(5f)//
					.draw(bitmapCanvas);
		} else {
			new HalfArchPart(center, maxRadius * 0.95f, maxRadius * 0.7f, PaintProvider.getBackgroundPaint())//
					.setUndercut(5f)//
					.draw(bitmapCanvas);
		}

		// Level
		new LevelPart(center, maxRadius * 0.92f, maxRadius * 0.7f, level, -180, 180, EZColoring.LevelColors)//
				.setSegemteAbstand(0.9f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);
		// Skala
		new SkalaLinePart(center, maxRadius * 0.75f, maxRadius * 0.7f, -180, 180)//
				.set5erRadiusAussen(maxRadius * 0.73f)//
				.setDicke10er(strokeWidth / 2)//
				.setDicke5er(strokeWidth / 3)//
				.setDraw100(true)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.77f, fontSizeScala, -180, 180)//
				.setDraw0(false)//
				.draw(bitmapCanvas);
		// innere Fläche
		new RingPart(center, maxRadius * 0.7f, maxRadius * 0, PaintProvider.getBackgroundPaint())//
				.setOutline(new Outline(Color.WHITE, strokeWidth))//
				.draw(bitmapCanvas);
		if (Settings.isShowZeiger()) {
			// Zeiger
			new ZeigerPart(center, level, maxRadius * 1f, maxRadius * 0.7f, strokeWidth, -180, 180, EZMode.Einer)//
					.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
					.draw(bitmapCanvas);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberBottom(bitmapCanvas, level, fontSizeLevel);
		// drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 182 + Math.round(level * 1.8f);

		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.87f);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = PaintProvider.getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.60f);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
