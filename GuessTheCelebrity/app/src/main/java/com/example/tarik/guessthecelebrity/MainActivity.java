package com.example.tarik.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> imgArr = new ArrayList<>();
    ArrayList<String> nameArr = new ArrayList<>();
    ArrayList<String> options = new ArrayList<>();
    int answerIndex;
    String answerName;
    String answerImg;
    int answerOptionIndex;
    public class CelebrityLoader extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = connection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data !=-1){
                    char current =(char)data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }
    }

    public class CelebrityImageLoader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                return  bitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void createOptions(){
        Random rnd = new Random();
        for(int i=0;i<4;i++){
            if(answerOptionIndex==i){
                options.add(answerName);
                String idName = "option"+Integer.toString(i);
                int optionId = getResources().getIdentifier(idName, "id", getPackageName());
                Button optionText = findViewById(optionId);
                optionText.setText(answerName);
            }else{
                String optionName = nameArr.get(rnd.nextInt(imgArr.size()));
                while (optionName==answerName ||options.contains(optionName)==true ){
                    optionName = nameArr.get(rnd.nextInt(imgArr.size()));
                }
                options.add(optionName);
                String idName = "option"+Integer.toString(i);
                int optionId = getResources().getIdentifier(idName, "id", getPackageName());
                Button optionText = findViewById(optionId);
                optionText.setText(optionName);
            }
        }
    }

    public void createQuestion(){
        Random rnd = new Random();
        options = new ArrayList<>();
        answerIndex = rnd.nextInt(imgArr.size());
        answerName = nameArr.get(answerIndex);
        answerImg = imgArr.get(answerIndex);
        CelebrityImageLoader celebrityImageLoader = new CelebrityImageLoader();
        try {
            Bitmap celebImg = celebrityImageLoader.execute(answerImg).get();
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(celebImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        answerOptionIndex = rnd.nextInt(3);
        createOptions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CelebrityLoader celebrityLoader = new CelebrityLoader();
        String webContent = "";
        try {
            webContent = celebrityLoader.execute("http://www.posh24.se/kandisar").get();
            String[] splitResult = webContent.split("<div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);
            while (m.find()){
                imgArr.add(m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);
            while (m.find()){
                nameArr.add(m.group(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        createQuestion();
    }

    public void checkAnswer(View view){
        String clickedTag = view.getTag().toString();
        if(Integer.parseInt(clickedTag)==answerOptionIndex){
            Toast.makeText(this,"Correct!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Wrong! It was "+answerName,Toast.LENGTH_SHORT).show();
        }
        createQuestion();
    }
}
