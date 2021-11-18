package Application;

import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class DictionaryManagement {
    public Dictionary dictionary = new Dictionary();

    /**
     * Nhap du lieu tu ban phim cho tu dien.
     */
    public void insertFromCommandline() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số lượng từ: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        for (int i = 1; i <= quantity; i++) {
            System.out.print("Tiếng Anh: ");
            String word_target = scanner.nextLine();
            System.out.print("Tiếng Việt: ");
            String word_explain = scanner.nextLine();
            Word word = new Word(word_target, word_explain);
            dictionary.push(word);
            System.out.println("Đã thêm từ '" + word_target + "' vào từ điển!");
        }
    }

    /**
     * Nhap du lieu tu file vao tu dien.
     */
    public void insertFromFile() {
        ReadFile readFile = new ReadFile();
        ArrayList<Word> listWord = readFile.read();
        for (Word word : listWord) {
            dictionary.push(word);
        }
        System.out.println("Đã thêm dữ liệu từ file vào từ điển!");
    }

    public void insertFromFile(String s) {
        ReadFile readFile = new ReadFile();
        ArrayList<Word> listWord = readFile.read(s);
        for (Word word : listWord) {
            dictionary.push(word);
        }
    }

    /**
     * Tim kiem tu trong tu dien.
     */
    public boolean dictionaryLookup() {
        boolean result = false;
        ArrayList<String> s = new ArrayList<>();
        System.out.println("Nhập số từ cần tìm kiếm: ");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i =0; i < n; i++) {
            System.out.println("Nhập từ bạn muốn tìm kiếm:");
            String temp = scanner.next();
            s.add(temp);
        }

        scanner.close();
        ArrayList<Word> word1 = dictionary.getWords();
        System.out.println(word1.size());
        vongto: for (int j = 0; j < n; j++) {
            for (int i = 0; i < word1.size(); i++) {
                if (word1.get(i).getWord_target().equals(s.get(j))) {
                    System.out.printf("%-3s | %-20s | %-20s%n", i, word1.get(i).getWord_target(), word1.get(i).getWord_explain());
                    result = true;
                    continue vongto;
                }

            }
            System.out.println("Không tìm thấy từ bạn nhập!" + s.get(j));
        }
        return result;
    }

    public boolean dictionaryLookup(String string) {
        ArrayList<Word> word1 = dictionary.getWords();
        System.out.println(word1.size());
        for (int i = 0; i < word1.size(); i++) {
            if (word1.get(i).getWord_target().equals(string)) {
                System.out.println(String.format("%-3s  %-20s  %-20s", i, word1.get(i).getWord_target(), word1.get(i).getWord_explain()));
                return true;
            }
        }
        return false;
    }

    /**
     * Xuat du lieu tu dien ra file.
     */
    public void dictionaryExportToFile() {
        File fileTxT = new File("Dictionary.txt");
        System.out.println("Đường dẫn mặc định: src/Dictionary.txt.");
        System.out.println("Bạn có muốn đổi đường dẫn không? (Y/N)");
        Scanner sc = new Scanner(System.in);
        String Ans = sc.next();
        if (Ans.equalsIgnoreCase("y")) {
            System.out.print("Nhập đường dẫn:...");
            fileTxT = new File(sc.next());
            sc.close();
        } else if (Ans.equalsIgnoreCase("n")) {
            fileTxT = new File("Dictionary.txt");
        } else {
            System.out.println("Yêu cầu không hợp lệ!");
            return;
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileTxT));
            ArrayList<Word> word1 = dictionary.getWords();
            for (int i = 0; i < word1.size(); i++) {
                String temp = String.format("%-3s | %-20s | %-20s", i + 1, word1.get(i).getWord_target(), word1.get(i).getWord_explain());
                bw.write(temp + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Them 1 tu moi vao tu dien co kiem tra.
     */
    public void addDictionary() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhập từ muốn thêm: ");
        System.out.print("Tiếng Anh: ");
        String word_target = scanner.nextLine();
        System.out.print("Tiếng Việt: ");
        String word_explain = scanner.nextLine();
        Word word = new Word(word_target, word_explain);

        ArrayList<Word> Dictionary = dictionary.getWords();
        boolean check = false;
        for (Word value : Dictionary) {
            if (value.getWord_target().equalsIgnoreCase(word_target)) {
                check = true;
                break;
            }
        }
        if (!check) {
            dictionary.push(word);
            System.out.println("Đã thêm từ '" + word_target + "' vào từ điển!");
        }
    }

    /**
     * them luon tu moi.
     */
    public void addDictionary(Word word) {
        dictionary.push(word);
    }

    /**
     * sua 1 tu trong tu dien.
     */
    public void editDictionary() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ cần sửa: ");
        String word_target = scanner.nextLine();

        ArrayList<Word> Dictionary = dictionary.getWords();
        boolean check = false;
        for (int i = 0; i < Dictionary.size(); i++) {
            if (Dictionary.get(i).getWord_target().equalsIgnoreCase(word_target)) {
                System.out.print("Nhập nghĩa mới: ");
                String word_explain = scanner.nextLine();
                dictionary.getWords().get(i).setWord_explain(word_explain);
                check = true;
                System.out.println("Đã sửa từ '" + word_target + "'");
            }
        }
        if (!check) {
            System.out.println("Không có từ '" + word_target + "' trong từ điển");
            System.out.println("Bạn có muốn thêm từ '" + word_target + "' vào từ điển không? (Y/N)");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                System.out.print("Tiếng Việt: ");
                String word_explain = scanner.nextLine();
                Word word = new Word(word_target, word_explain);
                dictionary.push(word);
                System.out.println("Đã thêm từ '" + word_target + "' vào từ điển");
            }
        }
    }

    /**
     * Xoa 1 tu trong tu dien co kiem tra.
     */
    public void removeDictionary() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ muốn xoá: ");
        String word_target = scanner.nextLine();

        ArrayList<Word> Dictionary = dictionary.getWords();
        boolean check = false;
        for (int i = 0; i < Dictionary.size(); i++) {
            if (Dictionary.get(i).getWord_target().equalsIgnoreCase(word_target)) {
                dictionary.getWords().remove(i);
                System.out.println("Đã xoá từ '" + word_target + "'");
                check = true;
            }
        }
        if ((!check)) {
            System.out.println("Không có từ '" + word_target + "' trong từ điển");
        }
    }

    /**
     * Xoa la xoa luon.
     */
    public void removeDictionary(String target) {
        dictionary.remove(target);
    }

    /**
     * xoa tat ca tu dien.
     */
    public void removeAllDictionary() {
        dictionary.removeAll();
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Doc du lieu tu database.
     */
    public void readFromDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionaries", "root", "12345678");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * from dictionaryData order by word asc");
        Speech speech = new Speech();
        while (resultSet.next()) {
            Word new_word = new Word(resultSet.getString(2), resultSet.getString(3));
            dictionary.push(new_word);
        }
        resultSet.close();
        statement.close();
    }

    /**
     * them 1 tu moi vao database tu dien.
     */
    public void addToDatabase(String target, String explain) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionaries", "root", "12345678");
        Statement statement = connection.createStatement();
        String sql = "insert into dictionaryData (word, detail) values (\"" + target + "\", \"" + explain + "\")";
        statement.executeUpdate(sql);
        statement.close();

    }

    /**
     * xoa 1 tu khoi database tu dien.
     */
    public void removeFromDatabase(String target) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionaries", "root", "12345678");
        Statement statement = connection.createStatement();
        String sqlQuery = "delete from dictionaryData where word = \"" + target + "\"";
        statement.executeUpdate(sqlQuery);
        statement.close();
    }

}
