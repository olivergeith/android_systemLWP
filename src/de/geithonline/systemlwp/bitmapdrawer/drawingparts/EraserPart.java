package de.geithonline.systemlwp.bitmapdrawer.drawingparts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import de.geithonline.systemlwp.settings.PaintProvider;

public class EraserPart {

	private final Path path;
	private final Paint paint;

	public EraserPart(final Path pathToErase) {
		path = pathToErase;
		paint = PaintProvider.getErasurePaint();
	}

	public void draw(final Canvas canvas) {
		canvas.drawPath(path, paint);
	}
}
