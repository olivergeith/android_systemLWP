package de.geithonline.systemlwp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {
	int	level;

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
			backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);
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

			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				// get the size of canvas
				final int width = canvas.getWidth();
				final int height = canvas.getHeight();
				if (canvas != null) {
					// clear the canvas
					canvas.drawColor(Color.BLACK);
					// draw the background image and stretch it to canvas
					canvas.drawBitmap(backgroundImage, null, new RectF(0, 0, width, height), null);

					final BitmapDrawerBigArc bigArc = new BitmapDrawerBigArc(canvas);
					bigArc.draw(level);

					level += 1;
					if (level > 100)
						level = 0;

				}
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}

			handler.removeCallbacks(drawRunner);
			if (visible) {
				handler.postDelayed(drawRunner, 25); // delay 10 mileseconds
			}

		}
	}
}