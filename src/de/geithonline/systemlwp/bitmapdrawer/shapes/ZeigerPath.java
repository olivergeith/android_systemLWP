package de.geithonline.systemlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.utils.PathHelper;

public class ZeigerPath extends Path {
	public ZeigerPath(final PointF center, final float outerRadius, final float innerRadius, final float dicke, final float rotateWinkel) {
		final Path p = new Path();
		final RectF zeiger = new RectF();
		zeiger.left = center.x + innerRadius;
		zeiger.right = center.x + outerRadius;
		zeiger.top = center.y - dicke / 2;
		zeiger.bottom = center.y + dicke / 2;
		p.addRect(zeiger, Direction.CW);
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}
}
