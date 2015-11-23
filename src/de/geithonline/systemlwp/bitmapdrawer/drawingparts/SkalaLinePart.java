package de.geithonline.systemlwp.bitmapdrawer.drawingparts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;

public class SkalaLinePart {

	private final PointF c;
	private final float ra10er;
	private float ra5er = 0f;
	private float ra1er = 0f;
	private final float ri10er;
	private float ri5er;

	private float ri1er;
	private final Paint paint;
	private float dicke = 2f;
	private boolean draw100 = false;

	private final float maxWinkel;
	private final float startWinkel;

	public SkalaLinePart(final PointF center, final float rad10er, final float radInnen, final float startWinkel, final float maxWinkel) {
		c = center;
		ra1er = 0f;
		ra5er = 0f;
		ra10er = rad10er;
		ri10er = radInnen;
		ri1er = radInnen;
		ri5er = radInnen;
		paint = PaintProvider.initScalePaint();
		this.maxWinkel = maxWinkel;
		this.startWinkel = startWinkel;

		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public SkalaLinePart setDraw100(final boolean draw100) {
		this.draw100 = draw100;
		return this;
	}

	/**
	 * @param ra1er
	 *            Auﬂenradius 1er Striche <br>
	 *            if set to 0f no 1eer Striche will be painted
	 * @return
	 */
	public SkalaLinePart set1erRadiusAussen(final float ra1er) {
		this.ra1er = ra1er;
		return this;
	}

	/**
	 * @param ra5er
	 *            Auﬂenradius 5er Striche <br>
	 *            if set to 0f...no 5er striche will be painted
	 * @return
	 */
	public SkalaLinePart set5erRadiusAussen(final float ra5er) {
		this.ra5er = ra5er;
		return this;
	}

	public SkalaLinePart set5erRadiusInnen(final float ri5er) {
		this.ri5er = ri5er;
		return this;
	}

	public SkalaLinePart set1erRadiusInnen(final float ri1er) {
		this.ri1er = ri1er;
		return this;
	}

	public SkalaLinePart setDicke(final float dicke) {
		this.dicke = dicke;
		return this;
	}

	public void draw(final Canvas canvas) {
		float ra = 0f;
		float ri = 0f;
		int forloopEnd = 100;
		if (draw100) {
			forloopEnd = 101;
		}
		for (int i = 0; i < forloopEnd; i = i + 1) {
			if (i % 10 == 0) {// 10 er
				ra = ra10er;
				ri = ri10er;
			} else if (i % 5 == 0) { // 5er
				ra = ra5er;
				ri = ri5er;
			} else { // einer
				ra = ra1er;
				ri = ri1er;
			}
			if (ra > 0f) {
				final float winkelProProzent = maxWinkel / 100;
				final float winkel = startWinkel + i * winkelProProzent;
				final Path path = new ZeigerShapePath(c, ra, ri, dicke, winkel, ZEIGER_TYP.rect);
				canvas.drawPath(path, paint);
			}
		}
	}

}
