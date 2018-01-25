package com.example.tarik.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes;
    static ArrayAdapter arrayAdapter;
    ListView listView;
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
            case R.id.addNote:
                notes.add("");
                Intent intent = new Intent(getApplicationContext(),addNoteActivity.class);
                intent.putExtra("note","");
                intent.putExtra("index", Integer.toString(notes.size()-1));
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("com.example.tarik.notes", Context.MODE_PRIVATE);

        listView = findViewById(R.id.listView);

        try {
            notes =(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("notes",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(notes.size()==0){
            notes.add("Example Note");
            try {
                sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(notes)).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),addNoteActivity.class);
                intent.putExtra("note",notes.get(i));
                intent.putExtra("index", Integer.toString(i));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,int itemIndex, long l) {
                final int itemToDelete = itemIndex;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are u sure?")
                        .setMessage("Do u delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                try {
                                    sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(notes)).apply();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
                return true;
            }
        });
    }

}
