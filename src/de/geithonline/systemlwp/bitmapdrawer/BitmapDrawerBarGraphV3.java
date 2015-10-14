package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerBarGraphV3 extends BitmapDrawer {

	private final int offset = 5;
	private int einerDicke = 20;
	private int fontSize = 150;
	private int fontSizeArc = 20;

	private Canvas bitmapCanvas;

	public BitmapDrawerBarGraphV3() {
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
			setBitmapSize(cWidth, cWidth / 2, true);
		} else {
			// quer
			setBitmapSize(cWidth, cWidth / 2, false);
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		einerDicke = Math.round(bHeight * 0.05f);
		fontSize = Math.round(bWidth * 0.1f);
		fontSizeArc = Math.round(bWidth * 0.025f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {

		// hintergrund
		Paint paint = getBackgroundPaint();
		final RectF levelRect = new RectF();
		levelRect.left = 0 + offset;
		levelRect.right = bWidth - offset;
		levelRect.top = bHeight - einerDicke - offset;
		levelRect.bottom = bHeight - offset;
		bitmapCanvas.drawRect(levelRect, paint);
		// level
		paint = getBatteryPaint(level);
		levelRect.left = 0 + offset;
		final int breite = (bWidth - 2 * offset) * level / 100;
		levelRect.right = offset + breite;
		levelRect.top = bHeight - einerDicke - offset;
		levelRect.bottom = bHeight - offset;
		bitmapCanvas.drawRect(levelRect, paint);
		if (Settings.isShowZeiger()) {
			// Zeiger
			paint = getZeigerPaint(level);
			levelRect.left = offset + breite - 3;
			levelRect.right = offset + breite + 3;
			levelRect.top = bHeight - einerDicke - 2 * offset;
			levelRect.bottom = bHeight;
			bitmapCanvas.drawRect(levelRect, paint);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		bitmapCanvas.drawText("" + level, 0 + 2 * offset, bHeight - 1 * einerDicke - 3 * offset, getNumberPaintAlignLeft(level, fontSize));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Paint p = getTextPaintAlignRight(level, fontSizeArc);
		int fader = 10 - level % 10;
		if (fader == 0) {
			fader = 1;
		}
		fader = p.getAlpha() * fader / 10;
		final String text = Settings.getChargingText();
		p.setAlpha(fader);
		bitmapCanvas.drawText(text, bWidth - 2 * offset, bHeight - 2 * offset, p);
	}

	@Override
	public void drawBattStatusText() {
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = getTextBattStatusPaint(fontSizeArc, Align.LEFT, true);
		bitmapCanvas.drawText(text, offset, bHeight - 2 * offset, p);
	}

}
