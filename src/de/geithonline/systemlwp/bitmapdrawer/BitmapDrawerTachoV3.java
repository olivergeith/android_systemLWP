package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerTachoV3 extends BitmapDrawer {

	private int offset = 5;
	private int bogenDicke = 5;
	private int skaleDicke = 100;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	protected Canvas bitmapCanvas;

	private int bWidth = 0;
	private int bHeight = 0;
	private int fontSizeScala = 20;

	public BitmapDrawerTachoV3() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsCenter() {
		return true;
	}

	@Override
	public boolean supportsShowRand() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Canvas canvas) {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (cWidth < cHeight) {
			// hochkant
			bWidth = cWidth;
			bHeight = cWidth;
		} else {
			// quer
			bWidth = cHeight;
			bHeight = cHeight;
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);

		bogenDicke = Math.round(bWidth * 0.01f);
		skaleDicke = Math.round(bWidth * 0.13f);
		fontSize = Math.round(bWidth * 0.37f);
		fontSizeArc = Math.round(bWidth * 0.04f);
		fontSizeScala = Math.round(bWidth * 0.05f);
		offset = fontSizeArc;

		drawBogen(level);
		return bitmap;
	}

	@Override
	public void drawOnCanvas(final Bitmap bitmap, final Canvas canvas) {
		// draw mittig
		if (Settings.isCenteredBattery()) {
			canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight / 2 - bHeight / 2, null);
		} else {
			// draw unten
			if (cWidth < cHeight) {
				// unten
				canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight - bHeight, null);
			} else {
				// links
				canvas.drawBitmap(bitmap, cWidth - bWidth, cHeight / 2 - bHeight / 2, null);
			}
		}
	}

	private void drawBogen(final int level) {
		final Paint randPaint = Settings.getBackgroundPaint();
		randPaint.setColor(Color.WHITE);
		randPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, 360, true, Settings.getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset), 270, Math.round(level * 3.6), true, Settings.getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, Settings.getErasurePaint());

		// Skalatext
		drawScalaText();

		if (Settings.isShowRand()) {
			// äußeren Rand
			randPaint.setStrokeWidth(bogenDicke);
			randPaint.setStyle(Style.STROKE);
			bitmapCanvas.drawArc(getRectForOffset(offset + 2), 270, 360, true, randPaint);
		}
		// Zeiger
		final Paint zp = Settings.getZeigerPaint(level);
		zp.setShadowLayer(10, 0, 0, Color.BLACK);
		bitmapCanvas.drawArc(getRectForOffset(0), 270 + Math.round(level * 3.6) - 1, 2, true, zp);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, Settings.getErasurePaint());

		// innerer Rand
		randPaint.setStyle(Style.FILL);
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 270, 360, true, randPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 0, 360, true, Settings.getErasurePaint());

		// innere Fläche
		final Paint bgPaint2 = Settings.getBackgroundPaint();
		bgPaint2.setColor(ColorHelper.darker(bgPaint2.getColor()));
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 270, 360, true, bgPaint2);
	}

	private void drawScalaText() {
		long winkel = 270 + Math.round(level * 3.6f);
		for (int i = 0; i < 100; i = i + 10) {
			// final int winkel = -81 + (i * 18);
			winkel = 252 + Math.round(i * 3.6f);
			final Path mArc = new Path();
			final RectF oval = getRectForOffset(offset + bogenDicke + fontSizeScala);
			mArc.addArc(oval, winkel, 36);
			final Paint p = Settings.getEraserTextPaint(i, fontSizeScala);
			p.setTextAlign(Align.CENTER);
			bitmapCanvas.drawTextOnPath("" + i, mArc, 0, 0, p);
		}
		for (int i = 0; i < 100; i = i + 10) {
			// Zeiger
			final Paint zp = Settings.getZeigerPaint(level);
			zp.setShadowLayer(10, 0, 0, Color.BLACK);
			bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke - fontSizeArc / 2), (float) (270f + i * 3.6 - 0.5f), 1f,
					true, zp);
		}

	}

	@Override
	public void drawLevelNumber(final int level) {
		final String text = "" + level;
		final Paint p = Settings.getTextPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		final PointF point = getTextCenterToDraw(text, getRectForOffset(0), p);
		bitmapCanvas.drawText(text, point.x, point.y, p);
	}

	private static PointF getTextCenterToDraw(final String text, final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 272 + Math.round(level * 3.6f);

		final Path mArc = new Path();
		final RectF oval = getRectForOffset(bogenDicke + skaleDicke);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = Settings.getTextArcPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke + fontSizeArc);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = Settings.getTextArcPaint(100, fontSizeArc, Align.CENTER);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bWidth - offset);
	}

}
