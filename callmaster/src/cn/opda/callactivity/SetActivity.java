package cn.opda.callactivity;


import cn.opda.R;
import cn.opda.service.ShareService;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class SetActivity extends PreferenceActivity {
	private static final String TAG = "SetActivity";
	private CheckBoxPreference startBox;
	private CheckBoxPreference beginBox;
	private Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.set);
        SharedPreferences preferences = ShareService.getShare(this, "opda");
        int startService = preferences.getInt("startService", 1);
        int beginAuto = preferences.getInt("beginAuto", 1);
        startBox = (CheckBoxPreference) getPreferenceScreen().findPreference("setStart");
        CheckBoxPreference startBoxPreference = (CheckBoxPreference) startBox;
        beginBox = (CheckBoxPreference) getPreferenceScreen().findPreference("setbeginAuto");
        CheckBoxPreference autoBoxPreference = (CheckBoxPreference) beginBox;
        if(startService==1){
        	startBoxPreference.setChecked(true); 
        }
        if(beginAuto==1){
        	autoBoxPreference.setChecked(true); 
        }
        editor = preferences.edit();
        
       
    }
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if (preference == startBox) {
            //normally called on the toggle click
            if (startBox.isChecked()) {
            	editor.remove("startService");
            	editor.putInt("startService", 1);
            	editor.commit();
            } else {
            	editor.remove("startService");
            	editor.putInt("startService", 0);
            	editor.commit();
            }
        } else if (preference == beginBox) {
            if (beginBox.isChecked()) {
            	editor.remove("beginAuto");
            	editor.putInt("beginAuto", 1);
            	editor.commit();
            } else {
            	editor.remove("beginAuto");
            	editor.putInt("beginAuto", 0);
            	editor.commit();
            }
        }

		return true;
	}
    
    
}
