package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerTachoWideV1 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 5;
	private int skaleDicke = 100;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	protected Canvas bitmapCanvas;

	private int bWidth = 0;
	private int bHeight = 0;
	private int fontSizeScala = 20;

	public BitmapDrawerTachoWideV1() {
	}

	@Override
	public boolean supportsOrientation() {
		return true;
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsMoveUP() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Canvas canvas) {
		if (Settings.getOrientation() == Settings.ORIENTATION_BOTTOM) {
			bWidth = cWidth;
			bHeight = cWidth / 2;
		} else {
			bWidth = cHeight;
			bHeight = cHeight / 2;
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(bWidth * 0.01f);
		skaleDicke = Math.round(bWidth * 0.13f);
		fontSize = Math.round(bWidth * 0.30f);
		fontSizeArc = Math.round(bWidth * 0.04f);
		fontSizeScala = Math.round(bWidth * 0.05f);
		offset = fontSizeArc;

		drawBogen(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {

		switch (Settings.getOrientation()) {
		default:
		case Settings.ORIENTATION_BOTTOM:
			canvas.drawBitmap(bitmap, 0, cHeight - bHeight - 5 - Settings.getVerticalPositionOffset(isPortrait()), null);
			break;
		case Settings.ORIENTATION_LEFT:
			canvas.drawBitmap(BitmapHelper.rotate(bitmap, 90f), 5, 0, null);
			break;
		case Settings.ORIENTATION_RIGHT:
			canvas.drawBitmap(BitmapHelper.rotate(bitmap, 270f), cWidth - 5 - bHeight, 0, null);
			break;
		}
	}

	private void drawBogen(final int level) {
		final Paint bgPaint = Settings.getBackgroundPaint();
		bgPaint.setColor(Color.WHITE);
		bgPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		// ‰uﬂeren Rand
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, bgPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, Settings.getErasurePaint());

		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 180, 180, true, Settings.getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 180, Math.round(level * 1.8), true, Settings.getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, Settings.getErasurePaint());

		// Skalatext
		drawScalaText();

		// Zeiger
		final Paint zp = Settings.getZeigerPaint(level);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		bitmapCanvas.drawArc(getRectForOffset(0), 180 + Math.round(level * 1.8) - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, Settings.getErasurePaint());

		// innerer Rand
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 180, 180, true, bgPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 0, 360, true, Settings.getErasurePaint());

		// innere Fl‰che
		final Paint bgPaint2 = Settings.getBackgroundPaint();
		bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 180, 180, true, bgPaint2);
	}

	private void drawScalaText() {
		long winkel = 180 + Math.round(level * 1.8f);
		for (int i = 10; i < 100; i = i + 10) {
			// final int winkel = -81 + (i * 18);
			winkel = 171 + Math.round(i * 1.8f);
			final Path mArc = new Path();
			final RectF oval = getRectForOffset(offset + bogenDicke + fontSizeScala);
			mArc.addArc(oval, winkel, 18);
			final Paint p = Settings.getEraserTextPaint(i, fontSizeScala);
			p.setTextAlign(Align.CENTER);
			bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
		}
		for (int i = 10; i < 100; i = i + 10) {
			// Zeiger
			final Paint zp = Settings.getZeigerPaint(level);
			zp.setShadowLayer(10, 0, 0, Color.BLACK);
			bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke - fontSizeArc / 2), (float) (180f + i * 1.8 - 0.5f), 1f,
					true, zp);
		}

	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		final Paint p = Settings.getTextPaint(level, fontSize);
		p.setShadowLayer(10, 0, 0, Color.BLACK);
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - 10, p);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 182 + Math.round(level * 1.8f);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc - 4);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = Settings.getTextArcPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	// @Override
	// public void drawChargeStatusText(final int level) {
	// final Path mArc = new Path();
	// final RectF oval = getRectForOffset(offset / 2);
	// mArc.addArc(oval, 200, 180);
	// final String text = Settings.getChargingText();
	// bitmapCanvas.drawTextOnPath(text, mArc, 0, 0,
	// Settings.getTextArcPaint(level, fontSizeArc));
	// }

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

}
