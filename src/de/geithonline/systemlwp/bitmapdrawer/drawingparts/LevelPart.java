package de.geithonline.systemlwp.bitmapdrawer.drawingparts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.LevelArcPath;
import de.geithonline.systemlwp.settings.PaintProvider;

public class LevelPart {

	public enum LEVEL_STYLE {
		normal, segmented_onlyactive, segmented_all;
	}

	private LEVEL_STYLE style = LEVEL_STYLE.normal;

	private Outline outline;
	private final PointF c;
	private final float ra;
	private final float ri;
	private final Paint paint;
	private final int level;
	private final float maxWinkel;
	private final float startWinkel;
	private int anzahlSegmente = 10;
	float abstandZwischenSegemten = 1.5f;

	private float strokeWidthSegmente = 1f;

	private boolean colorful = false;

	public LevelPart(final PointF center, final float radAussen, final float radInnen, final int level, final float startWinkel, final float maxWinkel) {
		c = center;
		ra = radAussen;
		ri = radInnen;
		this.level = level;
		this.maxWinkel = maxWinkel;
		this.startWinkel = startWinkel;
		paint = PaintProvider.getBatteryPaint(level);
		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public LevelPart setOutline(final Outline outline) {
		this.outline = outline;
		return this;
	}

	public LevelPart setColorful(final boolean colorful) {
		this.colorful = colorful;
		return this;
	}

	public void draw(final Canvas canvas) {
		switch (style) {
		default:
		case normal:
			drawNormal(canvas);
			break;
		case segmented_onlyactive:
			drawSegemtedOnlyAct(canvas);
			break;
		case segmented_all:
			drawSegemtedAll(canvas);
			break;
		}
	}

	private void drawSegemtedOnlyAct(final Canvas canvas) {
		final float abstand = 1.5f; // Abstand zwischen den Segmenten
		final float winkelProProzent = maxWinkel / 100;
		final float sweepProSeg = winkelProProzent * 100 / anzahlSegmente - abstand;

		for (int i = 0; i < 100; i = i + 100 / anzahlSegmente) {
			if (level >= i) {
				if (colorful) {
					paint.setColor(PaintProvider.getColorForLevel(i));
				}
				final float winkel = startWinkel + i * winkelProProzent + abstand / 2;
				final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
				canvas.drawPath(path, paint);
			}
		}
	}

	private void drawSegemtedAll(final Canvas canvas) {
		final float abstand = 1.5f; // Abstand zwischen den Segmenten
		final float winkelProProzent = maxWinkel / 100;
		final float sweepProSeg = winkelProProzent * 100 / anzahlSegmente - abstand;

		for (int i = 0; i < 100; i = i + 100 / anzahlSegmente) {
			paint.setStrokeWidth(strokeWidthSegmente);
			if (level >= i) {
				paint.setStyle(Style.FILL_AND_STROKE);
			} else {
				paint.setStyle(Style.STROKE);
			}
			if (colorful) {
				paint.setColor(PaintProvider.getColorForLevel(i));
			}
			final float winkel = startWinkel + i * winkelProProzent + abstand / 2;
			// Log.i("LevelPart", i + ".Winkel=" + winkel);
			final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
			canvas.drawPath(path, paint);
		}
	}

	private void drawNormal(final Canvas canvas) {
		final float step = maxWinkel / 100;
		final float sweepForLevel = level * step;
		final Path path = new LevelArcPath(c, ra, ri, startWinkel, sweepForLevel);
		canvas.drawPath(path, paint);
		if (outline != null) {
			// aufräumen für Outline
			paint.setShader(null);
			paint.setShadowLayer(0, 0, 0, Color.BLACK);
			// stroke einstellen
			paint.setColor(outline.getColor());
			paint.setStrokeWidth(outline.getStrokeWidth());
			paint.setStyle(Style.STROKE);
			// und nochmal zeichnen
			canvas.drawPath(path, paint);
		}
	}

	public LevelPart setStyle(final LEVEL_STYLE style) {
		this.style = style;
		return this;
	}

	public LevelPart configureSegemte(final int anzahl, final float abstand, final float strokWidth) {
		anzahlSegmente = anzahl;
		abstandZwischenSegemten = abstand;
		strokeWidthSegmente = strokWidth;
		return this;
	}

}
