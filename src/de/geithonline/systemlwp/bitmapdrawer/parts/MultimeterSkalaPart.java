package de.geithonline.systemlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;
import de.geithonline.systemlwp.utils.GeometrieHelper;

public class MultimeterSkalaPart {

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
	private FontAttributes attr = null;
	private float fontRadius;
	private float lineRadius = 0f;

	public MultimeterSkalaPart(final PointF center, final float ra, final float ri, final float startWinkel, final float sweep, final float[] scala) {
		c = center;
		this.ra = ra;
		this.ri = ri;
		mainScala = scala;
		paint = PaintProvider.initScalePaint();
		this.sweep = sweep;
		this.startWinkel = startWinkel;
		fontRadius = ra;

		initPaint();
	}

	public static MultimeterSkalaPart getDefaultVoltmeterPart(final PointF center, final float ra, final float ri, final float startWinkel, final float sweep) {
		final float[] scala = new float[] { 3.5f, 4.0f, 4.5f };
		final float[] deviderScala = new float[] { 3.6f, 3.7f, 3.8f, 3.9f, 4.1f, 4.2f, 4.3f, 4.4f };

		final MultimeterSkalaPart voltmeter = new MultimeterSkalaPart(center, ra, ri, startWinkel, sweep, scala)//
				.setDividerScala(deviderScala, ri + (ra - ri) / 2, ri);
		return voltmeter;
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public MultimeterSkalaPart setDividerScala(final float[] deviderScala, final float ra, final float ri) {
		deviderRa = ra;
		deviderRi = ri;
		this.deviderScala = deviderScala;
		return this;
	}

	public MultimeterSkalaPart setFontAttributes(final FontAttributes attr) {
		this.attr = attr;
		attr.setupPaint(paint);
		return this;
	}

	public MultimeterSkalaPart setFontRadius(final float radius) {
		fontRadius = radius;
		return this;
	}

	public MultimeterSkalaPart setDividerScala(final float[] deviderScala, final float ra, final float ri, final float dicke) {
		deviderRa = ra;
		deviderRi = ri;
		this.deviderScala = deviderScala;
		deviderDicke = dicke;
		return this;
	}

	public MultimeterSkalaPart setDicke(final float dicke) {
		this.dicke = dicke;
		return this;
	}

	public MultimeterSkalaPart setLineRadius(final float radius) {
		lineRadius = radius;
		return this;
	}

	public MultimeterSkalaPart setLineRadius() {
		lineRadius = ri;
		return this;
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

			if (attr != null) {
				final Path mArc = new Path();
				final RectF oval = GeometrieHelper.getCircle(c, fontRadius);
				mArc.addArc(oval, winkel - 18, 36);
				canvas.drawTextOnPath("" + s, mArc, 0, 0, paint);
			}
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
		// ggf linie zeichnen
		if (lineRadius > 0) {
			final RectF oval = GeometrieHelper.getCircle(c, lineRadius);
			final Path mArc = new Path();
			mArc.addArc(oval, startWinkel, sweep);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(dicke);
			canvas.drawPath(mArc, paint);
		}

	}

}
