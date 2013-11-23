package codeday.december.drop;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Controls {

	public static Bitmap bitmap;
	public static int width;
	public static int height;

	@SuppressWarnings("static-access")
	public Controls(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		width = (canvas.getHeight() * bitmap.getWidth()) / bitmap.getHeight();
		height = canvas.getHeight();
		canvas.drawBitmap(bitmap, null, new RectF(canvas.getWidth() - width, 0, canvas.getWidth(), canvas.getHeight()), paint);
	}
}
