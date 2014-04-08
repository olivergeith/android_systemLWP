package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerBrickV1 extends BitmapDrawer {

	private float rand = 5;
	private float gap = 5;
	protected Canvas bitmapCanvas;
	private int fontSize = 150;
	private int fontSizeArc = 20;

	public BitmapDrawerBrickV1() {
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
			setBitmapSize(cWidth, cWidth, true);
		} else {
			// quer
			setBitmapSize(cHeight, cHeight, false);
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);
		fontSize = Math.round(bWidth * 0.5f);
		fontSizeArc = Math.round(bWidth * 0.04f);
		rand = Math.round(bWidth * 0.05f);
		gap = Math.round(bWidth * 0.01f);

		drawSkala(level);
		return bitmap;
	}

	private void drawSkala(final int level) {
		final float w = (bWidth - 2 * rand - 9 * gap) / 10;
		final float h = (bHeight - 2 * rand - 9 * gap) / 10;

		final Paint bgPaint = getBackgroundPaint();
		final Paint battPaint = getBatteryPaint(level);

		for (int j = 0; j < 100; j++) {
			final int zehner = j / 10;
			final int einer = j % 10;

			final float x = rand + (einer) * (w + gap);
			final float y = bHeight - rand - h - zehner * (h + gap);
			final RectF r = new RectF(x, y, x + w, y + h);
			if (j < level) {
				bitmapCanvas.drawRect(r, battPaint);
			} else {
				bitmapCanvas.drawRect(r, bgPaint);
			}
			final String text = "" + (j + 1);
			final Paint p = getTextScalePaint(fontSizeArc, Align.CENTER, true);
			p.setTextAlign(Align.CENTER);
			p.setAlpha(255);
			bitmapCanvas.drawText(text, r.centerX(), r.centerY() + fontSizeArc / 2, p);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSize);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Paint p = getTextPaint(level, fontSizeArc * 3 / 2);
		int fader = 10 - level % 10;
		if (fader == 0) {
			fader = 1;
		}
		fader = p.getAlpha() * fader / 10;
		final String text = Settings.getChargingText();
		p.setTextAlign(Align.CENTER);
		p.setAlpha(fader);
		bitmapCanvas.drawText(text, bWidth / 2, rand + gap, p);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
