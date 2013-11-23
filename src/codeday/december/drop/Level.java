package codeday.december.drop;

import java.util.LinkedList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Level {
	private int y;
	private LinkedList<Integer> tileCombo;
	private Random tileGenerator = new Random();
	private Bitmap[] bitmap;
	private int dx = 0;
	private long lastFrame = -1;
	public int pixelsPerSecX = 50;
	public static final boolean LEFT = false;
	public static final boolean RIGHT = true;
	public boolean right;

	// default constructor
	public Level(Bitmap[] bitmap, int y, int pixelsPerSecX, boolean right) {
		this.y = y;
		this.bitmap = bitmap;
		tileCombo = new LinkedList<Integer>();

		// keep the number at 5 for rarity?
		for (int i = 0; i < (int) ((Game.width / bitmap[0].getWidth()) + 2); i++) {
			tileCombo.add(generateTile());
		}
		this.pixelsPerSecX = pixelsPerSecX;
		this.right = right;
	}

	// tileCombo should be 9 tiles
	public Level(LinkedList<Integer> tileCombo) {
		this.tileCombo = tileCombo;
	}

	// draws the level
	public void draw(Canvas canvas) {
		if (Game.isVisible) {
		
			int width = bitmap[0].getWidth();
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			long elapsed = System.currentTimeMillis() - lastFrame;
			if (lastFrame != -1) {
				int pixelsX = (int) ((elapsed / 1000.0) * pixelsPerSecX);
				dx += right ? pixelsX : -pixelsX;
				for (int i = 0; i < tileCombo.size(); i++) {
					if (Game.filtered != tileCombo.get(i)) {
						canvas.drawBitmap(bitmap[tileCombo.get(i)], null, new RectF(dx + (i * width) - bitmap[0].getWidth(), y, dx + (i * width) + width - bitmap[0].getWidth(), y + bitmap[0].getHeight()), paint);
					}

				}

				if (right) {
					if (dx > bitmap[0].getWidth()) {
						dx = 0;
						tileCombo.removeLast();
						tileCombo.addFirst(generateTile());
					}
				} else {
					if (dx < -bitmap[0].getWidth()) {
						dx = 0;
						tileCombo.removeFirst();
						tileCombo.add(generateTile());
					}
				}
			}
			lastFrame = System.currentTimeMillis();
		} else {
			long timeDiff = System.currentTimeMillis() - Game.invisible;
			if (timeDiff >= Game.timeInvisible) {
				Game.isVisible = true;
			}
		}

	}

	private int generateTile() {
		int newTile = tileGenerator.nextInt(5);
		if (tileGenerator.nextInt(80) == 69) {
			newTile = 5;
		}
		if (tileGenerator.nextInt(100) == 69) {
			newTile = 6;
		}
		return newTile;
	}

	public boolean getSolid(int x) {
		try {
			int numTile = tileCombo.get((int) ((x - dx) / 150) + 1);
			return Game.filtered != numTile;
		} catch (Exception e) {
			return true;
		}
	}

	public boolean getClock(int x) {
		try {
			int numTile = tileCombo.get((int) ((x - dx) / 150) + 1);
			return numTile == Game.CLOCK;
		} catch (Exception e) {
			return true;
		}
	}

	public boolean getBlackHole(int x) {
		try {
			int numTile = tileCombo.get((int) ((x - dx) / 150) + 1);
			return numTile == Game.BLACKHOLE;
		} catch (Exception e) {
			return true;
		}
	}

	public void appendDY(int dy) {
		this.y += dy;
	}

	public int getY() {
		return y;
	}

	public LinkedList<Integer> getTileCombo() {
		return tileCombo;
	}

}
