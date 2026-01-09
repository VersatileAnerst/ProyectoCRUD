/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud;

import applicationcrud.ui.AccountsController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author daniel
 */
public class ApplicationCRUD extends Application {
    
    @Override
public void start(Stage stage) throws Exception {

FXMLLoader loader= new FXMLLoader(getClass().getResource("ui/Accounts.fxml"));
Parent root = (Parent)loader.load();

    AccountsController controller =loader.getController();
    controller.init(stage, root);
    
        }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
