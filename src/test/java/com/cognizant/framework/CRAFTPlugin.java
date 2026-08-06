package com.cognizant.framework;

import com.cognizant.reportclient.plugins.clients.ClientManager;
import com.cognizant.reportclient.plugins.common.BaseConstants;
import com.cognizant.reportclient.plugins.common.TProperties;
import com.cognizant.reportclient.plugins.launch.LaunchManager;
import com.cognizant.reportclient.plugins.logger.LogAndImageProcessor;
import com.cognizant.reportclient.plugins.models.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.Level;
import org.json.JSONObject;

public class CRAFTPlugin {

	private static ExecutiveTestPlan plan;
	private static PerformanceTestPlan performancePlan;

	public static final TProperties PROPERTIES;

	static {
		PROPERTIES = TProperties.getInstance(getResourcePath() + BaseConstants.LEAP_COLLECTOR_PROPERTIES);
	}

	public static boolean isLeapEnable() {
		return Boolean.parseBoolean(PROPERTIES.getOrDefault(BaseConstants.LEAP_ENABLE, "true"));
	}

	public static void start() {
		getInitLaunch();
	}

	public static void update() {
		pushData();
	}

	private static void getInitLaunch() {
		if (plan == null) {
			plan = LaunchManager.getInitLaunch();
			plan.setToolName("CRAFT");
			plan.setTestSuites(new ArrayList<>());
		}
	}

	public static void createTestSuite(String testSuiteName, String result) {
		TestSuite testSuite = new TestSuite("", testSuiteName, testSuiteName);
		testSuite.setStartTime(Calendar.getInstance().getTime());
		testSuite.setResult("IN_QUEUE");
		plan.getTestSuites().add(testSuite);
		plan.getTestSuitCount().incrementAndGet();
		pushData();
	}

	public static void createTestCase(String testcaseName, String uniqueId) {
		TestCase testCase = new TestCase();
		testCase.setName(testcaseName);
		testCase.setStartTime(Calendar.getInstance().getTime());
		testCase.setDescription(testcaseName);
		testCase.setParentId(plan.getTestSuites().get(0).getName());
		testCase.setUniqueId(uniqueId);
		testCase.setSteps(new ArrayList<>());
		testCase.setResult("IN_QUEUE");

		plan.getTestSuites().get(0).getTestCases().add(testCase);
		plan.getTestSuites().get(0).getTestsCount().incrementAndGet();
		plan.getTestCaseCount().incrementAndGet();
		pushData();
	}

	public static void createStep(String testCaseUniqueId, String testcaseName, String stepName, String stepDescription,
			Status status, Date startTime, Date endTime, String fileName) {
		plan.getTestSuites().get(0).getTestCases().stream()
				.filter(aCase -> aCase.getUniqueId().equals(testCaseUniqueId)).findFirst().ifPresent(aCase -> {
					TestStep step1 = new TestStep();
					step1.setName(stepName);
					step1.setStartTime(startTime);
					step1.setEndTime(endTime);

					step1.setDuration(endTime.getTime() - startTime.getTime());
					step1.setUniqueId(String.format("%s.%s", testCaseUniqueId, stepName));
					step1.setParentId(testCaseUniqueId);

					step1.setDescription(stepDescription);
					step1.setExceptionStacktrace(null);
					step1.setRemarks(null);

					String result = getStepStatus(status);
					step1.setResult(result);
					if (!stepName.contains("DriverScript")) {
						LogAndImageProcessor processor = takeScreenShot(status, fileName, stepDescription);
						step1.setAttachments(processor.getAttachments());
						step1.setLogs(processor.getLogs());
					}
					aCase.getSteps().add(step1);
				});
	}

	public static void createStep(String testCaseUniqueId, String testcaseName, String stepName, String stepDescription,
			Status status, Date startTime, Date endTime, String fileName,TestStepsPerformance stepPT) {
		plan.getTestSuites().get(0).getTestCases().stream()
				.filter(aCase -> aCase.getUniqueId().equals(testCaseUniqueId)).findFirst().ifPresent(aCase -> {
					TestStep step1 = new TestStep();
					step1.setName(stepName);
					step1.setStartTime(startTime);
					step1.setEndTime(endTime);

					step1.setDuration(endTime.getTime() - startTime.getTime());
					step1.setUniqueId(String.format("%s.%s", testCaseUniqueId, stepName));
					step1.setParentId(testCaseUniqueId);

					step1.setDescription(stepDescription);
					step1.setExceptionStacktrace(null);
					step1.setRemarks(null);

					String result = getStepStatus(status);
					step1.setResult(result);
					if (!stepName.contains("DriverScript")) {
						LogAndImageProcessor processor = takeScreenShot(status, fileName, stepDescription);
						step1.setAttachments(processor.getAttachments());
						step1.setLogs(processor.getLogs());
					}

					step1.setPerformanceMetrics(stepPT);
					aCase.getSteps().add(step1);
				});
	}

