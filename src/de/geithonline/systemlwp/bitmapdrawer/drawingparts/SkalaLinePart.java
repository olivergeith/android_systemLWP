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
	private final float ri;
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
		ri = radInnen;
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

	public SkalaLinePart set1erRadius(final float r) {
		ra1er = r;
		return this;
	}

	public SkalaLinePart set5erRadius(final float r) {
		ra5er = r;
		return this;
	}

	public SkalaLinePart setDicke(final float dicke) {
		this.dicke = dicke;
		return this;
	}

	public void draw(final Canvas canvas) {
		float ra = 0f;
		int forloopEnd = 100;
		if (draw100) {
			forloopEnd = 101;
		}
		for (int i = 0; i < forloopEnd; i = i + 1) {
			if (i % 10 == 0) {// 10 er
				ra = ra10er;
			} else if (i % 5 == 0) { // 5er
				ra = ra5er;
			} else { // einer
				ra = ra1er;
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
