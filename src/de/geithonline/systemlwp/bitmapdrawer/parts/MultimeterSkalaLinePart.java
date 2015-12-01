package de.geithonline.systemlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;

public class MultimeterSkalaLinePart {

	private final PointF c;
	private final float ra;
	private final float ri;
	private float dicke = 2f;

	private final Paint paint;

	private final float sweep;
	private final float startWinkel;
	private final float[] mainScala;
	private float[] deviderScala = null;
	private float deviderRa;
	private float deviderRi;
	private float deviderDicke = 1.5f;

	public MultimeterSkalaLinePart(final PointF center, final float ra, final float ri, final float startWinkel, final float sweep, final float[] scala) {
		c = center;
		this.ra = ra;
		this.ri = ri;
		mainScala = scala;
		paint = PaintProvider.initScalePaint();
		this.sweep = sweep;
		this.startWinkel = startWinkel;

		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public void draw(final Canvas canvas) {

		if (mainScala == null || mainScala.length < 2) {
			return;
		}

		final int anz = mainScala.length;
		final float min = mainScala[0];
		final float max = mainScala[anz - 1];
		final float scalaDiff = max - min;

		for (final float s : mainScala) {
			final float valueDiff = s - min;
			final float valueSweep = sweep / scalaDiff * valueDiff;
			final float winkel = startWinkel + valueSweep;
			final Path path = new ZeigerShapePath(c, ra, ri, dicke, winkel, ZEIGER_TYP.rect);
			canvas.drawPath(path, paint);
		}
		// ggf devider zeichenen
		if (deviderScala != null && deviderScala.length > 0) {
			for (final float s : deviderScala) {
				final float valueDiff = s - min;
				final float valueSweep = sweep / scalaDiff * valueDiff;
				final float winkel = startWinkel + valueSweep;
				final Path path = new ZeigerShapePath(c, deviderRa, deviderRi, deviderDicke, winkel, ZEIGER_TYP.rect);
				canvas.drawPath(path, paint);
			}
		}

	}

	public MultimeterSkalaLinePart setDividerScala(final float[] deviderScala, final float ra, final float ri) {
		deviderRa = ra;
		deviderRi = ri;
		this.deviderScala = deviderScala;
		return this;
	}

	public MultimeterSkalaLinePart setDividerScala(final float[] deviderScala, final float ra, final float ri, final float dicke) {
		deviderRa = ra;
		deviderRi = ri;
		this.deviderScala = deviderScala;
		deviderDicke = dicke;
		return this;
	}

	public MultimeterSkalaLinePart setDicke(final float dicke) {
		this.dicke = dicke;
		return this;
	}

}
