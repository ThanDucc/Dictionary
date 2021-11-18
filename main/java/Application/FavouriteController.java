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
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class FavouriteController implements Initializable {
    @FXML
    private ListView<String> favouriteList = new ListView<>();

    @FXML
    private Label label = new Label();

    String current;
    private static DictionaryManagement dictionary = new DictionaryManagement();
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Stage removeStage;
    private static ArrayList<String> listTarget = new ArrayList<>();

    @FXML
    private TextField searchBox = new TextField();

    @FXML
    private TextField removeField = new TextField();

    @FXML
    private Text text = new Text();

    static ArrayList<Word> arrayWord = new ArrayList<>();

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

    /**
     * doc du lieu tu favouriteList.
     */
    private void readData() throws IOException {
        favouriteList.getItems().clear();
        listTarget.removeAll(listTarget);
        dictionary.removeAllDictionary();
        dictionary.insertFromFile("src/Favourite.txt");
        arrayWord = dictionary.getDictionary().getWords();
        for (Word word : arrayWord) {
            listTarget.add(word.getWord_target());
        }
        favouriteList.getItems().addAll(listTarget);
        if (favouriteList.getItems().size() == 0) {
            favouriteList.getItems().add("Favourite List is empty");
        }
    }

    /**
     * loc du lieu
     */
    private void filterData(String targetWord) throws IOException {
        favouriteList.getItems().clear();
        for (Word word : arrayWord) {
            if (word.getWord_target().toLowerCase().contains(targetWord.toLowerCase())) {
                favouriteList.getItems().add(word.getWord_target());
            }
        }
        if (favouriteList.getItems().size() == 0) {
            favouriteList.getItems().add("No result");
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

        favouriteList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            try {
                label.setText("Meaning");
                label.setStyle("-fx-font-size: 30");
                current = favouriteList.getSelectionModel().getSelectedItem();
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


    public void removeLine(String lineContent) throws IOException {
        File file = new File("src/Favourite.txt");
        List<String> out = Files.lines(file.toPath())
                .filter(line -> !line.contains(lineContent))
                .collect(Collectors.toList());
        Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @FXML
    public void refresh() throws IOException {
        for (String s : favouriteList.getItems()) {
            System.out.println(s);
        }
        System.out.println("\n");
        readData();
        for (String s : favouriteList.getItems()) {
            System.out.println(s);
        }
    }

    public void exitAddButton(Event event) {
        removeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        removeStage.close();
    }

    @FXML
    public void removeButton(Event event) throws IOException {
        Parent root2 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("RemoveFavourite.fxml")));
        removeStage = new Stage();
        removeStage.setTitle("Remove Word");
        removeStage.setScene(new Scene(root2));
        removeStage.show();

    }

    public void removeWord(Event event) throws IOException {
        if (!dictionary.dictionaryLookup(removeField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Notification");
            alert.setHeaderText("The word you deleted does not exist!");
            alert.showAndWait();
        } else {
            String rline = removeField.getText() + "\t\t" + dictionary.getDictionary().getWords().get(listTarget.indexOf(removeField.getText())).getWord_explain();
            removeLine(rline);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Remove successfully!");
            alert.setContentText("Click Refresh to update the dictionary");
            alert.setTitle("Notification");
            alert.showAndWait();
            exitAddButton(event);
            refresh();
        }
    }

    @FXML
    public void speak(Event event) {
        Speech speech = new Speech();
        if(favouriteList.getSelectionModel().getSelectedItem() == null) {
            if(label.getText().equalsIgnoreCase("Meaning of the word is empty") || label.getText().equalsIgnoreCase("Meaning")) {
                speech.TextToSpeech("Please choose a word to speak");
                return;
            }
            speech.TextToSpeech(label.getText());
            return;
        }
        speech.TextToSpeech(favouriteList.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void removeAllButton(Event event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notification");
        alert.setContentText("Are you sure to want to clear all speak favourite List?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            PrintWriter writer = new PrintWriter("src/Favourite.txt");
            writer.print("");
            writer.close();
        }
        refresh();
    }

    @FXML
    public void search(Event event) {
        if (favouriteList.getItems().contains(searchBox.getText())) {
            label.setStyle("-fx-font-size: 30");
            String temp = searchBox.getText();
            label.setText(temp);
            String wordEx = dictionary.getDictionary().getWords().get(listTarget.indexOf(temp)).getWord_explain().replace('#', '\n');
            text.setText(wordEx);
        }
    }
}

