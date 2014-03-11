package de.geithonline.systemlwp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;

public class Settings {

	public static int getOpacity() {
		final SharedPreferences prefs = LiveWallpaperService.prefs;
		final int op = Integer.valueOf(prefs.getString("opacity", "128"));
		return op;
	}

	public static Paint getErasurePaint() {
		final Paint paint = new Paint();
		paint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		paint.setXfermode(xfermode);
		paint.setStyle(Style.FILL);
		return paint;
	}

	public static int getColorForLevel(final int level) {

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

	public static Paint getTextPaint(final int level, final int fontSize) {
		final Paint paint = new Paint();
		paint.setColor(getColorForLevel(level));
		paint.setAlpha(getOpacity());
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		paint.setFakeBoldText(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextAlign(Align.CENTER);
		return paint;
	}

	public static Paint getBatteryPaint(final int level) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(getColorForLevel(level));
		paint.setAlpha(getOpacity());
		paint.setStyle(Style.FILL);
		return paint;
	}

	public static Paint getBackgroundPaint() {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.DKGRAY);
		paint.setAlpha(getOpacity());
		paint.setStyle(Style.FILL);
		return paint;
	}

}
