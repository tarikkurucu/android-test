package com.example.tarik.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> placesArr;
    static ArrayList<LatLng> locations;
    static ArrayAdapter<String> arrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.tarik.memorableplaces", Context.MODE_PRIVATE);
        placesArr = new ArrayList<>();
        locations = new ArrayList<>();
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();
        placesArr.clear();
        locations.clear();
        latitudes.clear();
        longitudes.clear();
        try {
            placesArr = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<>())));

            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes", ObjectSerializer.serialize(new ArrayList<>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes", ObjectSerializer.serialize(new ArrayList<>())));

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(placesArr.size()>0 && latitudes.size()>0 && longitudes.size()>0){
            if(placesArr.size()== latitudes.size() && latitudes.size()==longitudes.size()){
                for(int i=0 ;i<latitudes.size();i++){
                    LatLng location = new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i)));
                }
            }
        }
        locations.add(new LatLng(0, 0));
        placesArr.add("Add a new place...");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, placesArr);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeNumber", i);
                startActivity(intent);
            }
        });
    }
}
