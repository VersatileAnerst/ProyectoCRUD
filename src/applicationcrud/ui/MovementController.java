package applicationcrud.ui;

import applicationcrud.logic.MovementRESTClient;
import applicationcrud.model.Account;
import applicationcrud.model.Movement;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

public class MovementController {

    @FXML private TableView<Movement> tblMovements;
    @FXML private TableColumn<Movement, Date> colDate;
    @FXML private TableColumn<Movement, String> colDescription;
    @FXML private TableColumn<Movement, Double> colAmount;
    @FXML private TableColumn<Movement, Double> colBalance;
    @FXML private Button btnDelete;
    @FXML private Label lblInfo;
    @FXML private Button btnCreate;

    private Account account;
    private final MovementRESTClient client = new MovementRESTClient();
    private static final Logger LOGGER = Logger.getLogger("applicationcrud.ui");

    public void init(Stage stage, Parent root) {
        try {
            LOGGER.info("Initializing Movement window.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("BankApp - Movements");
            stage.setResizable(false);

            // Configuración de la tabla
            configureTableColumns();
            configureTableSelection();
            
            //Asociar Eventos A Manejadores
            //Estan en el fxml
            //btnDelete.setOnAction(this::handleDelete);
            

            stage.show();
            LOGGER.info("Movement window initialized");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening window", e);
            showError("Error Opening Window: " + e.getLocalizedMessage());
        }
    }

    private void configureTableColumns() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        tblMovements.setEditable(false);
    }

    private void configureTableSelection() {
        tblMovements.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblMovements.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> btnDelete.setDisable(newVal == null)
        );
    }

    public void setAccount(Account account) {
        this.account = account;
        loadMovements(); // Centralizamos la carga de datos
    }

    public void loadMovements() {
    if (account == null) return;
    try {
        LOGGER.info("Loading movements for account: " + account.getId());
        GenericType<List<Movement>> gt = new GenericType<List<Movement>>() {};
        List<Movement> movements = client.findMovementByAccount_XML(gt, account.getId().toString());

        ObservableList<Movement> data = FXCollections.observableArrayList(movements);
        
        // Ordenar por fecha (más reciente arriba suele ser mejor)
        data.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));

        // Actualizar UI
        tblMovements.setItems(null); // Limpiamos la referencia antigua
        tblMovements.setItems(data); // Inyectamos la lista nueva
        tblMovements.refresh();      // Forzamos el renderizado de celdas
        
        lblInfo.setText("Account: " + account.getId() + " - " + data.size() + " movements found.");
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error loading movements", e);
        showError("Could not load movements from server. Check connection.");
    }
}

    @FXML
    private void handleCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/applicationcrud/ui/MovementCreate.fxml"));
            Parent root = loader.load();
            
            MovementCreateController createController = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            
            createController.setAccount(this.account);
            createController.setStage(stage);
            
            stage.setScene(new Scene(root));
            stage.showAndWait(); 

            loadMovements(); // Refresca tras cerrar la ventana
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening creation window", e);
            showError("Could not open the creation window.");
        }
    }
    @FXML
    private void handleSearch() { 
    try {
        // ... tu código ...
    } catch (Exception e) {
        showError("Error en la búsqueda");
    }
}

    @FXML
    private void handleDelete() {
        Movement selected = tblMovements.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Lógica de borrado (ajustar según tu API)
            String seleccion = selected.getId().toString();
            client.remove(seleccion);
            loadMovements();
            LOGGER.info("Movement deleted.");
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}