package rmutt.sorawith.suradech.sensorsystemdesign;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class TempActivity extends AppCompatActivity {

    //Explicit
    private TextView thingTextView, countTextView, analogTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        thingTextView = (TextView) findViewById(R.id.textView7);
        countTextView = (TextView) findViewById(R.id.textView8);
        analogTextView = (TextView) findViewById(R.id.textView9);

        myLoop();

    }   // Main Method

    private class SynDweet extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url("https://dweet.io/get/latest/dweet/for/MasterUNG").build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("SensorV1", "e ==> " + e.toString());
                return null;
            }

        }   // doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("SensorV1", "JSON ==> " + s);

            try {

                String[] strResult = s.split("with");

                String strMyResult = strResult[1].substring(2);
                strMyResult = strMyResult.substring(0, (strMyResult.length() - 1));

                Log.d("SensorV4", "Result ==> " + strMyResult);

                JSONArray jsonArray = new JSONArray(strMyResult);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                thingTextView.setText(jsonObject.getString("thing"));
                JSONArray jsonArray1 = new JSONArray("[" +
                        jsonObject.getString("content") +
                        "]");
                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                countTextView.setText(jsonObject1.getString("count"));
                analogTextView.setText(jsonObject1.getString("Analog"));

            } catch (Exception e) {
                Log.d("SensorV3", "e onPost ==> " + e.toString());
            }


        }   // onPost

    }

    private void myLoop() {

        // To Do
        SynDweet synDweet = new SynDweet();
        synDweet.execute();



        //Loop
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myLoop();
            }
        }, 1000);


    }   // myLoop

}   // Main Class