	public static void createStep(String testCaseUniqueId, String testcaseName, String stepName, String stepDescription,
			Status status, Date startTime, Date endTime, String fileName, Exception stackTrace, String errorMessage) {
		plan.getTestSuites().get(0).getTestCases().stream()
				.filter(aCase -> aCase.getUniqueId().equals(testCaseUniqueId)).findFirst().ifPresent(aCase -> {
					TestStep step1 = new TestStep();
					step1.setName(stepName);
					step1.setStartTime(startTime);
					step1.setEndTime(endTime);

					step1.setDuration(endTime.getTime() - startTime.getTime());
					step1.setUniqueId(String.format("%s.%s", testCaseUniqueId, stepName));
					step1.setParentId(testCaseUniqueId);

					step1.setDescription(null);
					StringWriter errors = new StringWriter();
					stackTrace.printStackTrace(new PrintWriter(errors));

					step1.setExceptionStacktrace(errors.toString());
					step1.setRemarks(errorMessage);

					String result = getStepStatus(status);
					step1.setResult(result);
					if (!stepName.contains("DriverScript")) {
						LogAndImageProcessor processor = takeScreenShot(status, fileName, stepDescription);

						step1.setAttachments(processor.getAttachments());
						step1.setLogs(processor.getLogs());
					}
					aCase.getSteps().add(step1);
				});
	}

	public static void updateTestCase(String testCaseUniqueId, String testcaseName, String status) {
		Date endTime = Calendar.getInstance().getTime();
		plan.getTestSuites().get(0).getTestCases().stream()
				.filter(aCase -> aCase.getUniqueId().equals(testCaseUniqueId)).findFirst().ifPresent(aCase -> {
					aCase.setEndTime(endTime);
					aCase.setDuration(endTime.getTime() - aCase.getStartTime().getTime());
					String result = getStepStatus(Status.valueOf(status));
					aCase.setResult(result);

					if (result.equals("PASSED")) {
						plan.getTestSuites().get(0).getPassed().incrementAndGet();
						plan.getPassed().incrementAndGet();
					} else if (result.equals("FAILED")) {
						Optional<TestStep> failureStep = aCase.getSteps().stream()
								.filter(testStep -> "FAILED".equals(testStep.getResult())).findFirst();
						if (failureStep.isPresent()) {
							aCase.setExceptionStacktrace(failureStep.get().getExceptionStacktrace());
							aCase.setRemarks(failureStep.get().getRemarks());
						}
						plan.getTestSuites().get(0).getFailed().incrementAndGet();
						plan.getFailed().incrementAndGet();
					} else {
						plan.getTestSuites().get(0).getSkipped().incrementAndGet();
						plan.getSkipped().incrementAndGet();
					}
				});
		pushData();
	}

	public static void finish() {

		plan.getTestSuites().get(0).setEndTime(Calendar.getInstance().getTime());
		plan.getTestSuites().get(0).setDuration(new AtomicLong(
				Calendar.getInstance().getTimeInMillis() - plan.getTestSuites().get(0).getStartTime().getTime()));
		plan.setEndTime(Calendar.getInstance().getTime());
		plan.setDuration(Calendar.getInstance().getTimeInMillis() - plan.getStartTime().getTime());
		plan.getTestSuites().get(0).setResult("PASS");

		validatePlan();

		pushData();
	}

	private static void validatePlan() {
		TestSuite testSuite = plan.getTestSuites().get(0);
		testSuite.getTestCases().stream().forEach(testCase -> {
			if (testCase.getResult().equals(StatusEnum.IN_QUEUE.name())
					|| testCase.getResult().equals(StatusEnum.IN_PROGRESS.name())) {
				testCase.setResult(StatusEnum.FAILED.name());
				testSuite.getFailed().incrementAndGet();
				testSuite.setResult(StatusEnum.FAILED.name());
				plan.getFailed().incrementAndGet();
			}
		});
		plan.setTestSuites(Collections.singletonList(testSuite));
	}

	public static void pushData() {
		System.out.println("inside pushdata");
		System.out.println(plan);
		ExecutiveTestPlan executiveTestPlan = ClientManager.pushData(plan);
		plan.setId(executiveTestPlan.getId());
		plan.setExecutionNumber(executiveTestPlan.getExecutionNumber());
//		
//	       FileWriter fWriter;
//		try {
//			fWriter = new FileWriter("C:\\CCTP FT-PT\\postjson.json");
//			fWriter.write(plan.toString());
//			 fWriter.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	           
	}

	private static String getStepStatus(Status status) {
		switch (status) {
		case DONE:
		case PASS:
		case SCREENSHOT:
		case DEBUG:
		case WARNING:
			return StatusEnum.PASSED.name();
		case FAIL:
			return StatusEnum.FAILED.name();
		default:
			return StatusEnum.SKIPPED.name();
		}
	}

	private static LogAndImageProcessor takeScreenShot(Status status, String fileName, String stepDescription) {
		LogAndImageProcessor processor = new LogAndImageProcessor();
		if (fileName == null || "".equals(fileName))
			return processor;
		switch (status) {
		case PASS:
		case FAIL:
		case SCREENSHOT:
			File file = new File(fileName);
			if (file.exists()) {
				processor.processStaticLogs(file, stepDescription, Level.INFO);
			}
			break;
		case DONE:
		case DEBUG:
		case WARNING:
		default:
			break;
		}
		return processor;
	}

	public static String getResourcePath() {
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		String resourcePath = relativePath + Util.getFileSeparator() + "src" + Util.getFileSeparator() + "test"
				+ Util.getFileSeparator() + "resources" + Util.getFileSeparator();
//        String resourcePath =
//                relativePath + Util.getFileSeparator() + "target" + Util.getFileSeparator() + "test-classes" + Util.getFileSeparator();
		return resourcePath;
	}

}
