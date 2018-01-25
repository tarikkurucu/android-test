package com.example.tarik.timestables;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    public void generateTimesTable(int timesTable){
        ArrayList<String> numbers = new ArrayList<>();
        for(int i=0;i<10;i++){
            int count = timesTable*(i+1);
            numbers.add(i,Integer.toString(count));
        }
        listView = findViewById(R.id.myListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,numbers);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar seekBar = findViewById(R.id.seekBar);
        listView = findViewById(R.id.myListView);
        seekBar.setMax(20);
        seekBar.setProgress(10);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 1;
                int timesTable;
                if(progress<min){
                    timesTable = min;
                    seekBar.setProgress(min);
                }else{
                    timesTable = progress;
                }
                generateTimesTable(timesTable);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        this.generateTimesTable(10);
    }
}
