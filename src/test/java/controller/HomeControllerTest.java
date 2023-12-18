package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class HomeControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home-view.fxml"));
        Parent root = loader.load();
        loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    public void setUp() throws Exception {
        FxToolkit.setupStage(Stage::show);
    }

    @Test
    public void testLaunchTheGameWithEmptyUsername() {
        clickOn("#playButton");
        verifyThat("Please enter your username.", isVisible());
    }

    @Test
    public void testInitialize() {
        Button playButton = lookup("#playButton").query();
        Button scoreBoardButton = lookup("#scoreBoardButton").query();
        assertEquals("-fx-background-color: #6170ba; -fx-background-radius: 30 0 0 30;", playButton.getStyle());
        assertEquals("-fx-background-color: #6170ba; -fx-background-radius: 30 0 0 30;", scoreBoardButton.getStyle());
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}

