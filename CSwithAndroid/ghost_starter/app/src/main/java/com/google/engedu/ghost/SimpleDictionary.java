package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.lang.String;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Log.d("Test ", prefix);
        String possibleWord = "";

        if (prefix.isEmpty()){
            possibleWord = words.get(random.nextInt(words.size()));
            return possibleWord;
        }

        int start = 0;
        int end = words.size()-1;
        while(end >= start){
            int mid = (start+end)/2;
            String midWord = words.get(mid);
            if (midWord.startsWith(prefix)) {
                possibleWord = midWord;
                return possibleWord;
            }
            //if prefix is smaller
            if(midWord.compareTo(prefix) > 0){
                start = 0;
                end = mid-1;
            }
            //if prefix is bigger
            if(midWord.compareTo(prefix) < 0){
                start = mid+1;
            }
        }

        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        ArrayList<String> prefixArray = new ArrayList<>();

        if (prefix.isEmpty()){
            selected = words.get(random.nextInt(words.size()));
            return selected;
        }
        int start = 0;
        int end = words.size()-1;
        while (end >= start){
            int mid = (start+end)/2;
            String midWord = words.get(mid);
            if(midWord.startsWith(prefix)){
                String foundWord = midWord;
                prefixArray.add(midWord);
                ArrayList<String> wordsArray = getWordsRange(prefixArray, mid, prefix);
                ArrayList<String> evenArray = new ArrayList<>();
                ArrayList<String> oddArray = new ArrayList<>();

                for(int i=0; i<wordsArray.size(); i++){
                    String word = wordsArray.get(i);
                    if(word.length()%2 == 0){
                        evenArray.add(word);
                    }else{
                        oddArray.add(word);
                    }
                }
                // Concept here is if prefix is odd return odd word  and vice-versa for even
                // so that user is the first one to complete word. so loses.

                if(prefix.length()%2 == 0){
                    if (evenArray.size() > 0){
                        selected = evenArray.get(random.nextInt(evenArray.size()));
                        return selected;
                    }else{
                        return foundWord;
                    }

                }else{
                    if (oddArray.size() > 0){
                        selected = oddArray.get(random.nextInt(oddArray.size()));
                        return selected;
                    }else{
                        return foundWord;
                    }


                }



            }else if(midWord.compareTo(prefix) > 0){
                start = 0;
                end = mid-1;
            }else{
                start = mid+1;
            }
        }




        return selected;
    }

    protected ArrayList<String> getWordsRange(ArrayList<String> wordsArray, int midVal, String prefix){
        int posiIncre = midVal+1;
        int negiDecre = midVal-1;
        while(words.get(posiIncre).startsWith(prefix)){
            wordsArray.add(words.get(posiIncre));
            posiIncre ++;
        }
        while (words.get(negiDecre).startsWith(prefix)){
            wordsArray.add(words.get(negiDecre));
            negiDecre --;
        }
        return wordsArray;

    }


}
