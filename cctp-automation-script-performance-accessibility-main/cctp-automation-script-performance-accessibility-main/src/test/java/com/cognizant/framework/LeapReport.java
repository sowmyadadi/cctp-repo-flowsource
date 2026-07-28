package com.cognizant.framework;

import com.cognizant.framework.selenium.CraftDriver;
import com.cognizant.framework.selenium.SeleniumTestParameters;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.JavascriptExecutor;
import org.seleniumhq.jetty7.util.ajax.JSON;

import com.cognizant.reportclient.plugins.models.*;

import org.json.JSONArray;
import org.json.JSONObject;
import com.cognizant.craft.DriverScript;


class LeapReport implements ReportType {

	private ReportSettings reportSettings;
	private Date startTime;
	private SeleniumTestParameters testParameters;

	public LeapReport(ReportSettings reportSettings) {
		this.reportSettings = reportSettings;
	}

	@Override
	public void initializeResultSummary() {
		CRAFTPlugin.start();
	}

	@Override
	public void addResultSummarySubHeading(String subHeading1, String subHeading2, String subHeading3,
			String subHeading4) {

		if (subHeading1.equalsIgnoreCase("Run Configuration")) {
			CRAFTPlugin.createTestSuite(subHeading2.replace(":", "").trim(), "");
		}
	}

	@Override
	public void initializeTestLog(SeleniumTestParameters testParameters) {

		this.testParameters = testParameters;
		// Below Test case name, we can even pass ScenarioName along with Test Instance
		// for unique ID
		String testcaseName = testParameters.getCurrentTestcase();// reportSettings.getReportName().split("_")[1];
		String testCaseUniqueId = testParameters.getTestCaseUniqueId();
		CRAFTPlugin.createTestCase(testcaseName, testCaseUniqueId);
		startTime = Calendar.getInstance().getTime();
	}

	@Override
	public void updateTestLog(String stepNumber, String stepName, String stepDescription, Status stepStatus,
			String screenshotName) {
		if ((stepName != "CRAFT Info") && (stepStatus != Status.DEBUG)) {
			Date endTime = Calendar.getInstance().getTime();
			String screenshotPath = reportSettings.getReportPath() + Util.getFileSeparator() + "Screenshots"
					+ Util.getFileSeparator() + screenshotName;
			String testcaseName = testParameters.getCurrentTestcase();// reportSettings.getReportName().split("_")[1];
			String testCaseUniqueId = testParameters.getTestCaseUniqueId();
			CRAFTPlugin.createStep(testCaseUniqueId, testcaseName, stepName, stepDescription, stepStatus, startTime,
					endTime, screenshotPath);
			startTime = endTime;
		}
	}

