package com.example.tarik.tictactoe;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    int activePlayer=0;

    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    boolean isGameActive = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void dropIn(View view){
        if(isGameActive){
            ImageView counter = (ImageView) view;
            int clickedTag = Integer.parseInt(counter.getTag().toString());
            if(this.gameState[clickedTag]==2){
                counter.setTranslationY(-1000f);
                this.gameState[clickedTag] = this.activePlayer;
                if(this.activePlayer==0){
                    counter.setImageResource(R.drawable.yellow);
                    this.activePlayer=1;
                }else{
                    counter.setImageResource(R.drawable.red);
                    this.activePlayer=0;
                }
                counter.animate().translationYBy(1000f).setDuration(300);
                boolean isGameFinished = this.checkGameIsFinished();
                if(isGameFinished){
                    this.isGameActive = false;
                    String winner = "Red";
                    if(this.activePlayer == 1){
                        winner = "Yellow";
                    }
                    this.gameEndMessage(winner+" has won!");
                }else{
                    boolean gameIsEnd = true;
                    for(int state:this.gameState){
                        if(state==2){
                            gameIsEnd = false;
                        }
                    }
                    if(gameIsEnd){
                        this.gameEndMessage("It's draw");
                    }
                }
            }
        }

    }

    public void gameEndMessage(String message){
        TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);
        winnerMessage.setText(message);
        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
        layout.setVisibility(View.VISIBLE);
    }

    public boolean checkGameIsFinished(){
        for (int[] winningPosition : this.winningPositions){
            if(gameState[winningPosition[0]]==gameState[winningPosition[1]] && gameState[winningPosition[2]]==gameState[winningPosition[1]] && gameState[winningPosition[0]]!=2){
                return true;
            }
        }
        return false;
    }

    public void playAgain(View view){
        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
        layout.setVisibility(View.INVISIBLE);
        activePlayer=0;
        this.isGameActive = true;
        for (int i = 0;i<this.gameState.length;i++){
            gameState[i]=2;
        }
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        for(int i=0; i<gridLayout.getChildCount();i++){
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
        }

    }
}
