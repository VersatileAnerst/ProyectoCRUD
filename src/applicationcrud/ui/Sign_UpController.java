/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package applicationcrud.ui;

import applicationcrud.logic.CustomerRESTClient;
import applicationcrud.model.Customer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ForbiddenException;

/**
 *
 * @author jimmy
 */
public class Sign_UpController {
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfMiddlelnitial;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfConfirmPassword;
    @FXML
    private TextField tfCity;
    @FXML
    private TextField tfState;
    @FXML
    private TextField tfStreet;
    @FXML
    private TextField tfZip;
    @FXML
    private Hyperlink hySignIn; 
    @FXML
     Button btSignUp;
    @FXML
    private Button btBack;
    
    private static final Logger LOGGER=Logger.getLogger("applicationdeisignupsignin.ui");
    
    private Stage stage;
    

    public void init(Stage stage, Parent root) {
        this.stage = stage;
        LOGGER.info("Initializing login stage");
            Scene scene = new Scene(root);

             stage.setScene(scene);
             stage.setTitle("login");//customer
             stage.setResizable(false);
             stage.setOnShowing(this::handleWindowShowing);
           
        // ventana sin texto 
         //clearFields();
         //asociar eventos a manejadores
         btBack.setOnAction(this ::handlebtBackOnAction);
         btSignUp.setOnAction(this ::handlebtSignUpAction);
        hySignIn.setOnAction(this::handleHySignInOnAction);

         //asociacion de manejadores a properties
         
         tfFirstName.textProperty().addListener(this::handletfFirstNameTextChange);
         tfFirstName.focusedProperty().addListener(this::handletfFirstNameFocusChange);
         tfLastName.textProperty().addListener(this::handletfLastNameTextChange);
         tfPhone.textProperty().addListener(this::handletfPhoneTextChange);
         tfEmail.textProperty().addListener(this::handletEmailTextChange);
         tfCity.textProperty().addListener(this::handletfCityTextChange);
         tfStreet.textProperty().addListener(this::handletfStreetTextChange);
         tfZip.textProperty().addListener(this::handletfZipTextChange);
         pfPassword.textProperty().addListener(this::validateForm);
         pfConfirmPassword.textProperty().addListener(this::validateForm);
    
         stage.show();
         //Deshabilita el boton signUp
         btSignUp.setDisable(true);
         //Enfoca el campo FirstName
         tfFirstName.requestFocus();
         }
    private void handleWindowShowing(WindowEvent event){
        LOGGER.info("Beggin LoginController::handleWindowShowing");
        btSignUp.setDisable(true);
        
    }
    private void clearFields() {
        tfFirstName.clear();
        tfLastName.clear();
        tfMiddlelnitial.clear();
        tfPhone.clear();
        tfEmail.clear();
        pfPassword.clear();
        pfConfirmPassword.clear();
        tfCity.clear();
        tfStreet.clear();
        tfZip.clear();
        
   
    }
    protected void showErrorAlert(String errorMsg){
        //Shows error dialog.
        Alert alert=new Alert(Alert.AlertType.ERROR,
                              errorMsg,
                              ButtonType.OK);
        alert.showAndWait();
}   //validaciones nuevas .......



