package com.example.tarik.newsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> titles;
    static ArrayList<String> contents= new ArrayList<>();
    ArrayAdapter arrayAdapter;
    SQLiteDatabase articlesDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        titles = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,titles);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),newsView.class);
                intent.putExtra("content",contents.get(i));
                startActivity(intent);
            }
        });
        articlesDB = this.openOrCreateDatabase("Articles", MODE_PRIVATE,null);

        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleID INTEGER, title VARCHAR, content VARCHAR)");
        updateListView();
        DownloadTask task = new DownloadTask();
        try {
           task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateListView(){
        Cursor c = articlesDB.rawQuery("SELECT * FROM articles",null);

        int contentIndex = c.getColumnIndex("content");
        int titleIndex = c.getColumnIndex("title");

        if(c.moveToFirst()){
            titles.clear();
            contents.clear();

            do{
                titles.add(c.getString(titleIndex));
                contents.add(c.getString(contentIndex));
            }while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
        }

    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;

            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data!=-1){
                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }
                JSONArray jsonArray = new JSONArray(result);

                int numberOfItems=jsonArray.length()<20?jsonArray.length():20;

                articlesDB.execSQL("DELETE FROM articles");

                for(int i =0; i<numberOfItems;i++){
                     url = new URL("https://hacker-news.firebaseio.com/v0/item/"+jsonArray.getString(i)+".json?print=pretty");
                     urlConnection = (HttpURLConnection)url.openConnection();

                     inputStream = urlConnection.getInputStream();
                     reader = new InputStreamReader(inputStream);
                     data = reader.read();

                     String articleInfo = "";

                     while (data!=-1){
                         char current = (char)data;
                         articleInfo +=current;
                         data = reader.read();
                     }
                    JSONObject jsonObject = new JSONObject(articleInfo);
                    if(!jsonObject.isNull("title" ) && !jsonObject.isNull("url")){
                        String title = jsonObject.getString("title");
                        String URL = jsonObject.getString("url");
                        String articleID = jsonObject.getString("id");

                        URL articleUrl = new URL("https://hacker-news.firebaseio.com/v0/item/"+jsonArray.getString(i)+".json?print=pretty");
                        urlConnection = (HttpURLConnection)articleUrl.openConnection();

                        inputStream = urlConnection.getInputStream();
                        reader = new InputStreamReader(inputStream);
                        data = reader.read();

                        String articleContent = "";

                        while (data!=-1){
                            char current = (char)data;
                            articleContent +=current;
                            data = reader.read();
                        }

                        String sql = "INSERT INTO articles (articleID, title, content) VALUES (?, ?,?)";
                        SQLiteStatement statement = articlesDB.compileStatement(sql);

                        statement.bindString(1,articleID);
                        statement.bindString(2,title);
                        statement.bindString(3,URL);

                        statement.execute();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            updateListView();
        }
    }
}
