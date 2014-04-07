package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerSimpleArcV2 extends BitmapDrawer {

	private int offset = 10;
	private int einerDicke = 70;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerSimpleArcV2() {
	}

	@Override
	public boolean supportsShowRand() {
		return true;
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
	public Bitmap drawBitmap(final int level) {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (cWidth < cHeight) {
			// hochkant
			setBitmapSize(cWidth, cWidth / 2 + Math.round(cWidth * 0.05f), true);
		} else {
			// quer
			setBitmapSize(cWidth, cWidth / 2 + Math.round(cWidth * 0.05f), false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bWidth * 0.10f);
		offset = Math.round(bWidth * 0.01f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.03f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {
		final Paint bgPaint = Settings.getBackgroundPaint();
		bgPaint.setStrokeWidth(einerDicke);
		bgPaint.setStyle(Style.STROKE);
		bgPaint.setStrokeCap(Cap.ROUND);
		// scala
		Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + einerDicke / 2);
		mArc.addArc(oval, 180, 180);
		bitmapCanvas.drawPath(mArc, bgPaint);

		// level
		final Paint battPaint = Settings.getBatteryPaint(level);
		if (Settings.isShowRand()) {
			battPaint.setStrokeWidth(Math.round(einerDicke * 0.85f));
		} else {
			battPaint.setStrokeWidth(einerDicke);
		}
		battPaint.setStyle(Style.STROKE);
		battPaint.setStrokeCap(Cap.ROUND);

		mArc = new Path();
		mArc.addArc(oval, 180, level * 1.8f);
		bitmapCanvas.drawPath(mArc, battPaint);
		if (Settings.isShowZeiger()) {
			final Paint zeigerPaint = Settings.getZeigerPaint(level);
			zeigerPaint.setStrokeWidth(Math.round(einerDicke * 1.05f));
			zeigerPaint.setStyle(Style.STROKE);
			zeigerPaint.setStrokeCap(Cap.ROUND);
			mArc = new Path();
			mArc.addArc(oval, 180 + level * 1.8f - 0.2f, 0.4f);
			bitmapCanvas.drawPath(mArc, zeigerPaint);
		}
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final float startwinkel = 180f + level * 1.5f;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberBottom(bitmapCanvas, level, fontSize);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight * 2 - offset - einerDicke);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + einerDicke + fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}
}
