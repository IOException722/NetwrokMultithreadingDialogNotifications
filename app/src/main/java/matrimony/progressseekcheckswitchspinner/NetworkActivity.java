package matrimony.progressseekcheckswitchspinner;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NetworkActivity extends AppCompatActivity {

    Receiver receiver;
    ImageView mImageView;
    private static final int PROGRESS = 0x1;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    ProgressDialog progress;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        receiver = new Receiver();

        mImageView = (ImageView)findViewById(R.id.image_view);
        Intent intent = new Intent(this,HTTPService.class);
        startService(intent);
        String str="a";
        Log.v("a",Integer.toString(str.hashCode()));
        str=str+"b";
        Log.v("b", Integer.toString(str.hashCode()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onResume(){
        super.onResume();

        IntentFilter filterReceiver = new IntentFilter(Receiver.RECEIVER_KEY);
        filterReceiver.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filterReceiver);

    }

    @Override
    protected void onStop(){
        super.onStop();
        unregisterReceiver(receiver);
    }

    public class Receiver extends BroadcastReceiver {

        public static final String RECEIVER_KEY = "com.ipw.rahul.RECEIVER_KEY";

        @Override
        public void onReceive(Context context, Intent intent) {

            try{
                Log.v("onrecieve", "in on rceive");
                final ArrayList<String> list = (ArrayList<String>)intent.getSerializableExtra(HTTPService.KEY);
                Log.v("FROM ACT",list.toString());
                new MyAsyncTask().execute(list.get(0));
            }

            catch(ClassCastException exception){
                exception.toString();
            }
        }

    }

    public Bitmap loadBitmap(String url) {

        try {
            URL photolink = new URL(url);
            Bitmap bitmap = BitmapFactory.decodeStream(photolink.openConnection().getInputStream());
            return bitmap;
        }
        catch(MalformedURLException exception){
            exception.printStackTrace();
            return null;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    private class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress= ProgressDialog.show(NetworkActivity.this, "Image from server","Loading .....", true);
           /* progress = ProgressDialog.show(getApplicationContext(), "dialog title",
                    "dialog message", true);*/
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = loadBitmap(params[0]);
            return bitmap;
        }

        protected void onProgressUpdate(Integer... progres) {

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progress.dismiss();
            if(bitmap != null)
                mImageView.setImageBitmap(bitmap);
        }
    }
}
