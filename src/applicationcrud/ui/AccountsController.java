/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;

import applicationcrud.logic.AccountRESTClient;
import applicationcrud.model.Account;
import applicationcrud.model.AccountType;
import applicationcrud.model.Customer;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Daniel
 * @todo @fixme Hacer que la siguiente clase implemente las interfaces 
 * Initializable y MenuActionsHandler para que al pulsar en las acciones CRUD del 
 * menú Actions se ejecuten los métodos manejadores correspondientes a la vista 
 * que incluye el menú.
 * El método initialize debe llamar a setMenuActionsHandler() para establecer que este
 * controlador es el manejador de acciones del menú.
 */
public class AccountsController implements Initializable, MenuActionsHandler{
    /**
     * TODO: NO TOCAR La siguiente referencia debe llamarse así y tener este tipo.
     * JavaFX asigna automáticamente el campo menuIncludeController cuando usas fx:id="menuInclude".
     */
    @FXML
    private MenuController menuIncludeController;

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
    private TableColumn<Account, AccountType> colType;
    @FXML
    private TableColumn<Account, Date> colDate;
    @FXML
    private MenuController menuController;
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
    
    private Handler handler;

    private Customer customer;
    
    private AccountRESTClient client = new AccountRESTClient();

    private static final Logger LOGGER = Logger.getLogger("applicationcrud.ui");

