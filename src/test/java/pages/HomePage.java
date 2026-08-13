package pages;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

public class HomePage extends MasterPage {

    private static final Logger LOGGER = LogManager.getLogger(HomePage.class);

    @FindBy(linkText = "Todos")
    public WebElement btnTodo;

    @FindBy(xpath = "/html/body/app-root/app-list-todos/div[3]/div/button")
    public WebElement btnAdd;

    @FindBy(xpath = "//*[@name='description']")
    public WebElement txtDescription;

    @FindBy(xpath = "//*[@name='targetDate']")
    public WebElement txtTargetDate;

    @FindBy(xpath = "/html/body/app-root/app-todo/div/form/button")
    public WebElement btnSave;

    @FindBy(xpath = "/html/body/app-root/app-list-todos/div[3]/table/tbody/tr[1]/td[5]/button")
    public WebElement btnDeleteFirst;

    @FindBy(xpath = "//*[@class='alert alert-success']")
    public WebElement alert;

    @FindBy(linkText = "Home")
    public WebElement btnHome;

    @FindBy(linkText = "Logout")
    public WebElement btnLogout;

    public HomePage(ScriptHelper scriptHelper) {
        super(scriptHelper);
    }

    public void navigateTodoTask() {

        pauseScript(1);
        btnTodo.click();
        report.updateTestLog("Click", "Navagiation done", Status.PASS, driver);
    }

    public void addTodoTask(String task) {

        btnAdd.click();
        txtDescription.sendKeys(task);
        btnSave.click();
    }

    public void verifyTodoTask(String task) {

        pauseScript(2);
        List<WebElement> rowElements = driver.findElements(By.tagName("tr"));

        for (WebElement webElement : rowElements) {
            String addedCourseName = webElement.getText();
            if (addedCourseName.contains(task)) {
                report.updateTestLog("Task Add", "Task Added " + task, Status.PASS, driver);
                break;
            }
        }
    }

    public void deleteTask(String task) {

        pauseScript(1);
        List<WebElement> rowElements = driver.findElements(By.tagName("tr"));

        for (int i = 0; i < rowElements.size(); i++) {
            String addedCourseName = rowElements.get(i).getText();
            if (addedCourseName.contains(task)) {
                int index = i + 1;
                String xpath = "/html/body/app-root/app-list-todos/div[3]/table/tbody/tr[" + index + "]/td[5]/button";
                //WebElement btnDelete = driver.findElement(By.xpath(xpath));
                //pauseScript(1);
                //btnDelete.click();
                retryingFindClick(By.xpath(xpath));
                break;
            }
        }
    }

    public void verifyDelete(String task) {

        if (alert.isDisplayed()) {
            report.updateTestLog("Delete Task", "Task Deleted " + task, Status.PASS);
        } else {
            report.updateTestLog("Delete Task", "Task Couldn't Delete " + task, Status.FAIL);
        }
    }

    public void updateTask(String task) {

        pauseScript(1);
        btnTodo.click();
        pauseScript(1);
        List<WebElement> rowElements = driver.findElements(By.tagName("tr"));
        for (int i = 0; i < rowElements.size(); i++) {
            String addedCourseName = rowElements.get(i).getText();
            if (addedCourseName.contains(task)) {
                int index = i + 1;
                String xpath = "/html/body/app-root/app-list-todos/div[3]/table/tbody/tr[" + index + "]/td[4]/button";
                //WebElement btnUpdate = driver.findElement(By.xpath(xpath));
                //pauseScript(1);
                //btnUpdate.click();
                retryingFindClick(By.xpath(xpath));

                break;
            }
        }
    }

    public void updateTaskDetails(String task) {
        txtDescription.clear();
        txtDescription.sendKeys(task + " and hands on");
        btnSave.click();
    }

    public void logout() {

        cleanUp();
        btnLogout.click();
        boolean isLogout = checkElement(By.linkText("Login again"));
        Assert.assertTrue(isLogout, "Logged Out the Application Successfully");
        report.updateTestLog("Logout","Logged out successful",Status.PASS);
    }

    public void cleanUp() {
        pauseScript(1);
        List<WebElement> rowElements = driver.findElements(By.tagName("tr"));
        if (rowElements.size() > 3) {
            for (int i = 0; i < rowElements.size() - 3; i++) {
                int index = i + 1;
                String xpath = "/html/body/app-root/app-list-todos/div[3]/table/tbody/tr[1]/td[5]/button";
                //WebElement btnDelete = driver.findElement(By.xpath(xpath));
                //pauseScript(1);
                //btnDelete.click();
                retryingFindClick(By.xpath(xpath));
            }
        }

    }

    public boolean checkElement(By by) {

        boolean chkElement = false;
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
        WebElement element = wait
                .until(ExpectedConditions.visibilityOfElementLocated(by));
        return chkElement = element.isDisplayed();
    }

    public boolean retryingFindClick(By by) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 2) {
            try {
                pauseScript(1);
                driver.findElement(by).click();
                result = true;
                break;
            } catch (Exception e) {
            }
            attempts++;
        }
        return result;
    }

    public void pauseScript(int time) {
        try {
            Thread.sleep(1000 * time);
        } catch (Exception e) {

        }
    }
}
