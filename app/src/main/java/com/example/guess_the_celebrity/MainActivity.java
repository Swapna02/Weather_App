package com.example.guess_the_celebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    EditText cityName;
    TextView resultView;

    public void findWeather(View view) {



        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cityName.getWindowToken(),0);

        String encodedCityname = null;
        try {
            encodedCityname = URLEncoder.encode( cityName.getText().toString(),"UTF-8");
            Log.i("cityName",encodedCityname);
            DownloadTask task = new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?appid=559a8022af5e19a284dfeb416b573faa&q="+encodedCityname);


        } catch (UnsupportedEncodingException e) {

            //Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
            e.printStackTrace();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = (TextView)findViewById(R.id.ResultTextView);
        cityName = (EditText)findViewById(R.id.cityName);

    }
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url ;
            HttpURLConnection connection =null;

            try {

                url = new URL(urls[0]);
                connection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while (data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                //Log.i("temparary msg ",result);
                return result;


            } catch (MalformedURLException e) {
                //Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
                e.printStackTrace();

            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



                JSONObject jsonObject = null;
                try {
                    String massage = "";

                    if(result == null)
                    {
                        Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
                        Log.i("result", "null valuee");
                    }
                    else {
                    jsonObject = new JSONObject(result);
                    String weatherInfo = jsonObject.getString("weather");

                    Log.i("weather", weatherInfo);

                    JSONArray arr = new JSONArray(weatherInfo);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject JsonPart = arr.getJSONObject(i);

                        String main = "";
                        String description = "";

                        main = JsonPart.getString("main");
                        description = JsonPart.getString("description");

                        Log.i("main", JsonPart.getString("main"));
                        Log.i("description", JsonPart.getString("description"));

                        if (main != "" && description != "") {
                            massage += main + ":" + description + "\r\n";

                        }
                    }
                    }
                    if (massage != "") {
                        resultView.setText(massage);
                    } else {
                        resultView.setText(massage);
                         Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
                    }

            } catch (JSONException e) {
                    e.printStackTrace();
                }
            //Log.i("wesbsite content",result);
        }
    }

}
