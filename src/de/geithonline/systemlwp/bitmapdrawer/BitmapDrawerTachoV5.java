package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerTachoV5 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 5;
	private int skaleDicke = 100;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	protected Canvas bitmapCanvas;

	public BitmapDrawerTachoV5() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level) {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (cWidth < cHeight) {
			// hochkant
			setBitmapSize(cWidth, cWidth / 2, true);
		} else {
			// quer
			setBitmapSize(cWidth, cWidth / 2, false);
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(bWidth * 0.01f);
		skaleDicke = Math.round(bWidth * 0.12f);
		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.30f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		drawBogen(level);
		return bitmap;
	}

	private void drawBogen(final int level) {
		final Paint bgPaint = getBackgroundPaint();
		bgPaint.setColor(ColorHelper.brighter(bgPaint.getColor()));
		// äußeren Rand
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, bgPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, getErasurePaint());

		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 180, 180, true, getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 180, Math.round(level * 1.8), true, getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, getErasurePaint());

		// innerer Rand
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 180, 180, true, bgPaint);
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(offset), 180 + Math.round(level * 1.8) - 1, 2, true, getZeigerPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 0, 360, true, getErasurePaint());

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberBottom(bitmapCanvas, level, fontSize);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset / 2);
		mArc.addArc(oval, 200, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, getTextPaint(level, fontSizeArc));
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
		final Paint p = getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
