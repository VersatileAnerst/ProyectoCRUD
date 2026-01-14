/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;
import applicationcrud.logic.CustomerRESTClient;
import applicationcrud.model.Customer;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
/**
 *
 * @author daniel
 */
public class Sign_InController {
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btExit;
    @FXML
    private Button btSignIn;
    @FXML
    private Label lbError;
    
    private String customerUsername;
    
    private String customerPassword;
    
    private Stage stage;
    
    private static final Logger LOGGER=Logger.getLogger("applicationdeisignupsignin.ui");
    
    
    
    public void init(Stage stage, Parent root) {
        try{
        //Establece el stage
        this.stage = stage;
        LOGGER.info("Initializing Sign In window.");
        Scene scene =new Scene(root);   
        stage.setScene(scene);
        //Establecer el titulo de la ventana en "Bank App"
        stage.setTitle("BankApp");
        //Ventana no redimensionable
        stage.setResizable(false);
        //El foco debe estar en el Email
        tfEmail.requestFocus();
        //*asociar eventos a manejadores
        btSignIn.setOnAction(this::handleBtSignInOnAction);
        btExit.setOnAction(this::handleBtExitOnAction);
        
        //Establecer el botón de Sign In como defaultButton.
        btSignIn.setDefaultButton(true);
        //Establecer el botón de Exit como cancelButton. 
        btExit.setCancelButton(true);
        
        //para que el al cerrar la ventana pide confirmacion
        stage.setOnCloseRequest(this::handleBtExitOnAction);
        
        //Cuando presionas enter en password realiza el sign in
        pfPassword.setOnAction(this::handleBtSignInOnAction);
        //Asociación de manejadores a properties
        tfEmail.textProperty().addListener(this::handleTfEmailTextChange);
        //tfEmail.focusedProperty().addListener(this::handleTfEmailFocusChange);
        pfPassword.textProperty().addListener(this::handlePfPasswordTextChange);
        checkFields(); 

        //Mostrar la ventana 
        stage.show();
        LOGGER.info("Sign In window initialized");
        }catch(Exception e){
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Error Opening Window: " + e.getLocalizedMessage())
                 .showAndWait();
        }
    }
    /**
     * Este metodo sirve para comprobas si se ha cambido el texto en TextField Email
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleTfEmailTextChange(ObservableValue observable,
            String oldValue, String newValue){
        try{
        String username = tfEmail.getText().trim();

    if (this.tfEmail.getText().trim().equals("")) {
            
         lbError.setText("User Field Empty");
        return;
    }else{
        lbError.setText("");
    }
        //Invoca el metodo que habilita el boton si ambos campos estan informados
            checkFields();
        }catch(Exception e){
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Error Changing Username(Email): " + e.getLocalizedMessage())
                 .showAndWait();
        }
    }
    /**
     * Este metodo sirve para enfocar el texto Tfusername
     * @param observable
     * @param oldvalue
     * @param newValue 
     */
    private void handleTfEmailFocusChange(ObservableValue observable,
            Boolean oldValue, Boolean newValue){
        try{
        if(oldValue){
            
        }else{
            
        }
        }catch(Exception e){
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Error Focusing User: " + e.getLocalizedMessage())
                 .showAndWait();
        }
    }
    /**
     * Este metodo sirve para comprobar si se el PfPassword esta completo
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handlePfPasswordTextChange(ObservableValue observable,
                                        String oldValue,
                                        String newValue) {
    try{
    if (this.pfPassword.getText().trim().equals("")) {
        lbError.setText("Password Field Empty");
        return;
    }else{
        lbError.setText("");
    }
    //Invoca el metodo que habilita el boton si ambos campos estan informados
        checkFields();
    }catch(Exception e){
        LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Error Changing Password: " + e.getLocalizedMessage())
                 .showAndWait();
    }
   }/**
    * Este metodo comprueba que ambos campos estan informados y deshabilita si hay un campo vacio
    * 
    */
    private void checkFields() {
    boolean disable = tfEmail.getText().trim().isEmpty() 
            || pfPassword.getText().trim().isEmpty();
    btSignIn.setDisable(disable);
}
    /**
     * Este metodo maneja la accion del Boton Exit
     * @param event 
     */
    private void handleBtExitOnAction(Event event){
        //Se cambia de ActionEvent a Event para que el OnclosedRequest lo maneje
        try{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
                "Are you sure you want to close the app?", 
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
                 "Error Exiting Application: " + e.getLocalizedMessage())
                 .showAndWait();
        }
    }
    
    /**
     * Este metodo maneja la accion del Boton Sign In
     * @param event 
     */
    private void handleBtSignInOnAction(ActionEvent event){
        CustomerRESTClient client = new CustomerRESTClient();
        try{
        //Comprueba que la contraseña es de mas de 8 caracteres
        if (pfPassword.getText().trim().length() <= 8) {
             lbError.setText("Password need to be at least 8 characthers");
             return;
        }else{
            lbError.setText("");
        }
        if (!tfEmail.getText().trim().contains("@") && 
                !tfEmail.getText().trim().contains(".")){
            lbError.setText("Username(Email) need to contains @ and .");
             return;
        }else{
            lbError.setText("");
        }
        
        //Creo dos variables String para guardar el username y password
        customerUsername = new String(tfEmail.getText().trim());
        customerPassword = new String(pfPassword.getText().trim());
        
        //Utilizo la funcion find XML para iniciar sesion con el cliente
        Customer customer = client.findCustomerByEmailPassword_XML(Customer.class, customerUsername, customerPassword);
        LOGGER.info("Customer Signing In Succesfull.");
        
        //Abro la ventana Result si el Cliente inicia sesion
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Accounts.fxml"));
        Parent root = (Parent)loader.load();
        AccountsController controller =loader.getController();
        controller.init(stage, root);
        controller.setCustomer(customer);
        
        stage.show();//Abre la nueva ventana
        
        LOGGER.info("Accounts window initialized");
        
        }catch (NotAuthorizedException ne) {//Captura el error 403 
            LOGGER.warning(ne.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Incorrect Email or Password").showAndWait();
        } catch (InternalServerErrorException se) {//Captura los errores 500
            LOGGER.warning(se.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Internal server error").showAndWait();
        }catch (Exception e){//Captura el resto de excepciones
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Sign In error").showAndWait();
        }
    }    
}