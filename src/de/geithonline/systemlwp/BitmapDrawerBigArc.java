package de.geithonline.systemlwp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;

public class BitmapDrawerBigArc {

	private final int cHeight;
	private final int cWidth;
	private final Canvas canvas;
	private int offset = 5;
	private int bogenDicke = 30;
	private int skaleDicke = 100;
	private int abstand = 8;
	private final float gap = 2f;
	private int fontSize = 150;

	public BitmapDrawerBigArc(final Canvas canvas) {
		this.canvas = canvas;
		cWidth = canvas.getWidth();
		cHeight = canvas.getHeight();
		bogenDicke = Math.round(cWidth * 0.035f);
		skaleDicke = Math.round(cWidth * 0.14f);
		offset = Math.round(cWidth * 0.011f);
		abstand = Math.round(cWidth * 0.015f);
		fontSize = Math.round(cWidth * 0.25f);
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

		// draw percentage Number
		bitmapCanvas.drawText("" + level, cWidth / 2, cWidth / 2 - 10, getTextPaint(level));

		return bitmap;
	}

	private Paint getTextPaint(final int level) {
		final Paint paint = new Paint();
		paint.setColor(getColorForLevel(level));
		paint.setAlpha(128);
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		paint.setFakeBoldText(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextAlign(Align.CENTER);
		return paint;
	}

	private Paint getBatteryPaint(final int level) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(getColorForLevel(level));
		paint.setAlpha(128);
		paint.setStyle(Style.FILL);
		return paint;
	}

	private Paint getBackgroundPaint() {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.DKGRAY);
		paint.setAlpha(128);
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

	private int getColorForLevel(final int level) {

		if (level > 30) {
			return Color.WHITE;
		} else {
			if (level < 10) {
				return Color.RED;
			} else {
				return Color.YELLOW;
			}
		}
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

}
