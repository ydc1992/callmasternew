package cn.opda.callactivity;

import cn.opda.R;
import cn.opda.service.ShareService;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        SharedPreferences preferences = ShareService.getShare(this, "opda");
        int startService = preferences.getInt("startService", 1);
        int beginAuto = preferences.getInt("beginAuto", 1);
        CheckBox startBox = (CheckBox) findViewById(R.id.setStart);
        CheckBox beginBox = (CheckBox) findViewById(R.id.setbeginAuto);
        if(startService==1){
            startBox.setChecked(true); 
        }
        if(beginAuto==1){
            beginBox.setChecked(true); 
        }
        final Editor editor = preferences.edit();
        startBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        if(isChecked==true){
                            editor.remove("startService");
                            editor.putInt("startService", 1);
                            editor.commit();
                        }else{
                            editor.remove("startService");
                            editor.putInt("startService", 0);
                            editor.commit();
                        }
                    }
                });
        beginBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if(isChecked==true){
                    editor.remove("beginAuto");
                    editor.putInt("beginAuto", 1);
                    editor.commit();
                }else{
                    editor.remove("beginAuto");
                    editor.putInt("beginAuto", 0);
                    editor.commit();
                }
            }
        });
       
    }
}
