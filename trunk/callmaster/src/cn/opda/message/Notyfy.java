package cn.opda.message;


import cn.opda.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class Notyfy extends Activity {

	// 声明通知（消息）管理器
	private NotificationManager m_NotificationManager;
	private Intent m_Intent;
	private PendingIntent m_PendingIntent;
	// 声明Notification对象
	private Notification m_Notification;
	private String phone = "";
	private String body = "";

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle be = this.getIntent().getExtras();
		phone = be.getString("no_phone");
		body = be.getString("no_body");
		m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// 点击通知时转移内容
		m_Intent = new Intent(Notyfy.this, Test.class);

		// be.putString("a", phone);
		// be.putString("b",body);
		// m_Intent.putExtras(be);
		// m_Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 主要是设置点击通知时显示内容的类
		m_PendingIntent = PendingIntent
				.getActivity(Notyfy.this, 0, m_Intent, 0);
		// 构造Notification对象
		m_Notification = new Notification();
		m_Notification.icon = R.drawable.img1;
		m_Notification.tickerText = "短信通知内容...........";
		m_Notification.defaults = Notification.DEFAULT_SOUND;
		m_Notification.tickerText = phone + body;
		m_Notification.setLatestEventInfo(Notyfy.this, "新消息", phone,
				m_PendingIntent);
		m_NotificationManager.notify(0, m_Notification);
		this.finish();

	}

}
