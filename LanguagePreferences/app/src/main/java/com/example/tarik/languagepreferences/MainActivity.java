package com.example.tarik.languagepreferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.english:
                setLanguage("English");
                return true;
            case R.id.turkce:
                setLanguage("Turkce");
                return true;
            default:
                return false;
        }
    }

    public void setLanguage(String language){
        sharedPreferences.edit().putString("language",language).apply();
        setTextVievText(language);
    }

    public void setTextVievText(String text){
        TextView textView = findViewById(R.id.textView);
        textView.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("com.example.tarik.languagepreferences", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language","");
        if(language == ""){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_btn_speak_now)
                    .setTitle("Select Language")
                    .setMessage("Which one do you prefer")
                    .setPositiveButton("English", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setLanguage("English");
                        }
                    })
                    .setNegativeButton("Türkçe", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setLanguage("Türkçe");
                        }
                    }).show();
        }else{
            setTextVievText(language);
        }
    }
}
