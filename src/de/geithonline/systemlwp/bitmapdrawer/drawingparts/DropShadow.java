package de.geithonline.systemlwp.bitmapdrawer.drawingparts;

public class DropShadow {
	private final float radius;
	private final int color;
	private final float offsetX;
	private final float offsetY;

	public DropShadow(final float radius, final float offsetX, final float offsetY, final int color) {
		this.radius = radius;
		this.color = color;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public DropShadow(final float radius, final int color) {
		this.radius = radius;
		this.color = color;
		offsetX = 0;
		offsetY = 0;
	}

	public float getRadius() {
		return radius;
	}

	public int getColor() {
		return color;
	}

	public float getOffsetX() {
		return offsetX;
	}

	public float getOffsetY() {
		return offsetY;
	}

}
