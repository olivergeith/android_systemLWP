package de.geithonline.systemlwp.bitmapdrawer.drawingparts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.LevelArcPath;
import de.geithonline.systemlwp.settings.PaintProvider;

public class LevelPart {

	private EZStyle style = EZStyle.sweep;
	private EZMode modus = EZMode.Einer;
	private final PointF c;
	private final float ra;
	private final float ri;
	private final Paint paint;
	private int levelIntern;
	private final float maxWinkel;
	private final float startWinkel;
	private float abstandZwischenSegemten = 1.5f;
	private float anzahlSegmente = 10;

	private float strokeWidthSegmente = 1f;
	private final int level;
	private final EZColoring coloring;

	public LevelPart(final PointF center, final float radAussen, final float radInnen, //
			final int level, final float startWinkel, final float maxWinkel, final EZColoring coloring) {
		c = center;
		ra = radAussen;
		ri = radInnen;
		this.level = level;
		this.maxWinkel = maxWinkel;
		this.startWinkel = startWinkel;
		this.coloring = coloring;
		setMode(modus);
		switch (coloring) {
		default:
		case LevelColors:
			paint = PaintProvider.getBatteryPaint(this.level);
			break;
		case Colorfull:
		case Custom:
		case ColorOf100:
			paint = PaintProvider.getBatteryPaint(100);
			break;
		}
		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public LevelPart setColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public LevelPart setMode(final EZMode modus) {
		this.modus = modus;
		switch (modus) {
		default:
		case Einer:
			levelIntern = level;
			anzahlSegmente = 100;
			break;
		case EinerOnly9Segment:
			levelIntern = level % 10;
			anzahlSegmente = 9;
			break;
		case EinerOnly10Segmente:
			levelIntern = level % 10;
			anzahlSegmente = 10;
			break;
		case Fuenfer:
			levelIntern = level / 5;
			anzahlSegmente = 20;
			break;
		case Zehner:
			levelIntern = level / 10;
			anzahlSegmente = 10;
			break;
		}
		return this;
	}

	public LevelPart setStyle(final EZStyle style) {
		this.style = style;
		return this;
	}

	public LevelPart configureSegemte(final float abstand, final float strokWidth) {
		abstandZwischenSegemten = abstand;
		strokeWidthSegmente = strokWidth;
		return this;
	}

	public void draw(final Canvas canvas) {
		switch (style) {
		default:
		case segmented_all:
			drawSegemtedAll(canvas);
			break;
		case segmented_onlyactive:
			drawSegemtedOnlyAct(canvas);
			break;
		case sweep:
			drawSweep(canvas);
			break;

		}
	}

	private void drawSweep(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		final float sweep = winkelProSegment * levelIntern;
		final Path path = new LevelArcPath(c, ra, ri, startWinkel, sweep);
		canvas.drawPath(path, paint);
	}

	private void drawSegemtedOnlyAct(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		float sweepProSeg;
		if (winkelProSegment < 0) {
			sweepProSeg = winkelProSegment + abstandZwischenSegemten;
		} else {
			sweepProSeg = winkelProSegment - abstandZwischenSegemten;
		}

		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			float winkel;
			if (i < levelIntern) {
				if (winkelProSegment < 0) {
					winkel = startWinkel + i * winkelProSegment - abstandZwischenSegemten / 2;
				} else {
					winkel = startWinkel + i * winkelProSegment + abstandZwischenSegemten / 2;
				}
				if (coloring.equals(EZColoring.Colorfull)) {
					final int faktor = (int) (100 / anzahlSegmente);
					paint.setColor(PaintProvider.getColorForLevel(i * faktor));
				}
				final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
				canvas.drawPath(path, paint);
			} else {
				// do nothing
			}
		}

	}

	private void drawSegemtedAll(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		float sweepProSeg;
		if (winkelProSegment < 0) {
			sweepProSeg = winkelProSegment + abstandZwischenSegemten;
		} else {
			sweepProSeg = winkelProSegment - abstandZwischenSegemten;
		}
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			paint.setStrokeWidth(strokeWidthSegmente);
			if (levelIntern > i) {
				paint.setStyle(Style.FILL_AND_STROKE);
			} else {
				paint.setStyle(Style.STROKE);
			}
			if (coloring.equals(EZColoring.Colorfull)) {
				final int faktor = (int) (100 / anzahlSegmente);
				paint.setColor(PaintProvider.getColorForLevel(i * faktor));
			}

			float winkel;
			if (winkelProSegment < 0) {
				winkel = startWinkel + i * winkelProSegment - abstandZwischenSegemten / 2;
			} else {
				winkel = startWinkel + i * winkelProSegment + abstandZwischenSegemten / 2;
			}
			// Log.i("LevelPart", i + ".Winkel=" + winkel);
			final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
			canvas.drawPath(path, paint);
		}
	}

}
