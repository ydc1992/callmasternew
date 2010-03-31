package cn.opda.callactivity;


import cn.opda.R;
import cn.opda.message.BackStage;
import cn.opda.phone.OpdaState;
import cn.opda.service.ShareService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class SetActivity extends PreferenceActivity {
	private static final String TAG = "SetActivity";
	private CheckBoxPreference startBox;
	private CheckBoxPreference beginBox;
	private CheckBoxPreference messgeBox;
	private CheckBoxPreference blackBox;
	private Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.set);
        SharedPreferences preferences = ShareService.getShare(this, "opda");
        int startService = preferences.getInt(OpdaState.STATESERVICE, 1);
        int beginAuto = preferences.getInt(OpdaState.BEGINAUTO, 1);
        int message = preferences.getInt(OpdaState.MESSAGESERVICE, 1);
        int blackservice = preferences.getInt(OpdaState.BLACKSERVICE, 1);
        startBox = (CheckBoxPreference) getPreferenceScreen().findPreference("setStart");
        beginBox = (CheckBoxPreference) getPreferenceScreen().findPreference("setbeginAuto");
        messgeBox = (CheckBoxPreference) getPreferenceScreen().findPreference("messageService");
        blackBox = (CheckBoxPreference) getPreferenceScreen().findPreference("blackService");
        if(startService==1){
        	startBox.setChecked(true); 
        }
        if(beginAuto==1){
        	beginBox.setChecked(true); 
        }
        if(message==1){
        	messgeBox.setChecked(true); 
        }
        if(blackservice==1){
        	blackBox.setChecked(true); 
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
            	editor.remove(OpdaState.STATESERVICE);
            	editor.putInt(OpdaState.STATESERVICE, 1);
            	editor.commit();
            	Toast.makeText(this, R.string.summarystartService, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.STATESERVICE);
            	editor.putInt(OpdaState.STATESERVICE, 0);
            	editor.commit();
            	Toast.makeText(this, R.string.endService, Toast.LENGTH_SHORT).show();
            }
        } else if (preference == beginBox) {
            if (beginBox.isChecked()) {
            	editor.remove(OpdaState.BEGINAUTO);
            	editor.putInt(OpdaState.BEGINAUTO, 1);
            	editor.commit();
            	Toast.makeText(this, R.string.summarybeginAuto, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.BEGINAUTO);
            	editor.putInt(OpdaState.BEGINAUTO, 0);
            	editor.commit();
            	Toast.makeText(this, R.string.notBeginAuto, Toast.LENGTH_SHORT).show();
            }
        }else if (preference == messgeBox) {
            if (messgeBox.isChecked()) {
            	editor.remove(OpdaState.MESSAGESERVICE);
            	editor.putInt(OpdaState.MESSAGESERVICE, 1);
            	editor.commit();
            	Intent tn = new Intent(this, BackStage.class);
	            tn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				this.startService(tn);
            	Toast.makeText(this, R.string.beginMessageService, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.MESSAGESERVICE);
            	editor.putInt(OpdaState.MESSAGESERVICE, 0);
            	editor.commit();
            	Intent tn = new Intent(this, BackStage.class);
	            tn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				this.startService(tn);
            	Toast.makeText(this, R.string.offMessageService, Toast.LENGTH_SHORT).show();
            }
        }else if (preference == blackBox) {
            if (blackBox.isChecked()) {
            	editor.remove(OpdaState.BLACKSERVICE);
            	editor.putInt(OpdaState.BLACKSERVICE, 1);
            	editor.commit();
            	Toast.makeText(this, R.string.beginBlackService, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.BLACKSERVICE);
            	editor.putInt(OpdaState.BLACKSERVICE, 0);
            	editor.commit();
            	Toast.makeText(this, R.string.offBlackService, Toast.LENGTH_SHORT).show();
            }
        }

		return true;
	}
    
    
}
