package cn.kfkx.mes;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class Set extends PreferenceActivity {
	private CheckBoxPreference startBox;
	private Sms s;

	private IntentFilter t;
	private Test te;

	private Short sh;
	private Intent tn;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.she);

		t = new IntentFilter("android.intent.action.BOOT_COMPLETED");
		SharedPreferences preferences = ShareService.getShare(this, "opda");
		startBox = (CheckBoxPreference) getPreferenceScreen().findPreference(
				"setStart");
		s = new Sms();
		te = new Test();
		tn = new Intent(Set.this, BackStage.class);

		tn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if (preference == startBox) {
			if (startBox.isChecked()) {
				Editor sharedata = getSharedPreferences("data", 0).edit();
				sharedata.putBoolean("item", true);
				sharedata.commit();
				this.startService(tn);
			} else {
				Editor sharedata = getSharedPreferences("data", 0).edit();
				sharedata.remove("item");
				sharedata.putBoolean("item", false);
				sharedata.commit();
				this.stopService(tn);
			}
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

}
