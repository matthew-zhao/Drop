package codeday.december.drop;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class Character {
	private Bitmap bitmapLeft, bitmapRight;
	private static int X;
	private static int Y;
	private double dx = 0;
	public static final int gravity = 800; // pixels
	private long lastFrame = -1;
	private int howFar = 0;

	public Character(Bitmap bitmapLeft, Bitmap bitmapRight) {
		//
		X = ((Game.width - Controls.bitmap.getWidth()) / 2);
		Y = ((Game.height) / 2) - (bitmapLeft.getHeight() / 2);
		this.bitmapLeft = bitmapLeft;
		this.bitmapRight = bitmapRight;
	}

	public void setDX(double dx) {
		this.dx = dx;
		if (Game.isVisible)
			dx = 0;
	}
	
	public double getDX() {
		return dx;
	}

	public void draw(Canvas canvas) {
		long elapsed = System.currentTimeMillis() - lastFrame;
		if (dx < 0) {
			if (lastFrame != -1) {
				int dy = (int) ((elapsed / 1000.0) * gravity);
				if (Game.isVisible) {
					for (int inx = Y + bitmapLeft.getHeight() - 1; inx < Y + bitmapLeft.getHeight() + dy; inx++) {
						for (int jnx = 0; jnx < Game.levels.size(); jnx++) {

							if (Game.levels.get(jnx).getY() == inx && Game.levels.get(jnx).getSolid(X) || Game.levels.get(jnx).getY() == inx && Game.levels.get(jnx).getSolid(X + bitmapLeft.getWidth())) {

								if (Game.levels.get(jnx).getClock(X + (bitmapLeft.getWidth() / 2))) {
									// then you know it's a clock, add 5 seconds
									Game.startTime += 5000;
									break;
								} else if (Game.levels.get(jnx).getBlackHole(X + (bitmapLeft.getWidth() / 2))) {
									Game.isVisible = false;
									Game.invisible = System.currentTimeMillis();
									break;
								}
								dy = inx - Y - bitmapLeft.getHeight() + 1;
								break;
							}
						}
					}
				}
				howFar += dy;
				// keep moving down
				for (int jnx = 0; jnx < Game.levels.size(); jnx++) {
					Game.levels.get(jnx).appendDY(-dy);
				}
				Game.score = howFar * 10;
				Log.d("ggg", Game.score + "");
			}

			X += dx;
			if (X - bitmapLeft.getWidth() > Game.width - Controls.bitmap.getWidth()) {
				X = -bitmapLeft.getWidth();
			}
			if (X < -bitmapLeft.getWidth()) {
				X = Game.width - Controls.bitmap.getWidth();
			}
			canvas.drawBitmap(bitmapLeft, null, new RectF(X, Y, X + bitmapLeft.getWidth(), Y + bitmapLeft.getHeight()), new Paint());
		} else {
			if (lastFrame != -1) {
				int dy = (int) ((elapsed / 1000.0) * gravity);
				if (Game.isVisible) {
					for (int inx = Y + bitmapRight.getHeight() - 1; inx < Y + bitmapRight.getHeight() + dy; inx++) {
						for (int jnx = 0; jnx < Game.levels.size(); jnx++) {

							if (Game.levels.get(jnx).getY() == inx && Game.levels.get(jnx).getSolid(X) || Game.levels.get(jnx).getY() == inx && Game.levels.get(jnx).getSolid(X + bitmapRight.getWidth())) {

								if (Game.levels.get(jnx).getClock(X + (bitmapRight.getWidth() / 2))) {
									// then you know it's a clock, add 5 seconds
									Game.startTime += 5000;
									break;
								} else if (Game.levels.get(jnx).getBlackHole(X + (bitmapRight.getWidth() / 2))) {
									Game.isVisible = false;
									Game.invisible = System.currentTimeMillis();
									break;
								}
								dy = inx - Y - bitmapRight.getHeight() + 1;
								break;
							}
						}
					}
				}
				howFar += dy;
				// keep moving down
				for (int jnx = 0; jnx < Game.levels.size(); jnx++) {
					Game.levels.get(jnx).appendDY(-dy);
				}
				Game.score = howFar * 10;
				Log.d("ggg", Game.score + "");
			}

			X += dx;
			if (X - bitmapRight.getWidth() > Game.width - Controls.bitmap.getWidth()) {
				X = -bitmapRight.getWidth();
			}
			if (X < -bitmapRight.getWidth()) {
				X = Game.width - Controls.bitmap.getWidth();
			}
			canvas.drawBitmap(bitmapRight, null, new RectF(X, Y, X + bitmapRight.getWidth(), Y + bitmapRight.getHeight()), new Paint());
		}
		lastFrame = System.currentTimeMillis();
	}
}
