package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ChargeIconPath;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerBatteryV1 extends BitmapDrawer {

	private int offset = 10;
	private int knobOffset = 10;
	private int randDicke = 70;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private Canvas bitmapCanvas;
	private int zeigerDicke = 70;

	public BitmapDrawerBatteryV1() {
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
			setBitmapSize(cWidth, cWidth / 2, true);
		} else {
			// quer
			setBitmapSize(cHeight, cHeight / 2, false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		zeigerDicke = Math.round(bWidth * 0.01f);
		randDicke = Math.round(bWidth * 0.03f);
		offset = Math.round(bWidth * 0.01f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.045f);
		knobOffset = Math.round(bWidth * 0.07f);
		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {
		final Paint bgPaint = Settings.getBackgroundPaint();
		final int bgColor = bgPaint.getColor();
		bgPaint.setColor(ColorHelper.darker(bgPaint.getColor()));
		// Battery
		RectF rect = getRectForOffset(offset);
		final float radius = 5;
		bitmapCanvas.drawRoundRect(rect, radius, radius, bgPaint);
		// erase inner part
		rect = getRectForOffset(offset + randDicke);
		bitmapCanvas.drawRoundRect(rect, radius - 1, radius - 1, Settings.getErasurePaint());
		// Knob
		bgPaint.setStyle(Style.FILL);
		final RectF knobrect = getRectForOffset(offset);
		knobrect.left = knobrect.right + randDicke * 1 / 4;
		knobrect.right = knobrect.right + knobOffset;
		knobrect.top = knobrect.top + bHeight * 0.2f;
		knobrect.bottom = knobrect.bottom - bHeight * 0.2f;
		bitmapCanvas.drawRoundRect(knobrect, radius, radius, bgPaint);

		// Inere fläche
		bgPaint.setStyle(Style.FILL);
		bgPaint.setColor(bgColor);
		rect = getRectForOffset(offset + randDicke * 5 / 4);
		bitmapCanvas.drawRoundRect(rect, radius - 1, radius - 1, bgPaint);
		// Level fläche
		final RectF levelrect = new RectF(rect);
		final float lev = levelrect.width() * level / 100;
		levelrect.right = levelrect.left + lev;
		bitmapCanvas.drawRoundRect(levelrect, radius - 1, radius - 1, Settings.getBatteryPaint(level));
		// Zeiger
		if (Settings.isShowZeiger()) {
			levelrect.top = levelrect.top - zeigerDicke;
			levelrect.bottom = levelrect.bottom + zeigerDicke;
			levelrect.right = levelrect.left + lev + zeigerDicke / 2;
			levelrect.left = levelrect.left + lev - zeigerDicke / 2;
			bitmapCanvas.drawRoundRect(levelrect, radius, radius, Settings.getZeigerPaint(level));
		}
		// knob weiss bei 100 oder charging
		if (level == 100) {
			bitmapCanvas.drawRoundRect(knobrect, radius, radius, Settings.getBatteryPaint(level));
		}
		if (Settings.isCharging) {
			final RectF r = new RectF(knobrect);
			bitmapCanvas.drawPath(new ChargeIconPath(r), Settings.getErasurePaint());
		}
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final float startwinkel = 180f + level * 3.6f;

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc + randDicke);
		mArc.addArc(oval, startwinkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, Settings.getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSize);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - knobOffset - offset, bHeight - offset);
	}

	@Override
	public void drawBattStatusText() {
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = Settings.getTextPaint(100, fontSizeArc, Align.CENTER, true, false);
		bitmapCanvas.drawText(text, bWidth / 2, offset + fontSizeArc, p);
	}

}