	public void updateTestLog(String stepNumber, String stepName, String stepDescription, Status stepStatus,
			String screenshotName, CraftDriver driver) {
		System.out.println("updateTestLog for performance ");
		JavascriptExecutor js = (JavascriptExecutor) driver.getWebDriver();
		String pagehtml = driver.getPageSource().toString();
		long navigationStart = -1;
		long redirectStart = -1;
		long redirectEnd = -1;
		long unloadEventStart = -1;
		long unloadEventEnd = -1;
		long loadEventEnd = -1;
		long fetchStart = -1;
		long connectEnd = -1;
		long connectStart = -1;
		long domainLookupEnd = -1;
		long domainLookupStart = -1;
		long requestStart = -1;
		long responseStart = -1;
		long domInteractive = -1;
		long domLoading = -1;
		JSONArray msFirstPaint;
		long first_paint = 0;
		long first_contentful_paint = 0;
		long responseEnd = -1;
		long domContentLoadedEventStart = -1;
		long domComplete = -1;
		long domContentLoadedEventEnd = -1;
		long loadEventStart = -1;
		String resourceAPI;

		try {
			navigationStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.navigationStart;").toString())
					.doubleValue();
		} catch (Exception e) {
			navigationStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.fetchStart;").toString()).doubleValue();
		}

		try {
			navigationStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.navigationStart;").toString())
					.doubleValue();
		} catch (Exception e) {
			navigationStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.fetchStart;").toString()).doubleValue();
		}

		try {
			redirectStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.redirectStart;").toString())
					.doubleValue();

		} catch (Exception e) {
			redirectStart = -1;
		}

		try {
			redirectEnd = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.redirectEnd;").toString())
					.doubleValue();

		} catch (Exception e) {
			redirectEnd = -1;
		}

		try {
			unloadEventStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.unloadEventStart;").toString())
					.doubleValue();

		} catch (Exception e) {
			unloadEventStart = -1;
		}

		try {
			unloadEventEnd = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.unloadEventEnd;").toString())
					.doubleValue();

		} catch (Exception e) {
			unloadEventEnd = -1;
		}

		try {
			fetchStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.fetchStart;").toString()).doubleValue();

			if (navigationStart <= 0) {
				navigationStart = fetchStart;
			}
		} catch (Exception e) {
			fetchStart = -1;
		}

		try {
			connectEnd = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.connectEnd;").toString()).doubleValue();

		} catch (Exception e) {
			connectEnd = -1;
		}

		try {
			connectStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.connectStart;").toString())
					.doubleValue();

		} catch (Exception e) {
			connectStart = -1;
		}

		try {
			domainLookupEnd = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.domainLookupEnd;").toString())
					.doubleValue();

		} catch (Exception e) {
			domainLookupEnd = -1;
		}

		try {
			domainLookupStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.domainLookupStart;").toString())
					.doubleValue();

		} catch (Exception e) {
			domainLookupStart = -1;
		}

		try {
			requestStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.requestStart;").toString())
					.doubleValue();

		} catch (Exception e) {
			requestStart = -1;
		}
		try {
			responseStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.responseStart;").toString())
					.doubleValue();

		} catch (Exception e) {
			responseStart = -1;
		}
		try {
			domInteractive = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.domInteractive;").toString())
					.doubleValue();

		} catch (Exception e) {
			domInteractive = -1;
		}

		try {
			domLoading = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.domLoading;").toString()).doubleValue();

		} catch (Exception e) {
			domLoading = -1;
		}

		try {
			responseEnd = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.responseEnd;").toString())
					.doubleValue();
		} catch (Exception e) {
			responseEnd = -1;
		}

		try {
			domContentLoadedEventStart = (long) Double
					.valueOf(
							js.executeScript("return window.performance.timing.domContentLoadedEventStart;").toString())
					.doubleValue();
		} catch (Exception e) {
			domContentLoadedEventStart = -1;
		}

		try {
			domComplete = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.domComplete;").toString())
					.doubleValue();
		} catch (Exception e) {
			domComplete = -1;
		}

		try {
			domContentLoadedEventEnd = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.domContentLoadedEventEnd;").toString())
					.doubleValue();
		} catch (Exception e) {
			domContentLoadedEventEnd = -1;
		}

		try {
			loadEventStart = (long) Double
					.valueOf(js.executeScript("return window.performance.timing.loadEventStart;").toString())
					.doubleValue();
			// System.out.println(" loadEventStart: " + loadEventStart);
		} catch (Exception e) {
			loadEventStart = -1;
		}
		loadEventEnd = (long) Double
				.valueOf(js.executeScript("return window.performance.timing.loadEventEnd;").toString()).doubleValue();
		resourceAPI = (String) js.executeScript("return JSON.stringify(performance.getEntriesByType('resource'))");
		//System.out.println(resourceAPI);
		System.out.println(" loadEventEnd : " + loadEventEnd);

		String paintTemp = (String) js.executeScript("return JSON.stringify(performance.getEntriesByType('paint'))");
		//System.out.println(paintTemp);
		msFirstPaint = new JSONArray(paintTemp);
		
		for (int i = 0; i < msFirstPaint.length(); i++) {
			JSONObject temp = msFirstPaint.getJSONObject(i);
			if(temp.get("name").equals("first-paint")) {
				first_paint=((Number)temp.get("startTime")).longValue();			
			}else {
				first_contentful_paint=((Number) temp.get("startTime")).longValue();
			}
		}
		
		JSONArray resourceTime = new JSONArray(resourceAPI);
		int requestCount = resourceTime.length();
		int pageSize = 0;
		
		int temp = 0, script = 0, link = 0,img = 0,video = 0;
		int css = 0,textxml = 0, iframe = 0, other = 0;	
		
		System.out.println(resourceTime.length());
		System.out.println(new Date());
		for(int i = 0; i < resourceTime.length(); i++) {
//			JSONObject temp = resourceTime.getJSONObject(i);
//			System.out.println(temp);
			JSONObject obj = resourceTime.getJSONObject(i);

			Integer size = (Integer) obj.get("transferSize");
			temp = temp + size;

			String type = (String) obj.get("initiatorType");

			if (type.equalsIgnoreCase("script")) {
				script++;
			} else if (type.equalsIgnoreCase("link")) {
				link++;
			}
			else if (type.equalsIgnoreCase("img")) {
				img++;
			} else if (type.equalsIgnoreCase("video")) {
				video++;
			} else if (type.equalsIgnoreCase("css")) {
				css++;
			} else if (type.equalsIgnoreCase("xmlhttprequest")) {
				textxml++;
			} else if (type.equalsIgnoreCase("iframe")) {
				iframe++;
			} else {
				other++;
			}
		}
		System.out.println(new Date());
//		PageProfiling.put("image", img);
//		PageProfiling.put("video",video );
//		PageProfiling.put("css",css );
//		PageProfiling.put("script", script);
//		PageProfiling.put("xmlhttprequest", textxml);
//		PageProfiling.put("link",link );
//		PageProfiling.put("iframe", iframe);
//		PageProfiling.put("other",other );
//		PageProfiling.put("pagesize",temp );	
//		PageProfiling.put("requestCount",requestCount );
		
		PageProfiling PageProfile=new PageProfiling();

		PageProfile.setCss(css);
		PageProfile.setIframe(iframe);
		PageProfile.setImage(img);
		PageProfile.setLink(link);
		PageProfile.setOther(other);
		PageProfile.setPagesize(temp);
		PageProfile.setRequestCount(requestCount);
		PageProfile.setScript(script);
		PageProfile.setVideo(video);
		PageProfile.setXmlhttprequest(textxml);

		TestStepsPerformance stepPT = new TestStepsPerformance();

		stepPT.setNavigationStart(navigationStart);
		stepPT.setRedirectStart(redirectStart);
		stepPT.setRedirectEnd(redirectEnd);
		stepPT.setUnloadEventStart(unloadEventStart);
		stepPT.setUnloadEventEnd(unloadEventEnd);
		stepPT.setLoadEventStart(loadEventStart);
		stepPT.setLoadEventEnd(loadEventEnd);
		stepPT.setFetchStart(fetchStart);
		stepPT.setConnectStart(connectStart);
		stepPT.setConnectEnd(connectEnd);
		stepPT.setDomainLookupStart(domainLookupStart);
		stepPT.setDomainLookupEnd(domainLookupEnd);
		stepPT.setRequestStart(requestStart);
		stepPT.setResponseStart(responseStart);
		stepPT.setResponseEnd(responseEnd);
		stepPT.setDomInteractive(domInteractive);
		stepPT.setDomLoading(domLoading);
		stepPT.setDomComplete(domComplete);
		stepPT.setDomContentLoadedEventStart(domContentLoadedEventStart);
		stepPT.setDomContentLoadedEventEnd(domContentLoadedEventEnd);
		stepPT.setFirst_contentful_paint(first_contentful_paint);
		stepPT.setFirst_paint(first_paint);
		stepPT.setPageProfiling(PageProfile);
		stepPT.setPageHtml(pagehtml);
		

		
//		JSONObject performancesMetrics = new JSONObject();
//		performancesMetrics.put("navigationStart", navigationStart);
//		performancesMetrics.put("redirectStart", redirectStart);
//		performancesMetrics.put("redirectEnd", redirectEnd);
//		performancesMetrics.put("unloadEventStart", unloadEventStart);
//		performancesMetrics.put("unloadEventEnd", unloadEventEnd);
//		performancesMetrics.put("loadEventEnd", loadEventEnd);
//		performancesMetrics.put("fetchStart", fetchStart);
//		performancesMetrics.put("connectEnd", connectEnd);
//		performancesMetrics.put("connectStart", connectStart);
//		performancesMetrics.put("domainLookupEnd", domainLookupEnd);
//		performancesMetrics.put("domainLookupStart", domainLookupStart);
//		performancesMetrics.put("requestStart", requestStart);
//		performancesMetrics.put("responseStart", responseStart);
//		performancesMetrics.put("domInteractive", domInteractive);
//		performancesMetrics.put("domLoading", domLoading);
//		performancesMetrics.put("first_paint", first_paint);
//		performancesMetrics.put("first_contentful_paint", first_contentful_paint);		
//		performancesMetrics.put("responseEnd", responseEnd);
//		performancesMetrics.put("domContentLoadedEventStart", domContentLoadedEventStart);
//		performancesMetrics.put("domComplete", domComplete);
//		performancesMetrics.put("domContentLoadedEventEnd", domContentLoadedEventEnd);
//		performancesMetrics.put("loadEventStart", loadEventStart);
//		performancesMetrics.put("pageProfiling", PageProfiling);
//		performancesMetrics.put("pageHtml", "");  //pagehtml
		
		if ((stepName != "CRAFT Info") && (stepStatus != Status.DEBUG)) {
			Date endTime = Calendar.getInstance().getTime();
			String screenshotPath = reportSettings.getReportPath() + Util.getFileSeparator() + "Screenshots"
					+ Util.getFileSeparator() + screenshotName;
			String testcaseName = testParameters.getCurrentTestcase();// reportSettings.getReportName().split("_")[1];
			String testCaseUniqueId = testParameters.getTestCaseUniqueId();
			CRAFTPlugin.createStep(testCaseUniqueId, testcaseName, stepName, stepDescription, stepStatus, startTime,
					endTime, screenshotPath, stepPT);
			startTime = endTime;
		}

	}

