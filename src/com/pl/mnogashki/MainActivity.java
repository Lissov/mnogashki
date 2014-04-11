package com.pl.mnogashki;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class MainActivity extends Activity
{
	private GameView gameView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		// remove title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//					 WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
		
		LinearLayout llMain = (LinearLayout)findViewById(R.id.llMain);
		gameView = new GameView(this);
		llMain.addView(gameView);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(1, 1, 1, R.string.menu_shuffle);
		menu.add(2, 2, 2, R.string.menu_preferenes);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()){
			case 1:
				gameView.shuffle();
				return true;
			case 2:
				startActivity(new Intent(this, PrefsActivity.class));
				return true;		
		}
		return super.onOptionsItemSelected(item);
	}
}
