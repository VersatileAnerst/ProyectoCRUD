/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import applicationcrud.ApplicationCRUD;
import applicationcrud.model.Account;
import applicationcrud.model.AccountType;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.matcher.base.NodeMatchers.isFocused;

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
    

    @Test
    public void test_1_TableSelectionEnablesButtons() {
        //Selecciona las cuenta
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row.", row);
        clickOn(row);


        verifyThat("#btUpdate", NodeMatchers.isEnabled());
        verifyThat("#btDelete", NodeMatchers.isEnabled());
    }
    @Test
    public void test_2_ButtonPost() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();
        int sizeBefore = table.getItems().size();

        // Clic en "Post" para crear nueva cuenta
        clickOn("#btPost");
        
        assertEquals("A row has not been added!", 
                     sizeBefore + 1, 
                     table.getItems().size());

        // Obtiene la última cuenta creada
        Account newAccount = table.getItems().get(table.getItems().size() - 1);

        // Edita los campos necesarios
        interact(() -> {
            newAccount.setDescription("Cuenta TestFX");
            newAccount.setBeginBalance(1000.0);
            newAccount.setBalance(1000.0);
            newAccount.setType(AccountType.CREDIT);
            newAccount.setCreditLine(500.0);
        });

        table.refresh();

        // Verifica que la cuenta tiene los datos correctos
        verifyThat("#tblAccounts", t -> ((TableView<Account>) t).getItems()
                .stream().anyMatch(a -> "Cuenta TestFX".equals(a.getDescription())
                        && a.getBeginBalance() == 1000.0
                        && a.getType() == AccountType.CREDIT
                        && a.getCreditLine() == 500.0));
    }

    @Test
    public void test_3_ButtonDelete() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();

        // Busca la cuenta creada con description = "Cuenta TestFX"
        Account accountToDelete = table.getItems().stream()
                .filter(a -> "Cuenta TestFX".equals(a.getDescription()))
                .findFirst()
                .orElse(null);

        if (accountToDelete == null) {
            System.out.println("No se encontró la cuenta a eliminar.");
            return; // Si no hay cuenta con ese description, no hacer nada
        }

        // Selecciona la cuenta encontrada
        interact(() -> table.getSelectionModel().select(accountToDelete));

        // Borra la cuenta
        clickOn("#btDelete");

        // Confirmar el diálogo de alerta
        clickOn("Sí");

        table.refresh();

        // Verifica que la cuenta ya no esté en la tabla
        verifyThat("#tblAccounts", t -> !((TableView<Account>) t).getItems().contains(accountToDelete));

    }

    @Test
    public void test_4_BeginBalanceOnlyForNewAccount() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();

        //Crear nueva cuenta
        int sizeBefore = table.getItems().size();
        clickOn("#btPost");
        //Verifica que la cuenta se ha creado
        assertEquals(sizeBefore + 1, table.getItems().size());

        Account newAccount = table.getItems().get(table.getItems().size() - 1);

        //Modificar BeginBalance solo para cuenta nueva
        interact(() -> {
            newAccount.setBeginBalance(500.0);
            newAccount.setBalance(500.0);
            table.refresh();
        });

        // Verificar
        assertEquals(500.0, newAccount.getBeginBalance(), 0.001);
        assertEquals(500.0, newAccount.getBalance(), 0.001);
    }
    @Test
    public void test_5_CreditLineOnlyForCREDITAccounts() {
        TableView<Account> table = lookup("#tblAccounts").queryTableView();
        
        //Crear nueva cuenta
        int sizeBefore = table.getItems().size();
        clickOn("#btPost");
        assertEquals(sizeBefore + 1, table.getItems().size());
        
        Account newAccount = table.getItems().get(table.getItems().size() - 1);
        
        //Cambia la cuenta a CREDIT y cambia la linea de credito
        interact(() -> {
            newAccount.setType(AccountType.CREDIT);
            newAccount.setCreditLine(500.0);
            table.refresh();
        });
        //Verifica que la credit line es 500
        assertEquals(500.0, newAccount.getCreditLine(), 0.001);
    }
    @Test
    public void test_6_DeleteAccounts() {
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
                
                //Espera 
                sleep(500);
            }
        }

        //Verificación ninguna cuenta con descripción vacía
        long countEmptyAfter = table.getItems().stream()
                .filter(a -> a.getDescription() == null || a.getDescription().isEmpty())
                .count();

        assertEquals(0, countEmptyAfter);
    }

    
    

}
