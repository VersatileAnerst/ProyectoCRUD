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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author Daniel
 */
public class AccountsController {

    @FXML
    private TableView<Account> tAccounts;
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
    
    private AccountRESTClient accountClient;
    
    private ObservableList<Account> accountsData;

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
            //Listener
            tAccounts.getSelectionModel().selectedItemProperty()
                    .addListener(this::handleAccountsTableSelectionChanged);

            //Establecer el botón de Exit como cancelButton. 
            btExit.setCancelButton(true);
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
        loadTestData();
    }

    private void loadAccounts() {
        try {
            accountClient = new AccountRESTClient();

            List<Account> accountList =
                    accountClient.findAccountsByCustomerId_XML(
                            List.class,
                            customer.getId().toString());

            accountsData = FXCollections.observableArrayList(accountList);

            tAccounts.setItems(accountsData);
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error loading Accounts: " + e.getLocalizedMessage())
                    .showAndWait();
        }
    }
    private void handleAccountsTableSelectionChanged(ObservableValue observable,
                                                     Object oldValue,
                                                     Object newValue) {

        if (newValue != null) {
            btUpdate.setDisable(false);
            btDelete.setDisable(false);
            btMovement.setDisable(false);
        }
    }
    private void loadTestData() {
    accountsData = FXCollections.observableArrayList();

    Account a = new Account();
    a.setId(1L);
    a.setDescription("Account 1 Test");
    a.setBalance(1000.0);
    a.setBeginBalance(1000.0);
    a.setCreditLine(0.0); // Cuenta estándar
    a.setType(applicationcrud.model.AccountType.STANDARD);
    a.setBeginBalanceTimestamp(new java.util.Date());

    accountsData.add(a);
 
    tAccounts.setItems(accountsData);
}

}
