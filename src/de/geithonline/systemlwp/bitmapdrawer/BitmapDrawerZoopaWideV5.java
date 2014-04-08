package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaWideV5 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private final float gap = 0.8f;
	private int fontSize = 150;
	private Canvas bitmapCanvas;
	private int fontSizeArc = 20;

	public BitmapDrawerZoopaWideV5() {
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
		skaleDicke = Math.round(bWidth * 0.14f);
		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.25f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;

		// Bogen 1
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, getBackgroundPaint());
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, getErasurePaint());

		// Skala Hintergergrund einer
		final Paint paint = getBackgroundPaint();
		for (int i = 0; i < segmente; i++) {
			final float startwinkel = 180f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset), startwinkel, winkelOneSegment, true, paint);

			final int winkel = -81 + (i * 18);
			bitmapCanvas.save();
			bitmapCanvas.rotate(winkel, bWidth / 2, bWidth / 2);
			bitmapCanvas.drawText("" + (i + 1) * 10, bWidth / 2, offset + bogenDicke + offset + skaleDicke * 2 / 3,
					getTextScalePaint(fontSize / 4, Align.CENTER, true));
			bitmapCanvas.restore();

		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset + skaleDicke), 0, 360, true, getErasurePaint());

		// Bogen 2
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset + skaleDicke + offset), 180, 180, true, getBackgroundPaint());

		// overpaint level
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, Math.round(level * 1.8), true, getBatteryPaintSourceIn(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + offset + skaleDicke + offset + bogenDicke), 0, 360, true, getErasurePaint());

	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset / 2);
		mArc.addArc(oval, 200, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberBottom(bitmapCanvas, level, fontSize);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight * 2 - offset);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
