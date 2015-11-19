package de.geithonline.systemlwp.settings;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class PaintProvider {

	private static Paint initBattStatusPaint() {
		final Paint battStatusPaint = new Paint();
		battStatusPaint.setAntiAlias(true);
		battStatusPaint.setAlpha(255);
		battStatusPaint.setFakeBoldText(true);
		battStatusPaint.setStyle(Style.FILL);
		return battStatusPaint;
	}

	private static Paint initScalePaint() {
		final Paint scalePaint = new Paint();
		scalePaint.setAntiAlias(true);
		scalePaint.setAlpha(255);
		scalePaint.setFakeBoldText(true);
		scalePaint.setStyle(Style.FILL);
		return scalePaint;
	}

	private static Paint initBackgroundPaint() {
		final Paint backgroundPaint = new Paint();
		backgroundPaint.setAntiAlias(true);
		backgroundPaint.setStyle(Style.FILL);
		return backgroundPaint;
	}

	private static Paint initZeigerPaint() {
		final Paint zeigerPaint = new Paint();
		zeigerPaint.setAntiAlias(true);
		zeigerPaint.setAlpha(255);
		zeigerPaint.setStyle(Style.FILL);
		return zeigerPaint;
	}

	private static Paint initErasurePaint() {
		final Paint erasurePaint = new Paint();
		erasurePaint.setAntiAlias(true);
		erasurePaint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		erasurePaint.setXfermode(xfermode);
		erasurePaint.setStyle(Style.FILL);
		return erasurePaint;
	}

	private static Paint initNumberPaint() {
		final Paint numberPaint = new Paint();
		numberPaint.setAntiAlias(true);
		numberPaint.setFakeBoldText(true);
		return numberPaint;
	}

	private static Paint initTextPaint() {
		final Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setAntiAlias(true);
		textPaint.setFakeBoldText(true);
		return textPaint;
	}

	private static Paint initBattPaint() {
		final Paint battPaint = new Paint();
		battPaint.setAntiAlias(true);
		battPaint.setStyle(Style.FILL);
		return battPaint;
	}

	// #####################################################################################
	// LevelColors
	// #####################################################################################
	public static int getColorForLevel(final int level) {
		if (Settings.isCharging && Settings.isUseChargeColors()) {
			return Settings.getChargeColor();
		}
		if (level > Settings.getMidThreshold()) {
			if (Settings.isGradientColors()) {
				return getGradientColorForLevel(level);
			} else {
				return Settings.getBattColor();
			}
		} else {
			if (level < Settings.getLowThreshold()) {
				return Settings.getBattColorLow();
			} else {
				if (Settings.isGradientColorsMid()) {
					return getGradientColorForLevel(level);
				} else {
					return Settings.getBattColorMid();
				}
			}
		}
	}

	public static int getGradientColorForLevel(final int level) {

		if (level > Settings.getMidThreshold()) {
			return ColorHelper.getRadiantColor(Settings.getBattColor(), Settings.getBattColorMid(), level, 100, Settings.getMidThreshold());
		} else {
			if (level < Settings.getLowThreshold()) {
				return Settings.getBattColorLow();
			} else {
				return ColorHelper.getRadiantColor(Settings.getBattColorLow(), Settings.getBattColorMid(), level, Settings.getLowThreshold(),
						Settings.getMidThreshold());
			}
		}
	}

	// #####################################################################################
	// Different Paints
	// #####################################################################################
	public static Paint getErasurePaint() {
		return initErasurePaint();
	}

	public static Paint getNumberPaint(final int level, final int fontSize) {
		return getNumberPaint(level, fontSize, Align.CENTER, true, false);
	}

	public static Paint getNumberPaintAlignLeft(final int level, final int fontSize) {
		return getNumberPaint(level, fontSize, Align.LEFT, true, false);
	}

	public static Paint getNumberPaint(final int level, final float fontSize, final Align align, final boolean bold, final boolean erase) {
		final Paint numberPaint = initNumberPaint();
		numberPaint.setAlpha(Settings.getOpacity());
		if (Settings.isColoredNumber()) {
			numberPaint.setColor(getColorForLevel(level));
		} else {
			numberPaint.setColor(Settings.getBattColor());
		}
		final float fSize = adjustFontSize(level, fontSize);
		numberPaint.setTextSize(fSize);
		if (bold) {
			numberPaint.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			numberPaint.setTypeface(Typeface.DEFAULT);
		}
		numberPaint.setTextAlign(align);
		if (erase) {
			final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
			numberPaint.setXfermode(xfermode);
		}
		return numberPaint;
	}

	public static Paint getTextPaint(final int level, final int fontSize) {
		return getTextPaint(level, fontSize, Align.LEFT, true, false);
	}

	public static Paint getTextPaintAlignRight(final int level, final int fontSize) {
		return getTextPaint(level, fontSize, Align.RIGHT, true, false);
	}

	public static Paint getTextPaint(final int level, final int fontSize, final Align align, final boolean bold, final boolean erase) {
		final Paint textPaint = initTextPaint();
		textPaint.setAlpha(Settings.getOpacity());
		if (Settings.isColoredNumber()) {
			textPaint.setColor(getColorForLevel(level));
		} else {
			textPaint.setColor(Settings.getBattColor());
		}
		textPaint.setTextSize(fontSize);
		if (bold) {
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			textPaint.setTypeface(Typeface.DEFAULT);
		}
		textPaint.setTextAlign(align);
		if (erase) {
			final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
			textPaint.setXfermode(xfermode);
		}
		return textPaint;
	}

	private static float adjustFontSize(final int level, final float fontSize) {
		float fSize = fontSize;
		if (level == 100) {
			fSize = Math.round(fontSize * Settings.getFontSize100() / 100f);
		}
		// generelle fontadjust einbeziehen
		fSize = Math.round(fSize * Settings.getFontSize() / 100f);
		return fSize;
	}

	public static Paint getBatteryPaint(final int level) {
		final Paint battPaint = initBattPaint();
		// battPaint.setAlpha(Settings.getOpacity());
		battPaint.setColor(getColorForLevel(level));
		battPaint.setAlpha(Settings.getOpacity());
		return battPaint;
	}

	public static Paint getBatteryPaintSourceIn(final int level) {
		final Paint paint = getBatteryPaint(level);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.SRC_IN);
		// because of SRC in the alphas of background and overpaint somhow "add"
		// so the SRC in must be mor opacid then normal
		int alpha = Settings.getOpacity() + Settings.getBackgroundOpacity();
		if (alpha > 255) {
			alpha = 255;
		}
		paint.setAlpha(alpha);
		paint.setXfermode(xfermode);
		return paint;
	}

	public static Paint getZeigerPaint(final int level) {
		return getZeigerPaint(level, false);
	}

	public static Paint getZeigerPaint(final int level, final boolean dropShadow) {
		final Paint zeigerPaint = initZeigerPaint();
		zeigerPaint.setColor(Settings.getZeigerColor());
		if (dropShadow) {
			zeigerPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		}
		return zeigerPaint;
	}

	public static Paint getBackgroundPaint() {
		final Paint backgrdPaint = initBackgroundPaint();
		backgrdPaint.setColor(Settings.getBackgroundColor());
		backgrdPaint.setAlpha(Settings.getBackgroundOpacity());
		return backgrdPaint;
	}

	public static Paint getTextScalePaint(final float fontSize, final Align align, final boolean bold) {
		return getTextScalePaint((int) fontSize, align, bold);
	}

	public static Paint getTextScalePaint(final int fontSize, final Align align, final boolean bold) {
		final Paint scalePaint = initScalePaint();
		scalePaint.setColor(Settings.getScaleColor());
		scalePaint.setTextSize(fontSize);
		if (bold) {
			scalePaint.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			scalePaint.setTypeface(Typeface.DEFAULT);
		}
		scalePaint.setTextAlign(align);
		if (Settings.isScaleTransparent()) {
			final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
			scalePaint.setXfermode(xfermode);
		}
		return scalePaint;
	}

	public static Paint getTextBattStatusPaint(final int fontSize, final Align align, final boolean bold) {
		final Paint battStatusPaint = initBattStatusPaint();
		battStatusPaint.setColor(Settings.getBattStatusColor());
		battStatusPaint.setTextSize(fontSize);
		if (bold) {
			battStatusPaint.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			battStatusPaint.setTypeface(Typeface.DEFAULT);
		}
		battStatusPaint.setTextAlign(align);
		return battStatusPaint;
	}
}
