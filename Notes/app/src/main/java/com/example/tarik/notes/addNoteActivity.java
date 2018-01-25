package com.example.tarik.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

public class addNoteActivity extends AppCompatActivity {
    Integer index;
    String note;
    EditText editText;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        sharedPreferences = this.getSharedPreferences("com.example.tarik.notes", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        note = intent.getStringExtra("note");
        index = Integer.parseInt(intent.getStringExtra("index"));
        editText = findViewById(R.id.editText);
        if(note!=null){
            editText.setText(note);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                MainActivity.notes.set(index,editable.toString());
                MainActivity.arrayAdapter.notifyDataSetChanged();
                try {
                    sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(MainActivity.notes)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
