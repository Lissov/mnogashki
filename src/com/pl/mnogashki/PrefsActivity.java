package com.pl.mnogashki;
import android.preference.PreferenceActivity;
import android.os.*;
import android.preference.*;
import android.content.*;

public class PrefsActivity extends PreferenceActivity
{
	public static final String KEY_HORZ = "size_h";
	public static final String KEY_VERT = "size_v";
	public static final String KEY_SIZE = "size_overall";
	
	public static boolean changed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.OnSharedPreferenceChangeListener listener = 
			new SharedPreferences.OnSharedPreferenceChangeListener() {

            	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
					preferenceChanged(prefs, key);
            	}
        	};
		sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
		
		setPreferenceScreen(createPrefs());
		
		listener.onSharedPreferenceChanged(sharedPrefs, KEY_HORZ);
		listener.onSharedPreferenceChanged(sharedPrefs, KEY_VERT);
		
		changed = true;
	}
	
	private void preferenceChanged(SharedPreferences prefs, String key){
		Preference pref = findPreference(key);
		if (pref instanceof ListPreference){
			ListPreference lp = (ListPreference)pref;
			lp.setSummary(lp.getEntry());						
			
			switch (key)
			{
				case KEY_SIZE:
					String s = lp.getEntry().toString();
					s = s.substring(0, s.length() / 2);

					((ListPreference)findPreference(KEY_HORZ)).setValue(s);
					((ListPreference)findPreference(KEY_HORZ)).setSummary(s);
					((ListPreference)findPreference(KEY_VERT)).setValue(s);
					((ListPreference)findPreference(KEY_VERT)).setSummary(s);
					break;
				case KEY_HORZ:
				case KEY_VERT:
					String h = 	prefs.getString(KEY_HORZ, "4");
					String v = 	prefs.getString(KEY_VERT, "4");
					if (h.equals(v)){
						((ListPreference)findPreference(KEY_SIZE)).setSummary(v + "x" + h);
					} else {
						((ListPreference)findPreference(KEY_SIZE)).setSummary(
							this.getResources().getString(R.string.prefs_size_custom));						
					}
					break;
			}
		}
	}
	
	private PreferenceScreen createPrefs(){
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		String[] entries = new String[] {"2", "3", "4", "5", "6", "7" };
		String[] entries_x = new String[] {"2x2", "3x3", "4x4", "5x5", "6x6", "7x7" };
		
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
		
		ListPreference sizePref = new ListPreference(this);
		sizePref.setTitle(this.getResources().getString(R.string.prefs_size));
		sizePref.setKey(KEY_SIZE);
		sizePref.setEntries(entries_x);
		sizePref.setEntryValues(entries_x);
		root.addPreference(sizePref);
		
		return root;
	}
}
