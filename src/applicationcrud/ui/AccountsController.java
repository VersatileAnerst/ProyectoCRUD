/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;

import applicationcrud.model.Customer;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 *
 * @author Daniel
 */
public class AccountsController {
    @FXML
    private TableView tAccounts;
    @FXML
    private TableColumn colDescription;
    @FXML
    private TableColumn colBalance;
    @FXML
    private TableColumn colType;
    @FXML
    private Label lbMessage;
    @FXML
    private Button btExit;
    @FXML
    private Button btPost;
    @FXML
    private Button btUpdate;
    @FXML
    private Button btDelete;
    
    private Stage stage;
    
    private Customer customer;
    
    private static final Logger LOGGER=Logger.getLogger("applicationcrud.ui");
    
    public void init(Stage stage, Parent root) {
        try{
        LOGGER.info("Initializing Account window.");
        Scene scene =new Scene(root);   
        stage.setScene(scene);
        //Establecer el titulo de la ventana en "Bank App"
        stage.setTitle("BankApp");
        //Ventana no redimensionable
        stage.setResizable(false);
        //Asociación de manejadores a properties
        
        //Mostrar la ventana
        stage.show();
        LOGGER.info("Account window initialized");
        }catch(Exception e){
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Error Opening Window: " + e.getLocalizedMessage())
                 .showAndWait();
        }
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
        lbMessage.setText("Welcome " + customer.getEmail());
        
    }


}
