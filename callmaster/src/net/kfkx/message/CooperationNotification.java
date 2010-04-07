package net.kfkx.message;
import java.util.List;
import net.kfkx.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class CooperationNotification {
	private NotificationManager manager;
	private Context ctx;
	private int notytype;
	
	public static final int SIMPLE_NOTIFICATION_ID = 1;
	public CooperationNotification(Context ctx,NotificationManager manager, int notytype) {
		this.manager = manager;
		this.ctx = ctx;
		this.notytype = notytype;
	}
	
	public void send(){
			Notification notify = new Notification(R.drawable.img1,null,System.currentTimeMillis());
			CharSequence text ="";
			if(notytype == 1){
				text = "�յ���������";
			}else if(notytype == 2){
				text = "�յ�����������";
			}
			
			Intent notifyIntent = new Intent(ctx,Test.class);
			PendingIntent intent = PendingIntent.getActivity(ctx, 0, notifyIntent,
					android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
			notify.setLatestEventInfo(ctx, "�۷ѿ�����ʾ", text, intent);
			manager.notify(SIMPLE_NOTIFICATION_ID,notify);
	}
}