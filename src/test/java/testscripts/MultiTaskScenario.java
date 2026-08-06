package testscripts;

import org.testng.annotations.Test;

import com.cognizant.craft.DriverScript;
import com.cognizant.craft.TestConfigurations;
import com.cognizant.framework.selenium.SeleniumTestParameters;

public class MultiTaskScenario extends TestConfigurations {

    @Test(dataProvider = "ChromeHeadlessBrowser", dataProviderClass = TestConfigurations.class)
    public void TestForCreatingMultiTodoList(SeleniumTestParameters testParameters) {

        testParameters.setCurrentTestDescription("Test for creating multipleTodo List");

        DriverScript driverScript = new DriverScript(testParameters);
        driverScript.driveTestExecution();

        tearDownTestRunner(testParameters, driverScript);
    }

    @Test(dataProvider = "ChromeHeadlessBrowser", dataProviderClass = TestConfigurations.class)
    public void TestForUpdatingMultiTodoList(SeleniumTestParameters testParameters) {

        testParameters.setCurrentTestDescription("Test for updating multiple Todo List");

        DriverScript driverScript = new DriverScript(testParameters);
        driverScript.driveTestExecution();

        tearDownTestRunner(testParameters, driverScript);
    }

}
