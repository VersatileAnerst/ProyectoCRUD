/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import applicationcrud.ApplicationCRUD;
import applicationcrud.model.Movement;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author jimmy
 * @fixme Los métodos de test presentados son insuficientes.
 * @fixme Crear sendos métodos de test para Read,Create y Delete (último movimiento) sobre la tabla de Movements que verifiquen sobre los items de la tabla cada caso de uso.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MovementControllerTest extends ApplicationTest {

    private TableView<Object> tblMovements;
     
        
                   
    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationCRUD().start(stage);
    }

    @Before
    public void setUp() throws Exception {
        // 1. Login
        clickOn("#tfEmail");
        write("awallace@gmail.com");
        clickOn("#pfPassword");
        write("qwerty*9876");
        clickOn("#btSignIn");

        // 2. Seleccionar la primera fila de la tabla de cuentas
        // Esperamos a que la tabla sea visible antes de interactuar
        verifyThat("#tblAccounts", isVisible());
        
        clickOn("#tblAccounts");
        type(KeyCode.DOWN); // Selecciona la primera cuenta
        
        // 3. Entrar a la ventana de movimientos 
        // (Asumo que tienes un botón llamado btMovements o similar)
        clickOn("#btMovement"); 
        
        // Esperar a que cargue la ventana de movimientos
        verifyThat("#tblMovements", isVisible());
    }

    /*@Test 
    public void test_1ButtonSearchExists() { 
        // Ahora sí, verificamos en la ventana de movimientos
        verifyThat("#btnSearch", isVisible());
    } 

    @Test  
    public void test_2DeleteButtonIsDisabledByDefault() { 
        // El botón borrar debe estar deshabilitado al inicio (sin selección)
        verifyThat("#btnDelete", isDisabled()); 

    } */
    @Test  
    //@Ignore
 
    public void test_3readMovements() {  
      TableView<Movement> table = lookup("#tblMovements").queryTableView(); 

          int initialSize = table.getItems().size();  

     tblMovements = lookup("#tblMovements").queryTableView(); 
     
         assertNotNull("No se encontró la TableView con ID #tblMovements", tblMovements); 

         assertNotNull("La tabla no tiene referencia de items", tblMovements.getItems()); 

         assertTrue("La lista de movimientos debería ser accesible", initialSize >= 0); 
            // Verificación visual de TestFX 
         verifyThat("#tblMovements", isVisible()); 

} 
  //Comprobar que la tabla carga movimientos    
  

     @Test  
     //@Ignore

    public void test_4_createMovement() {  

        TableView<Movement> table = lookup("#tblMovements").queryTableView(); 

        int initialSize = table.getItems().size();  
  
        clickOn("#btnCreate");  
        
        clickOn("#txtDescription");
        write("hipoteca");  

        clickOn("#txtAmount");
        write("50");  

        clickOn("#btnSave");  
          // Verificación 
        assertEquals("El movimiento no se ha añadido", initialSize + 1, table.getItems().size());  
        Movement last = table.getItems().get(table.getItems().size() - 1);  

        assertEquals("La descripción ", "hipoteca", last.getDescription());  

        assertEquals("El monto no coincide", 50.0, last.getAmount(), 0.01);  

    } 
      
//Borrado de movimiento   
 @Test  
    //@Ignore
    public void test_5_deleteMovement() {  

        TableView<Movement> table = lookup("#tblMovements").queryTableView(); 
         org.junit.Assume.assumeTrue("No hay datos para borrar", table.getItems().size() > 0); 
         
        int initialSize = table.getItems().size(); 
          // Seleccionar la primera fila para habilitar btnDelete 
        Node row = lookup(".table-row-cell").nth(0).query();  
        clickOn(row);  
        verifyThat("#btnDelete", isEnabled());  
        clickOn("#btnDelete");  
          
        //sleep(500); 

  

        assertEquals("El movimiento eliminado", initialSize - 1, table.getItems().size());  

    } 

} 



    //@Test 
    //public void test_3OpenCreateModal() {
        // Clic en el botón de crear
        //clickOn("#btnCreate"); 
        
        // IMPORTANTE: Verifica que los IDs coincidan con tu FXML de creación
        //verifyThat("#btnSave", isDisabled()); 
        
        // Cerramos para no dejar la modal abierta para el siguiente test
        //clickOn("#btnCancel");
    //} 
    //@Test
    //public void test_4CreateMovement(){
    //    clickOn("#btnCreate"); 
    //    clickOn("#txtDescription");
    //    write("GASTO LUZ");
    //    clickOn("#txtAmount");
    //    write("20");
    //    clickOn("#btnSave");
        
    //}

   