    public void init(Stage stage, Parent root) {
        try {
            LOGGER.info("Initializing Account window.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //Ventana Modal
            stage.initModality(Modality.APPLICATION_MODAL);
            //Establecer el titulo de la ventana en "Bank App"
            stage.setTitle("BankApp");
            //Ventana no redimensionable
            stage.setResizable(false);
            //Controlador de menu
            menuController.init(stage, root);
            //Tabla editable
            tblAccounts.setEditable(true);
            //Factorias para el valor de celda
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
            colCreditLine.setCellValueFactory(new PropertyValueFactory<>("creditLine"));
            colBeginBalance.setCellValueFactory(new PropertyValueFactory<>("beginBalance"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("beginBalanceTimestamp"));
            //Factorias de Celda
            //Descripcion
            colDescription.setCellFactory(TextFieldTableCell.<Account>forTableColumn());
            colDescription.setOnEditCommit(this::handleDescriptionEdit);
            //BeginBalance
            colBeginBalance.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            colBeginBalance.setOnEditCommit(this::handleBeginBalance);
            //CreditLine
            colCreditLine.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            colCreditLine.setOnEditCommit(this::handleCreditLineEdit);
            //Tipo de cuenta
            colType.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<AccountType>() {
                @Override
                public String toString(AccountType type) {
                    return type == null ? "" : type.name();
                }

                @Override
                public AccountType fromString(String string) {
                    return AccountType.valueOf(string);
                }
            }, AccountType.values()));
            colType.setOnEditCommit(this::handleTypeEdit);
            //Seleccion en Tabla
            tblAccounts.getSelectionModel().selectedItemProperty()
                    .addListener(this::handleAccountsTableSelectionChanged);
            //Asociar Eventos A Manejadores
            btPost.setOnAction(this::handleBtPostOnAction);
            btUpdate.setOnAction(this::handleBtUpdateOnAction);
            btDelete.setOnAction(this::handleBtDeleteOnAction);
            btMovement.setOnAction(this::handleBtMovementOnAction);
            btExit.setOnAction(this::handleBtExitOnAction);

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
     * Este metodo establece el customer del signIn y carga los datos de las
     * cuentas
     *
     * @param customer
     */
    public void setCustomer(Customer customer) {
        try {
            this.customer = customer;
            //Carga de datos a las columnas
            tblAccounts.setItems(FXCollections.observableArrayList(
                    client.findAccountsByCustomerId_XML(new GenericType<List<Account>>() {
                    }, customer.getId().toString())));
            LOGGER.info("Table Accounts Uploaded");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Accounts data cannot be charged:"
                    + e.getMessage(),
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Este metodo actualiza la vista de la tabla
     *
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
     * Metodo que maneja la accion de la celda Description
     *
     * @param event
     */
    private void handleDescriptionEdit(CellEditEvent<Account, String> event) {
        Account account = event.getRowValue();
        //Para la descripcion
        account.setDescription(event.getNewValue());
        //Guarda la descripcion en la base de datos
        client.updateAccount_XML(account);
        LOGGER.info("Description Updated");
    }

    /**
     * Metodo que maneja la edicion de celda de Credit line
     *
     * @param event
     */
    private void handleCreditLineEdit(CellEditEvent<Account, Double> event) {
        Account account = event.getRowValue();
        //Si el type es Credit se puede editar 
        if (account.getType() == AccountType.CREDIT) {
            //Actualiza en la tabla
            account.setCreditLine(event.getNewValue());
            //Actualiza en la base de atos
            client.updateAccount_XML(account);
            lbMessage.setText("");
        } else {
            //Si no es type Credit la tabla se refresca y muestra un mensaje
            event.getTableView().refresh();
            LOGGER.info("Table Updated");
            lbMessage.setText("Only Credit Accounts can edit CreditLine");
        }
    }

    /**
     * Metodo que maneja la edicion de la celda begin balance solo al crear
     * nueva cuenta
     *
     * @param event
     */
    private void handleBeginBalance(CellEditEvent<Account, Double> event) {
        Account account = event.getRowValue();
        Double newBeginBalance = event.getNewValue();
        if (account.getBeginBalance() != null) {
            event.getTableView().refresh();
            lbMessage.setText("Begin Balance is not editable");
            return;
        }
        if (newBeginBalance == null) {
            event.getTableView().refresh();
            return;
        }

        // Establece el beginBalance y sincroniza el balance actual
        account.setBeginBalance(newBeginBalance);
        account.setBalance(newBeginBalance);

        client.updateAccount_XML(account);

        LOGGER.info("BeginBalance established");
    }

    /**
     * Metodo que maneja la edicion de celda de AccountType
     *
     * @param event
     */
    private void handleTypeEdit(CellEditEvent<Account, AccountType> event) {
        Account account = event.getRowValue();
        account.setType(event.getNewValue());

        //Lo guarda en la base de datos
        client.updateAccount_XML(account);

        LOGGER.info("Account Type Updated");
    }

    /**
     * Este metodo maneja la accion del boton exit
     *
     * @param event
     */
    @FXML
    private void handleBtExitOnAction(ActionEvent event) {
        try {
            //Alerta al salir
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to Sign Out",
                    ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirm Exit");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Stage stage = (Stage) btExit.getScene().getWindow();
                stage.close();
            }
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Signing Out Accounts Window: " + e.getLocalizedMessage())
                    .showAndWait();
        }
    }

    /**
     * Maneja la accion del boton Post
     *
     * @param event
     */
    @FXML
    private void handleBtPostOnAction(ActionEvent event) {
        try {
            //Creo una variable
            Account newAccount = new Account();
            Random id = new Random();
            //Genera ID aleatorio de 10 cifras
            long random = (long) (Math.random() * 900000000L) + 1000000000L;
            newAccount.setId(random);
            //Establezco Valores por defecto
            newAccount.setBeginBalanceTimestamp(new Date());
            newAccount.setType(AccountType.STANDARD);
            newAccount.setCreditLine(0.0);
            newAccount.setDescription("");
            //Creo un hash set y añado el customer para que se asocie a la cuenta
            Set<Customer> customers = new HashSet<>();
            customers.add(customer);
            newAccount.setCustomers(customers);
            //Anadir a la tabla
            tblAccounts.getItems().add(newAccount);
            tblAccounts.getSelectionModel().select(newAccount);
            //Funcion del RESTClient Crea la cuenta
            client.createAccount_XML(newAccount);
            tblAccounts.refresh();
            LOGGER.info("Table Account Updated");
        } catch (InternalServerErrorException se) {//Captura los errores 500
            LOGGER.warning(se.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Internal server error").showAndWait();
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error creating new Account:" + e.getLocalizedMessage())
                    .showAndWait();
        }

    }
   
    /**
     * Maneja la accion del boton Update
     *
     * @param event
     */
    @FXML
    private void handleBtUpdateOnAction(ActionEvent event) {
        try {
            Account selectedAccount
                    = tblAccounts.getSelectionModel().getSelectedItem();
            if (selectedAccount == null) {
                lbMessage.setText("Select an Account");
                return;
            }
            //Usa una funcion del AccountRESTClient
            client.updateAccount_XML(selectedAccount);

            tblAccounts.refresh();
            LOGGER.info("Table Account Updated");
        } catch (InternalServerErrorException se) {//Captura los errores 500
            LOGGER.warning(se.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Internal server error").showAndWait();
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Updating Account:" + e.getLocalizedMessage())
                    .showAndWait();
        }
    }

    /**
     * Este metodo maneja la accion del Boton Delete
     *
     * @param event
     */
    @FXML
    private void handleBtDeleteOnAction(ActionEvent event) {
        try {
            //Guarda la cuenta seleccionada en una variable
            Account selectedAccount
                    = tblAccounts.getSelectionModel().getSelectedItem();
            if (selectedAccount == null) {
                lbMessage.setText("Select an Account");
                return;
            }
            //Alerta para estar seguro de eliminar
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this account?",
                    ButtonType.YES,
                    ButtonType.NO
            );
            alert.setTitle("Confirm Delete");
            alert.showAndWait();
            if (alert.getResult() != ButtonType.YES) {
                LOGGER.info("Account not deleted Action canceled");
                return;
            }
            //Elimina la cuenta seleccionada si no tiene ningun movimiento
            if (selectedAccount.getMovements() != null && !selectedAccount.getMovements().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, 
                        "This account has movements and cannot be deleted",
                        ButtonType.OK).showAndWait();
            } else {
                //Llama a una funcion del AccountRestClient
                client.removeAccount(selectedAccount.getId().toString());
                //Borro la fila de la tabla
                tblAccounts.getItems().remove(selectedAccount);
                tblAccounts.refresh();

                LOGGER.info("Account Deleted");
            }
        } catch (ClientErrorException e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Deleting Account:" + e.getLocalizedMessage())
                    .showAndWait();
        }
    }

    /**
     * Este metodo maneja la accion del boton Movement que abre una nueva
     * ventana
     *
     * @param event
     */
    @FXML
    private void handleBtMovementOnAction(ActionEvent event) {
        try {
            //Selecciona una cuenta y la guarda en una variable
            Account selectedAccount
                    = tblAccounts.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Movement.fxml"));
            Parent root = (Parent) loader.load();
            MovementController controller = loader.getController();
            //Crea nueva ventana
            Stage movementStage = new Stage();

            controller.init(movementStage, root);
            //Le pasa la cuenta seleccionada al controlador de Movement
            controller.setAccount(selectedAccount);
            movementStage.show();
            LOGGER.info("Movement Window initialized");
        } catch (InternalServerErrorException se) {//Captura los errores 500
            LOGGER.warning(se.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Internal server error").showAndWait();
        } catch (Exception e) {//Captura el resto de excepciones
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Movement Button error").showAndWait();
        }

    }

    @Override
    public void onCreate() {
        ActionEvent event = null;
        handleBtPostOnAction(event);
    }

    @Override
    public void onRefresh() {
        setCustomer(customer);
    }

    @Override
    public void onUpdate() {
        ActionEvent event = null;
        handleBtUpdateOnAction(event);
    }

    @Override
    public void onDelete() {
        ActionEvent event = null;
        handleBtDeleteOnAction(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMenuActionsHandler();
    }


}
