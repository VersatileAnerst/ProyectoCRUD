/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationcrud.ui;

import applicationcrud.ApplicationCRUD;
import applicationcrud.model.Account;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
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
 */
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
    public void Test_1_EXIT() {
        //Comprueba el boton Exit esta enfocado
        clickOn("Exit");
        clickOn("YES");

    }

    @Test
    public void test_1_TableSelectionEnablesButtons() {
        clickOn("#tblAccounts");
        type(KeyCode.DOWN);

        verifyThat("#btUpdate", NodeMatchers.isEnabled());
        verifyThat("#btDelete", NodeMatchers.isEnabled());
    }
    @Ignore
    @Test
    public void test_2_BeginBalanceOnlyForNewAccount() {

        TableView<Account> table = lookup("#tblAccounts").queryTableView();

        //Crear nueva cuenta
        int sizeBefore = table.getItems().size();
        clickOn("#btPost");
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
    public void test_5_DeleteAccounts() {

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

                //click en si en la alerta
                clickOn("Sí");

                //Espera el refresh
                sleep(500);
            }
        }

        //Verificación final: no debe quedar ninguna cuenta con descripción vacía
        long countEmptyAfter = table.getItems().stream()
                .filter(a -> a.getDescription() == null || a.getDescription().isEmpty())
                .count();

        assertEquals(0, countEmptyAfter);
    }

    @Override
    public void stop() {
    }

}
