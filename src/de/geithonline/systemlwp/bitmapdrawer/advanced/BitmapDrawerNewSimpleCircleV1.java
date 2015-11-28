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
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.LevelPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.RingPart;
import de.geithonline.systemlwp.bitmapdrawer.drawingparts.ZeigerPart;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.systemlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class BitmapDrawerNewSimpleCircleV1 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;

	private float maxRadius;

	private final PointF center = new PointF();

	private final float levelRi;

	private final float levelRa;

	private final float bgRi;

	private final float bgRa;

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.5f;
	}

	public BitmapDrawerNewSimpleCircleV1() {
		bgRa = 0.99f;
		bgRi = 0.70f;
		levelRa = 0.99f;
		levelRi = 0.70f;
	}

	public BitmapDrawerNewSimpleCircleV1(final float bgRa, final float bgRi, final float levelRa, final float levelRi) {
		this.bgRa = bgRa;
		this.bgRi = bgRi;
		this.levelRa = levelRa;
		this.levelRi = levelRi;
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
		return false;
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

		// SkalaBackground
		new RingPart(center, maxRadius * bgRa, maxRadius * bgRi, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * levelRa, maxRadius * levelRi, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1.25f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// SkalaGlow
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.70f, maxRadius * 0.60f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 8, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.70f, maxRadius * 0.60f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		if (Settings.isShowZeiger()) {
			// Zeiger
			new ZeigerPart(center, level, maxRadius * 1f, maxRadius * 0.70f, strokeWidth, -90, 360, EZMode.Einer)//
					.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
					.draw(bitmapCanvas);
		}
		// Ausen Ring
		new RingPart(center, maxRadius * 0.70f, maxRadius * 0.60f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.59f, maxRadius * 0, PaintProvider.getBackgroundPaint())//
				// .setOutline(new Outline(Color.LTGRAY, strokeWidth / 2))//
				.draw(bitmapCanvas);
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 270 + Math.round(level * 3.6f);

		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.50f);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = PaintProvider.getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.40f);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
