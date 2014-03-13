package de.geithonline.systemlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import de.geithonline.systemlwp.settings.Settings;
import de.geithonline.systemlwp.utils.ColorHelper;

public class BitmapDrawerTachoWideV5 implements IBitmapDrawer {

	private int cHeight;
	private int cWidth;
	private int offset = 5;
	private int bogenDicke = 5;
	private int skaleDicke = 100;
	private int fontSize = 150;
	private Bitmap bitmap;
	private Canvas bitmapCanvas;
	private int level = -99;

	public BitmapDrawerTachoWideV5() {
	}

	@Override
	public void draw(final int level, final Canvas canvas) {

		// Bitmap neu berechnen wenn Level sich �ndert oder Canvas dimensions
		// anders
		if (this.level != level || canvas.getWidth() != cWidth || canvas.getHeight() != cHeight) {
			cWidth = canvas.getWidth();
			cHeight = canvas.getHeight();
			bitmap = Bitmap.createBitmap(cWidth, cWidth / 2, Bitmap.Config.ARGB_8888);
			bitmapCanvas = new Canvas(bitmap);

			bogenDicke = Math.round(cWidth * 0.01f);
			skaleDicke = Math.round(cWidth * 0.12f);
			offset = Math.round(cWidth * 0.011f);
			fontSize = Math.round(cWidth * 0.30f);

			drawBogen(level);
		}

		canvas.drawBitmap(bitmap, 0, cHeight - cWidth / 2 - 5, null);
		this.level = level;
	}

	private void drawBogen(final int level) {
		final Paint bgPaint = Settings.getBackgroundPaint();
		bgPaint.setColor(ColorHelper.brighter(bgPaint.getColor()));
		// �u�eren Rand
		bitmapCanvas.drawArc(getRectForOffset(offset), 180, 180, true, bgPaint);
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 0, 360, true, Settings.getErasurePaint());

		// scala
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 180, 180, true, Settings.getBackgroundPaint());
		// level
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke), 180, Math.round(level * 1.8), true, Settings.getBatteryPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 0, 360, true, Settings.getErasurePaint());

		// innerer Rand
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke), 180, 180, true, bgPaint);
		// Zeiger
		bitmapCanvas.drawArc(getRectForOffset(offset), 180 + Math.round(level * 1.8) - 1, 2, true, Settings.getZeigerPaint(level));
		// delete inner Circle
		bitmapCanvas.drawArc(getRectForOffset(offset + bogenDicke + skaleDicke + bogenDicke), 0, 360, true, Settings.getErasurePaint());

		// draw percentage Number
		bitmapCanvas.drawText("" + level, cWidth / 2, cWidth / 2 - 10, Settings.getTextPaint(level, fontSize));

	}

	private RectF getRectForOffset(final int offset) {
		return new RectF(offset, offset, cWidth - offset, cWidth - offset);
	}

}