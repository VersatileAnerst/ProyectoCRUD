/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;

import applicationcrud.logic.AccountRESTClient;
import applicationcrud.model.Account;
import applicationcrud.model.Customer;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Daniel
 */
public class AccountsController {

    @FXML
    private TableView<Account> tblAccounts;
    @FXML
    private TableColumn<Account, Long> colId;
    @FXML
    private TableColumn<Account, String> colDescription;
    @FXML
    private TableColumn<Account, Double> colBalance;
    @FXML
    private TableColumn<Account, Double> colCreditLine;
    @FXML
    private TableColumn<Account, Double> colBeginBalance;
    @FXML
    private TableColumn<Account, String> colType;
    @FXML
    private TableColumn<Account, Date> colDate;
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
    @FXML
    private Button btMovement;

    private Stage stage;

    private Customer customer;
    
    private AccountRESTClient client = new AccountRESTClient();
        
    private static final Logger LOGGER = Logger.getLogger("applicationcrud.ui");

    public void init(Stage stage, Parent root) {
        try {
            LOGGER.info("Initializing Account window.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //Establecer el titulo de la ventana en "Bank App"
            stage.setTitle("BankApp");
            //Ventana no redimensionable
            stage.setResizable(false);
            //Factorias para el valor de celda
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
            colCreditLine.setCellValueFactory(new PropertyValueFactory<>("creditLine"));
            colBeginBalance.setCellValueFactory(new PropertyValueFactory<>("beginBalance"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            
            //Seleccion en Tabla
            tblAccounts.getSelectionModel().selectedItemProperty()
                    .addListener(this::handleAccountsTableSelectionChanged);
            //Asociar Eventos A Manejadores
            btMovement.setOnAction(this::handleBtMovementOnAction);
            btExit.setOnAction(this::handleBtExitOnAction);
            // Botones deshabilitados
            btUpdate.setDisable(true);
            btDelete.setDisable(true);
            btMovement.setDisable(true);

            //Mostrar la ventana
            stage.show();
            LOGGER.info("Account window initialized");
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Opening Window: " + e.getLocalizedMessage())
                    .showAndWait();
            }
    }
    
    /**
     * Este metodo establece el customer del signIn y carga los datos de las cuentas
     * @param customer 
     */
    public void setCustomer(Customer customer) {
        try{
        this.customer = customer;
        //Carga de datos a las columnas
            tblAccounts.setItems(FXCollections.observableArrayList(
            client.findAccountsByCustomerId_XML(new GenericType<List<Account>>() {}, customer.getId().toString())));
            LOGGER.info("Table Accounts Uploaded");
        }catch(Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR,
                            "No se ha podido cargar los datos:"+
                             e.getMessage(),
                             ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Este metodo actualiza la vista de la tabla
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleAccountsTableSelectionChanged(ObservableValue observable,
                                                     Object oldValue,
                                                     Object newValue) {

        if (newValue != null) {
            
            btUpdate.setDisable(false);
            btDelete.setDisable(false);
            btMovement.setDisable(false);
        }
    }
    /**
     * Este metodo maneja la accion del boton exit
     * @param event 
     */
    private void handleBtExitOnAction(ActionEvent event){
        try{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
                "Are you sure you want to Sign Out", 
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Exit");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Stage stage = (Stage) btExit.getScene().getWindow();
            stage.close();
        }
        }catch(Exception e){
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Error Signing Out: " + e.getLocalizedMessage())
                 .showAndWait();
        }
    }
    
    private void handleBtPostOnAction(){
        try{
        tblAccounts.refresh();
        LOGGER.info("Table Account Updated");
        }catch (InternalServerErrorException se) {//Captura los errores 500
            LOGGER.warning(se.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Internal server error").showAndWait();    
        }catch(Exception e){
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error creating new Account:" + e.getLocalizedMessage())
                    .showAndWait();
        }
        
    }
    @FXML
    private void handleBtUpdateOnAction(ActionEvent event){
        try{
            tblAccounts.refresh();
            LOGGER.info("Table Account Updated");
        }catch (InternalServerErrorException se) {//Captura los errores 500
            LOGGER.warning(se.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Internal server error").showAndWait();   
        }catch(Exception e){
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Updating Account:" + e.getLocalizedMessage())
                    .showAndWait();
        }
    }
    /**
     * Este metodo maneja la accion del Boton Delete
     * @param event 
     */
    @FXML
    private void handleBtDeleteOnAction(ActionEvent event){
        try{
        //Guarda la cuenta seleccionada en una variable
        Account selectedAccount =
                tblAccounts.getSelectionModel().getSelectedItem();
        //Elimina ola cuenta seleccionada
        tblAccounts.getItems().remove(selectedAccount);
        LOGGER.info("Account Removed");
        //Refresca la tabla
        tblAccounts.refresh();
        LOGGER.info("Table Account Updated");
        }catch(Exception e){
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Deleting Account:" + e.getLocalizedMessage())
                    .showAndWait();
        }
    }
    @FXML
    private void handleBtMovementOnAction(ActionEvent event){
        try{
        //Selecciona una cuenta y la guarda en una variable
        Account selectedAccount =
                tblAccounts.getSelectionModel().getSelectedItem();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Movement.fxml"));
        Parent root = (Parent)loader.load();
        MovementController controller =loader.getController();
        //Crea nueva ventana
        Stage movementStage = new Stage(); 
        
        controller.init(movementStage, root);
        //le pasa la cuenta seleccionada al controlador de Movement
        controller.setAccount(selectedAccount);
        movementStage.show();
        LOGGER.info("Movement Windonw initialized");
        }catch (InternalServerErrorException se) {//Captura los errores 500
            LOGGER.warning(se.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Internal server error").showAndWait();
        }catch (Exception e){//Captura el resto de excepciones
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Movement Button error").showAndWait();
        }
        
    }

}