    /**
     * 
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handletfFirstNameTextChange( ObservableValue observable ,
                                                String oldValue,
                                                String newValue){
        
        if (newValue.length() > 255) {
            tfFirstName.setText(oldValue);
            showErrorAlert("El nombre de usuario supera el numero maximo de caracteres.");           
      
    
    }}
    /**
     * 
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handletfFirstNameFocusChange(ObservableValue observable,
            Boolean oldValue, Boolean newValue){
        if (oldValue ){
            if (!newValue) { // perdió el foco
            if (tfFirstName.getText().trim().isEmpty()) {
                tfFirstName.setStyle("-fx-border-color: red;");
            } else {
                tfFirstName.setStyle(null);
            }
        }
            
        }
    }
    private void handletfLastNameTextChange( ObservableValue observable ,
                                                String oldValue,
                                                String newValue){
        if (newValue.length() > 255) {
            tfLastName.setText(oldValue);
            showErrorAlert("El apellido de usuario supera el numero maximo de caracte");
    }
    
    }
    private void handletfPhoneTextChange( ObservableValue observable ,
                                                String oldValue,
                                                String newValue){
        if (newValue.length() > 20) {
            tfPhone.setText(oldValue);
            showErrorAlert("El telefono usuario supera el numero maximo de caracteres.");
    
    }}
    private void handletEmailTextChange( ObservableValue observable ,
                                                String oldValue,
                                                String newValue){
        if (newValue.length() > 255) {
            tfEmail.setText(oldValue);
            showErrorAlert("El nombre de usuario supera el numero maximo de caracteres.");
    }
    
    }
    private void handletfCityTextChange( ObservableValue observable ,
                                                String oldValue,
                                                String newValue){
        if (newValue.length() > 255) {
            tfCity.setText(oldValue);
            showErrorAlert("la calle del usuario supera el numero maximo de caracteres.");
    
    }}
    private void handletfStreetTextChange( ObservableValue observable ,
                                                String oldValue,
                                                String newValue){
        if(newValue.length() > 255) {
            tfStreet.setText(oldValue);
            showErrorAlert("la ciudad del usuario supera el numero maximo de caracteres.");
        
    
    }}
    private void handletfZipTextChange( ObservableValue observable ,
                                                String oldValue,
                                                String newValue){
        if(newValue.length() > 255) {
            tfZip.setText(oldValue);
            showErrorAlert("El ZIP de usuario supera el numero maximo de caracteres.");
    
    }}
     
    private void handlebtBackOnAction(ActionEvent event){
        try {
          
            FXMLLoader loader= new FXMLLoader(getClass().getResource("DisenoSignIn.fxml"));
            Parent root = (Parent)loader.load();
            Sign_InController controller =loader.getController();
            controller.init(stage, root);
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "Back Button error: " + e.getLocalizedMessage())
                 .showAndWait();
        }
    }
    private void handleHySignInOnAction(ActionEvent event){
       try{
       FXMLLoader loader= new FXMLLoader(getClass().getResource("DisenoSignIn.fxml"));
        Parent root = (Parent)loader.load();
        Sign_InController controller =loader.getController();
        controller.init(stage, root);
       }catch (Exception e){
           LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                 "HyperLink error: " + e.getLocalizedMessage())
                 .showAndWait();
       }  
    }

    private void handlebtSignUpAction(ActionEvent event) {
     try {
        Customer c = new Customer();
        c.setFirstName(tfFirstName.getText());
        c.setLastName(tfLastName.getText());
        c.setMiddleInitial(tfMiddlelnitial.getText());
        c.setPhone(Long.parseLong(tfPhone.getText()));
        c.setEmail(tfEmail.getText().trim());
        c.setPassword(pfPassword.getText());
        c.setCity(tfCity.getText());
        c.setStreet(tfStreet.getText());
        c.setState(tfState.getText());
        c.setZip(Integer.parseInt(tfZip.getText()));
        CustomerRESTClient cliente =new CustomerRESTClient();
        cliente.create_XML(c); 

        LOGGER.log(Level.INFO, "Customer registered:", c.toString());
        showInfoAlert("Customer registered successfully!");
        //Ahora nos metet en el sign In
        FXMLLoader loader= new FXMLLoader(getClass().getResource("DisenoSignIn.fxml"));
        Parent root = (Parent)loader.load();
        Sign_InController controller =loader.getController();
        controller.init(stage, root);
        clearFields();
        btSignUp.setDisable(true);
        tfFirstName.requestFocus();
    }catch (ForbiddenException fe) {
        LOGGER.warning(fe.getLocalizedMessage());
        showErrorAlert("Error Email Registered");
    } 
     catch (NumberFormatException ne) {
        LOGGER.warning(ne.getLocalizedMessage());
        showErrorAlert("Error registering customer: " +
        ne.getMessage());
    }catch (Exception e) {
        LOGGER.warning(e.getLocalizedMessage());
        showErrorAlert("Error registering customer: " +
        e.getMessage());
    }
   }

               //    create_XML(customer);
           //new Alert("msg");

       private void showInfoAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,message , ButtonType.OK);
            alert.showAndWait();
    }

    
     private void validateForm(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        boolean allFilled = !pfPassword.getText().isEmpty() &&
                            !pfConfirmPassword.getText().isEmpty();

        btSignUp.setDisable(!allFilled);
    }
     private void handleTextChange(ObservableValue<? extends String>
    observable, String oldValue, String newValue) {
    validateForm();
    }
    private void validateForm() {
    boolean valid = true;
    String msg = "";
      if(tfFirstName.getText().trim().isEmpty()) {
    valid = false;
    msg = "First Name is required.";
    } else if (!tfFirstName.getText().matches("[A-Za-z]+")) {
    valid = false;
    msg = "First Name must contain only letters.";
    } else if (tfLastName.getText().trim().isEmpty()) {
    valid = false;
    msg = "Last Name is required.";
    } else if (!tfLastName.getText().matches("[A-Za-z]+")) {
    valid = false;
    msg = "Last Name must contain only letters.";
    } else if (!tfMiddlelnitial.getText().isEmpty() &&
    !tfMiddlelnitial.getText().matches("^[A-Za-z]$")) {
    valid = false;
    msg = "Only one character allowed for Middle Initial.";
    } else if (!tfPhone.getText().matches("\\d{9,15}")) {
    valid = false;
    msg = "Invalid phone number.";
    } else if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
    tfEmail.getText())) {
    valid = false;
    msg = "Invalid email format.";
    } else if (pfPassword.getText().length() < 8) {valid = false;
    msg = "Password must be at least 8 characters.";
    } else if
    (!pfPassword.getText().equals(pfConfirmPassword.getText())) {
    valid = false;
    msg = "Passwords do not match.";
    } else if (tfCity.getText().isEmpty() ||
    tfStreet.getText().isEmpty() || tfZip.getText().isEmpty()) {
    valid = false;
    msg = "Address fields are required.";
    }
    btSignUp.setDisable(!valid);
    if (!valid && !msg.isEmpty()) {
    LOGGER.warning(msg);
    }
    }
        
    }