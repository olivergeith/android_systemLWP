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
	private final static SharedPreferences prefs = LiveWallpaperService.prefs;

	private static boolean isColoredNumber() {
		return prefs.getBoolean("colored_numbers", false);
	}

	private static int getMidThreshold() {
		final int thr = Integer.valueOf(prefs.getString("threshold_mid", "30"));
		return thr;
	}

	private static int getLowThreshold() {
		final int thr = Integer.valueOf(prefs.getString("threshold_low", "10"));
		return thr;
	}

	private static int getOpacity() {
		final int op = Integer.valueOf(prefs.getString("opacity", "128"));
		return op;
	}

	private static int getBackgroundOpacity() {
		final int op = Integer.valueOf(prefs.getString("background_opacity", "128"));
		return op;
	}

	private static int getBackgroundColor() {
		final int col = prefs.getInt("background_color", R.integer.COLOR_DARKGRAY);
		return col;
	}

	private static int getBattColor() {
		final int col = prefs.getInt("battery_color", R.integer.COLOR_WHITE);
		return col;
	}

	private static int getBattColorMid() {
		final int col = prefs.getInt("battery_color_mid", R.integer.COLOR_ORANGE);
		return col;
	}

	private static int getBattColorLow() {
		final int col = prefs.getInt("battery_color_low", R.integer.COLOR_RED);
		return col;
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

		if (level > getMidThreshold()) {
			return getBattColor();
		} else {
			if (level < getLowThreshold()) {
				return getBattColorLow();
			} else {
				return getBattColorMid();
			}
		}
	}

	public static Paint getTextPaint(final int level, final int fontSize) {
		final Paint paint = new Paint();
		if (isColoredNumber()) {
			paint.setColor(getColorForLevel(level));
		} else {
			paint.setColor(getBattColor());
		}
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
		paint.setColor(getBackgroundColor());
		paint.setAlpha(getBackgroundOpacity());
		paint.setStyle(Style.FILL);
		return paint;
	}

}
