package de.geithonline.systemlwp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class BitmapDrawerBigArc {

	private final int		cHeight;
	private final int		cWidth;
	private final Canvas	canvas;
	private final int		offset		= 5;
	private final int		bogenDicke	= 30;
	private final int		skaleDicke	= 100;
	private final int		abstand		= 8;
	private final float		gap			= 2f;

	public BitmapDrawerBigArc(final Canvas canvas) {
		this.canvas = canvas;
		cWidth = canvas.getWidth();
		cHeight = canvas.getHeight();
	}

	public void draw(final int level) {
		canvas.drawBitmap(drawBogen(level), 0, cHeight - cWidth / 2 - 5, null);
		canvas.drawBitmap(drawSegmente(level), 0, cHeight - cWidth / 2 - 5, null);
		canvas.drawBitmap(drawZeiger(level), 0, cHeight - cWidth / 2 - 5, null);
	}

	private Bitmap drawBogen(final int level) {
		final Bitmap bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
		final Canvas bitmapCanvas = new Canvas(bitmap);

		// Background
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, getBackgroundPaint());
		// Level
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, Math.round(level * 1.8), true, getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, getErasurePaint());

		return bitmap;
	}

	private Bitmap drawSegmente(final int level) {
		final Bitmap bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
		final Canvas bitmapCanvas = new Canvas(bitmap);

		final int segmente = 10;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;
		final int zehner = level / 10;

		final int off = offset + bogenDicke + abstand;

		// Skala Hintergergrund einer
		Paint paint;
		for (int i = 0; i < segmente; i++) {
			if (i < zehner || level == 100) {
				paint = getBatteryPaint(level);
			} else {
				paint = getBackgroundPaint();
			}
			final float startwinkel = 180f + i * (winkelOneSegment + gap);
			bitmapCanvas.drawArc(getRectForOffset(off), startwinkel, winkelOneSegment, true, paint);
		}

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, getErasurePaint());
		return bitmap;
	}

	private Bitmap drawZeiger(final int level) {
		final Bitmap bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
		final Canvas bitmapCanvas = new Canvas(bitmap);

		final int segmente = 10;
		final float winkelOneSegment = (180f - (segmente - 1) * gap) / segmente;
		int zehner = level / 10;
		final int einer = level % 10;

		final int off = offset + bogenDicke + abstand;
		final int radiusDelta = skaleDicke * einer / 10;

		// Skala Hintergergrund einer
		final Paint paint = getBatteryPaint(level);
		if (zehner == 10)
			zehner = 9;
		final float startwinkel = 180f + zehner * (winkelOneSegment + gap);

		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke - radiusDelta), startwinkel, winkelOneSegment, true, paint);

		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(off + skaleDicke), 0, 360, true, getErasurePaint());
		return bitmap;
	}

	private Paint getBatteryPaint(final int level) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		return paint;
	}

	private Paint getBackgroundPaint() {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.DKGRAY);
		paint.setStyle(Style.FILL);
		return paint;
	}

	private Paint getErasurePaint() {
		final Paint paint = new Paint();
		paint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		paint.setXfermode(xfermode);
		paint.setStyle(Style.FILL);
		return paint;
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

}
