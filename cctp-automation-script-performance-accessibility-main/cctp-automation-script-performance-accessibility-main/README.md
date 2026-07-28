### Migrate CRAFT Leap Changes into existing project

###### Please download & place report-client-0.0.1-SNAPSHOT.jar under src/test/resources

###### POM.xml add below dependency
```
        <dependency>
            <groupId>com.cognizant</groupId>
            <artifactId>report-client</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/src/test/resources/report-client-0.0.1-SNAPSHOT.jar</systemPath>
        </dependency>
```

###### Add leap-collector.properties under src/test/resources & add below sample value : Take these values from leap platform reporting section
```
leap.report.enabled = true
leap.report.host = http://10.120.100.56:9002/reports/
leap.report.project = demo_rerun
leap.report.execution = craft_execution
leap.report.token = 38b96768-c06b-43ec-b848-83217318693b
```

###### Add CRAFTPlugin.java & LeapReport.java to com.cognizant.framework package

##### After adding LeapReport.java , there will be errors in Report.java, ReportType.java & HTMLReport.java

* Please add testParameters as parameter to the initializeTestLog() method in all please

```
Report.java
 /**
     * Function to initialize the test log
     */
    public void initializeTestLog() {
        if ("".equals(reportSettings.getReportName())) {
            throw new FrameworkException("The report name cannot be empty!");
        }

        for (int i = 0; i < reportTypes.size(); i++) {
            reportTypes.get(i).initializeTestLog(testParameters);
        }
    }

HTMLReport.java
@Override
	public void initializeTestLog(SeleniumTestParameters testParameters) {
		File testLogFile = new File(testLogPath);
		try {
			testLogFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while creating HTML test log file");
		}

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(testLogFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Cannot find HTML test log file");
		}
		PrintStream printStream = new PrintStream(outputStream);

		String testLogHeadSection;

		testLogHeadSection = "<!DOCTYPE html> \n" + "<html> \n" + "\t <head> \n" + "\t\t <meta charset='UTF-8'> \n"
				+ "\t\t <title>" + reportSettings.getProjectName() + " - " + reportSettings.getReportName()
				+ " Automation Execution Results" + "</title> \n\n" + getThemeCss() + getJavascriptFunctions()
				+ "\t </head> \n";

		printStream.println(testLogHeadSection);
		printStream.close();
	}

ReportType.java
/**
	 * Function to initialize the test log
	 * @param testParameters
	 */
	public void initializeTestLog(SeleniumTestParameters testParameters);


```

###### Add below snippet in Report.java -> initialize() method
```
         // For Leap Report Integration
        if (CRAFTPlugin.isLeapEnable()) {
            LeapReport leapReport = new LeapReport(reportSettings);
            reportTypes.add(leapReport);
        }
```

###### Replace Below Java files into CRAFT Framework

```
Replace TestParameters.java under com.cognizant.framework
```

```
Replace SeleniumTestParameters.java under com.cognizant.framework.selenium
```

```
Replace SeleniumParametersBuilders under com.cognizant.framework.selenium
```



###### Add packageName parameter in TestConfigurations.java or replace this java file

```
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
```

####### Add packageName declaration in CRAFTTestCase.java & CRAFTLiteTestCase.java

```
protected String packageName;  // added changes for leap
```

###### Allocator.java , please add packageName values as Null

###### Latest Changes for CRAFT - May 31st 2021

1. Replace CRAFTPlugin, ExcelReport, HtmlReport, LeapReport, Report, ReportType, Settings
    CRAFTPlugin, LeapReport -> LEAP Changes
    ReportType, Report, HtmlReport, ExcelReport -> added new function (updateTestLog)
    Settings-> added code to Read leap-collector.properties file
2. Change the DriverScript.java-> method exceptionHandler

```
  private void exceptionHandler(Exception ex, String exceptionName) {
        // Error reporting
        String exceptionDescription = ex.getMessage();
        if (exceptionDescription == null) {
            exceptionDescription = ex.toString();
        }

        // Change for LEAP
        Properties leapProperties = Settings.getLeapPropertiesInstance();

        if (Boolean.parseBoolean(leapProperties.getProperty("leap.report.enabled"))) {
            report.updateTestLog(report.getBusinessComponent(),ex.getLocalizedMessage(),ex,ex.getMessage(),Status.FAIL);
        } else {
            if (ex.getCause() != null) {
                report.updateTestLog(exceptionName, exceptionDescription + " <b>Caused by: </b>" + ex.getCause(),
                        Status.FAIL);
            } else {
                report.updateTestLog(exceptionName, exceptionDescription, Status.FAIL);
            }
        }


        // Print stack trace for detailed debug information
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        String stackTrace = stringWriter.toString();
        report.updateTestLog("Exception stack trace", stackTrace, Status.DEBUG);

        // Error response
        if (frameworkParameters.getStopExecution()) {
            report.updateTestLog("CRAFT Info", "Test execution terminated by user! All subsequent tests aborted...",
                    Status.DONE);
            currentIteration = testParameters.getEndIteration();
        } else {
            OnError onError = OnError.valueOf(properties.getProperty("OnError"));
            switch (onError) {
                // Stop option is not relevant when run from QC
                case NEXT_ITERATION:
                    report.updateTestLog("CRAFT Info",
                            "Test case iteration terminated by user! Proceeding to next iteration (if applicable)...",
                            Status.DONE);
                    break;

                case NEXT_TESTCASE:
                    report.updateTestLog("CRAFT Info",
                            "Test case terminated by user! Proceeding to next test case (if applicable)...", Status.DONE);
                    currentIteration = testParameters.getEndIteration();
                    break;

                case STOP:
                    frameworkParameters.setStopExecution(true);
                    report.updateTestLog("CRAFT Info", "Test execution terminated by user! All subsequent tests aborted...",
                            Status.DONE);
                    currentIteration = testParameters.getEndIteration();
                    break;

                default:
                    throw new FrameworkException("Unhandled OnError option!");
            }
        }
    }
```
###### Steps/changes for Client-Side perfomance and Accessibility reports
```
    Replace report-client.jar, LeapReport.java, CraftPlugin.java, Report.java, ReportType.interface, HtmlReport.java, ExcelReport.java

    Add below dependency in pom.xml
    <dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20230227</version>
    </dependency>

    ***Add argument (driver) to report.updateTestLog as below***
    report.updateTestLog("Launch Application", "Application launched Successful", Status.PASS, driver);
```

