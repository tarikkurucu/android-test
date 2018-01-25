package com.example.tarik.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ArrayList<TextView> myTextViews;
    EditText editText;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                String result = "";
                int data = inputStreamReader.read();
                while (data != -1) {
                    result += (char) data;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Cold not find weather",Toast.LENGTH_LONG);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                myTextViews = new ArrayList<>();
                String message = "";
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    message += jsonPart.getString("main")+": " + jsonPart.getString("description")+"\r\n";
                }
                TextView textView = findViewById(R.id.textView2);
                textView.setText(message);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Cold not find weather",Toast.LENGTH_LONG);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        myTextViews = new ArrayList<>();
    }

    public void getWeather(View view) {
        DownloadTask downloadTask = new DownloadTask();
        if (editText.getText().length() > 0) {
            String encodedCityName = "";
            try {
                encodedCityName = URLEncoder.encode(editText.getText().toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String weatherAPI = "http://samples.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=b6907d289e10d714a6e88b30761fae22";
            try {
                downloadTask.execute(weatherAPI).get();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Cold not find weather",Toast.LENGTH_LONG);
            }
        } else {
            Toast.makeText(this, "Lütfen bir il giriniz", Toast.LENGTH_SHORT).show();
        }

    }

    public void createTextView(String main, String description,int i) {
        /*LinearLayout linearLayout = findViewById(R.id.linearLayout);
        final TextView rowTextView = new TextView(this);
        rowTextView.setText(main+": "+description);
        linearLayout.addView(rowTextView);
        myTextViews.set(i,rowTextView);*/
    }
}
