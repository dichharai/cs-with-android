package edu.uiowa.dichha.scarnesdice;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int userTotalScore;
    private static int userTurnScore;
    private static int computerTotalScore;
    private static int computerTurnScore;
    private Random random = new Random();
    private static final int DICE_MIN = 1;
    private static final int DICE_MAX = 6;
    private static final String TAG = "DiceDebug";

    /* needs to implement computer turn on a thread */
    Handler timeHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long milis = System.currentTimeMillis();
            int seconds = (int) (milis/1000);
            int minutes = seconds/60;
            seconds = seconds%60;
            timeHandler.postDelayed(this, 500);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Roll Button functionality */

        Button rollBtn = (Button)findViewById(R.id.rollBtn);
        rollBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int num = rollDie();
                calculateUserScore(num);


                Log.d(TAG, Integer.toString(num)+" user");
                Drawable diceImage = getImage(num);
                ImageView imageView = (ImageView) findViewById(R.id.diceImageView);
                imageView.setImageDrawable(diceImage);


            }
        });

        /*Reset Button functionality */
        Button resetBtn = (Button) findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                userTurnScore=0;
                userTotalScore=0;
                computerTurnScore=0;
                computerTotalScore=0;
                TextView scoreTV = (TextView) findViewById(R.id.scoreboard);
                scoreTV.setText("Your score: "+ Integer.toString(userTotalScore)+" computer score: "+Integer.toString(computerTotalScore)+ "\nYour turn score: "+Integer.toString(userTurnScore)+ " computer turn score: "+Integer.toString(computerTurnScore));

            }
        });

        /* Hold Button functionality */
        Button holdBtn = (Button) findViewById(R.id.holdBtn);
        holdBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                userTotalScore += userTurnScore;
                userTurnScore=0;
                TextView scoreTV = (TextView) findViewById(R.id.scoreboard);
                scoreTV.setText("Your score: "+ Integer.toString(userTotalScore)+" computer score: "+Integer.toString(computerTotalScore)+ "\nYour turn score: "+Integer.toString(userTurnScore)+ " computer turn score: "+Integer.toString(computerTurnScore));
                computerTurn();



            }
        });


    }
    private void calculateUserScore(int num){
        if (num != 1){
            userTurnScore += num;

            TextView scoreTV = (TextView)findViewById(R.id.scoreboard);
            scoreTV.setText("Your score: "+ Integer.toString(userTotalScore)+" computer score: "+Integer.toString(computerTotalScore)+ "\nYour turn score: "+Integer.toString(userTurnScore)+ " computer turn score: "+Integer.toString(computerTurnScore));
        }else{
            userTotalScore += userTurnScore;
            userTurnScore = 0;
            TextView scoreTV = (TextView)findViewById(R.id.scoreboard);
            scoreTV.setText("Your score: "+ Integer.toString(userTotalScore)+" computer score: "+Integer.toString(computerTotalScore)+ "\nYour turn score: "+Integer.toString(userTurnScore)+ " computer turn score: "+Integer.toString(computerTurnScore));

            computerTurn();
        }
    }
    private int rollDie(){
        int diceNum = random.nextInt(DICE_MAX-DICE_MIN + 1)+DICE_MIN;
        return diceNum;

    }
    private Drawable getImage(int num){

        switch(num){

            case 1:
                num = R.drawable.dice1;
                break;
            case 2:
                num = R.drawable.dice2;
                break;
            case 3:
                num = R.drawable.dice3;
                break;
            case 4:
                num = R.drawable.dice4;
                break;
            case 5:
                num = R.drawable.dice5;
                break;
            case 6:
                num = R.drawable.dice6;
                break;
        }


        Drawable diceImage = getResources().getDrawable(num);

        return diceImage;
    }


    private void computerTurn(){
        /* disable roll button */
        Button rollBtn = (Button)findViewById(R.id.rollBtn);
        rollBtn.setEnabled(false);

        /* disable hold button */
        Button holdBtn = (Button) findViewById(R.id.holdBtn);
        holdBtn.setEnabled(false);

        int num = rollDie();
        calculateComputerScore(num);



    }
    private void calculateComputerScore(int cnum){
       //Log.d(TAG, Integer.toString(cnum)+" computer");
        Drawable diceImage = getImage(cnum);
        ImageView imageView = (ImageView) findViewById(R.id.diceImageView);
        imageView.setImageDrawable(diceImage);

        TextView scoreTV = (TextView) findViewById(R.id.scoreboard);

        while(cnum != 1 && computerTurnScore < 20){
            computerTurnScore += cnum;
            cnum = rollDie();
            Log.d(TAG, Integer.toString(cnum)+" computer");
            diceImage = getImage(cnum);
            imageView = (ImageView) findViewById(R.id.diceImageView);
            imageView.setImageDrawable(diceImage);
            scoreTV.setText("Your score: "+ Integer.toString(userTotalScore)+" computer score: "+Integer.toString(computerTotalScore)+ "\n Your turn score: "+Integer.toString(userTurnScore)+ " computer turn score: "+Integer.toString(computerTurnScore));

        }

        if(cnum == 1 || computerTurnScore >= 20){
            computerTotalScore += computerTurnScore;
            computerTurnScore = 0;
            scoreTV.setText("Your score: "+ Integer.toString(userTotalScore)+" computer score: "+Integer.toString(computerTotalScore)+ "\n Your turn score: "+Integer.toString(userTurnScore)+ " computer turn score: "+Integer.toString(computerTurnScore));
            /* enable roll button */
            Button rollBtn = (Button)findViewById(R.id.rollBtn);
            rollBtn.setEnabled(true);

            /* enable hold button */
            Button holdBtn = (Button) findViewById(R.id.holdBtn);
            holdBtn.setEnabled(true);


      }

    }


}
