package de.geithonline.systemlwp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {
	int	x, y, level;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	class MyWallpaperEngine extends Engine {

		private final Handler	handler		= new Handler();
		private final Runnable	drawRunner	= new Runnable() {
												@Override
												public void run() {
													draw();
												}
											};
		private boolean			visible		= true;
		public Bitmap			image1, backgroundImage;

		MyWallpaperEngine() {
			// get the fish and background image references
			image1 = BitmapFactory.decodeResource(getResources(), R.drawable.fish);
			backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			x = 0; // initialize x position
			y = 200; // initialize y position
			level = 0;

		}

		@Override
		public void onCreate(final SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		@Override
		public void onVisibilityChanged(final boolean visible) {
			this.visible = visible;
			// if screen wallpaper is visible then draw the image otherwise do
			// not draw
			if (visible) {
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}

		@Override
		public void onSurfaceDestroyed(final SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}

		@Override
		public void onOffsetsChanged(final float xOffset, final float yOffset, final float xStep, final float yStep, final int xPixels, final int yPixels) {
			draw();
		}

		void draw() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				// get the size of canvas
				final int width = c.getWidth();
				final int height = c.getHeight();

				// clear the canvas
				c.drawColor(Color.BLACK);
				if (c != null) {
					// draw the background image and stretch it to canvas
					// c.drawBitmap(backgroundImage, 0, 0, null);
					c.drawBitmap(backgroundImage, null, new RectF(0, 0, width, height), null);
					// draw the fish
					c.drawBitmap(image1, x, y, null);

					final BitmapDrawerCircle drawerCircle = new BitmapDrawerCircle(c);
					c.drawBitmap(drawerCircle.drawBitmap(level), width / 2 - 32, height / 2 - 32, null);

					final BitmapDrawerBigArc bigArc = new BitmapDrawerBigArc(c);
					c.drawBitmap(bigArc.drawBitmap(level), 0, height - width / 2 - 5, null);

					// draw text
					final Paint paint = new Paint();
					paint.setColor(Color.WHITE);
					paint.setStyle(Style.FILL);
					c.drawText("Hello World", x, 300, paint);

					// if x crosses the width means x has reached to right edge
					if (x > width) {
						// assign initial value to start with
						x = 0;
					}
					// change the x position/value by 1 pixel
					x = x + 1;
					level += 1;

					if (level > 100)
						level = 0;

				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			handler.removeCallbacks(drawRunner);
			if (visible) {
				handler.postDelayed(drawRunner, 25); // delay 10 mileseconds
			}

		}
	}
}