	@Override
	public void updateTestLog(String stepNumber, String stepName, String stepDescription, Exception stackTrace,
			String errorMessage, Status stepStatus, String screenshotName) {
		if ((stepName != "CRAFT Info") && (stepStatus != Status.DEBUG)) {
			Date endTime = Calendar.getInstance().getTime();
			String screenshotPath = reportSettings.getReportPath() + Util.getFileSeparator() + "Screenshots"
					+ Util.getFileSeparator() + screenshotName;
			String testcaseName = testParameters.getCurrentTestcase();// reportSettings.getReportName().split("_")[1];
			String testCaseUniqueId = testParameters.getTestCaseUniqueId();
			CRAFTPlugin.createStep(testCaseUniqueId, testcaseName, stepName, stepDescription, stepStatus, startTime,
					endTime, screenshotPath, stackTrace, errorMessage);
			startTime = endTime;
		}
	}

	@Override
	public void updateResultSummary(TestParameters testParameters, String testReportName, String executionTime,
			String testStatus) {
		String status;
		if (testStatus.equalsIgnoreCase("PASSED")) {
			status = "PASS";
		} else if (testStatus.equalsIgnoreCase("FAILED")) {
			status = "FAIL";
		} else {
			status = "DONE";
		}
		String testCaseUniqueId = testParameters.getTestCaseUniqueId();
		CRAFTPlugin.updateTestCase(testCaseUniqueId, testParameters.getCurrentTestcase().toString(), status);
	}

	@Override
	public void addResultSummaryFooter(String totalExecutionTime, int nTestsPassed, int nTestsFailed) {
		CRAFTPlugin.finish();
	}

	/***
	 * UnUsed for Leap Reporting
	 */

	@Override
	public void addTestLogSection(String section) {

	}

	@Override
	public void addTestLogSubSection(String subSection) {

	}

	@Override
	public void addTestLogFooter(String executionTime, int nStepsPassed, int nStepsFailed) {

	}

	@Override
	public void addResultSummaryHeading(String heading) {

	}

	@Override
	public void addResultSummaryTableHeadings() {

	}

	@Override
	public void updateTestLog(String string, String endPoint, Object expectedValue, Object actualValue,
			Status stepStatus) {

	}

	@Override
	public void addTestLogHeading(String heading) {

	}

	@Override
	public void addTestLogSubHeading(String subHeading1, String subHeading2, String subHeading3, String subHeading4) {

	}

	@Override
	public void addTestLogTableHeadings() {

	}
}
