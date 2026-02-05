/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;

import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author daniel
 */
public class MenuController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuAction;
    @FXML
    private Menu menuHelp;
    @FXML
    private Menu menuAbout;
    @FXML
    private MenuItem menuItemLogout;
    @FXML
    private MenuItem menuItemHelp;
    @FXML
    private MenuItem menuItemAbout;

    private Stage stage;

    private static final Logger LOGGER = Logger.getLogger("applicationcrud.ui");

    public void init(Stage stage, Parent root) {
        this.stage = stage;
        try {
            //Handlers de los menu items
            menuItemLogout.setOnAction(e -> handleLogout());
            menuItemHelp.setOnAction(e -> handleHelp());
            menuItemAbout.setOnAction(e -> handleAbout());
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Opening Window: " + e.getLocalizedMessage())
                    .showAndWait();
        }
    }

    /**
     * Metodo para la accion de menu de cierre de sesion mediante Alert
     *
     * @param event
     */
    private void handleLogout() {
        LOGGER.info("Logout clicked");
        //Cierra la ventana
        stage.close();
    }

    /**
     * Metodo para la accion de ayuda de la ventana mediante Alert
     *
     * @param event
     */
    private void handleHelp() {
        LOGGER.info("Help clicked");
        try {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            java.net.URL url = getClass().getResource("help.html");
            if (url != null) {
                webEngine.load(url.toExternalForm());
                //Crea un nuevo stage
                Stage stageHelp = new Stage();
                stageHelp.setTitle("Ayuda del sistema");
                StackPane root = new StackPane(webView);
                Scene scene = new Scene(root, 800, 600);
                stageHelp.setScene(scene);
                stageHelp.show();
            }
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Menu Help: " + e.getLocalizedMessage())
                    .showAndWait();
        }
    }

    /**
     * Metodo para la accion de about/informacion de app mediante Alert
     *
     * @param event
     */
    private void handleAbout() {
        LOGGER.info("About clicked");
        Stage stageAbout = new Stage();
        Label label = new Label("BankApp v1.0\nAuthor: Jimmy y Daniel");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Scene scene = new Scene(root, 400, 100);
        stageAbout.setScene(scene);
        stageAbout.show();
    }

}
