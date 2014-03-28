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
import android.os.BatteryManager;
import android.util.Log;
import de.geithonline.systemlwp.BackgroundPreferencesFragment;
import de.geithonline.systemlwp.LiveWallpaperService;
import de.geithonline.systemlwp.R;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerAokpCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphVerticalV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphVerticalV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphVerticalV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerNumberOnlyV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoV4;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoV5;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV4;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV5;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV6;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.utils.BitmapHelper;
import de.geithonline.systemlwp.utils.ColorHelper;

public class Settings {
	public static SharedPreferences prefs = LiveWallpaperService.prefs;
	private static String style = "aaa";
	private static IBitmapDrawer bitmapDrawer;
	public static final int ANIMATION_STYLE_0_TO_100 = 1;
	public static final int ANIMATION_STYLE_0_TO_LEVEL = 2;

	public static final int ORIENTATION_BOTTOM = 0;
	public static final int ORIENTATION_LEFT = 90;
	public static final int ORIENTATION_RIGHT = 270;

	public static boolean isCharging = false;
	public static boolean isChargeUSB = false;
	public static boolean isChargeAC = false;
	public static int battTemperature = -1;
	public static int battHealth = -1;
	public static int battVoltage = -1;

	public static boolean isShowStatus() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_status", true);
	}

	public static String getBattStatusCompleteShort() {
		return "Battery: health " + getHealthText(battHealth) + ", " + (float) battTemperature / 10 + "°C, " + (float) (battVoltage / 10) / 100 + "V";
	}

	public static String getBattTemperatureString() {
		return "Temperature is " + (float) battTemperature / 10 + " °C";
	}

	public static String getBattHealthString() {
		return "Health is " + getHealthText(battHealth);
	}

	public static String getBattVoltageString() {
		return "Voltage is " + (float) battVoltage / 1000 + "V";
	}

	private static String getHealthText(final int health) {
		switch (health) {
			case BatteryManager.BATTERY_HEALTH_GOOD:
				return "good";
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				return "overheat";
			case BatteryManager.BATTERY_HEALTH_DEAD:
				return "dead";
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				return "overvoltage";
			case BatteryManager.BATTERY_HEALTH_COLD:
				return "cold";
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
				return "failure";

			default:
				return "unknown";
		}
	}

	public static boolean isShowRand() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_rand", true);
	}

	public static boolean isShowNumber() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_number", true);
	}

	public static boolean isPremium() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("muimerp", false);
	}

	private static int getChargeColor() {
		if (prefs == null) {
			return R.integer.COLOR_GREEN;
		}
		final int col = prefs.getInt("charge_color", R.integer.COLOR_GREEN);
		return col;
	}

	public static boolean isUseChargeColors() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("charge_colors_enable", false);
	}

	public static boolean isShowChargeState() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("charge_enable", true);
	}

	public static String getChargingText() {
		String text;
		if (isChargeUSB) {
			text = "Charging on USB";
		} else if (isChargeAC) {
			text = "Charging on AC";
		} else {
			text = "Charging...";
		}
		return text;
	}

	private static boolean isVerticalPositionOffsetOnlyInPortrait() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("vertical_position_only_portrait", true);
	}

	public static int getVerticalPositionOffset(final boolean inPortrait) {
		final int defVal = 0;
		if (prefs == null) {
			return defVal;
		}
		if (!inPortrait && isVerticalPositionOffsetOnlyInPortrait()) {
			return 0;
		}
		final int thr = Integer.valueOf(prefs.getString("vertical_position", "0"));
		return thr;
	}

	public static boolean isAnimationEnabled() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("animation_enable", true);
	}

	public static int getOrientation() {
		if (prefs == null) {
			return 0;
		}
		final int size = Integer.valueOf(prefs.getString("rotation", "0"));
		return size;
	}

	public static boolean isShowZeiger() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_zeiger", true);
	}

	public static int getAnimationDelaý() {
		if (prefs == null) {
			return 50;
		}
		final int thr = Integer.valueOf(prefs.getString("animation_delay", "50"));
		return thr;
	}

	public static int getAnimationDelaýOnCurrentLevel() {
		if (prefs == null) {
			return 2500;
		}
		final int thr = Integer.valueOf(prefs.getString("animation_delay_level", "2500"));
		return thr;
	}

	public static boolean isDebuggingMessages() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("debug2", false);
	}

	public static boolean isDebugging() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("debug", false);
	}

	public static int getAnimationStyle() {
		if (prefs == null) {
			return 1;
		}
		final int size = Integer.valueOf(prefs.getString("animationStyle", "1"));
		return size;
	}

	public static boolean isCenteredBattery() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("centerBattery", true);
	}

	private static int getFontSize() {
		if (prefs == null) {
			return 100;
		}
		final int size = Integer.valueOf(prefs.getString("fontsize", "100"));
		return size;
	}

	private static int getFontSize100() {
		if (prefs == null) {
			return 100;
		}
		final int size = Integer.valueOf(prefs.getString("fontsize100", "100"));
		return size;
	}

	private static boolean isColoredNumber() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("colored_numbers", false);
	}

	private static boolean isGradientColors() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("gradient_colors", false);
	}

	private static boolean isGradientColorsMid() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("gradient_colors_mid", false);
	}

	private static int getMidThreshold() {
		if (prefs == null) {
			return 30;
		}
		final int thr = Integer.valueOf(prefs.getString("threshold_mid", "30"));
		return thr;
	}

	private static int getLowThreshold() {
		if (prefs == null) {
			return 10;
		}
		final int thr = Integer.valueOf(prefs.getString("threshold_low", "10"));
		return thr;
	}

	private static int getOpacity() {
		if (prefs == null) {
			return 128;
		}
		final int op = Integer.valueOf(prefs.getString("opacity", "128"));
		return op;
	}

	private static int getBackgroundOpacity() {
		if (prefs == null) {
			return 128;
		}
		final int op = Integer.valueOf(prefs.getString("background_opacity", "128"));
		return op;
	}

	private static int getBackgroundColor() {
		if (prefs == null) {
			return R.integer.COLOR_DARKGRAY;
		}
		final int col = prefs.getInt("background_color", R.integer.COLOR_DARKGRAY);
		return col;
	}

	private static int getBattColor() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("battery_color", R.integer.COLOR_WHITE);
		return col;
	}

	private static int getZeigerColor() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("color_zeiger", R.integer.COLOR_WHITE);
		return col;
	}

	private static int getBattColorMid() {
		if (prefs == null) {
			return R.integer.COLOR_ORANGE;
		}
		final int col = prefs.getInt("battery_color_mid", R.integer.COLOR_ORANGE);
		return col;
	}

	private static int getBattColorLow() {
		if (prefs == null) {
			return R.integer.COLOR_RED;
		}
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
		if (battStyle.equals("ZoopaWideV6")) {
			drawer = new BitmapDrawerZoopaWideV6();
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

		if (battStyle.equals("TachoV1")) {
			drawer = new BitmapDrawerTachoV1();
			return drawer;
		}
		if (battStyle.equals("TachoV2")) {
			drawer = new BitmapDrawerTachoV2();
			return drawer;
		}
		if (battStyle.equals("TachoV3")) {
			drawer = new BitmapDrawerTachoV3();
			return drawer;
		}
		if (battStyle.equals("TachoV4")) {
			drawer = new BitmapDrawerTachoV4();
			return drawer;
		}
		if (battStyle.equals("TachoV5")) {
			drawer = new BitmapDrawerTachoV5();
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
		if (battStyle.equals("BarGraphVerticalV3")) {
			drawer = new BitmapDrawerBarGraphVerticalV3();
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

		if (battStyle.equals("NumberOnlyV1")) {
			drawer = new BitmapDrawerNumberOnlyV1();
			return drawer;
		}
		drawer = new BitmapDrawerZoopaWideV3();
		return drawer;
	}

	// #####################################################################################
	// LevelColors
	// #####################################################################################
	public static int getColorForLevel(final int level) {
		if (Settings.isCharging && isUseChargeColors()) {
			return getChargeColor();
		}
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

	public static Paint getNumberPaint(final int level, final int fontSize) {
		return getNumberPaint(level, fontSize, Align.CENTER, true, false);
	}

	public static Paint getNumberPaint(final int level, final int fontSize, final Align align, final boolean bold, final boolean erase) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		if (isColoredNumber()) {
			paint.setColor(getColorForLevel(level));
		} else {
			paint.setColor(getBattColor());
		}
		paint.setAlpha(getOpacity());
		final int fSize = adjustFontSize(level, fontSize);
		paint.setTextSize(fSize);
		paint.setFakeBoldText(true);
		if (bold) {
			paint.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			paint.setTypeface(Typeface.DEFAULT);
		}
		paint.setTextAlign(align);
		if (erase) {
			final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
			paint.setXfermode(xfermode);
		}
		return paint;
	}

	public static Paint getTextPaint(final int level, final int fontSize) {
		return getTextPaint(level, fontSize, Align.LEFT, true, false);
	}

	public static Paint getTextPaint(final int level, final int fontSize, final Align align, final boolean bold, final boolean erase) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		if (isColoredNumber()) {
			paint.setColor(getColorForLevel(level));
		} else {
			paint.setColor(getBattColor());
		}
		paint.setAlpha(getOpacity());
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		paint.setFakeBoldText(true);
		if (bold) {
			paint.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			paint.setTypeface(Typeface.DEFAULT);
		}
		paint.setTextAlign(align);
		if (erase) {
			final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
			paint.setXfermode(xfermode);
		}
		return paint;
	}

	private static int adjustFontSize(final int level, final int fontSize) {
		int fSize = fontSize;
		if (level == 100) {
			fSize = Math.round(fontSize * getFontSize100() / 100f);
		}
		// generelle fontadjust einbeziehen
		fSize = Math.round(fSize * getFontSize() / 100f);
		return fSize;
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
		// because of SRC in the alphas of background and overpaint somhow "add"
		// so the SRC in must be mor opacid then normal
		int alpha = getOpacity() + getBackgroundOpacity();
		if (alpha > 255) {
			alpha = 255;
		}
		paint.setAlpha(alpha);
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
		if (prefs == null) {
			return true;
		}
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
			options.inDither = false; // Disable Dithering mode
			options.inPurgeable = true; // Tell to gc that whether it needs free
										// memory, the Bitmap can be cleared
			options.inInputShareable = true; // Which kind of reference will be
												// used to recover the Bitmap
												// data after being clear, when
												// it will be used in the future
			options.inTempStorage = new byte[32 * 1024];
			final Bitmap b = BitmapFactory.decodeFile(filePath, options);
			BitmapHelper.logBackgroundFileInfo(filePath);
			return b;
		}
		return null;
	}

	public static String getCustomBackgroundFilePath() {
		if (prefs == null) {
			return "aaa";
		}
		final String filePath = prefs.getString(BackgroundPreferencesFragment.BACKGROUND_PICKER_KEY, "aaa");
		return filePath;
	}

	private static int getBackgroundColor1() {
		if (prefs == null) {
			return R.integer.COLOR_BLACK;
		}
		final int col = prefs.getInt("color_plain_bgrnd", R.integer.COLOR_BLACK);
		return col;
	}

	private static int getBackgroundColor2() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("color2_plain_bgrnd", R.integer.COLOR_WHITE);
		return col;
	}

	private static boolean isGradientBackground() {
		if (prefs == null) {
			return false;
		}
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

	/**
	 * Initializes some preferences on first run with defaults
	 * 
	 * @param preferences
	 */
	public static void initPrefs(final SharedPreferences preferences) {
		prefs = preferences;
		if (prefs.getBoolean("firstrun", true)) {
			Log.i("GEITH", "FirstRun --> initializing the SharedPreferences with some colors...");
			prefs.edit().putBoolean("firstrun", false).commit();
			// init colors
			prefs.edit().putInt("charge_color", Color.GREEN).commit();
			prefs.edit().putInt("battery_color", Color.WHITE).commit();
			prefs.edit().putInt("background_color", Color.DKGRAY).commit();
			prefs.edit().putInt("battery_color_mid", Color.YELLOW).commit();
			prefs.edit().putInt("battery_color_low", Color.RED).commit();
			prefs.edit().putInt("color_zeiger", Color.WHITE).commit();
		}
	}

	// ############################################################################
	// POremium
	// ############################################################################
	// /**
	// * Initializes some preferences on first run with defaults
	// *
	// * @param preferences
	// */
	// public static void saveProStatus(final boolean isPre) {
	// if (prefs != null) {
	// prefs.edit().putBoolean("muimerp", isPre).commit();
	// }
	// isPremium = isPre;
	// }
	//
	// public static boolean isPremiumApp() {
	// if (prefs == null) {
	// return isPremium;
	// }
	// return prefs.getBoolean("muimerp", false);
	// }

}
