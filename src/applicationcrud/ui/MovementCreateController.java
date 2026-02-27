package applicationcrud.ui;

import applicationcrud.logic.MovementRESTClient;
import applicationcrud.model.Account;
import applicationcrud.model.Movement;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;

public class MovementCreateController {

    private static final Logger LOGGER = Logger.getLogger(MovementCreateController.class.getName());

    @FXML private TextField txtDescription;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> cbType; // Nuevo campo para el FXML
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private Stage stage;
    private Account account;
    private final MovementRESTClient client = new MovementRESTClient();

    @FXML
    public void initialize() {
        // Configuramos las opciones del ComboBox
        cbType.setItems(FXCollections.observableArrayList("Deposit", "Payment"));
        cbType.getSelectionModel().selectFirst(); // Selecciona Deposit por defecto
    }

    @FXML
    private void handleSave() {
    try {
        if (txtDescription.getText().isEmpty() || txtAmount.getText().isEmpty()) {
            showError("Por favor, rellena todos los campos.");
            return; // [cite: 2]
        }

        double amount = Double.parseDouble(txtAmount.getText());
        String description = txtDescription.getText().toLowerCase();

        Movement m = new Movement();
        m.setDescription(txtDescription.getText());
        m.setTimestamp(new Date()); // [cite: 2]
        m.setAccount(this.account); // [cite: 2]

        // LÓGICA DE SALDO:
        // Si la descripción contiene palabras clave de gasto, convertimos a negativo
        if (description.contains("pago") || description.contains("reintegro") || description.contains("compra")) {
            m.setAmount(-Math.abs(amount)); // Forzamos que sea negativo (resta)
        } else {
            m.setAmount(Math.abs(amount));  // Forzamos que sea positivo (suma/depósito)
        }

        // El balance se suele calcular sumando al balance actual de la cuenta
        // m.setBalance(this.account.getBalance() + m.getAmount());

        LOGGER.info("Enviando movimiento al servidor...");
        client.create_XML(m, this.account.getId().toString()); // [cite: 2]

       

    } catch (NumberFormatException e) {
        showError("El importe debe ser un número válido."); // [cite: 2]
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error al guardar", e);
        showError("No se pudo completar la operación.");
    }
}

    @FXML
    private void handleRefresh() {
        handler.onRefresh();
    }

   

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
    }

    void setAccount(Account account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setStage(Stage stage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}