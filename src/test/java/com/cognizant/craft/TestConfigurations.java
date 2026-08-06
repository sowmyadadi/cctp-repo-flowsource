package com.cognizant.craft;

import com.cognizant.framework.selenium.Browser;
import com.cognizant.framework.selenium.ExecutionMode;
import com.cognizant.framework.selenium.SeleniumParametersBuilders;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class TestConfigurations extends CRAFTTestCase {

    @DataProvider(name = "ChromeBrowser")
    public Object[][] chromeBrowser(Method currentMethod) {
        currentScenario = currentMethod.getDeclaringClass().getSimpleName();
        currentTestcase = currentMethod.getName();
        currentTestcase = currentTestcase.substring(0, 1).toUpperCase().concat(currentTestcase.substring(1));
        packageName = currentMethod.getDeclaringClass().getPackage().getName(); // added changes for leap

        return new Object[][]{
                {new SeleniumParametersBuilders(currentScenario, currentTestcase, packageName).extentReport(extentReport) // added changes for leap
                        .extentTest(extentTest).testInstance("Instance1").executionMode(ExecutionMode.LOCAL)
                        .browser(Browser.CHROME).build()}};
    }

    @DataProvider(name = "ChromeHeadlessBrowser")
    public Object[][] chromeHeadlessBrowser(Method currentMethod) {
        currentScenario = currentMethod.getDeclaringClass().getSimpleName();
        currentTestcase = currentMethod.getName();
        currentTestcase = currentTestcase.substring(0, 1).toUpperCase().concat(currentTestcase.substring(1));
        packageName = currentMethod.getDeclaringClass().getPackage().getName(); // added changes for leap

        return new Object[][]{
                {new SeleniumParametersBuilders(currentScenario, currentTestcase, packageName).extentReport(extentReport) // added changes for leap
                        .extentTest(extentTest).testInstance("Instance1").executionMode(ExecutionMode.LOCAL)
                        .browser(Browser.CHROME_HEADLESS).build()}};
    }

    @DataProvider(name = "ChromeAllBrowser")
    public Object[][] chromeAllBrowser(Method currentMethod) {
        currentScenario = currentMethod.getDeclaringClass().getSimpleName();
        currentTestcase = currentMethod.getName();
        currentTestcase = currentTestcase.substring(0, 1).toUpperCase().concat(currentTestcase.substring(1));
        packageName = currentMethod.getDeclaringClass().getPackage().getName(); // added changes for leap

        return new Object[][]{
                {new SeleniumParametersBuilders(currentScenario, currentTestcase, packageName).extentReport(extentReport) // added changes for leap
                        .extentTest(extentTest).testInstance("Instance1").executionMode(ExecutionMode.LOCAL)
                        .browser(Browser.CHROME_HEADLESS).build()},
                {new SeleniumParametersBuilders(currentScenario, currentTestcase, packageName).extentReport(extentReport) // added changes for leap
                        .extentTest(extentTest).testInstance("Instance2").executionMode(ExecutionMode.LOCAL)
                        .browser(Browser.CHROME).build()}};
    }
}