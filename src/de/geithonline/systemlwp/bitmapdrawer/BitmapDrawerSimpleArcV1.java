package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerSimpleArcV1 extends BitmapDrawer {

	private int bWidth = 0;
	private int bHeight = 0;
	private int offset = 10;
	private int einerDicke = 70;
	private int zehnerDicke = 70;
	private final float gap = 0.6f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerSimpleArcV1() {
	}

	@Override
	public boolean supportsPointerColor() {
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
	public boolean supportsMoveUP() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level) {
		// welche kantge ist schmaler?
		if (cWidth < cHeight) {
			bWidth = cWidth;
			bHeight = cWidth / 2;
		} else {
			bWidth = cHeight;
			bHeight = cHeight / 2;
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bWidth * 0.03f);
		zehnerDicke = Math.round(bWidth * 0.12f);
		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.20f);
		fontSizeArc = Math.round(bWidth * 0.045f);

		drawSegmente(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {

		canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight - bHeight - offset - Settings.getVerticalPositionOffset(isPortrait()), null);
	}

	private void drawSegmente(final int level) {

		final int segmente = 100;
		final float winkelOneSegment = (180f - (segmente - 0) * gap) / segmente;

		Paint paint;
		final Paint bgPaint = Settings.getBackgroundPaint();
		final Paint btPaint = Settings.getBatteryPaint(100);
		for (int i = 0; i < segmente; i++) {
			if (i < level || level == 100) {
				paint = bgPaint;
			} else {
				paint = btPaint;
			}
			final float startwinkel = 180f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke), 0, 360, true, Settings.getErasurePaint());

		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset), 180, 180, true, Settings.getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset), 180, Math.round(level * 1.8), true, Settings.getBatteryPaint(level));

		// Skalatext
		drawScalaText();

		// Zeiger
		final Paint zp = Settings.getZeigerPaint(level);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		bitmapCanvas.drawArc(getRectForOffset(fontSizeArc), 180 + Math.round(level * 1.8) - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 0, 360, true, Settings.getErasurePaint());
		// Rand
		if (Settings.isShowRand()) {
			final Paint randPaint = Settings.getBackgroundPaint();
			randPaint.setColor(Color.WHITE);
			randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
			// äußeren Rand
			randPaint.setStrokeWidth(offset);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 180, 180, true, randPaint);
			// innere Fläche
			final Paint bgPaint2 = Settings.getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke), 180, 180, true, bgPaint2);
		}

	}

	private void drawScalaText() {
		final Paint p = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		p.setTextAlign(Align.CENTER);
		p.setAlpha(255);
		final RectF oval = getRectForOffset(offset + fontSizeArc + einerDicke + offset + fontSizeArc);
		for (int i = 0; i <= 100; i = i + 10) {
			if (i > 0 && i < 100) {
				final long winkel = 180 - 18 + Math.round(i * 1.8f);
				final Path mArc = new Path();
				mArc.addArc(oval, winkel, 36);
				bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
			}
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke / 2), (float) (180f + i * 1.8 - 0.5f), 1f, true, p);
		}
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final int segmente = 100;
		final float winkelOneSegment = (180f - (segmente - 0) * gap) / segmente;
		final float startwinkel = 182f + level * (winkelOneSegment + gap) + gap / 2;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + einerDicke + offset + zehnerDicke + fontSizeArc);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		final Paint p = Settings.getNumberPaint(level, fontSize);
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - 10, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight * 2 - offset);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}
}
