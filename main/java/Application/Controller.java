package Application;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


public class Controller implements Initializable {


    @FXML
    private ListView<String> wordList = new ListView<>();

    @FXML
    private Label label = new Label();

    @FXML
    Text text = new Text();

    String current;
    private static DictionaryManagement dictionary = new DictionaryManagement();
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Stage addStage;
    private Stage removeStage;
    private static ArrayList<String> listTarget = new ArrayList<>();

    @FXML
    private TextField searchBox = new TextField();
    @FXML
    private TextField engField = new TextField();
    @FXML
    private TextField vieField = new TextField();
    @FXML
    private TextField removeField = new TextField();

    private static ArrayList<Word> arrayWord = new ArrayList<>();
    @FXML
    private Button refresh = new Button();

    /**
     * Done
     */
    @FXML
    public void pressButton(ActionEvent event) {
        System.exit(1);
    }


    /**
     * Done
     */
    @FXML
    public void switchscreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Screen_1.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Done
     */
    @FXML
    public void switchHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Mainscene.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Text
     */
    private void readData() throws IOException, SQLException {
        /*Clear old data */
        if (dictionary.getDictionary().getWords().size() > 1) {
            refresh();
            return;
        }
        wordList.getItems().clear();
        listTarget.removeAll(listTarget);
        dictionary.removeAllDictionary();

        /* get data from database */
        dictionary.readFromDatabase();
        arrayWord = dictionary.getDictionary().getWords();
        for (Word word : arrayWord) {
            listTarget.add(word.getWord_target());
        }
        for (String s : listTarget) {
            wordList.getItems().add(s);
            if (wordList.getItems().size() > 100) {
                break;
            }
        }
        Collections.sort(wordList.getItems());
    }

