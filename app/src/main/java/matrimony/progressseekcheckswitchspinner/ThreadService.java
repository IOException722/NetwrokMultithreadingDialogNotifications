package matrimony.progressseekcheckswitchspinner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ThreadService extends Service {

    Timer mTimer;
    Handler mHandler;
    int mCount = 0;
    NotificationManager mNM;

    private final IBinder mBinder = new LocalBinder();

    public ThreadService() {
    }


    public class LocalBinder extends Binder{
        ThreadService getService()
        {
            return ThreadService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.v("On bind", "On bindd!!");
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v("ON START COMMAND","ON START COMMAND");
        Log.v("FLAGS",Integer.toString(flags));
        Log.v("START ID",Integer.toString(startId));
        mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mCount++;
                Log.v("TIMER TASK", Integer.toString(mCount));
            }
        }, 2000, 3000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("ON CREATE", "ON CREATE");
        mHandler = new Handler();

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.
        showNotification();
    }

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        /*CharSequence text = getText("Char squence text");*/
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_template_icon_bg)  // the status icon
                .setTicker("notification")  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Notification")  // the label of the entry
                .setContentText("this is the notification from pending intent")  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        Log.v("show not","show notification");
        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(121, notification);
    }



    @Override
    public boolean onUnbind(Intent intent) {

        Log.v("ON UNBIND","ON UNBIND");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v("ON DESTROY","ON DESTROY");
    }

}
