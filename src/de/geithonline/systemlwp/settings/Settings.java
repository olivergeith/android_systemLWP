package de.geithonline.systemlwp.settings;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;
import de.geithonline.systemlwp.BackgroundPreferencesFragment;
import de.geithonline.systemlwp.LiveWallpaperService;
import de.geithonline.systemlwp.R;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerAokpCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphVerticalV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphVerticalV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoWideV5;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV4;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV5;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.utils.ColorHelper;

public class Settings {
	public final static SharedPreferences prefs = LiveWallpaperService.prefs;
	private static String style = "aaa";
	private static IBitmapDrawer bitmapDrawer;
	public static final int ANIMATION_STYLE_0_TO_100 = 1;
	public static final int ANIMATION_STYLE_0_TO_LEVEL = 2;
	private static Bitmap backgroundImage = null;

	public static final int ORIENTATION_BOTTOM = 0;
	public static final int ORIENTATION_LEFT = 90;
	public static final int ORIENTATION_RIGHT = 270;

	public static boolean isAnimationEnabled() {
		return prefs.getBoolean("animation_enable", true);
	}

	public static int getOrientation() {
		final int size = Integer.valueOf(prefs.getString("rotation", "0"));
		return size;
	}

	public static boolean isShowZeiger() {
		return prefs.getBoolean("show_zeiger", true);
	}

	public static int getAnimationDelaý() {
		final int thr = Integer.valueOf(prefs.getString("animation_delay", "50"));
		return thr;
	}

	public static int getAnimationDelaýOnCurrentLevel() {
		final int thr = Integer.valueOf(prefs.getString("animation_delay_level", "2500"));
		return thr;
	}

	public static boolean isDebugging() {
		return prefs.getBoolean("debug", false);
	}

	public static int getAnimationStyle() {
		final int size = Integer.valueOf(prefs.getString("animationStyle", "1"));
		return size;
	}

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

	// #####################################################################################
	// Styles
	// #####################################################################################
	public static IBitmapDrawer getBatteryStyle() {
		// wenns den drawer noch nicht gibt, oder der style sich geändert hat
		if (bitmapDrawer == null || !style.equals(getStyle())) {
			// getting Style from Settings
			style = getStyle();
			// returning the right Style
			bitmapDrawer = getDrawerForStyle(style);
			return bitmapDrawer;
		}
		return bitmapDrawer;
	}

	public static String getStyle() {
		if (prefs == null) {
			return "ZoopaWideV3";
		}
		return prefs.getString("batt_style", "ZoopaWideV3");
	}

	public static IBitmapDrawer getDrawerForStyle(final String battStyle) {
		IBitmapDrawer drawer;
		if (battStyle.equals("ZoopaWideV1")) {
			drawer = new BitmapDrawerZoopaWideV1();
			return drawer;
		}
		if (battStyle.equals("ZoopaWideV2")) {
			drawer = new BitmapDrawerZoopaWideV2();
			return drawer;
		}
		if (battStyle.equals("ZoopaWideV3")) {
			drawer = new BitmapDrawerZoopaWideV3();
			return drawer;
		}
		if (battStyle.equals("ZoopaWideV4")) {
			drawer = new BitmapDrawerZoopaWideV4();
			return drawer;
		}
		if (battStyle.equals("ZoopaWideV5")) {
			drawer = new BitmapDrawerZoopaWideV5();
			return drawer;
		}

		if (battStyle.equals("ZoopaCircleV1")) {
			drawer = new BitmapDrawerZoopaCircleV1();
			return drawer;
		}
		if (battStyle.equals("ZoopaCircleV2")) {
			drawer = new BitmapDrawerZoopaCircleV2();
			return drawer;
		}

		if (battStyle.equals("TachoWideV5")) {
			drawer = new BitmapDrawerTachoWideV5();
			return drawer;
		}

		if (battStyle.equals("BarGraphV1")) {
			drawer = new BitmapDrawerBarGraphV1();
			return drawer;
		}
		if (battStyle.equals("BarGraphV2")) {
			drawer = new BitmapDrawerBarGraphV2();
			return drawer;
		}
		if (battStyle.equals("BarGraphVerticalV1")) {
			drawer = new BitmapDrawerBarGraphVerticalV1();
			return drawer;
		}
		if (battStyle.equals("BarGraphVerticalV2")) {
			drawer = new BitmapDrawerBarGraphVerticalV2();
			return drawer;
		}

		if (battStyle.equals("SimpleCircleV1")) {
			drawer = new BitmapDrawerSimpleCircleV1();
			return drawer;
		}
		if (battStyle.equals("SimpleCircleV2")) {
			drawer = new BitmapDrawerSimpleCircleV2();
			return drawer;
		}
		if (battStyle.equals("AokpCircleV1")) {
			drawer = new BitmapDrawerAokpCircleV1();
			return drawer;
		}
		drawer = new BitmapDrawerZoopaWideV3();
		return drawer;
	}

	// #####################################################################################
	// LevelColors
	// #####################################################################################
	public static int getColorForLevel(final int level) {
		if (level > getMidThreshold()) {
			if (isGradientColors()) {
				return getGradientColorForLevel(level);
			} else {
				return getBattColor();
			}
		} else {
			if (level < getLowThreshold()) {
				return getBattColorLow();
			} else {
				if (isGradientColorsMid()) {
					return getGradientColorForLevel(level);
				} else {
					return getBattColorMid();
				}
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

	// #####################################################################################
	// Different Paints
	// #####################################################################################
	public static Paint getErasurePaint() {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		paint.setXfermode(xfermode);
		paint.setStyle(Style.FILL);
		return paint;
	}

	public static Paint getTextPaint(final int level, final int fontSize) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
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

	public static Paint getEraserTextPaint(final int level, final int fontSize) {
		final Paint paint = getErasurePaint();
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

	public static Paint getBatteryPaintSourceIn(final int level) {
		final Paint paint = getBatteryPaint(level);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.SRC_IN);
		paint.setXfermode(xfermode);
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

	// #####################################################################################
	// Custom Background
	// #####################################################################################
	public static boolean isLoadCustomBackground() {
		return prefs.getBoolean("customBackground", true);
	}

	/**
	 * @return Bitmap or null...
	 */
	public static Bitmap getCustomBackground() {
		final String filePath = getCustomBackgroundFilePath();
		if (!filePath.equals("aaa")) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			backgroundImage = BitmapFactory.decodeFile(filePath, options);
		}
		Log.i("Geith", "Custom BG = " + filePath);
		return backgroundImage;
	}

	public static String getCustomBackgroundFilePath() {
		if (prefs == null) {
			return "aaa";
		}
		final String filePath = prefs.getString(BackgroundPreferencesFragment.BACKGROUND_PICKER_KEY, "aaa");
		return filePath;
	}

	private static int getBackgroundColor1() {
		final int col = prefs.getInt("color_plain_bgrnd", R.integer.COLOR_BLACK);
		return col;
	}

	private static int getBackgroundColor2() {
		final int col = prefs.getInt("color2_plain_bgrnd", R.integer.COLOR_WHITE);
		return col;
	}

	private static boolean isGradientBackground() {
		return prefs.getBoolean("gradientBackground", false);
	}

	public static Paint getWallpaperBackgroundPaint(final int width, final int height) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		if (isGradientBackground()) {
			paint.setShader(new LinearGradient(0, 0, 0, height, getBackgroundColor1(), getBackgroundColor2(), Shader.TileMode.MIRROR));
		} else {
			paint.setColor(getBackgroundColor1());
		}
		return paint;
	}
}
