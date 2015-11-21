package de.geithonline.systemlwp.bitmapdrawer.drawingparts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.systemlwp.bitmapdrawer.shapes.LevelArcPath;
import de.geithonline.systemlwp.settings.PaintProvider;

public class LevelEinerZehnerPart {

	public enum EZ_STYLE {
		segmented_onlyactive, segmented_all;
	}

	public enum EZ_MODUS {
		einer, zehner;
	}

	public enum EZ_COLORING {
		AllLevelColors, ColorOf100, Custom;
	}

	private EZ_STYLE style = EZ_STYLE.segmented_all;
	private EZ_MODUS modus = EZ_MODUS.einer;

	private final PointF c;
	private final float ra;
	private final float ri;
	private final Paint paint;
	private final int level;
	private final float maxWinkel;
	private final float startWinkel;
	private float abstandZwischenSegemten = 1.5f;
	private float anzahlSegmente = 10;

	private float strokeWidthSegmente = 1f;

	public LevelEinerZehnerPart(final PointF center, final float radAussen, final float radInnen, final int level, final float startWinkel,
			final float maxWinkel, final EZ_MODUS modus, final EZ_COLORING colorfull) {
		c = center;
		ra = radAussen;
		ri = radInnen;
		this.modus = modus;
		switch (this.modus) {
		default:
		case einer:
			this.level = level % 10;
			anzahlSegmente = 9;
			break;
		case zehner:
			this.level = level / 10;
			anzahlSegmente = 10;
			break;
		}
		this.maxWinkel = maxWinkel;
		this.startWinkel = startWinkel;
		switch (colorfull) {
		default:
		case AllLevelColors:
			paint = PaintProvider.getBatteryPaint(level);
			break;
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

	public LevelEinerZehnerPart setColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public LevelEinerZehnerPart setStyle(final EZ_STYLE style) {
		this.style = style;
		return this;
	}

	public LevelEinerZehnerPart configureSegemte(final float abstand, final float strokWidth) {
		abstandZwischenSegemten = abstand;
		strokeWidthSegmente = strokWidth;
		return this;
	}

	public void draw(final Canvas canvas) {
		switch (style) {
		default:
		case segmented_onlyactive:
			drawSegemtedOnlyAct(canvas);
			break;
		case segmented_all:
			drawSegemtedAll(canvas);
			break;
		}
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
			if (i < level) {
				final float winkel = startWinkel + i * winkelProSegment + abstandZwischenSegemten / 2;
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
			if (level > i) {
				paint.setStyle(Style.FILL_AND_STROKE);
			} else {
				paint.setStyle(Style.STROKE);
			}
			final float winkel = startWinkel + i * winkelProSegment + abstandZwischenSegemten / 2;
			// Log.i("LevelPart", i + ".Winkel=" + winkel);
			final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
			canvas.drawPath(path, paint);
		}
	}

}
