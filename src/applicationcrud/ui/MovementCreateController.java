package applicationcrud.ui;

import applicationcrud.logic.MovementRESTClient;
import applicationcrud.model.Account;
import applicationcrud.model.Movement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;

public class MovementCreateController {

    @FXML private TextField txtDescription;
    @FXML private TextField txtAmount;

    private Stage stage;
    private Account account;
    private final MovementRESTClient client = new MovementRESTClient();

    public void setStage(Stage stage) { this.stage = stage; }
    public void setAccount(Account account) { this.account = account; }

    @FXML
    private void handleSave() {
        try {
            // 1. Validar campos vacíos
            if (txtDescription.getText().trim().isEmpty() || txtAmount.getText().trim().isEmpty()) {
                showWarning("Validation Error", "Please fill all fields.");
                return;
            }

            // 2. Crear y configurar objeto
            Movement m = new Movement();
            m.setAccount(this.account); // Asegúrate de que tu modelo acepte el objeto Account o usa m.setAccountId
            m.setDescription(txtDescription.getText().trim());
            
            try {
                m.setAmount(Double.parseDouble(txtAmount.getText()));
            } catch (NumberFormatException e) {
                showWarning("Format Error", "Amount must be a valid number.");
                return;
            }
            
            m.setTimestamp(new Date());

            // 3. Persistencia
            client.create_XML(m);

            // 4. Éxito y cierre
            stage.close();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error saving movement: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}