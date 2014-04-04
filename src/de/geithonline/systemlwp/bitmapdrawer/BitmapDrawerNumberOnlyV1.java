package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerNumberOnlyV1 extends BitmapDrawer {

	private int offset = 10;
	private final float gap = 0.6f;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerNumberOnlyV1() {
	}

	@Override
	public Bitmap drawBitmap(final int level) {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (cWidth < cHeight) {
			// hochkant
			setBitmapSize(cWidth, cWidth, true);
		} else {
			// quer
			setBitmapSize(cHeight, cHeight, false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		offset = Math.round(bWidth * 0.011f);
		fontSize = Math.round(bWidth * 0.55f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		// Logig is the other way round here because we want to force the
		// numberdrawing in this style
		// else is handled in abstract class!
		if (!Settings.isShowNumber()) {
			drawLevelNumber(level);
		}
		return bitmap;
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final int segmente = 101;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
		final float startwinkel = 270f + level * (winkelOneSegment + gap) + gap / 2;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberinCenterofBitmap(bitmapCanvas, level, fontSize);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
