package matrimony.progressseekcheckswitchspinner;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

/*import org.apache.http.NameValuePair;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */

public class HTTPService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.networking.action.FOO";
    private static final String ACTION_BAZ = "com.networking.action.BAZ";
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.networking.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.networking.extra.PARAM2";

    public static final String KEY = "KEY";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, HTTPService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, HTTPService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public HTTPService() {
        super("HTTPService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }

        HttpURLConnection connection = null;

        try{

            String result = null;

            URL url = new URL("http://api.tumblr.com/v2/blog/derekg.org/posts?id=7431599279&api_key=dDSuWRULaql3il36Wau3cKXRuwo0fauWH25MBGSijzbaBP3CuL");
            Log.v("urlss", url.toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            Log.v("after waiting 10 min", "10000");

			/*List<NameValuePair> params= new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("",""));
			params.add(new BasicNameValuePair("",""));

			OutputStream outputStream = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			writer.write(params.toString());
			writer.flush();
			writer.close();
  			outputStream.close();*/
            connection.connect();

            if(connection.getResponseCode() == 200){

                InputStream inputStream = (InputStream)connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String metadata = "";

                StringBuilder stringBuilder = new StringBuilder();

                while((metadata= reader.readLine()) != null){

                    stringBuilder.append(metadata);
                }

                reader.close();
                inputStream.close();

                result = stringBuilder.toString();

                Log.d("Result", result);

                ArrayList<String> resultList = jsonParser(result);

                Intent broadcastIntent = new Intent();
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.setAction(matrimony.progressseekcheckswitchspinner.NetworkActivity.Receiver.RECEIVER_KEY);
                broadcastIntent.putStringArrayListExtra(KEY,resultList/*jsonReader(result)*/);
                sendBroadcast(broadcastIntent);
            }
        }
        catch(MalformedURLException exception){

            exception.printStackTrace();
        }
        catch(IOException exception){

            exception.printStackTrace();
        }
        catch(JSONException exception){

            exception.printStackTrace();
        }
        finally{

            connection.disconnect();

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(NameValuePair pair : params){

            if(first == true){

                first = false;

            }
            else{

                result.append("&");
            }

            result.append(URLEncoder.encode(pair.getName(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));

        }

        return result.toString();
    }*/


    private ArrayList<HashMap<String,String>> jsonReader(String result) throws JSONException{

        ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();
        JSONArray jsonArray = new JSONArray(result);

        int length = jsonArray.length();
        for(int index = 0; index <length; index++){

            JSONObject jsonObject = jsonArray.getJSONObject(index);

            HashMap<String,String> map = new HashMap<String,String>();
            map.put(jsonObject.getString("id"),jsonObject.getString("filename"));

            mList.add(map);
        }

        return mList;
    }

    private ArrayList<String> jsonParser(String result) throws JSONException{

        ArrayList<String> urlList = new ArrayList<String>();

        JSONObject jsonObject = new JSONObject(result);
        JSONObject responseObject = jsonObject.getJSONObject("response");
        JSONArray postsArray = responseObject.getJSONArray("posts");
        JSONObject innerObject = postsArray.getJSONObject(0);
        JSONArray photosArray = innerObject.getJSONArray("photos");
        Log.v("PHOTOS",photosArray.toString());

        JSONObject finalObject = photosArray.getJSONObject(0);

        JSONArray altSizesArray = finalObject.getJSONArray("alt_sizes");

        int length = altSizesArray.length();

        for(int i=0; i<length; i++){

            JSONObject objectJSON = altSizesArray.getJSONObject(i);
            String url = objectJSON.getString("url");
            urlList.add(url);
        }

        Log.v("URLList",urlList.toString());
        return urlList;
    }
}
