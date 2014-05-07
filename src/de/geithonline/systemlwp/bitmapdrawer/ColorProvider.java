package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class ColorProvider {

	private final Paint erasurePaint = initErasurePaint();
	private final Paint numberPaint = initNumberPaint();
	private final Paint textPaint = initTextPaint();
	private final Paint battPaint = initBattPaint();
	private final Paint zeigerPaint = initZeigerPaint();
	private final Paint backgrdPaint = initBackgroundPaint();
	private final Paint scalePaint = initScalePaint();
	private final Paint battStatusPaint = initBattStatusPaint();

	private Paint initBattStatusPaint() {
		final Paint battStatusPaint = new Paint();
		battStatusPaint.setAntiAlias(true);
		battStatusPaint.setAlpha(255);
		battStatusPaint.setFakeBoldText(true);
		return battStatusPaint;
	}

	private Paint initScalePaint() {
		final Paint scalePaint = new Paint();
		scalePaint.setAntiAlias(true);
		scalePaint.setAlpha(255);
		scalePaint.setFakeBoldText(true);
		return scalePaint;
	}

	private Paint initBackgroundPaint() {
		final Paint backgroundPaint = new Paint();
		backgroundPaint.setAntiAlias(true);
		backgroundPaint.setStyle(Style.FILL);
		return backgroundPaint;
	}

	private Paint initZeigerPaint() {
		final Paint zeigerPaint = new Paint();
		zeigerPaint.setAntiAlias(true);
		zeigerPaint.setAlpha(255);
		zeigerPaint.setStyle(Style.FILL);
		return zeigerPaint;
	}

	private Paint initErasurePaint() {
		final Paint erasurePaint = new Paint();
		erasurePaint.setAntiAlias(true);
		erasurePaint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		erasurePaint.setXfermode(xfermode);
		erasurePaint.setStyle(Style.FILL);
		return erasurePaint;
	}

	private Paint initNumberPaint() {
		final Paint numberPaint = new Paint();
		numberPaint.setAntiAlias(true);
		numberPaint.setFakeBoldText(true);
		return numberPaint;
	}

	private Paint initTextPaint() {
		final Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setAntiAlias(true);
		textPaint.setFakeBoldText(true);
		return textPaint;
	}

	private Paint initBattPaint() {
		final Paint battPaint = new Paint();
		battPaint.setAntiAlias(true);
		battPaint.setStyle(Style.FILL);
		return battPaint;
	}

	// #####################################################################################
	// LevelColors
	// #####################################################################################
	public int getColorForLevel(final int level) {
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

	public int getGradientColorForLevel(final int level) {

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
	public Paint getErasurePaint() {
		return erasurePaint;
	}

	public Paint getNumberPaint(final int level, final int fontSize) {
		return getNumberPaint(level, fontSize, Align.CENTER, true, false);
	}

	public Paint getNumberPaint(final int level, final int fontSize, final Align align, final boolean bold, final boolean erase) {
		numberPaint.setAlpha(Settings.getOpacity());
		if (Settings.isColoredNumber()) {
			numberPaint.setColor(getColorForLevel(level));
		} else {
			numberPaint.setColor(Settings.getBattColor());
		}
		final int fSize = adjustFontSize(level, fontSize);
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

	public Paint getTextPaint(final int level, final int fontSize) {
		return getTextPaint(level, fontSize, Align.LEFT, true, false);
	}

	public Paint getTextPaint(final int level, final int fontSize, final Align align, final boolean bold, final boolean erase) {
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

	private int adjustFontSize(final int level, final int fontSize) {
		int fSize = fontSize;
		if (level == 100) {
			fSize = Math.round(fontSize * Settings.getFontSize100() / 100f);
		}
		// generelle fontadjust einbeziehen
		fSize = Math.round(fSize * Settings.getFontSize() / 100f);
		return fSize;
	}

	public Paint getBatteryPaint(final int level) {
		battPaint.setAlpha(Settings.getOpacity());
		battPaint.setColor(getColorForLevel(level));
		return battPaint;
	}

	public Paint getBatteryPaintSourceIn(final int level) {
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

	public Paint getZeigerPaint(final int level) {
		return getZeigerPaint(level, false);
	}

	public Paint getZeigerPaint(final int level, final boolean dropShadow) {
		zeigerPaint.setColor(Settings.getZeigerColor());
		if (dropShadow) {
			zeigerPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		}
		return zeigerPaint;
	}

	public Paint getBackgroundPaint() {
		backgrdPaint.setColor(Settings.getBackgroundColor());
		backgrdPaint.setAlpha(Settings.getBackgroundOpacity());
		return backgrdPaint;
	}

	public Paint getTextScalePaint(final int fontSize, final Align align, final boolean bold) {
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

	public Paint getTextBattStatusPaint(final int fontSize, final Align align, final boolean bold) {
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
