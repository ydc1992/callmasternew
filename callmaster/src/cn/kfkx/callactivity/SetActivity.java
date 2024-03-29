package cn.kfkx.callactivity;


import java.util.Date;

import cn.kfkx.mes.BackStage;
import cn.kfkx.phone.OpdaState;
import cn.kfkx.service.ShareService;

import cn.kfkx.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class SetActivity extends PreferenceActivity {
	private static final String TAG = "SetActivity";
	private CheckBoxPreference startBox;
	private CheckBoxPreference beginBox;
	private CheckBoxPreference netChangeBox;
	private CheckBoxPreference blackBox;
	private CheckBoxPreference areaBox;
	private CheckBoxPreference messageBox;
	private CheckBoxPreference sendUpBox;
	private Editor editor;
	private Intent messageIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.set);
        messageIntent = new Intent(this, BackStage.class);
        messageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SharedPreferences preferences = ShareService.getShare(this, "kfkx");
        int startService = preferences.getInt(OpdaState.STATESERVICE, 1);
        int beginAuto = preferences.getInt(OpdaState.BEGINAUTO, 1);
        int areaService = preferences.getInt(OpdaState.AREASERVICE, 1);
        int net = preferences.getInt(OpdaState.DWONAUTO, 1);
        int blackservice = preferences.getInt(OpdaState.BLACKSERVICE, 1);
        int sendUpSrvice = preferences.getInt(OpdaState.SENDUP, 1);
        int messageService = preferences.getInt(OpdaState.MESSAGESERVICE, 1);
        startBox = (CheckBoxPreference) getPreferenceScreen().findPreference("setStart");
        beginBox = (CheckBoxPreference) getPreferenceScreen().findPreference("setbeginAuto");
        netChangeBox = (CheckBoxPreference) getPreferenceScreen().findPreference("netChangeService");
        blackBox = (CheckBoxPreference) getPreferenceScreen().findPreference("blackService");
        areaBox = (CheckBoxPreference) getPreferenceScreen().findPreference("areaService");
        sendUpBox = (CheckBoxPreference) getPreferenceScreen().findPreference("sendUpService");
        messageBox = (CheckBoxPreference) getPreferenceScreen().findPreference("messageService");
        if(startService==1){
        	startBox.setChecked(true); 
        }
        if(beginAuto==1){
        	beginBox.setChecked(true); 
        }
        if(net==1){
        	netChangeBox.setChecked(true); 
        }
        if(blackservice==1){
        	blackBox.setChecked(true); 
        }
        if(areaService==1){
        	areaBox.setChecked(true); 
        }
        if(sendUpSrvice==1){
        	sendUpBox.setChecked(true);
        }
        if(messageService==1){
        	messageBox.setChecked(true);
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
            	startBox.setChecked(true); 
            	editor.commit();
            	Toast.makeText(this, R.string.summarystartService, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.STATESERVICE);
            	editor.putInt(OpdaState.STATESERVICE, 0);
            	startBox.setChecked(false); 
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
        }else if (preference == netChangeBox) {
            if (netChangeBox.isChecked()) {
            	editor.remove(OpdaState.DWONAUTO);
            	editor.putInt(OpdaState.DWONAUTO, 1);
            	long time = new Date().getTime();
            	editor.remove(OpdaState.DOWNTIME);
            	editor.putLong(OpdaState.DOWNTIME, time);
            	editor.commit();
            	Toast.makeText(this, R.string.beginNetChange, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.DWONAUTO);
            	editor.putInt(OpdaState.DWONAUTO, 0);
            	editor.commit();
            	Toast.makeText(this, R.string.offNetChange, Toast.LENGTH_SHORT).show();
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
        }else if (preference == areaBox) {
            if (areaBox.isChecked()) {
            	editor.remove(OpdaState.AREASERVICE);
            	editor.putInt(OpdaState.AREASERVICE, 1);
            	editor.commit();
            	Toast.makeText(this, R.string.showCallArea, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.AREASERVICE);
            	editor.putInt(OpdaState.AREASERVICE, 0);
            	editor.commit();
            	Toast.makeText(this, R.string.offCallArea, Toast.LENGTH_SHORT).show();
            }
        }else if (preference == sendUpBox) {
            if (sendUpBox.isChecked()) {
				long uptime = new Date().getTime();
				editor.remove(OpdaState.UPTIME);
				editor.putLong(OpdaState.UPTIME, uptime);
            	editor.remove(OpdaState.SENDUP);
            	editor.putInt(OpdaState.SENDUP, 1);
            	editor.commit();
            	Toast.makeText(this, R.string.beginSendUpService, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.SENDUP);
            	editor.putInt(OpdaState.SENDUP, 0);
            	editor.commit();
            	Toast.makeText(this, R.string.offbeginSendUpService, Toast.LENGTH_SHORT).show();
            }
        }else if (preference == messageBox) {
            if (messageBox.isChecked()) {
            	editor.remove(OpdaState.MESSAGESERVICE);
            	editor.putInt(OpdaState.MESSAGESERVICE, 1);
            	editor.commit();
            	this.startService(messageIntent);
            	Toast.makeText(this, R.string.beginMessageService, Toast.LENGTH_SHORT).show();
            } else {
            	editor.remove(OpdaState.MESSAGESERVICE);
            	editor.putInt(OpdaState.MESSAGESERVICE, 0);
            	editor.commit();
            	this.stopService(messageIntent);
            	Toast.makeText(this, R.string.offMessageService, Toast.LENGTH_SHORT).show();
            }
        }


		return true;
	}
    
    
}
