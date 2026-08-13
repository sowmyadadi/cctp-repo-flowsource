package testscripts;

import org.testng.annotations.Test;

import com.cognizant.craft.DriverScript;
import com.cognizant.craft.TestConfigurations;
import com.cognizant.framework.selenium.SeleniumTestParameters;

public class SingleTaskScenario extends TestConfigurations {

	@Test(dataProvider = "ChromeBrowser", dataProviderClass = TestConfigurations.class)
	public void TestForCreatingTodoList(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for creating a Todo List");

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}
	
	@Test(dataProvider = "ChromeHeadlessBrowser", dataProviderClass = TestConfigurations.class)
	public void TestForCreatingDeletingTodoList(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for creating and deleting a Todo List");

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}
	
	@Test(dataProvider = "ChromeHeadlessBrowser", dataProviderClass = TestConfigurations.class)
	public void TestForUpdatingTodoList(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for updating a Todo List");

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}
	

}
