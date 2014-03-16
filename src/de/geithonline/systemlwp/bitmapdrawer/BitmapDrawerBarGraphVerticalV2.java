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
	private int bWidth;
	private int bHeight;

	public BitmapDrawerBarGraphVerticalV2() {
	}

	@Override
	public Bitmap drawBitmap(final int level, final Canvas canvas) {
		bWidth = cWidth / 2;
		bHeight = cHeight;

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bWidth * 0.08f);
		zehnerDicke = Math.round(bWidth * 0.9f);
		fontSize = Math.round(cWidth * 0.25f);

		drawSegmente(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, null);
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final int zehner = level / 10;

		final float hoeheOneSegment = (bHeight - 2 * offset - (segmente - 1) * gap) / segmente;
		final int bargraphHoehe = (bHeight - 2 * offset) * level / 100;

		// hintergrund
		Paint paint = Settings.getBackgroundPaint();
		final RectF levelRect = new RectF();
		levelRect.left = offset;
		levelRect.right = offset + einerDicke;
		levelRect.top = offset;
		levelRect.bottom = bHeight - offset;
		bitmapCanvas.drawRect(levelRect, paint);
		// level
		paint = Settings.getBatteryPaint(level);
		levelRect.left = offset;
		levelRect.right = offset + einerDicke;
		levelRect.top = bHeight - offset - bargraphHoehe;
		levelRect.bottom = bHeight - offset;
		bitmapCanvas.drawRect(levelRect, paint);
		if (Settings.isShowZeiger()) {
			// Zeiger
			paint = Settings.getZeigerPaint(level);
			levelRect.left = 0;
			levelRect.right = 2 * offset + einerDicke;
			levelRect.top = bHeight - offset - bargraphHoehe - 2;
			levelRect.bottom = bHeight - offset - bargraphHoehe + 2;
			bitmapCanvas.drawRect(levelRect, paint);
		}
		for (int i = 0; i < segmente; i++) {
			if (i <= zehner || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else {
				paint = Settings.getBackgroundPaint();
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
		bitmapCanvas.drawArc(er, 0, 360, true, Settings.getErasurePaint());

		// draw percentage Number
		final Paint tp = Settings.getTextPaint(level, fontSize);
		tp.setTextAlign(Align.LEFT);
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - offset, Settings.getTextPaint(level, fontSize));

	}

}
