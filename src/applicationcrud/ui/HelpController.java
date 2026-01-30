/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * Controlador para mostrar la ventana de ayuda
 * @author daniel
 */
public class HelpController {
    
    @FXML
    private WebView webView;
   
    public void initAndShowStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Help for Bank's Account's");
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowing);
        stage.show();
    }
    
    private void handleWindowShowing(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        //Load help page.
        webEngine.load(getClass()
                .getResource("/applicationcrud/ui/help.html").toExternalForm());
    }
}