package codeday.december.drop;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class GameEnding extends Activity implements OnTouchListener {

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.endlayout);
		refresh();
	}

	private void refresh() {
		TextView tv = (TextView) findViewById(R.id.highScoreTV);
		tv.setOnTouchListener(this);
		tv.setText("Score:\n" + Game.score);
	}

	public void replay(View view) {
		Intent i = new Intent(this, Game.class);
		startActivity(i);
		Game.filtered = -1;
		Game.levels = new LinkedList<Level>();
		finish();
	}

	public void exit(View view) {
		finish();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		finish();
		return true;
	}
}