    private void filterData(String targetWord) throws IOException {
        wordList.getItems().clear();
        int searchlength = targetWord.length();
        for (Word word : arrayWord) {
            StringBuilder checker = new StringBuilder();
            checker.append(word.getWord_target());
            if (word.getWord_target().length() < searchlength) {
                continue;
            }
            checker.delete(searchlength, word.getWord_target().length());
            if (String.valueOf(checker).equals(targetWord)) {
                wordList.getItems().add(word.getWord_target());
            }
        }
        if (wordList.getItems().size() == 0) {
            wordList.getItems().add("No result");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle agr1) throws IndexOutOfBoundsException {
        try {
            readData();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        wordList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            try {
                Speech speech = new Speech();
                label.setStyle("-fx-font-size: 30");
                current = wordList.getSelectionModel().getSelectedItem();
                if (current != null) {
                    if (current.length() >= 15) {
                        label.setStyle("-fx-font-size: 15");
                    }
                    if (current.length() >= 20) {
                        label.setStyle("-fx-font-size: 12");
                    }
                }
                label.setText(current);
                String wordEx = arrayWord.get(listTarget.indexOf(current)).getWord_explain().replace('#' , '\n');
                text.setText(wordEx);
                speech.addHistory(current
                        + "\t\t"
                        + arrayWord.get(listTarget.indexOf(current)).getWord_explain());
            } catch (IndexOutOfBoundsException | IOException indexOutOfBoundsException) {
                text.setText("");
                label.setText("Meaning of the word is empty");
                label.setStyle("-fx-font-size: 15");
            }
        });

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                try {
                    refresh();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    filterData(newValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addWord(Event event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddWindows.fxml")));
        addStage = new Stage();
        addStage.setTitle("Add Word");
        addStage.setScene(new Scene(root));
        addStage.show();

    }

    public void exitAddButton(Event event) {
        addStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addStage.close();
    }

    @FXML
    public void addButton(Event event) throws IOException, SQLException {
        if (dictionary.dictionaryLookup(engField.getText())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("The word you want to add already exists!\n"
                    + dictionary.getDictionary().getWords().get(listTarget.indexOf(engField.getText())).getWord_target());
            alert.setContentText("Do you want to correct that word?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                addStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                addStage.close();
                return;
            }
            dictionary.removeFromDatabase(engField.getText());
            dictionary.removeDictionary(engField.getText());
        }
        if (engField.getText().length() >= 1 && vieField.getText().length() >= 1) {
            dictionary.addToDatabase(engField.getText(), vieField.getText());
            dictionary.addDictionary(new Word(engField.getText(), vieField.getText()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Add successfully!");
            alert.setContentText("Click Refresh to update the dictionary");
            alert.setTitle("Notification");
            alert.showAndWait();
            refresh();
            exitAddButton(event);
        }

    }

    @FXML
    public void refresh() throws IOException, SQLException {

        wordList.getItems().clear();
        arrayWord = dictionary.getDictionary().getWords();
        listTarget.clear();
        for (Word word : arrayWord) {
            listTarget.add(word.getWord_target());
        }
        for (String s : listTarget) {
            wordList.getItems().add(s);
            if (wordList.getItems().size() > 100) {
                break;
            }
        }
        Collections.sort(wordList.getItems());
    }

    @FXML
    public void removeButton(Event event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("RemoveWindows.fxml")));
        removeStage = new Stage();
        removeStage.setTitle("Remove Word");
        removeStage.setScene(new Scene(root));
        removeStage.show();

    }

    public void removeWord(Event event) throws IOException, SQLException {
        if (!dictionary.dictionaryLookup(removeField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Notification!");
            alert.setHeaderText("The word you deleted does not exist!");
            alert.showAndWait();
        } else {
            dictionary.removeFromDatabase(removeField.getText());
            dictionary.removeDictionary(removeField.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Remove successfully!");
            alert.setContentText("Click Refresh to update the dictionary");
            alert.setTitle("Notification");
            alert.showAndWait();
            exitAddButton(event);
        }
    }

    @FXML
    public void speak(Event event) {
        Speech speech = new Speech();
        if (wordList.getSelectionModel().getSelectedItem() == null) {
            if (label.getText().equalsIgnoreCase("Meaning of the word is empty") || label.getText().equalsIgnoreCase("Meaning")) {
                speech.TextToSpeech("Please choose a word to speak");
                return;
            }
            speech.TextToSpeech(label.getText());
            return;
        }
        speech.TextToSpeech(wordList.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void switchGuide(ActionEvent event) throws IOException {
        Parent root3 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GuideScene.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root3);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchFavourite(ActionEvent event) throws IOException {
        Parent root4 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Favourite.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root4);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void addFavourite(ActionEvent event) throws IOException {
        DictionaryManagement favouriteDictionary = new DictionaryManagement();
        favouriteDictionary.insertFromFile("src/Favourite.txt");
        if (label.getText().equalsIgnoreCase("Meaning") || label.getText().equalsIgnoreCase("Meaning of the word is empty")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No word to add to favourite List!");
            alert.setContentText("Please choose a word to add to favourite List");
            alert.setTitle("Notification");
            alert.showAndWait();
        } else {
            if (favouriteDictionary.dictionaryLookup(label.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("The word you want to add already exists!");
                alert.setTitle("Notification");
                alert.showAndWait();
            } else {
                String temp = label.getText();
                Writer writer = new BufferedWriter(new FileWriter("src/Favourite.txt", true));
                writer.append("\n");
                writer.append(temp);
                writer.append("\t\t");
                writer.append(dictionary.getDictionary().getWords().get(listTarget.indexOf(temp)).getWord_explain());
                writer.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Add successfully!");
                alert.setContentText("Click Refresh to update the list");
                alert.setTitle("Notification");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void switchHistory(Event event) throws IOException {
        Parent root5 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("History.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root5);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void search(Event event) {
        if (wordList.getItems().contains(searchBox.getText())) {
            label.setStyle("-fx-font-size: 30");
            String temp = searchBox.getText();
            label.setText(temp);
            String wordEx = dictionary.getDictionary().getWords().get(listTarget.indexOf(temp)).getWord_explain().replace('#', '\n');
            text.setText(wordEx);
        }
    }
}
