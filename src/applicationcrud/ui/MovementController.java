package applicationcrud.ui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

  

import applicationcrud.logic.MovementRESTClient;
import applicationcrud.model.Account;
import applicationcrud.model.Movement;
import javafx.beans.value.ChangeListener; 

import javafx.beans.value.ObservableValue; 

import javafx.fxml.FXML; 

import javafx.fxml.Initializable; 

import javafx.scene.control.*; 



  

import java.lang.Double; 
import java.lang.reflect.Type;

import java.net.URL; 

import java.time.LocalDate; 
import java.util.List;

import java.util.ResourceBundle; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author jimmy
 */

  

public class MovementController   { 

  

    @FXML 

    private TableView<Movement> tblMovements; 

  

    @FXML 

    private TableColumn<Movement, LocalDate> colDate; 

  

    @FXML 

    private TableColumn<Movement, String> colDescription; 

  

    @FXML 

    private TableColumn<Movement, Double> colAmount; 

  

    @FXML 

    private TableColumn<Movement, Double> colBalance; 

  

    @FXML 

    private DatePicker dpFrom; 

  

    @FXML 

    private DatePicker dpTo; 

  

    @FXML 

    private Button btnDelete; 

  

    @FXML 

    private Label lblInfo; 
    
    private Account account;
    
    private MovementRESTClient client = new MovementRESTClient();
    
     private static final Logger LOGGER = Logger.getLogger("applicationcrud.ui");

     
     public void init(Stage stage, Parent root) {
        try {
            LOGGER.info("Initializing Movemnet window.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //Establecer el titulo de la ventana en "Bank App"
            stage.setTitle("BankApp");
            //Ventana no redimensionable
            stage.setResizable(false);
            //FACTORIAS DE CELDA
            colDate.setCellValueFactory(new PropertyValueFactory<>("date")); 
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description")); 
            colAmount.setCellValueFactory(new PropertyValueFactory<>("amount")); 
            colBalance.setCellValueFactory(new PropertyValueFactory<>("balance")); 

            //LISTENER
            tblMovements.getSelectionModel().selectedItemProperty().
                    addListener(this:: handleMovementTableSelectionChanged);
            //Mostrar la ventana
            stage.show();
            LOGGER.info("Movement window initialized");
        } catch (Exception e) {
            LOGGER.warning(e.getLocalizedMessage());
            new Alert(Alert.AlertType.ERROR,
                    "Error Opening Window: " + e.getLocalizedMessage())
                    .showAndWait();
        }
       
    }
     public void setAccount(Account account){
         this.account = account;
         //Carga datos
         tblMovements.setItems(FXCollections.observableArrayList(
            client.findMovementByAccount_XML(new GenericType<List<Movement>>() {}, account.getId().toString())));
     }
     
     private void handleMovementTableSelectionChanged(ObservableValue observable,
                                                     Object oldValue,
                                                     Object newValue) {
         
        
    }
/**
 * 
 * @param location
 * @param resources 
 */

  public void init(URL location, ResourceBundle resources) { 

  

        configureTableColumns(); 

        configureTableSelection(); 

        loadInitialData(); 

    } 

    

  
/**
 * 
 */
    private void configureTableColumns() { 

  

        
  

        tblMovements.setEditable(false); 

    } 

  

   
/**
 * 
 */
    private void configureTableSelection() { 

  

        tblMovements.getSelectionModel() 

                .setSelectionMode(SelectionMode.SINGLE); 

  

        tblMovements.getSelectionModel() 

                .selectedItemProperty() 

                .addListener((obs, oldSelection, newSelection) -> { 

  

                    if (newSelection == null) { 

                        btnDelete.setDisable(true); 

                    } else { 

                        btnDelete.setDisable(!isLastMovement(newSelection)); 

                    } 

                }); 

    } 

  
/**
 * 
 * @param movement
 * @return 
 */
    private boolean isLastMovement(Movement movement) { 

  

        if (tblMovements.getItems().isEmpty()) { 

            return false; 

        } 

  

        Movement last = 

                tblMovements.getItems() 

                        .get(tblMovements.getItems().size() - 1); 

  

        return movement.equals(last); 

    } 

        
/**
 * 
 */
    private void loadInitialData() { 

          

        //  llamada REST 

        lblInfo.setText("Mostrando todos los movimientos"); 

    } 
    public void loadMovements() { 
        
        //Movement movement = new Movement();
        //Carga de datos a las columnas
            //tblMovements.setItems(FXCollections.observableArrayList(
            //client.findMovementByAccount_XML(new GenericType<List<Movement>>() {}, movement.getId().toString())));
    //try { 
         //GenericType Movement = null;
        //Movement = new GenericType((Type) Movement);
      //  Movement[] movement = Movement.findMovementByAccount_XML(Movement[].class, "MovementRESTClient"); 
       // ObservableList<Movement> dataMovement = FXCollections.observableArrayList(MovementRESTClient.findMovementBYAccount_XML(
         //new GenericType<List<Movement>>() {}));
        //tblMovements.setItems(dataMovement); 
    //} catch (Exception e) { 
       // handlelblError("Error to charge movements"); 
      //  LOGGER.log(Level.INFO, "Error to charge movements{0}", e.getMessage()); 
    //} 
} 


   

    @FXML 

    private void handleSearch() { 

       
    } 

  

    @FXML 

    private void handleCreate() { 
        
    }

    private void handlelblError(String error_to_charge_movements) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

        

    

  

    @FXML 

     private void handleDelete() { 

       

     } 
}

   

      

 

