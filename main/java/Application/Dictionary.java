package Application;

import java.util.ArrayList;

public class Dictionary {
    private ArrayList<Word> words = new ArrayList<>();

    public void push(Word word) {
        words.add(word);
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public void removeAll() {
        words.removeAll(words);
    }

    public void remove(String target) {
        for(Word word1 : words) {
            if (word1.getWord_target().equals(target)) {
                words.remove(word1);
                return;
            }
        }
    }
}
