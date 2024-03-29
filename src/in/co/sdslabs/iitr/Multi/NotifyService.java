package in.co.sdslabs.iitr.Multi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


/**
 * This service is started when an Alarm has been raised
 * 
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 * 
 * @author paul.blundell
 */
public class NotifyService extends Service {

	/**
	 * Class for clients to access
	 */
	public class ServiceBinder extends Binder {
		NotifyService getService() {
			return NotifyService.this;
		}
	}

	// Unique id to identify the notification.
	private static final int NOTIFICATION = 123;
	// Name of an intent extra we can use to identify if this service was started to create a notification	
	public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
	// The system notification manager
	private NotificationManager mNM;

	@Override
	public void onCreate() {
		Log.i("NotifyService", "onCreate()");
		
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		// If this service was started by out AlarmTask intent then we want to show our notification
		if(intent.getBooleanExtra(INTENT_NOTIFY, false))
			showNotification();
		
		
//		String ns = Context.NOTIFICATION_SERVICE;
//	    NotificationManager nMgr = (NotificationManager) getBaseContext().getSystemService(ns);
//	    nMgr.cancel(22);
		// We don't care if this service is stopped as we have already delivered our notification
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients
	private final IBinder mBinder = new ServiceBinder();

	@Override
    public void onDestroy()
    {
	 Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	 String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager nMgr = (NotificationManager) getBaseContext().getSystemService(ns);
	    nMgr.cancel(22);
        super.onDestroy();
    }
	/**
	 * Creates a notification and shows it in the OS drag-down status bar
	 */
	private void showNotification() {
		// This is the 'title' of the notification
		
		CharSequence title = "Alarm!!";
		// This is the icon to use on the notification
		int icon = R.drawable.icon;
		// This is the scrolling text of the notification
		CharSequence text = "WAKE UP!!!";		
		// What time to show on the notification
		long time = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, text, time);

		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SecondActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, title, text, contentIntent);

		// Clear the notification when it is pressed
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//notification.defaults |= Notification.DEFAULT_SOUND;
		//notification.sound = Uri.parse("android.resource://test.pkr.namespace/" +R.raw.alarm);
       // notification.sound = Uri.parse("file:///sdcard/alarm.mp3");
		//notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
		notification.sound = 
			    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);	
		long[] vibrate = {100, 200, 100, 800,100,100};
        notification.vibrate = vibrate;
        //notification.defaults|= Notification.DEFAULT_SOUND;
		// Send the notification to the system.
		mNM.notify(NOTIFICATION, notification);
		mNM.cancel(22);
		
		// Stop the service when we are finished
		stopSelf();
	}
}