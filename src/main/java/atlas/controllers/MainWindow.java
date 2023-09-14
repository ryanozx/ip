package atlas.controllers;

import java.util.Objects;

import atlas.Atlas;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Atlas's GUI
 */
public class MainWindow extends AnchorPane {
    static final String USER_PROFILE_IMG_PATH = "/images/user.png";
    static final String ATLAS_PROFILE_IMG_PATH = "/images/atlas.png";

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Atlas atlas;

    private final Image userImage = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream(
            USER_PROFILE_IMG_PATH)));
    private final Image atlasImage = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream(
            ATLAS_PROFILE_IMG_PATH)));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setAtlas(Atlas a) {
        atlas = a;
        dialogContainer.getChildren().add(
                DialogBox.getAtlasDialog(a.getWelcome(), atlasImage)
        );
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = atlas.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getAtlasDialog(response, atlasImage)
        );
        userInput.clear();
    }
}
