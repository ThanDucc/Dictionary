package Application;

import java.io.*;
import java.util.ArrayList;

public class ReadFile {
    public ArrayList<Word> read() {
        String[] words;
        ArrayList<Word> listWords = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader("src/dictionariesss.txt"));
            String line = bufferedReader.readLine();
            while (line != null) {
                words = line.split("\t");
                if (words.length >= 2) {
                    Word word = new Word(words[0], words[1]);
                    listWords.add(word);
                }
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listWords;
    }

    public ArrayList<Word> read(String s) {
        String[] words;
        ArrayList<Word> listWords = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(s));
            String line = bufferedReader.readLine();
            while (line != null) {
                words = line.split("\t\t");
                if (words.length >= 2) {
                    Word word = new Word(words[0], words[1]);
                    listWords.add(word);
                }
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listWords;
    }
}
