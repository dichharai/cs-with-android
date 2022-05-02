package com.google.engedu.anagrams;

import android.util.Log;
import android.widget.SeekBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.StringTokenizer;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static final String TAG = "AnagramDic";
    private Random random = new Random();

    ArrayList<String> wordList = new ArrayList<String>();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String,ArrayList<String>>();
    HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private static int wordLength = DEFAULT_WORD_LENGTH;



    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            /* Mapping len to words */
            int len = word.length();
            if(sizeToWords.containsKey(len)){
                ArrayList<String> sameLenWords = sizeToWords.get(len);
                sameLenWords.add(word);

            }else{
                ArrayList<String> sameLenWord = new ArrayList<String>();
                sameLenWord.add(word);
                sizeToWords.put(len, sameLenWord);
            }

            /* Mapping letters to words*/
            String sortedLetter = sortLetters(word);
            if (lettersToWord.containsKey(sortedLetter)){
                ArrayList<String> keyArray = lettersToWord.get(sortedLetter);
                keyArray.add(word);
            }else{
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(word);
                lettersToWord.put(sortedLetter, arrayList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        boolean present = wordSet.contains(word);
        boolean isSubstring = word.contains(base);
        if(present && !isSubstring){
            Log.d(TAG, "true");
            return true;

        }else{
            Log.d(TAG, "false");
            return false;
        }
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTarget = sortLetters(targetWord);
        Log.d(TAG, "Sorted Letter " + sortedTarget);

        /*for (int i = 0; i < wordList.size(); i++) {
            //Log.d(TAG, "WordList word " + wordList.get(i));
            String check = wordList.get(i);
            Log.d(TAG, (Integer.toString(check.length())));
            if (check.length() == targetWord.length()){
                String sortedCheck = sortLetters(check);
                if (sortedCheck.equals(sortedTarget)){
                    result.add(check);
                    //Log.d(TAG, "checking check word: " + check);
                }
            }
        }
        Log.d(TAG,"getting anagrams for " + targetWord);
        for(int i=0; i<result.size(); i++){
            Log.d(TAG, result.get(i));
            //Log.d(TAG, "I am in getAnagrams() function");
        }*/
        if(lettersToWord.containsKey(sortedTarget)){
            result = lettersToWord.get(sortedTarget);
            for(int i=0; i<result.size(); i++){
                Log.d(TAG,"res: " + result.get(i));
            }
        }else{
            Log.d(TAG, "no result");
        }
        return result;
    }

    public String sortLetters(String targetWord){
        char[] chars = targetWord.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return sorted;


    }


    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i= 97; i <= 122; i++){
            char addedChar = (char)i;
            //Log.d(TAG, Character.toString(addedChar));
            String newWord = word+Character.toString(addedChar);
            String sortedKey = sortLetters(newWord);
            Log.d(TAG, sortedKey);

            if(lettersToWord.containsKey(sortedKey)){
                ArrayList<String> arrayListChar = lettersToWord.get(sortedKey);
                if(arrayListChar.size() > 0) {
                    for (int j = 0; j < arrayListChar.size(); j++) {

                        result.add(arrayListChar.get(j));
                        Log.d(TAG, arrayListChar.get(j));
                    }
                }

            }


        }
        return result;
    }

    public String pickGoodStarterWord() {

        //int nextInt = random.nextInt(10000)+1;
        String nextGoodStartWord = "";

        //Log.d(TAG, "next good word's random index: "+nextInt);
        //String checkGoodStartWord = wordList.get(nextInt);
        ArrayList<String> lenArrayList = sizeToWords.get(wordLength);
        //Log.d(TAG, "Size of lenArrayList " + Integer.toString(lenArrayList.size()));
        int nextInt = random.nextInt(lenArrayList.size());
        String checkGoodStartWord = lenArrayList.get(nextInt);



        int lenGoodStartWord = checkGoodStartWord.length();
        //if(lenGoodStartWord == wordLength && wordLength <= MAX_WORD_LENGTH){

            ArrayList arrayListCheck = getAnagrams(checkGoodStartWord);
            if(arrayListCheck.size() <= 5){
                nextGoodStartWord = checkGoodStartWord;
                Log.d(TAG, "Good Word " + nextGoodStartWord);
                /*for (int i=0; i < arrayListCheck.size(); i++){
                    Log.d(TAG,(String)arrayListCheck.get(i));
                }*/
            }else{
                return pickGoodStarterWord();

            }
        /*}else{
            return pickGoodStarterWord();
        }*/

        wordLength+=1;
        if(wordLength > MAX_WORD_LENGTH){
            wordLength = DEFAULT_WORD_LENGTH;
        }



        return nextGoodStartWord;
        //return "stop";
    }
}
