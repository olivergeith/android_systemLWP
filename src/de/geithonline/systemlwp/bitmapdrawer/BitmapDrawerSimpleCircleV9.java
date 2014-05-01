package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerSimpleCircleV9 extends BitmapDrawer {

	private int offset = 10;
	private int levelDicke = 70;
	private int abstand = 8;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private final float gap = 1f;

	private Canvas bitmapCanvas;

	public BitmapDrawerSimpleCircleV9() {
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
	public boolean supportsShowRand() {
		return true;
	}

	public Paint getShadowRingPaint(final RectF rect, final boolean invert) {
		final Paint paint = getBackgroundPaint();
		paint.setAlpha(192);
		paint.setAntiAlias(true);
		if (!invert) {
			paint.setShader(new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, Color.BLACK, Color.LTGRAY, Shader.TileMode.MIRROR));
		} else {
			paint.setShader(new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, Color.LTGRAY, Color.BLACK, Shader.TileMode.MIRROR));
		}
		return paint;
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
		abstand = Math.round(bWidth * 0.01f);
		levelDicke = Math.round(bWidth * 0.05f);
		offset = Math.round(bWidth * 0.02f);
		fontSize = Math.round(bWidth * 0.35f);
		fontSizeArc = Math.round(bWidth * 0.04f);

		drawSegmente(level);
		return bitmap;
	}

	private void drawSegmente(final int level) {

		Paint bgPaint = getShadowRingPaint(getRectForOffset(offset + fontSizeArc), false);
		// Au�erer Rand
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc), 270, 360, true, bgPaint);
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand), 0, 360, true, getErasurePaint());

		// LevelBackground
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand), 270, 360, true, getBackgroundPaint());
		// Level
		final int segmente = 101;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;

		Paint paint;
		for (int i = 0; i < segmente; i++) {
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			if (i < level || level == 100) {
				paint = getBatteryPaint(level);
				bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand), startwinkel, winkelOneSegment, true, paint);
			} else if (i == level) {
				if (Settings.isShowZeiger()) {
					paint = getZeigerPaint(level);
				} else {
					paint = getBatteryPaint(level);
				}
				bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand), startwinkel, winkelOneSegment, true, paint);
			} else {
				// do nothing
				// paint = getBackgroundPaint();
			}
		}
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand + levelDicke), 0, 360, true, getErasurePaint());

		// glow
		if (Settings.isShowZeiger()) {
			bgPaint = getZeigerPaint(level);
			// bgPaint.setAlpha(192);
			// bgPaint.setShadowLayer(20, 0, 0,
			// getBatteryPaint(level).getColor());
			bgPaint.setShadowLayer(15, 0, 0, getZeigerPaint(level).getColor());
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand + levelDicke), 270, 360, true, bgPaint);
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand + levelDicke), 0, 360, true, getErasurePaint());
		}
		// Innerer Rand
		bgPaint = getShadowRingPaint(getRectForOffset(offset + fontSizeArc + abstand + levelDicke), true);
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand + levelDicke), 270, 360, true, bgPaint);
		bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand + levelDicke + abstand), 0, 360, true, getErasurePaint());

		if (Settings.isShowRand()) {
			final Paint randPaint = getBackgroundPaint();
			randPaint.setColor(Color.WHITE);
			randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
			// // �u�eren Rand
			// randPaint.setStrokeWidth(offset);
			// randPaint.setStyle(Style.STROKE);
			// bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc +
			// abstand + levelDicke + abstand + offset), 270, 360, true,
			// randPaint);
			// innere Fl�che
			final Paint bgPaint2 = getBackgroundPaint();
			bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
			bitmapCanvas.drawArc(getRectForOffset(offset + fontSizeArc + abstand + levelDicke + abstand), 270, 360, true, bgPaint2);
		}

	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 270 + Math.round(level * 3.6);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + fontSizeArc + abstand + levelDicke + abstand + fontSizeArc);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, getTextPaint(level, fontSizeArc));
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSize);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
