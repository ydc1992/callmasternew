package cn.opda.contact;

import cn.opda.R;
import android.os.Bundle;


public class CallHistoryList extends BaseCallHistoryList {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hostory_list);
		setListAdapter("", null);
	}
}
