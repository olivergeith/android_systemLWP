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
	private final int		d	= 30;

	public BitmapDrawerBigArc(final Canvas canvas) {
		this.canvas = canvas;
		cWidth = canvas.getWidth();
		cHeight = canvas.getHeight();
	}

	public Bitmap drawBitmap(final int level) {

		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);

		final Bitmap bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
		final Canvas c = new Canvas(bitmap);
		c.drawArc(new RectF(5, 5, cWidth - 5, cWidth - 5), 180, Math.round(level * 1.8), true, paint);

		// delete inner Circle
		paint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		paint.setXfermode(xfermode);
		paint.setStyle(Style.FILL);
		c.drawArc(new RectF(d, d, cWidth - d, cWidth - d), 0, 360, true, paint);
		return bitmap;
	}

}
