package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;

public class BitmapDrawerZoopaCircleV2 implements IBitmapDrawer {

	private int cHeight;
	private int cWidth;
	private int bWidth = 0;
	private int bHeight = 0;
	private int offset = 10;
	private int einerDicke = 30;
	private int zehnerDicke = 100;
	private int abstand = 8;
	private final float gap = 2f;
	private int fontSize = 150;
	private Bitmap bitmap;
	private Canvas bitmapCanvas;
	private int level = -99;

	public BitmapDrawerZoopaCircleV2() {
	}

	@Override
	public void draw(final int level, final Canvas canvas) {

		// Bitmap neu berechnen wenn Level sich �ndert oder Canvas dimensions
		// anders
		if (this.level != level || canvas.getWidth() != cWidth || canvas.getHeight() != cHeight) {
			cWidth = canvas.getWidth();
			cHeight = canvas.getHeight();
			// welche kantge ist schmaler?
			if (cWidth < cHeight) {
				bWidth = cWidth;
				bHeight = cWidth;
			} else {
				bWidth = cHeight;
				bHeight = cHeight;
			}
			bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
			bitmapCanvas = new Canvas(bitmap);

			einerDicke = Math.round(bWidth * 0.05f);
			zehnerDicke = Math.round(bWidth * 0.12f);
			offset = Math.round(bWidth * 0.011f);
			abstand = Math.round(bWidth * 0.015f);
			fontSize = Math.round(bWidth * 0.35f);

			drawSegmente(level);
			drawNumber(level);
		}

		// draw mittig
		// canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight / 2 -
		// bHeight / 2, null);
		// draw unten
		if (cWidth < cHeight) {
			// unten
			canvas.drawBitmap(bitmap, cWidth / 2 - bWidth / 2, cHeight - bHeight, null);
		} else {
			// links
			canvas.drawBitmap(bitmap, cWidth - bWidth, cHeight / 2 - bHeight / 2, null);
		}

		this.level = level;
	}

	private void drawSegmente(final int level) {

		final int segmente = 10;
		final float winkelOneSegment = (360f - (segmente - 0) * gap) / segmente;
		final int zehner = level / 10;
		final int einer = level % 10;

		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i < einer || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else if (i == einer) {
				paint = Settings.getZeigerPaint(level);
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke), 0, 360, true, Settings.getErasurePaint());

		for (int i = 0; i < segmente; i++) {
			if (i < zehner || level == 100) {
				paint = Settings.getBatteryPaint(level);
			} else if (i == zehner) {
				paint = Settings.getZeigerPaint(level);
			} else {
				paint = Settings.getBackgroundPaint();
			}
			final float startwinkel = 270f + i * (winkelOneSegment + gap) + gap / 2;
			bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke + abstand), startwinkel, winkelOneSegment, true, paint);
		}
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + einerDicke + abstand + zehnerDicke), 0, 360, true, Settings.getErasurePaint());
	}

	private void drawNumber(final int level) {
		final String text = "" + level;
		final Paint p = Settings.getTextPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		final PointF point = getTextCenterToDraw(text, getRectForOffset(0), p);
		bitmapCanvas.drawText(text, point.x, point.y, p);
		// draw percentage Number
		// bitmapCanvas.drawText(text, bWidth / 2, bHeight / 2 - bounds.height()
		// / 2, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	private static PointF getTextCenterToDraw(final String text, final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

}
