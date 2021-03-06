package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerTachoV2 extends BitmapDrawer {

	private int rand = 5;
	private final int offset = 5;
	private int rahmenDicke = 5;
	private int fontSize = 150;
	private int fontSizeArc = 20;
	private int zeigerBreite = 4;
	protected Canvas bitmapCanvas;
	private int fontSizeScala = 20;

	public BitmapDrawerTachoV2() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level) {
		// Batterie ist immer gleich gro�....
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		if (cWidth < cHeight) {
			// hochkant
			setBitmapSize(cWidth, cWidth / 5, true);
		} else {
			// quer
			setBitmapSize(cHeight, cHeight / 5, false);
		}

		final Bitmap bitmap = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);
		zeigerBreite = Math.round(bWidth * 0.01f);
		rahmenDicke = Math.round(bWidth * 0.01f);
		fontSize = Math.round(bWidth * 0.12f);
		fontSizeArc = Math.round(bWidth * 0.04f);
		fontSizeScala = Math.round(bWidth * 0.035f);
		rand = Math.round(bWidth * 0.03f);

		drawSkala(level);
		return bitmap;
	}

	private void drawSkala(final int level) {
		final int skalaBreite = bWidth - 2 * offset - 2 * rahmenDicke - 2 * rand;
		final Paint rahmenPaint = getBackgroundPaint();
		final int w = offset + rahmenDicke + rand + Math.round(skalaBreite * level / 100);
		rahmenPaint.setColor(Color.WHITE);
		rahmenPaint.setShadowLayer(10, 0, 0, Color.BLACK);
		rahmenPaint.setStyle(Style.STROKE);
		rahmenPaint.setStrokeWidth(rahmenDicke);

		final Paint skalaPaint = getBackgroundPaint();
		skalaPaint.setColor(Color.WHITE);
		// skalaPaint.setShadowLayer(10, 0, 0, Color.BLACK);

		final Paint zeigerPaint = getZeigerPaint(level);
		zeigerPaint.setShadowLayer(10, 0, 0, Color.BLACK);

		final Paint bgPaint = getBackgroundPaint();
		bgPaint.setShader(new LinearGradient(0, 0, bWidth / 2, 0, ColorHelper.darker2times(bgPaint.getColor()), bgPaint.getColor(), Shader.TileMode.MIRROR));

		// Fl�che
		bitmapCanvas.drawRect(getRectForOffset(offset), bgPaint);

		// Skala
		final Paint p = getTextPaint(100, fontSizeScala, Align.CENTER, false, false);
		final RectF zs = getRectForOffset(offset);
		for (int i = 0; i <= 100; i = i + 10) {
			// zahlen
			final int x = offset + rahmenDicke + rand + Math.round(skalaBreite * i / 100);
			bitmapCanvas.drawText("" + i, x, offset + rahmenDicke + fontSizeScala, p);
			// striche
			if (i == 0 || i == 100) {
				zs.top = offset + rahmenDicke + fontSizeScala + offset;
			} else {
				zs.top = bHeight - offset - rahmenDicke - offset;
			}
			zs.left = x - zeigerBreite / 3;
			zs.right = x + zeigerBreite / 3;
			bitmapCanvas.drawRect(zs, skalaPaint);
		}

		// Zeiger
		final RectF zb = getRectForOffset(offset);
		zb.left = w - zeigerBreite / 2;
		zb.right = w + zeigerBreite / 2;
		bitmapCanvas.drawRect(zb, zeigerPaint);

		// Rahmen
		bitmapCanvas.drawRect(getRectForOffset(offset), rahmenPaint);

	}

	@Override
	public void drawLevelNumber(final int level) {
		// draw percentage Number
		final Paint p = getNumberPaint(level, fontSize);
		bitmapCanvas.drawText("" + level, bWidth / 2, bHeight - offset - rahmenDicke, p);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final Paint p = getTextPaint(level, fontSizeArc);
		int fader = 10 - level % 10;
		if (fader == 0) {
			fader = 1;
		}
		fader = p.getAlpha() * fader / 10;
		final String text = Settings.getChargingText();
		p.setAlpha(fader);
		bitmapCanvas.drawText(text, offset + rahmenDicke + rand + offset, bHeight - offset - rahmenDicke, p);
	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, bWidth - offset, bHeight - offset);
	}

	@Override
	public void drawBattStatusText() {
		// TODO Auto-generated method stub

	}

}
