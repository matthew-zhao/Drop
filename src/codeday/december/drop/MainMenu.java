package codeday.december.drop;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainMenu extends Activity implements OnTouchListener {
	MediaPlayer mp;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.menu);
		ImageView m = (ImageView) findViewById(R.id.menuView);
		m.setOnTouchListener(this);
		mp = MediaPlayer.create(this, R.raw.mainmenu);
		mp.setLooping(true);
		mp.start();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			finish();
			Intent i = new Intent(this, Instructions.class);
			startActivity(i);
			mp.stop();
		}
		return true;
	}
}
