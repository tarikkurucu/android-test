package com.example.tarik.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    int countSecond;
    CountDownTimer timer;
    public void setTextView(int progress){
        TextView textView = findViewById(R.id.textView);
        int minutes = progress/60;
        int seconds = progress%60;
        String text = (seconds < 10 ? "0" : "") + Integer.toString(seconds);
        String time = Integer.toString(minutes)+":"+text;
        textView.setText(time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);

        seekBar.setMax(600);
        seekBar.setProgress(5);
        countSecond = 5;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<5){
                    seekBar.setProgress(5);
                    countSecond=5;
                }else{
                    setTextView(progress);
                    countSecond=progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void disableSeekBar(){
        seekBar.setEnabled(false);
    }

    public void enabledSeekBar(){
        seekBar.setEnabled(true);
    }

    public void startTimer(View view){
        disableSeekBar();
        timer = new CountDownTimer(countSecond*1000,1000){
            public void onTick(long millisecondsUntilDone){
                countSecond--;
                setTextView(((int)millisecondsUntilDone/1000)+1);
            }
            public void onFinish(){
                countSecond--;
                setTextView(0);
                playHorn();
                hideStopButton();
                enabledSeekBar();
            }
        }.start();
        hidePlayButton();
    }

    public void playHorn(){
        MediaPlayer mPlayer = MediaPlayer.create(this,R.raw.airhorn);
        mPlayer.start();
    }

    public void stopTimer(View view){
        timer.cancel();
        hideStopButton();
        enabledSeekBar();
    }
    public void hideStopButton(){
        View stopButton = findViewById(R.id.buttonStop);
        stopButton.setVisibility(View.INVISIBLE);
        View GOButton = findViewById(R.id.button);
        GOButton.setVisibility(View.VISIBLE);
        setTextView(30);
        seekBar.setProgress(30);
        countSecond = 30;
    }
    public void hidePlayButton(){
        View GOButton = findViewById(R.id.button);
        GOButton.setVisibility(View.INVISIBLE);
        View stopButton = findViewById(R.id.buttonStop);
        stopButton.setVisibility(View.VISIBLE);
    }
}
