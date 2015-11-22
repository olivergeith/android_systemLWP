package de.geithonline.systemlwp.bitmapdrawer.drawingparts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath;
import de.geithonline.systemlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.systemlwp.settings.PaintProvider;

public class ZeigerPart {

	private final PointF c;
	private final float ra;
	private final float ri;
	private final Paint paint;
	private final int levelIntern;
	private final float maxWinkel;
	private final float startWinkel;
	private final float dicke;
	private Outline outline = null;
	private ZEIGER_TYP zeigerType = ZEIGER_TYP.rect;
	private final EZMode modus;
	private int anzahlLevel;
	private final int level;
	// private boolean isEinerZeiger = false;

	public ZeigerPart(final PointF center, final int level, final float radAussen, final float radInnen, final float dicke, final float startWinkel,
			final float maxWinkel, final EZMode modus) {
		this.dicke = dicke;
		this.modus = modus;
		this.level = level;
		this.maxWinkel = maxWinkel;
		this.startWinkel = startWinkel;
		c = center;
		ra = radAussen;
		ri = radInnen;
		switch (this.modus) {
		default:
		case all:
			levelIntern = this.level;
			anzahlLevel = 100;
			break;
		case einer:
			levelIntern = this.level % 10;
			anzahlLevel = 10;
			break;
		case zehner:
			levelIntern = this.level / 10;
			anzahlLevel = 10;
			break;
		}

		paint = PaintProvider.getZeigerPaint();
		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public ZeigerPart setZeigerType(final ZEIGER_TYP zeigerType) {
		this.zeigerType = zeigerType;
		return this;
	}

	public ZeigerPart overrideColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public ZeigerPart setOutline(final Outline outline) {
		this.outline = outline;
		return this;
	}

	public ZeigerPart setDropShadow(final DropShadow dropShadow) {
		paint.setShadowLayer(dropShadow.getRadius(), dropShadow.getOffsetX(), dropShadow.getOffsetY(), dropShadow.getColor());
		return this;
	}

	public void draw(final Canvas canvas) {
		final float winkelProlevel = maxWinkel / anzahlLevel;
		final float winkel = startWinkel + levelIntern * winkelProlevel;
		final Path path = new ZeigerShapePath(c, ra, ri, dicke, winkel, zeigerType);
		canvas.drawPath(path, paint);
		if (outline != null) {
			paint.setShadowLayer(0, 0, 0, Color.BLACK);
			paint.setColor(outline.getColor());
			paint.setAlpha(255);
			paint.setStrokeWidth(outline.getStrokeWidth());
			paint.setStyle(Style.STROKE);
			canvas.drawPath(path, paint);
		}
	}

}
