package codeday.december.drop;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class Game extends Activity implements OnTouchListener, SensorEventListener {

	public static int score = 0;
	public static long startTime;
	public static boolean isVisible = true;
	public static final int timeInvisible = 5000;
	GraphicsView graphicsView;
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int YELLOW = 3;
	public static final int PURPLE = 4;
	public static final int CLOCK = 5;
	public static final int BLACKHOLE = 6;
	public static int filtered = -1;
	public static long invisible;

	private float rotation = 0;

	MediaPlayer mpB;

	public static int minSpeed = 200;
	public static int maxSpeed = 300;

	public static int width, height;
	// test
	// private Level level;
	public static LinkedList<Level> levels;
	private Bitmap background, controlsBitmap, selectorBitmap, red, green, blue, yellow, purple, bitmapLeft, bitmapRight, clock, blackhole, spiral;
	private Controls controls;
	private Character character;
	private SensorManager mSensorManager;
	private Sensor mSensor;

	@SuppressLint("UseValueOf")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bitmaps
		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		red = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red), 150, 100, false);
		green = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green), 150, 100, false);
		blue = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blue), 150, 100, false);
		yellow = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.yellow), 150, 100, false);
		purple = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.purple), 150, 100, false);
		clock = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.clock), 150, 100, false);
		blackhole = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blackhole), 150, 100, false);
		bitmapLeft = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.megamanleft), 100, 100, false);
		bitmapRight = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.megamanright), 100, 100, false);
		spiral = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blackholespin), height, height, false);
		controlsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.controls);
		selectorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.selector);
		levels = new LinkedList<Level>();
		for (int i = 0; i < (int) ((Game.height) / (red.getHeight() * 2) + 1); i++) {
			boolean right;
			if (Math.round(Math.random()) == 1)
				right = Level.RIGHT;
			else
				right = Level.LEFT;
			levels.add(new Level(new Bitmap[] { red, green, blue, yellow, purple, clock, blackhole }, (2 * height / 3) + ((2 * red.getHeight()) * i), minSpeed + (int) (Math.random() * (maxSpeed - minSpeed)), right));
		}
		graphicsView = new GraphicsView(this);
		graphicsView.setOnTouchListener(this);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		setContentView(graphicsView);
		controls = new Controls(controlsBitmap);
		character = new Character(bitmapLeft, bitmapRight);
		startTime = System.currentTimeMillis();
		// Background music
		mpB = MediaPlayer.create(this, R.raw.backgroundmusic);
		mpB.setLooping(true);
		mpB.start();
	}

	@Override
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
		graphicsView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		graphicsView.pause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mSensorManager.unregisterListener(this);
		mpB.stop();
		graphicsView.stop();
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			MediaPlayer mp = MediaPlayer.create(this, R.raw.select);
			mp.start();
			int holder = filtered;
			int x = (int) event.getX();
			int y = (int) event.getY();
			double block = Controls.height / 5;
			if (x > width - Controls.width) {
				if (y > 0 && y < block) {
					Log.d("CodeDay", "RED FILTERED");
					filtered = RED;
				}
				if (y > block && y < block * 2) {
					Log.d("CodeDay", "GREEN FILTERED");
					filtered = GREEN;
				}
				if (y > block * 2 && y < block * 3) {
					Log.d("CodeDay", "BLUE FILTERED");
					filtered = BLUE;
				}
				if (y > block * 3 && y < block * 4) {
					Log.d("CodeDay", "YELLOW FILTERED");
					filtered = YELLOW;
				}
				if (y > block * 4 && y < block * 5) {
					Log.d("CodeDay", "PURPLE FILTERED");
					filtered = PURPLE;
				}
			}
			if (filtered == holder) {
				filtered = -1;
			}
		}
		return true;
	}

	public class GraphicsView extends SurfaceView implements Runnable {

		Thread thread;
		SurfaceHolder holder;
		boolean draw = false; // If the game should be drawing

		public GraphicsView(Context context) {
			super(context);
			holder = getHolder();
		}

		public void run() {
			while (draw && System.currentTimeMillis() - startTime <= 60000) {
				if (!holder.getSurface().isValid()) {
					continue;
				}
				Canvas canvas = holder.lockCanvas();
				canvas.save();
				clear(canvas);
				canvas.rotate(rotation, canvas.getWidth() / 2, canvas.getHeight() / 2);
				// Draw methods
				draw(canvas);
				holder.unlockCanvasAndPost(canvas);
			}
			finish();
			Intent i = new Intent(Game.this, GameEnding.class);
			startActivity(i);

		}

		public void clear(Canvas canvas) {
			// Draw background
			Paint paint = new Paint();
			canvas.drawBitmap(background, null, new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
		}

		public void draw(Canvas canvas) {
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// Draw levels & controls
			// test
			// level.draw(canvas);
			for (int i = 0; i < levels.size(); i++) {
				levels.get(i).draw(canvas);
			}
			if (levels.get(0).getY() < -red.getHeight()) {
				levels.remove(0);
				boolean right;
				if (Math.round(Math.random()) == 1)
					right = Level.RIGHT;
				else
					right = Level.LEFT;
				levels.add(new Level(new Bitmap[] { red, green, blue, yellow, purple, clock, blackhole }, (levels.get(levels.size() - 1)).getY() + red.getHeight() * 2, minSpeed + (int) (Math.random() * (maxSpeed - minSpeed)), right));
			}
			if (!isVisible) {
				Matrix m = new Matrix();
				m.postTranslate(width / 2 - (height / 2), 0);
				m.postRotate(rotation);
				canvas.drawBitmap(spiral, m, paint);
			}
			// Draw character
			character.draw(canvas);
			// Draw the selector
			canvas.restore();
			controls.draw(canvas);
			double block = Controls.height / 5;
			canvas.drawBitmap(selectorBitmap, null, new RectF(width - Controls.width, (int) (block * filtered), canvas.getWidth(), (int) (block * (filtered + 1) + 0.5)), paint);
			// Draw text
			// canvas.drawBitmap(topBitmap, null, new RectF(0, 0, width -
			// controls.width, canvas.getHeight() / 7), paint);
			paint.setColor(Color.WHITE);
			paint.setTextSize(40);
			paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			canvas.drawText("Score: " + score, 5, 40, paint);
			paint.setTextAlign(Align.RIGHT);
			canvas.drawText("Time: " + (60 - (int) ((System.currentTimeMillis() - startTime) / 1000.0)), width - Controls.width - 5, 40, paint);
		}

		public void pause() {
			draw = false;
			while (true) {
				try {
					thread.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			thread = null;
		}

		public void resume() {
			draw = true;
			thread = new Thread(this);
			thread.start();
		}

		public void stop() {
			draw = false;
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d("GG", event.values[1] + "");
		double dx = (((6 * (int) event.values[1]) - character.getDX()) / 10.0) + character.getDX();
		character.setDX(dx);
		rotation = -(int)dx;
	}
}