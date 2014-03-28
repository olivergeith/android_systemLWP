package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.BitmapHelper;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerTachoV5 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 5;
	private int skaleDicke = 100;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	protected Canvas bitmapCanvas;

	private int bWidth = 0;
	private int bHeight = 0;

	public BitmapDrawerTachoV5() {
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
		skaleDicke = Math.round(bWidth * 0.12f);
		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.30f);
		fontSizeArc = Math.round(cWidth * 0.04f);

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
		bgPaint.setColor(ColorHelper.brighter(bgPaint.getColor()));
		// �u�eren Rand
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, bgPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, Settings.getErasurePaint());

		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 180, 180, true, Settings.getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 180, Math.round(level * 1.8), true, Settings.getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, Settings.getErasurePaint());

		// innerer Rand
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 180, 180, true, bgPaint);
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(offset), 180 + Math.round(level * 1.8) - 1, 2, true, Settings.getZeigerPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 0, 360, true, Settings.getErasurePaint());

	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - 10, Settings.getNumberPaint(level, fontSize));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset / 2);
		mArc.addArc(oval, 200, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextPaint(level, fontSizeArc));
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke + fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
