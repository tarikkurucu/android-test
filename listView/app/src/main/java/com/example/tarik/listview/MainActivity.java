package com.example.tarik.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView myListView = findViewById(R.id.myListView);

        final ArrayList<String> myFriends = new ArrayList<String>();

        myFriends.add("Eren");
        myFriends.add("Ali");
        myFriends.add("Emre");
        myFriends.add("Metin");
        myFriends.add("Erman");
        myFriends.add("Damla");
        myFriends.add("Tuncay");
        myFriends.add("Ergün");
        myFriends.add("Seda");
        myFriends.add("Lokum");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,myFriends);

        myListView.setAdapter(arrayAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),"Merhaba "+myFriends.get(position),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
