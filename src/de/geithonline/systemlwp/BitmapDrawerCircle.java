package de.geithonline.systemlwp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class BitmapDrawerCircle {

	private final int		cHeight;
	private final int		cWidth;
	private final Canvas	canvas;
	private final int		d	= 10;

	public BitmapDrawerCircle(final Canvas canvas) {
		this.canvas = canvas;
		cWidth = canvas.getWidth();
		cHeight = canvas.getHeight();
	}

	public Bitmap drawBitmap(final int level) {

		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);

		final Bitmap bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
		final Canvas c = new Canvas(bitmap);
		c.drawArc(new RectF(0, 0, 64, 64), 0, Math.round(level * 3.6), true, paint);

		// delete inner Circle
		paint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		paint.setXfermode(xfermode);
		paint.setStyle(Style.FILL);
		c.drawArc(new RectF(d, d, 64 - d, 64 - d), 0, Math.round(level * 3.6), true, paint);
		return bitmap;
	}

}
