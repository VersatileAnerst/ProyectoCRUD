package applicationcrud.ui;

import applicationcrud.logic.MovementRESTClient;
import applicationcrud.model.Account;
import applicationcrud.model.Movement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private Stage stage;
    private Account account;
    private final MovementRESTClient client = new MovementRESTClient();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @FXML
    private void handleSave() {
        try {
            // Validaciones básicas
            if (txtDescription.getText().isEmpty() || txtAmount.getText().isEmpty()) {
                showError("Por favor, rellena todos los campos.");
                return;
            }

            // 1. Crear el objeto Movement
            Movement m = new Movement();
            m.setDescription(txtDescription.getText());
            m.setAmount(Double.parseDouble(txtAmount.getText()));
            m.setTimestamp(new Date());
            m.setAccount(this.account);
            m.setBalance(0.0); 
            String account_id = this.account.getId().toString();
            // 2. Enviar al servidor
            LOGGER.info("Enviando movimiento al servidor...");
            client.create_XML(m, account_id);

            // 3. Cerrar ventana
            cerrarVentana();

        } catch (NumberFormatException e) {
            showError("El importe debe ser un número válido.");
        } catch (ClientErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar", e);
            showError("No se pudo guardar: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        if (stage != null) {
            stage.close();
        } else {
            // Backup por si el stage no se inyectó correctamente
            Stage stageActual = (Stage) txtDescription.getScene().getWindow();
            stageActual.close();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}