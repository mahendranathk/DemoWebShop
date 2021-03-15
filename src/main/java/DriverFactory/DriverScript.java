package DriverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunctionLibrary.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {

    WebDriver driver;
    ExtentReports report;
    ExtentTest logger;

    public void startTest() throws Throwable {
        ExcelFileUtil excel = new ExcelFileUtil();

        // Module Sheet
        for (int i = 1; i <= excel.rowCount("MasterTestCases"); i++) {

            String ModuleStatus = "";

            if (excel.getData("MasterTestCases", i, 2).equalsIgnoreCase("Y")) {
                // Define Module Name
                String TCModule = excel.getData("MasterTestCases", i, 1);

                // Generating Reports
                report = new ExtentReports("./Reports/" + TCModule + ".html" + "_" + FunctionLibrary.generateDate());

                logger = report.startTest(TCModule);

                int rowcount = excel.rowCount(TCModule);

                // Corresponding Sheets
                for (int j = 1; j <= rowcount; j++) {
                    String Description = excel.getData(TCModule, j, 0);
                    String Object_Type = excel.getData(TCModule, j, 1);
                    String Locator_Type = excel.getData(TCModule, j, 2);
                    String Locator_Value = excel.getData(TCModule, j, 3);
                    String Test_Data = excel.getData(TCModule, j, 4);

                    try {

                        if (Object_Type.equalsIgnoreCase("startBrowser")) {
                            driver = FunctionLibrary.startBrowser(driver);
                            logger.log(LogStatus.INFO, Description);
                        }

                        if (Object_Type.equalsIgnoreCase("openApplication")) {
                            FunctionLibrary.openApplication(driver);
                            logger.log(LogStatus.INFO, Description);
                        }

                        if (Object_Type.equalsIgnoreCase("closeApplication")) {
                            FunctionLibrary.closeApplication(driver);
                            logger.log(LogStatus.INFO, Description);
                        }

                        if (Object_Type.equalsIgnoreCase("clickAction")) {
                            FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
                            logger.log(LogStatus.INFO, Description);
                        }

                        if (Object_Type.equalsIgnoreCase("typeAction")) {
                            FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
                            logger.log(LogStatus.INFO, Description);
                        }

                        if (Object_Type.equalsIgnoreCase("waitForElement")) {
                            FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value);
                            logger.log(LogStatus.INFO, Description);
                        }

                        if (Object_Type.equalsIgnoreCase("titleValidation")) {
                            FunctionLibrary.titleValidation(driver, Test_Data);
                            logger.log(LogStatus.INFO, Description);
                        }

                        if (Object_Type.equalsIgnoreCase("captureData")) {
                            FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
                            logger.log(LogStatus.INFO, Description);
                        }


                        if (Object_Type.equalsIgnoreCase("selectAction")) {
                            FunctionLibrary.selectAction(driver, Locator_Type, Locator_Value, Test_Data);
                            logger.log(LogStatus.INFO, Description);
                        }
                        if (Object_Type.equalsIgnoreCase("getCapturedData")) {
                            FunctionLibrary.getCapturedData();
                            logger.log(LogStatus.INFO, Description);
                        }
                        if (Object_Type.equalsIgnoreCase("verifyOrderSubTotal")) {
                            FunctionLibrary.verifyOrderSubTotal(driver);
                            logger.log(LogStatus.INFO, Description);
                        }
                        excel.setData(TCModule, j, 5, "Pass");

                        ModuleStatus = "true";

                        logger.log(LogStatus.PASS, Description + " Pass");

                    } catch (Exception e) {
                        excel.setData(TCModule, j, 5, "Fail");

                        ModuleStatus = "false";

                        logger.log(LogStatus.FAIL, Description + "  Fail");

                        // Generating Screenshots
                        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                        FileUtils.copyFile(scrFile, new File("./Screenshots/" + Description + "_" + FunctionLibrary.generateDate() + ".jpg"));

                        break;
                    } catch (AssertionError e) {
                        excel.setData(TCModule, j, 5, "Fail");

                        ModuleStatus = "false";

                        logger.log(LogStatus.FAIL, Description + "  Fail");

                        // Generating Screenshots
                        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                        FileUtils.copyFile(scrFile, new File("./Screenshots/" + Description + "_" + FunctionLibrary.generateDate() + ".jpg"));

                        break;
                    }

                }

                if (ModuleStatus.equalsIgnoreCase("true")) {
                    excel.setData("MasterTestCases", i, 3, "Pass");
                } else {
                    excel.setData("MasterTestCases", i, 3, "Fail");
                }
            } else {
                excel.setData("MasterTestCases", i, 3, "Not Executed");
            }

            report.endTest(logger);

            report.flush();
            driver.quit();
        }
    }
}	