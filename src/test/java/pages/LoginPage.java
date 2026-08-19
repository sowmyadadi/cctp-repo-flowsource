package pages;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.Properties;

public class LoginPage extends MasterPage {

    private static final Logger LOGGER = LogManager.getLogger(LoginPage.class);

    // @FindBy(linkText = "Login")
    @FindBy(xpath = "//*[@value='Log In']")
    public WebElement linkTxtLogin;

    @FindBy(id = "login")
    public WebElement txtUsername;

    @FindBy(id = "password")
    public WebElement txtPassword;

    @FindBy(xpath = "//*[@value='Log In']")
    public WebElement btnLogin;

    public LoginPage(ScriptHelper scriptHelper) {
        super(scriptHelper);
    }


    public void launchUrl() {

        Properties properties = Settings.getInstance();
        driver.get(properties.getProperty("ApplicationUrl"));

        System.out.println("Application URL -----------Opening URL-");
       
        boolean isApplicationLaunched = checkElement(By.xpath("//*[@value='Log In']"));
        System.out.println("Application Launched Successfully");
        // Assert.assertTrue(isApplicationLaunched, "Application Launched Successfully");
        report.updateTestLog("Launch", "Application Launched", Status.DONE, driver);
    }

    public void login() {

        txtUsername.sendKeys("leapdemo");
        txtPassword.sendKeys("dd");
        btnLogin.click();
        verifyLogin();
        report.updateTestLog("Login", "Login Successfully", Status.DONE, driver);
    }

    private void verifyLogin() {
        pauseScript(1);

        boolean isLoginSuccessful = checkElement(By.linkText("Home"));
        Assert.assertTrue(isLoginSuccessful, "Login Successful");
    }

    public boolean checkElement(By by) {
        boolean chkElement = false;
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
        WebElement element = wait
                .until(ExpectedConditions.visibilityOfElementLocated(by));
        return chkElement = element.isDisplayed();
    }

    public void pauseScript(int time) {
        try {
            Thread.sleep(2000 * time);
        } catch (Exception e) {

        }
    }
}
