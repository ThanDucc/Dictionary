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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

public class HistoryController implements Initializable {

    @FXML
    private ListView<String> historyList = new ListView<>();

    @FXML
    private Label label = new Label();

    String current;
    private DictionaryManagement dictionary = new DictionaryManagement();
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ArrayList<String> listTarget = new ArrayList<>();

    @FXML
    private TextField searchBox = new TextField();

    @FXML
    private Text text = new Text();

    ArrayList<Word> arrayWord = new ArrayList<>();

    @FXML
    private Button refresh = new Button();

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


    private void readData() throws IOException {
        historyList.getItems().clear();
        listTarget.removeAll(listTarget);
        dictionary.removeAllDictionary();
        dictionary.insertFromFile("src/History.txt");
        arrayWord = dictionary.getDictionary().getWords();
        for (Word word : arrayWord) {
            listTarget.add(word.getWord_target());
        }
        historyList.getItems().addAll(listTarget);
        if (historyList.getItems().size() == 0) {
            historyList.getItems().add("History List is empty");
        }
    }

    private void filterData(String targetWord) throws IOException {
        historyList.getItems().clear();
        for (Word word : arrayWord) {
            if (word.getWord_target().toLowerCase().contains(targetWord.toLowerCase())) {
                historyList.getItems().add(word.getWord_target());
            }
        }
        if (historyList.getItems().size() == 0) {
            historyList.getItems().add("No result");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle agr1) throws IndexOutOfBoundsException {
        try {
            readData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        text.setText("Meaning of the word is empty");
        historyList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            try {
                label.setText("Meaning");
                label.setStyle("-fx-font-size: 30");
                current = historyList.getSelectionModel().getSelectedItem();
                label.setText(current);
                String wordEx = arrayWord.get(listTarget.indexOf(current)).getWord_explain().replace('#', '\n');
                text.setText(wordEx);
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                label.setText("Meaning of the word is empty");
                label.setStyle("-fx-font-size: 15");
                text.setText("");
            }
        });

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                try {
                    readData();
                } catch (IOException e) {
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


    @FXML
    public void refresh(Event event) throws IOException {
        for (String s : historyList.getItems()) {
            System.out.println(s);
        }
        System.out.println("\n");
        readData();
        for (String s : historyList.getItems()) {
            System.out.println(s);
        }
    }


    @FXML
    public void speak(Event event) {
        Speech speech = new Speech();
        if (historyList.getSelectionModel().getSelectedItem() == null) {
            if (label.getText().equalsIgnoreCase("Meaning of the word is empty") || label.getText().equalsIgnoreCase("Meaning")) {
                speech.TextToSpeech("Please choose a word to speak");
                return;
            }
            speech.TextToSpeech(label.getText());
            return;
        }
        speech.TextToSpeech(historyList.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void removeAllButton(Event event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure to want to clear all history?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            PrintWriter writer = new PrintWriter("src/History.txt");
            writer.print("");
            writer.close();
        }
        refresh(event);
    }

    @FXML
    public void search(Event event) {
        if (historyList.getItems().contains(searchBox.getText())) {
            label.setStyle("-fx-font-size: 30");
            String temp = searchBox.getText();
            label.setText(temp);
            String wordEx = dictionary.getDictionary().getWords().get(listTarget.indexOf(temp)).getWord_explain().replace('#', '\n');
            text.setText(wordEx);
        }
    }
}

