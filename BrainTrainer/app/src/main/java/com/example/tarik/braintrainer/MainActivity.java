package com.example.tarik.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String question;
    int answer;
    int correctAnswerCount;
    int totalAnswerCount;
    TextView questionText;
    ArrayList<String> options;
    CountDownTimer timer;
    TextView answerInfoText;
    View playAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        answerInfoText = findViewById(R.id.answerInfo);
        playAgainButton = findViewById(R.id.playAgain);
    }

    public void createQuestion(){
        Random rand = new Random();
        int firstNumber = rand.nextInt(20)+1;
        int secondNumber = rand.nextInt(20)+1;
        answer = firstNumber+secondNumber;
        question = Integer.toString(firstNumber)+ "+" +Integer.toString(secondNumber);
        questionText = findViewById(R.id.question);
        questionText.setText(question);
        options = new ArrayList<>();
        createOptions();
    }

    public void createOptions(){
        Random rand = new Random();
        int answerPlace = rand.nextInt(3);
        for(int i=0;i<4;i++){
            if(i==answerPlace){
                options.add(Integer.toString(answer));
                String idName = "option"+Integer.toString(i);
                int optionId = getResources().getIdentifier(idName, "id", getPackageName());
                TextView optionText = findViewById(optionId);
                optionText.setText(Integer.toString(answer));
            }else{
                int option = rand.nextInt(20)+1;
                while (options.contains(Integer.toString(option))==true || option==answer){
                    option = rand.nextInt(20)+1;
                }
                options.add(Integer.toString(option));
                String idName = "option"+Integer.toString(i);
                int optionId = getResources().getIdentifier(idName, "id", getPackageName());
                TextView optionText = findViewById(optionId);
                optionText.setText(Integer.toString(option));
            }
        }
    }

    public void toggleEnable(boolean isEnable){
        GridLayout layoutLinear = findViewById(R.id.gridLayout);
        for (int i = 0; i < layoutLinear.getChildCount(); i++) {
            View child = layoutLinear.getChildAt(i);
            child.setEnabled(isEnable);
        }
    }

    public void playButton(View view){
        answerInfoText.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);
        toggleEnable(true);
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                TextView secondTextView = findViewById(R.id.second);
                secondTextView.setText(String.valueOf(l/1000)+"s");
            }

            @Override
            public void onFinish() {
                String infoText = "Your score: "+ Integer.toString(correctAnswerCount)+"/"+Integer.toString(totalAnswerCount);
                correctAnswerCount = 0;
                totalAnswerCount = 0;
                answerInfoText.setText(infoText);
                toggleEnable(false);
                playAgainButton.setVisibility(View.VISIBLE);
            }
        }.start();
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.VISIBLE);
        view.setVisibility(View.INVISIBLE);
        createQuestion();
    }

    public void checkAnswer(View view){

        String answerTag = view.getTag().toString();
        String infoText = "";
        if(options.get(Integer.parseInt(answerTag))== Integer.toString(answer)){
          correctAnswerCount++;
          infoText = "Correct";
        }else{
            infoText = "Wrong!";
        }
        totalAnswerCount++;
        TextView resultText = findViewById(R.id.result);
        String result = Integer.toString(correctAnswerCount)+ "/"+Integer.toString(totalAnswerCount);
        resultText.setText(result);
        answerInfoText.setVisibility(View.VISIBLE);
        answerInfoText.setText(infoText);
        createQuestion();
    }
}
