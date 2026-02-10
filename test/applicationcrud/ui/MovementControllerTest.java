/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;

import applicationcrud.ApplicationCRUD;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author jimmy
 * @fixme Los métodos de test presentados son insuficientes.
 * @fixme Crear sendos métodos de test para Read,Create y Delete (último movimiento) sobre la tabla de Movements que verifiquen sobre los items de la tabla cada caso de uso.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MovementControllerTest extends ApplicationTest {
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

    @Test 
    public void test_1ButtonSearchExists() { 
        // Ahora sí, verificamos en la ventana de movimientos
        verifyThat("#btnSearch", isVisible());
    } 

    @Test  
    public void test_2DeleteButtonIsDisabledByDefault() { 
        // El botón borrar debe estar deshabilitado al inicio (sin selección)
        verifyThat("#btnDelete", isDisabled()); 

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

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }
}



