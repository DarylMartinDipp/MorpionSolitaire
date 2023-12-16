

import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;



public class homeController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private  Player player;
    private  String mode;
    @FXML
    private Button PlayButtonn;
    @FXML
    private ComboBox<String> gameModeOptions;

    @FXML
    private Button historyButton;

    @FXML
    private AnchorPane  scene1AnchorPane;

    @FXML
    private TextField usernameField;

    private static final double CELL_SIZE = 30;


    public homeController() {

    }


    @FXML
    public void OnSwitch2Click(ActionEvent event) throws IOException {
        player= new Player(homeController.setName(usernameField));
        setupOptions();
        GameManager gm = new GameManager();
        gm.initBoard(mode);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MorpionSolitaire-view.fxml"));
        Parent root = loader.load();
        MorpionSolitaireController mpc = loader.getController();

        mpc.start();



        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void displayHistory() {

    }
    @FXML
    private void gameModeChanged(){
        if(gameModeOptions.getSelectionModel().getSelectedItem()=="5D")
        {
            mode="5T";
        }
        else if(gameModeOptions.getSelectionModel().getSelectedItem()=="5T")
        {
            mode="5D";
        }

    }


    private void setupOptions() {
        //canvasView.setTheme(GameCanvasView.DARK_THEME);
        //themeOptions.getParent().getScene().getRoot().setStyle("-fx-base:black");
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5T", "5D");
        gameModeOptions.getSelectionModel().select("5D");

    }

    public void start()
    {
        setupOptions();
        reset();

}



    private void reset() {

    }

    public static String setName(TextField usernameField){
        String username = usernameField.getText().trim();
        return username;
    }
}