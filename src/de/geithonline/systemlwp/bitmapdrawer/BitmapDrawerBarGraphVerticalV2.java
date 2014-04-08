package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerBarGraphVerticalV2 extends BitmapDrawer {

	private final int offset = 5;
	private int einerDicke = 30;
	private int zehnerDicke = 100;
	private final float gap = 5f;
	private int fontSize = 150;
	private Canvas bitmapCanvas;

	public BitmapDrawerBarGraphVerticalV2() {
	}

	@Override
	public boolean supportsShowPointer() {
		return true;
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
			setBitmapSize(cWidth, cHeight, true);
		} else {
			// quer
			setBitmapSize(cWidth, cHeight, false);
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bWidth * 0.08f);
		zehnerDicke = Math.round(bWidth * 0.9f);
		fontSize = Math.round(bWidth * 0.25f);

		drawSegmente(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		if (Settings.isCenteredBattery()) {
			canvas.drawBitmap(bitmap, 0, cHeight / 2 - bHeight / 2, null);
		} else {
			canvas.drawBitmap(bitmap, 0, cHeight - bHeight - Settings.getVerticalPositionOffset(isPortrait()), null);
		}
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final int zehner = level / 10;

		final float hoeheOneSegment = (bHeight - 2 * offset - (segmente - 1) * gap) / segmente;
		final int bargraphHoehe = (bHeight - 2 * offset) * level / 100;

		// hintergrund
		Paint paint = getBackgroundPaint();
		final RectF levelRect = new RectF();
		levelRect.left = offset;
		levelRect.right = offset + einerDicke;
		levelRect.top = offset;
		levelRect.bottom = bHeight - offset;
		bitmapCanvas.drawRect(levelRect, paint);
		// level
		paint = getBatteryPaint(level);
		levelRect.left = offset;
		levelRect.right = offset + einerDicke;
		levelRect.top = bHeight - offset - bargraphHoehe;
		levelRect.bottom = bHeight - offset;
		bitmapCanvas.drawRect(levelRect, paint);
		if (Settings.isShowZeiger()) {
			// Zeiger
			paint = getZeigerPaint(level);
			levelRect.left = 0;
			levelRect.right = 2 * offset + einerDicke;
			levelRect.top = bHeight - offset - bargraphHoehe - 2;
			levelRect.bottom = bHeight - offset - bargraphHoehe + 2;
			bitmapCanvas.drawRect(levelRect, paint);
		}
		for (int i = 0; i < segmente; i++) {
			if (i <= zehner || level == 100) {
				paint = getBatteryPaint(level);
			} else {
				paint = getBackgroundPaint();
			}
			final float startY = bHeight - offset - i * (hoeheOneSegment + gap);
			final RectF r = new RectF();
			r.left = offset + einerDicke + offset;
			r.right = offset + einerDicke + offset + zehnerDicke;
			r.top = startY - hoeheOneSegment;
			r.bottom = startY;
			bitmapCanvas.drawRect(r, paint);
		}

		// delete inner Circle
		final RectF er = new RectF();
		er.left = 2 * offset + 2 * einerDicke;
		er.right = bWidth * 2;
		er.top = 0;
		er.bottom = 2 * bHeight;
		bitmapCanvas.drawArc(er, 0, 360, true, getErasurePaint());
	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		final Paint tp = getNumberPaint(level, fontSize);
		tp.setTextAlign(Align.LEFT);
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - offset, getNumberPaint(level, fontSize));
	}

	@Override
	public void drawChargeStatusText(final int level) {
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
