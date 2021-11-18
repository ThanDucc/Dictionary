package Application;

import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryCommandline {
    DictionaryManagement dictionaryManagement = new DictionaryManagement();

    /**
     * hien thi tu dien.
     */
    public void showAllWords() {
        ArrayList<Word> dictionary = dictionaryManagement.getDictionary().getWords();
        System.out.println("\tTừ điển");
        System.out.printf("%-3s | %-20s | %-20s%n", "No", "English", "Vietnamese");
        for (int i = 1; i <= dictionary.size(); i++) {
            System.out.printf("%-3s | %-20s | %-20s%n", i, dictionary.get(i - 1).getWord_target(), dictionary.get(i - 1).getWord_explain());
        }
    }

    public void dictionaryBasic() {
        dictionaryManagement.insertFromCommandline();
        //dictionaryManagement.insertFromFile();
        showAllWords();
    }

    public void dictionaryAdvanced() {
        dictionaryManagement.insertFromFile();
        showAllWords();
        dictionaryManagement.dictionaryLookup();
    }

    /**
     * tim kiem va hien thi tu goi y.
     */
    public void dictionarySeacher() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ muốn tìm kiếm: ");
        String word_target = scanner.nextLine();
        ArrayList<Word> Dictionary = dictionaryManagement.dictionary.getWords();
        boolean check = false;
        int wordLength = word_target.length();
        int j = 1;
        for (Word word : Dictionary) {
            String wordSearcher = word.getWord_target().substring(0, wordLength);
            if (wordSearcher.equalsIgnoreCase(word_target)) {
                System.out.printf("%-3s | %-20s | %-20s%n", j++, word.getWord_target(), word.getWord_explain());
                check = true;
            }
        }
        if ((!check)) {
            System.out.println("Không có từ nào bắt đầu bằng '" + word_target + "' trong từ điển");
        }
    }

}
