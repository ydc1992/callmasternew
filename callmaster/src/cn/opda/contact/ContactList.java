package cn.opda.contact;

import android.os.Bundle;
import cn.opda.R;

public class ContactList extends BaseContactList {
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		setListAdapter("", null);
	}
}
