package com.pl.mnogashki;
import android.preference.PreferenceActivity;
import android.os.*;
import android.preference.*;
import android.content.*;

public class PrefsActivity extends PreferenceActivity
{
	public static final String KEY_HORZ = "size_h";
	public static final String KEY_VERT = "size_v";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.OnSharedPreferenceChangeListener listener = 
			new SharedPreferences.OnSharedPreferenceChangeListener() {

            	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                	Preference pref = findPreference(key);
					if (pref instanceof ListPreference){
						ListPreference lp = (ListPreference)pref;
						lp.setSummary(lp.getEntry());						
					}
            	}
        	};
		sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
		
		setPreferenceScreen(createPrefs());
		
		listener.onSharedPreferenceChanged(sharedPrefs, KEY_HORZ);
		listener.onSharedPreferenceChanged(sharedPrefs, KEY_VERT);
	}
	
	private PreferenceScreen createPrefs(){
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		String[] entries = new String[] {"2", "3", "4", "5", "6", "7" };
		
		ListPreference horzPref = new ListPreference(this);
		horzPref.setTitle(this.getResources().getString(R.string.prefs_size_h));
		horzPref.setKey(KEY_HORZ);
		horzPref.setEntries(entries);
		horzPref.setEntryValues(entries);
        if (!sharedPrefs.contains(KEY_HORZ)) {
            horzPref.setValue("4");
        }
		root.addPreference(horzPref);
		
		ListPreference vertPref = new ListPreference(this);
		vertPref.setTitle(this.getResources().getString(R.string.prefs_size_v));
		vertPref.setKey(KEY_VERT);
		vertPref.setEntries(entries);
		vertPref.setEntryValues(entries);
        if (!sharedPrefs.contains(KEY_HORZ)) {
            vertPref.setValue("4");
        }
		root.addPreference(vertPref);
		
		return root;
	}
}
