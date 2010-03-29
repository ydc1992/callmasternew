package cn.opda.service;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareService {
    public static SharedPreferences getShare(Context context, String sharename){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                sharename, Context.MODE_WORLD_READABLE
                        + Context.MODE_WORLD_WRITEABLE);
        return sharedPreferences;
    }
    
}
