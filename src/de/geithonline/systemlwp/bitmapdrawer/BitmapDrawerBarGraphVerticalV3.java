package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerBarGraphVerticalV3 extends BitmapDrawer {

	private final int offset = 5;
	private int einerDicke = 30;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;

	public BitmapDrawerBarGraphVerticalV3() {
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
		fontSize = Math.round(bWidth * 0.25f);
		fontSizeArc = Math.round(bWidth * 0.04f);

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
		final int levelHoehe = (bHeight - 2 * offset) * level / 100;

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
		levelRect.top = bHeight - offset - levelHoehe;
		levelRect.bottom = bHeight - offset;
		bitmapCanvas.drawRect(levelRect, paint);
		if (Settings.isShowZeiger()) {
			// Zeiger
			paint = Settings.getZeigerPaint(level);
			levelRect.left = 0;
			levelRect.right = 2 * offset + einerDicke;
			levelRect.top = bHeight - offset - levelHoehe - 2;
			levelRect.bottom = bHeight - offset - levelHoehe + 2;
			bitmapCanvas.drawRect(levelRect, paint);
		}
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final String text = Settings.getChargingText();
		final int levelHoehe = (bHeight - 6 * offset) * level / 100;
		final int top = bHeight - 2 * offset - levelHoehe;
		bitmapCanvas.drawText(text, offset + einerDicke + offset, top, Settings.getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		final Paint tp = Settings.getNumberPaint(level, fontSize);
		tp.setTextAlign(Align.LEFT);
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - offset, Settings.getNumberPaint(level, fontSize));
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}
}
