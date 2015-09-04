package matrimony.progressseekcheckswitchspinner;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    TextView t1,t2,t3;
    Button b1, bl, br;
    MyAsyncTask mTask;
    Handler mHandler,mHandlerLeft, mHandlerRight;
    ThreadService mService;
    ServiceConnection mServiceConnection;
    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        t1 = (TextView)view.findViewById(R.id.tv1);
        t2 = (TextView) view.findViewById(R.id.tv2);
        t3 = (TextView) view.findViewById(R.id.tv3);
        b1 =(Button) view.findViewById(R.id.btn);
        bl = (Button) view.findViewById(R.id.bleft);
        br = (Button) view.findViewById(R.id.bright);
        t2.setVisibility(View.VISIBLE);

        b1.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Intent in = new Intent(getActivity(), NetworkActivity.class);
                                      startActivity(in);
                                  }
                              }
        );

        mHandlerLeft = new Handler();
        mHandlerRight = new Handler();

        Looper looperLeft = mHandlerLeft.getLooper();
        Looper looperRight = mHandlerRight.getLooper();

        Thread threadLeft = looperLeft.getThread();
        Thread threadRight = looperRight.getThread();

        Log.v("THREAD LEFT", threadLeft.getName());
        Log.v("THREAD RIGHT", threadRight.getName());

        bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandlerLeft.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("HANDLER LEFT","HANDLER LEFT");
                    }
                });

            }
        });



        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandlerRight.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("HANDLER RIGHT","HANDLER RIGHT");
                    }
                });
            }
        });

        return view;
    }

    public void  onResume()
    {
        super.onResume();
        mTask = new MyAsyncTask();
        mTask.execute();
        mHandler = new Handler();
        Looper main = mHandler.getLooper();
        Thread mainn = main.getThread();
        Log.v("Main thread name is", mainn.getName());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                t2.setText("In mHandler");
            }
        });

        t2.setText("in main thread");
        String string = t2.getText().toString();
        Log.v("OUTPUTTEXTVIEW", string);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                t3.setText("FROM EXTERNAL THREAD");
                String output = t1.getText().toString();
                Log.v("from EXTERNAL THREAD",output);
            }
        });

        thread.run();
        String name = thread.getName();
        Log.v("external thread name", name);

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.v("Service connected", "Service connected !!");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.v("Service DIsconnected", "On Service disconnneted !!");
            }
        };

        Intent intent = new Intent(getActivity(),ThreadService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, mServiceConnection, getActivity().BIND_AUTO_CREATE);

    }


    private class MyAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
          // t2.setText("FOO");
            return null;
        }
    }
}
