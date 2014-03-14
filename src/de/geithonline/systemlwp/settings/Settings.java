package de.geithonline.systemlwp.settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import de.geithonline.systemlwp.LiveWallpaperService;
import de.geithonline.systemlwp.R;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoWideV5;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV4;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.utils.ColorHelper;

public class Settings {
	private final static SharedPreferences prefs = LiveWallpaperService.prefs;
	private static String style = "aaa";
	private static IBitmapDrawer bitmapDrawer;

	public static boolean isCenteredBattery() {
		return prefs.getBoolean("centerBattery", true);
	}

	private static int getFontSize100() {
		final int size = Integer.valueOf(prefs.getString("fontsize100", "100"));
		return size;
	}

	private static boolean isColoredNumber() {
		return prefs.getBoolean("colored_numbers", false);
	}

	private static boolean isGradientColors() {
		return prefs.getBoolean("gradient_colors", false);
	}

	private static boolean isGradientColorsMid() {
		return prefs.getBoolean("gradient_colors_mid", false);
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

	private static int getZeigerColor() {
		final int col = prefs.getInt("color_zeiger", R.integer.COLOR_WHITE);
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

	public static IBitmapDrawer getBatteryStyle() {
		// wenns den drawer noch nicht gibt, oder der style sich geändert hat
		if (bitmapDrawer == null || !style.equals(prefs.getString("batt_style", "ZoopaWideV3"))) {
			// getting Style from Settings
			style = prefs.getString("batt_style", "ZoopaWideV3");
			// returning the right Style
			if (style.equals("ZoopaWideV1")) {
				bitmapDrawer = new BitmapDrawerZoopaWideV1();
				return bitmapDrawer;
			}
			if (style.equals("ZoopaWideV2")) {
				bitmapDrawer = new BitmapDrawerZoopaWideV2();
				return bitmapDrawer;
			}
			if (style.equals("ZoopaWideV3")) {
				bitmapDrawer = new BitmapDrawerZoopaWideV3();
				return bitmapDrawer;
			}
			if (style.equals("ZoopaWideV4")) {
				bitmapDrawer = new BitmapDrawerZoopaWideV4();
				return bitmapDrawer;
			}

			if (style.equals("ZoopaCircleV1")) {
				bitmapDrawer = new BitmapDrawerZoopaCircleV1();
				return bitmapDrawer;
			}
			if (style.equals("ZoopaCircleV2")) {
				bitmapDrawer = new BitmapDrawerZoopaCircleV2();
				return bitmapDrawer;
			}

			if (style.equals("TachoWideV5")) {
				bitmapDrawer = new BitmapDrawerTachoWideV5();
				return bitmapDrawer;
			}
			bitmapDrawer = new BitmapDrawerZoopaWideV3();
			return bitmapDrawer;
		}
		return bitmapDrawer;
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
			if (isGradientColors())
				return getGradientColorForLevel(level);
			else
				return getBattColor();
		} else {
			if (level < getLowThreshold()) {
				return getBattColorLow();
			} else {
				if (isGradientColorsMid())
					return getGradientColorForLevel(level);
				else
					return getBattColorMid();
			}
		}
	}

	public static int getGradientColorForLevel(final int level) {

		if (level > getMidThreshold()) {
			return ColorHelper.getRadiantColor(getBattColor(), getBattColorMid(), level, 100, getMidThreshold());
		} else {
			if (level < getLowThreshold()) {
				return getBattColorLow();
			} else {
				return ColorHelper.getRadiantColor(getBattColorLow(), getBattColorMid(), level, getLowThreshold(), getMidThreshold());
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

		int fSize = fontSize;
		if (level == 100) {
			fSize = Math.round(fontSize * getFontSize100() / 100f);
		}
		paint.setTextSize(fSize);
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

	public static Paint getZeigerPaint(final int level) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(getZeigerColor());
		paint.setAlpha(255);
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
