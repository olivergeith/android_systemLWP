package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerBarGraphV1 extends BitmapDrawer {

	private final int offset = 5;
	private int einerDicke = 30;
	private int zehnerDicke = 100;
	private final float gap = 5f;
	private int fontSize = 150;
	private int fontSizeArc = 20;

	private Canvas bitmapCanvas;

	public BitmapDrawerBarGraphV1() {
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

		einerDicke = Math.round(bHeight * 0.08f);
		zehnerDicke = Math.round(bWidth * 0.6f);
		fontSize = Math.round(bWidth * 0.25f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float breiteOneSegment = (bWidth - 2 * offset - (segmente - 1) * gap) / segmente;
		final int zehner = level / 10;
		final int einer = level % 10;

		Paint paint;

		for (int i = 0; i < segmente; i++) {
			if (i < einer || level == 100) {
				paint = getBatteryPaint(level);
			} else if (i == einer) {
				paint = getZeigerPaint(level);
			} else {
				paint = getBackgroundPaint();
			}
			final float startX = 0f + i * (breiteOneSegment + gap);
			final RectF r = new RectF();
			r.left = startX + offset;
			r.right = startX + breiteOneSegment;
			r.top = bHeight - einerDicke - offset;
			r.bottom = bHeight - offset;
			bitmapCanvas.drawRect(r, paint);
		}

		for (int i = 0; i < segmente; i++) {
			if (i < zehner || level == 100) {
				paint = getBatteryPaint(level);
			} else if (i == zehner) {
				paint = getZeigerPaint(level);
			} else {
				paint = getBackgroundPaint();
			}
			final float startX = 0f + i * (breiteOneSegment + gap);
			final RectF r = new RectF();
			r.left = startX + offset;
			r.right = startX + breiteOneSegment;
			r.top = bHeight - -einerDicke - zehnerDicke - 2 * offset;
			r.bottom = bHeight - einerDicke - 2 * offset;
			bitmapCanvas.drawRect(r, paint);
		}

		// delete inner Circle
		final RectF er = getCutOutRect();
		bitmapCanvas.drawArc(er, 0, 360, true, getErasurePaint());

		drawLevelNumber(level);

	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - 3 * einerDicke - 3 * offset, getNumberPaint(level, fontSize));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = getCutOutRect();
		oval.offset(0, -5);
		mArc.addArc(oval, 89, -90);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, getTextPaint(level, fontSizeArc));
	}

	private RectF getCutOutRect() {
		final RectF er = new RectF();
		er.left = -bWidth;
		er.right = bWidth;
		er.top = -bHeight;
		er.bottom = bHeight - 2 * offset - einerDicke - einerDicke;
		return er;
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
