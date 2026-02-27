package applicationcrud.ui;

import applicationcrud.logic.MovementRESTClient;
import applicationcrud.model.Account;
import applicationcrud.model.AccountType;
import applicationcrud.model.Customer;
import applicationcrud.model.Movement;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import static sun.misc.Signal.handle;

/**
 * @todo @fixme Hacer que la siguiente clase implemente las interfaces 
 * Initializable y MenuActionsHandler para que al pulsar en las acciones CRUD del 
 * menú Actions se ejecuten los métodos manejadores correspondientes a la vista 
 * que incluye el menú.
 * El método initialize debe llamar a setMenuActionsHandler() para establecer que este
 * controlador es el manejador de acciones del menú.
*/
public class MovementController {
    
    /**
     * TODO: NO TOCAR La siguiente referencia debe llamarse así y tener este tipo.
     * JavaFX asigna automáticamente el campo menuIncludeController cuando usas fx:id="menuInclude".
     */
    @FXML
    private MenuController menuIncludeController;

    @FXML private TableView<Movement> tblMovements;
    @FXML private TableColumn<Movement, Date> colDate;
    @FXML private TableColumn<Movement, String> colDescription;
    @FXML private TableColumn<Movement, Double> colAmount;
    @FXML private TableColumn<Movement, Double> colBalance;
    @FXML private Button btnDelete;
    @FXML private Label lblInfo;
    @FXML private Button btnCreate;
    @FXML private Button btnPrint;

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
        handler.onCreate();
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
        
    } catch (Exception e) {
        showError("Error en la búsqueda");
    }
}

    @FXML
     private void handleDelete() {
         
    // Obtener el elemento seleccionado por el usuario
     Movement selected = tblMovements.getSelectionModel().getSelectedItem();
    // Obtener la lista completa de la tabla
     ObservableList<Movement> allMovements = tblMovements.getItems();

    if (selected != null && !allMovements.isEmpty()) {
        // Asumiendo que la lista está ordenada por fecha descendente (el index 0 es el más nuevo)
        Movement latest = allMovements.get(0);

        // Validamos que el seleccionado sea realmente el último
        if (selected.equals(latest)) {
            try {
                String idToDelete = selected.getId().toString();
                client.remove(idToDelete); // Borrado mediante el cliente REST
                loadMovements(); // Recarga la tabla para ver los cambios
                LOGGER.info("Último movimiento eliminado correctamente.");
            } catch (Exception e) {
                showError("Error al eliminar del servidor.");
            }
        } else {
            showError("Operación no permitida: Solo puedes borrar el movimiento más reciente.");
        }
    }
}

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
    
    @FXML
    private void handleRefresh() {
        handler.onRefresh();
    
    }
    @FXML
    private void  handleUpdate(){
        handler.onUpdate();
    }
    
     @FXML
    private void handleReport(ActionEvent event){
        try {
            LOGGER.info("Beginning printing action...");
            JasperReport report=
                JasperCompileManager.compileReport(getClass()
                    .getResourceAsStream("/applicationcrud/ui/report/MovementReport.jrxml"));
            //Data for the report: a collection of UserBean passed as a JRDataSource 
            //implementation 
            JRBeanCollectionDataSource dataItems=
                    new JRBeanCollectionDataSource((Collection<Movement>)this.tblMovements.getItems());
            //Map of parameter to be passed to the report
            Map<String,Object> parameters=new HashMap<>();
            //Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,dataItems);
            //Create and show the report window. The second parameter false value makes 
            //report window not to close app.
            JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
            jasperViewer.setVisible(true);
           // jasperViewer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        } catch (JRException ex) {
            //If there is an error show message and
            //log it.
            showError("Error al imprimir:\n"+
                            ex.getMessage());
            LOGGER.log(Level.SEVERE,
                        "UI GestionUsuariosController: Error printing report: {0}",
                        ex.getMessage());
        }
    }
    
}