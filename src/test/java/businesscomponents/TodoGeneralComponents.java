package businesscomponents;

import org.openqa.selenium.Dimension;
import com.cognizant.craft.DriverScript;
import com.cognizant.craft.ReusableLibrary;
import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.Status;

import org.openqa.selenium.By;
import pages.*;

/**
 * Class for storing general purpose business components
 *
 * @author Cognizant
 */

public class TodoGeneralComponents extends ReusableLibrary {
    /**
     * Constructor to initialize the component library
     *
     * @param scriptHelper
     * The {@link ScriptHelper} object passed from the
     * {@link DriverScript}
     */

    // HomeTodoApplnPage createTodoApplnPage = new HomeTodoApplnPage(scriptHelper);
    HomePage homePage = new HomePage(scriptHelper);
    LoginPage loginPage = new LoginPage(scriptHelper);

    public TodoGeneralComponents(ScriptHelper scriptHelper) {
        super(scriptHelper);
    }

    public void loginTodoApplication() throws InterruptedException {

        loginPage.launchUrl();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        loginPage.login();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        report.updateTestLog("Login", "Login Successful", Status.PASS);

    }

    public void navigateTodoPage() {
        homePage.navigateTodoTask();
        homePage.btnHome.click();

        report.updateTestLog("Home Page", "Navigated to Home Page", Status.PASS);
    }

    public void createTodoList() throws InterruptedException {

        String task = dataTable.getData("General_Data", "TodoTask");
        homePage.navigateTodoTask();
        homePage.addTodoTask(task);
        homePage.verifyTodoTask(task);

        report.updateTestLog("create todo", "creation of todo list is successful", Status.PASS);
    }

    public void deleteTodoList() throws InterruptedException {

        String task = dataTable.getData("General_Data", "TodoTask");
        homePage.navigateTodoTask();
        homePage.deleteTask(task);
        homePage.verifyDelete(task);

        report.updateTestLog("delete todo", "deletion of todo list is successful", Status.PASS);
    }

    public void updateTodoList() throws InterruptedException {

        String task = dataTable.getData("General_Data", "TodoTask");
        homePage.navigateTodoTask();
        homePage.updateTask(task);
        homePage.updateTaskDetails(task);
        report.updateTestLog("updateTodoList", "Update is successful", Status.PASS);

    }

    public void logOut() {

        homePage.logout();
        report.updateTestLog("Logout", "Logout success message is present", Status.PASS);
    }


}
