/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;

import applicationcrud.ApplicationCRUD;
import applicationcrud.model.Account;
import applicationcrud.model.AccountType;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.ButtonMatchers.isDefaultButton;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
/**
 *
 * @author daniel
 * @fixme Los métodos de test presentados son insuficientes.
 * @fixme Crear sendos métodos de test para Read,Create,Update y Delete sobre la tabla de Cuentas que verifiquen sobre los items de la tabla cada caso de uso.

 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountsControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationCRUD().start(stage);

    }

    @Before
    public void testchangeWindow() {
        clickOn("#tfEmail");
        write("awallace@gmail.com");
        clickOn("#pfPassword");
        write("qwerty*9876");
        clickOn("#btSignIn");
        
        
        
    }
    @Ignore
    @Test
    public void test_1_TableSelectionEnablesButtons() {
        //Selecciona la primera cuenta
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row.", row);
        clickOn(row);

        //Botones habilitados
        verifyThat("#btUpdate", NodeMatchers.isEnabled());
        verifyThat("#btDelete", NodeMatchers.isEnabled());
    }
    @Ignore
    @Test
    public void test_2_READ() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();
        
        //Verificacion de que los items de la tabla son ACCOUNT
         assertTrue(table.getItems().stream()
                .allMatch(a -> a instanceof Account));
        
    }
    //@Ignore
    @Test
    public void test_3_Create() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();
        int sizeBefore = table.getItems().size();

        // Clic en "Post" para crear nueva cuenta
        clickOn("#btPost");
        //La ultima creada
        Node row = lookup(".table-row-cell").nth(sizeBefore).query();
        //La celda descripcion
        Node cellDesc = from(row).lookup(".table-cell").nth(1).query();
        //Doble Click en celda
        doubleClickOn(cellDesc);
        write("Cuenta TestPost");
        press(KeyCode.ENTER);
        table.refresh();
        
        assertEquals("La fila no se añadió a la tabla", sizeBefore + 1, table.getItems().size());
        //Verifica que en la tabla Cuentas que se encuentra una cuenta con la descripcion TestPost
        assertTrue("La Cuenta TestPost no se encuentra en la tabla",
                table.getItems().stream().anyMatch(a -> a.getDescription().equals("Cuenta TestPost")));
    }
    @Ignore
    @Test
    public void test_4_UPDATE_TEST() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();
        int sizetable=table.getItems().size();
        clickOn("#btPost");
        //La ultima creada
        Node row = lookup(".table-row-cell").nth(sizetable).query();
        //La celda descripcion
        Node cellDesc = from(row).lookup(".table-cell").nth(1).query();
        //Doble Click en celda
        doubleClickOn(cellDesc);
        write("Cuenta TestUpdate");
        //Apreta Enter
        press(KeyCode.ENTER);
        table.refresh();

        // Verificacion
        assertTrue("La Cuenta TestUpdate no se encuentra en la tabla",
                table.getItems().stream().anyMatch(a -> a.getDescription().equals("Cuenta TestUpdate")));
    }
    @Ignore
    @Test
    public void test_5_Delete() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();
        int sizetable=table.getItems().size();
        //Crea una cuenta 
        clickOn("#btPost");
        //La cuenta creada pillo el id 
        Account accountCreated = table.getItems().get(sizetable);
        Long id = accountCreated.getId();
        // Selecciona la cuenta a eliminar
        Node row = lookup(".table-row-cell").nth(sizetable).query();
        
        // Borra la cuenta
        clickOn("#btDelete");
        
        // Confirmar el diálogo de alerta
        clickOn("Sí");
        //isDefaultButton() me apreta exit 
        
        //Refresca la tabla por si acaso
        table.refresh();
        //Verifica que no hay ninguna cuenta con esa Descripcion
        boolean CuentaExiste = table.getItems().stream()
                               .anyMatch(a -> a.getId().equals(id));
        assertFalse("Account not deleted ID found", CuentaExiste);
        //Tiene que tener el mismo tamaño que al inicio por que crea una nueva tabla a borrar
        assertEquals("The row has not been deleted",
                    sizetable,table.getItems().size());

    }
    
    @Ignore
    @Test
    public void test_6_BeginBalanceOnlyForNewAccount() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();

        //Crear nueva cuenta
        int sizeBefore = table.getItems().size();
        clickOn("#btPost");
        //Verifica que se ha añadido una nueva fila en la tabla
        assertEquals(sizeBefore + 1, table.getItems().size());

        Node row = lookup(".table-row-cell").nth(sizeBefore).query();
        //Modificar BeginBalance solo para cuenta nueva
        Node cellBegin = from(row).lookup(".table-cell").nth(4).query();
        //Doble Click en celda
        doubleClickOn(cellBegin);
        write("500").push(KeyCode.ENTER);
        
        table.refresh();
        // Verificar
        boolean exists = table.getItems().stream()
        .anyMatch(a -> a.getBeginBalance() == 500.0 && a.getBalance() == 500.0 
                && a.getBalance() != null && a.getBeginBalance() != null);
        assertTrue(exists);
    }
    
    @Ignore
    @Test
    public void test_7_NoDelete(){
        TableView<Account> table = lookup("#tblAccounts").queryTableView();
        //Tamaño tabla
        int sizeBefore = table.getItems().size();
        Account accountToDelete = table.getItems().stream()
                .filter(a -> "Check Account".equals(a.getDescription()))
                .findFirst()
                .orElse(null);
        //Selecciona la cuenta
        interact(() -> table.getSelectionModel().select(accountToDelete));
        
        
        // Borra la cuenta
        clickOn("#btDelete");
        
        // Confirmar el diálogo de alerta
        clickOn("Sí");
        
        //Verifica que funciona la alerta
        verifyThat("This account has movements and cannot be deleted", isVisible());
        
        clickOn("Aceptar");
        assertEquals("An Account got deleted",sizeBefore,table.getItems().size());
    }   
    @Ignore
    @Test
    public void test_8_DeleteAccounts() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();

        //Mientras la tabla tenga una cuenta con la descripcion vacia
        while (table.getItems().stream().anyMatch(a -> a.getDescription() == null || a.getDescription().isEmpty())) {

            // Selecciona la cuenta con descripción vacía
            Account accountToDelete = table.getItems().stream()
                    .filter(a -> a.getDescription() == null || a.getDescription().isEmpty())
                    .findFirst()
                    .orElse(null);

            if (accountToDelete != null) {
                //Selecciona la fila 
                interact(() -> {
                    table.getSelectionModel().select(accountToDelete);
                    table.scrollTo(accountToDelete);
                });

                //click en Delete
                clickOn("#btDelete");
                clickOn("Sí");
                //isDefaultButton() Apreta exit
                
                //Espera 
                sleep(500);
            }
        }

        //Verificación ninguna cuenta con descripción vacía
        long countEmpty = table.getItems().stream()
                .filter(a -> a.getDescription() == null || a.getDescription().isEmpty() || a.getDescription() == "Cuenta TestPost"|| a.getDescription() == "Cuenta TestDelete")
                .count();

        assertEquals(0, countEmpty);//Comprueba que no hay mas accounts sin descripcion
    }

    @After
    public void closeWindow() throws Exception{
        FxToolkit.hideStage();
        FxToolkit.cleanupStages();
    }
    

}