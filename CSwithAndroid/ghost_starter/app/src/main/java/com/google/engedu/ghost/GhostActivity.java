package com.google.engedu.ghost;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_SCORE = "Computer's score: ";
    private static final String USER_SCORE = "Your score: ";
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private static final String TAG = "ghostActivityDebug";
    String wordFrag;
    private static int uScore = 0;
    private static int cScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        //check whether we're recreating a previously destroyed instance

        if(savedInstanceState != null){
            // Restore value of member from saved state
            uScore = savedInstanceState.getInt(USER_SCORE);
            cScore = savedInstanceState.getInt(COMPUTER_SCORE);
            TextView userScore = (TextView) findViewById(R.id.user_score);
            userScore.setText(USER_SCORE + Integer.toString(uScore));
            TextView computerScore = (TextView) findViewById(R.id.computer_score);
            computerScore.setText(COMPUTER_SCORE + Integer.toString(cScore));

        }else{
            // Initialize members with default values for a new instance
            TextView userScore = (TextView) findViewById(R.id.user_score);
            userScore.setText(USER_SCORE + Integer.toString(uScore));
            TextView computerScore = (TextView) findViewById(R.id.computer_score);
            computerScore.setText(COMPUTER_SCORE + Integer.toString(cScore));
        }
        /*
        TextView userScore = (TextView) findViewById(R.id.user_score);
        userScore.setText(USER_SCORE + Integer.toString(uScore));
        TextView computerScore = (TextView) findViewById(R.id.computer_score);
        computerScore.setText(COMPUTER_SCORE + Integer.toString(cScore));
        */
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        Button challengeBtn = (Button) findViewById(R.id.challenge_btn);
        TextView wordInput = (TextView) findViewById(R.id.ghostText);
        userTurn = random.nextBoolean();
        wordInput.setText("");
        wordFrag = "";
        challengeBtn.setEnabled(true);
        Log.d(TAG, "In onStart()");
        TextView wordStatus = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            wordStatus.setText(USER_TURN);
        } else {
            wordStatus.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        Log.d(TAG, "In computerTurn()");
        TextView wordStatus = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        TextView wordInput = (TextView) findViewById(R.id.ghostText);
        Button challengeBtn = (Button) findViewById(R.id.challenge_btn);

        if((wordFrag.length() >= 4) && (dictionary.isWord(wordFrag))){

                wordInput.setText(wordFrag);
                wordStatus.setText("Computer Won!");
                increaseComputerScore();
                challengeBtn.setEnabled(false);
        }else{

            Log.d(TAG, "getting a word with "+ wordFrag);
            String possibleWord = dictionary.getAnyWordStartingWith(wordFrag);

            if(possibleWord != null){
                Log.d(TAG, "Possible word: " + possibleWord);
                String nextLetter = possibleWord.substring(wordFrag.length(),wordFrag.length()+1);
                wordFrag += nextLetter;
                wordInput.setText(wordFrag);
                Log.d(TAG, "New word fragment: " + wordFrag);
                userTurn = true;
                wordStatus.setText(USER_TURN);

            }else{
                Log.d(TAG, "No word can be formed with "+ wordFrag);
                wordInput.setText(wordFrag);
                wordStatus.setText("Computer Won!");
                increaseComputerScore();
                Log.d(TAG, "Computer Won!");
                wordInput.setEnabled(false);
                challengeBtn.setEnabled(false);

            }

        }



    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "In onKeyUp()");
        TextView wordStatus = (TextView) findViewById(R.id.gameStatus);
        TextView wordInput = (TextView) findViewById(R.id.ghostText);
        char unicodeChar = (char)event.getUnicodeChar();

        if(Character.isLetter(unicodeChar)){
            Log.d(TAG, "KeyCode: " + Character.toString(unicodeChar));
            wordFrag += Character.toString(unicodeChar);
            Log.d(TAG, "wordFrag in onKeyUp " + wordFrag);
            wordInput.setText(wordFrag);
            wordStatus.setText(COMPUTER_TURN);
            Log.d(TAG, wordFrag);
            /*if ((wordFrag.length()>= 4) && (dictionary.isWord(wordFrag))){
                wordStatus.setText("Not a right game rule!");

            }*/
            computerTurn();
            return true;
        }else{
            return super.onKeyUp(keyCode, event);
        }
    }

    protected void onChallenge(View view){
        TextView wordStatus = (TextView)findViewById(R.id.gameStatus);
        TextView wordInput = (TextView) findViewById(R.id.ghostText);
        String currentFrag = wordFrag;
        Log.d(TAG, "onChallenge() currentFrag: " + currentFrag);
        //String possibleWord = dictionary.getAnyWordStartingWith(currentFrag);
        String possibleWord = dictionary.getGoodWordStartingWith(currentFrag);
        if((currentFrag.length() >= 4) && dictionary.isWord(currentFrag)){
            wordStatus.setText("User Won!");
            increaseUserScore();
        }else if(possibleWord != null){
            wordStatus.setText("Computer won!");
            wordInput.setText(possibleWord);
            increaseComputerScore();
        }else{
            wordStatus.setText("User won!");
            increaseUserScore();
        }

    }
    protected void increaseComputerScore(){
        cScore += 1;
        TextView computerScore = (TextView) findViewById(R.id.computer_score);
        computerScore.setText(COMPUTER_SCORE + Integer.toString(cScore));
    }
    protected void increaseUserScore(){
        uScore += 1;
        TextView userScore = (TextView) findViewById(R.id.user_score);
        userScore.setText(USER_SCORE + Integer.toString(uScore));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "In onSaveInstanceState()");
        // Save current game state
        outState.putInt(COMPUTER_SCORE, cScore);
        outState.putInt(USER_SCORE, uScore);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }
}
