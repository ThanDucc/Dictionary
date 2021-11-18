package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class GuideController {
    @FXML
    private Pane generalPane = new Pane();
    @FXML
    private Pane addPane = new Pane();
    @FXML
    private Pane removePane = new Pane();
    @FXML
    private Pane hearPane = new Pane();
    @FXML
    private Pane importantPane = new Pane();
    @FXML
    private Pane recentPane = new Pane();
    @FXML
    private Pane aboutPane = new Pane();

    @FXML
    private Button generalButton = new Button();
    @FXML
    private Button addButton = new Button();
    @FXML
    private Button removeButton = new Button();
    @FXML
    private Button hearButton = new Button();
    @FXML
    private Button importantButton = new Button();
    @FXML
    private Button recentButton = new Button();
    @FXML
    private Button aboutButton = new Button();


    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == generalButton) {
            generalPane.toFront();
        } else if (event.getSource() == addButton) {
            addPane.toFront();
        } else if (event.getSource() == removeButton) {
            removePane.toFront();
        } else if (event.getSource() == hearButton) {
            hearPane.toFront();
        } else if (event.getSource() == importantButton) {
            importantPane.toFront();
        } else if (event.getSource() == recentButton) {
            recentPane.toFront();
        } else if (event.getSource() == aboutButton) {
            aboutPane.toFront();
        }
    }

    @FXML
    public void switchMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Mainscene.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    Hyperlink thanh = new Hyperlink();
    @FXML
    Hyperlink than = new Hyperlink();
    @FXML
    Hyperlink huy = new Hyperlink();

    public void openBrowser(ActionEvent event) throws URISyntaxException, IOException {
        Speech speech = new Speech();
        if (event.getSource() == thanh) {
            speech.openURL(thanh.getText());
        } else if (event.getSource() == than) {
            speech.openURL(than.getText());
        } else if (event.getSource() == huy) {
            speech.openURL(huy.getText());
        }
    }


}

