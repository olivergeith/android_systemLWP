package de.geithonline.systemlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.systemlwp.utils.PathHelper;

public class ZeigerShapePath extends Path {

	public enum ZEIGER_TYP {
		triangle, circle, rect, raute;
	}

	public ZeigerShapePath(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel, final ZEIGER_TYP typ) {

		switch (typ) {
		default:
		case triangle:
			drawTriangle(center, ra, ri, size, rotateWinkel);
			break;
		case circle:
			drawCircle(center, ra, ri, size, rotateWinkel);
			break;
		case rect:
			drawRect(center, ra, ri, size, rotateWinkel);
			break;
		case raute:
			drawRaute(center, ra, ri, size, rotateWinkel);
			break;

		}
	}

	private void drawRaute(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		final float rm = ri + (ra - ri) / 2;
		p.moveTo(center.x + ra, center.y);
		p.lineTo(center.x + rm, center.y + size / 2);
		p.lineTo(center.x + ri, center.y);
		p.lineTo(center.x + rm, center.y - size / 2);
		p.close();
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);

	}

	private void drawRect(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		final RectF zeiger = new RectF();
		zeiger.left = center.x + ri;
		zeiger.right = center.x + ra;
		zeiger.top = center.y - size / 2;
		zeiger.bottom = center.y + size / 2;
		p.addRect(zeiger, Direction.CW);
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}

	private void drawTriangle(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		p.moveTo(center.x + ra, center.y);
		p.lineTo(center.x + ri, center.y + size / 2);
		p.lineTo(center.x + ri, center.y - size / 2);
		p.close();
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}

	private void drawCircle(final PointF center, final float ra, final float ri, final float size, final float rotateWinkel) {
		final Path p = new Path();
		final RectF zeiger = new RectF();
		zeiger.left = center.x + ri;
		zeiger.right = center.x + ra;
		zeiger.top = center.y - size / 2;
		zeiger.bottom = center.y + size / 2;
		p.addOval(zeiger, Direction.CW);
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel);
		addPath(p);
	}

}
