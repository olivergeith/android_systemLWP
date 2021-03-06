package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerBarGraphVerticalV1 extends BitmapDrawer {

	private final int offset = 5;
	private int einerDicke = 30;
	private final float gap = 5f;
	private int fontSize = 150;
	private Canvas bitmapCanvas;

	public BitmapDrawerBarGraphVerticalV1() {
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

		einerDicke = Math.round(bWidth * 0.10f);
		fontSize = Math.round(bWidth * 0.05f);

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

		final float hoeheOneSegment = (bHeight - 2 * offset - (segmente - 1) * gap) / segmente;
		final int bargraphHoehe = (bHeight - 2 * offset) * level / 100;

		for (int i = 0; i < segmente; i++) {
			Paint paint;
			paint = getBackgroundPaint();
			final float startY = bHeight - offset - i * (hoeheOneSegment + gap);
			final RectF r = new RectF();
			r.left = offset;
			r.right = offset + einerDicke;
			r.top = startY - hoeheOneSegment;
			r.bottom = startY;
			bitmapCanvas.drawRect(r, paint);
			bitmapCanvas.drawText("" + (i + 1) * 10, offset + einerDicke / 2, startY - hoeheOneSegment + fontSize - offset,
					getTextPaint(level, fontSize, Align.CENTER, true, true));
		}
		// level

		final RectF levelRect = new RectF();
		levelRect.left = offset;
		levelRect.right = offset + einerDicke;
		levelRect.top = bHeight - offset - bargraphHoehe;
		levelRect.bottom = bHeight - offset;
		bitmapCanvas.drawRect(levelRect, getBatteryPaintSourceIn(level));

	}

	@Override
	public void drawLevelNumber(final int level) {
		// nothing to do
	}

	@Override
	public void drawChargeStatusText(final int level) {
		// nothing to do
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}
}
