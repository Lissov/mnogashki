package com.pl.mnogashki;
import android.preference.*;
import android.os.*;

public class PrefsActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.prefs);
	}
}
