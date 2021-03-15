package CommonFunctionLibrary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Utilities.ProductDetails;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.PropertyFileUtil;

import javax.swing.*;

public class FunctionLibrary {


    ProductDetails productDetails = new ProductDetails();


    /*
     To open the browser based on the value for the key
     */
    public static WebDriver startBrowser(WebDriver driver) throws Throwable, Throwable {
        if (PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("Firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("Chrome")) {
            WebDriverManager.chromedriver().setup();
            // System.setProperty("webdriver.chrome.driver", "CommonJarFiles/chromedriver.exe");
            driver = new ChromeDriver();
        } else if (PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("IE")) {
            WebDriverManager.iedriver().setup();
            // System.setProperty("webdriver.ie.driver", "CommonJarFiles/IEDriverServer.exe");
            driver = new InternetExplorerDriver();
        }
        return driver;
    }

    /*
    To open the application with the URL loaded from the property file
     */

    public static void openApplication(WebDriver driver) throws Throwable, Throwable {
        driver.manage().window().maximize();
        driver.get(PropertyFileUtil.getValueForKey("URL"));
    }

    /*
    To close the application
     */
    public static void closeApplication(WebDriver driver) {
        driver.close();
    }

    /*
    To perform the click action in the application
     */
    public static void clickAction(WebDriver driver, String locatorType, String locatorValue) {
        if (locatorType.equalsIgnoreCase("id")) {
            driver.findElement(By.id(locatorValue)).click();
        } else if (locatorType.equalsIgnoreCase("name")) {
            driver.findElement(By.name(locatorValue)).click();
        } else if (locatorType.equalsIgnoreCase("xpath")) {
            driver.findElement(By.xpath(locatorValue)).click();
        }
    }

    /*
    To perform the type action in the application
     */
    public static void typeAction(WebDriver driver, String locatorType, String locatorValue, String data) {
        if (locatorType.equalsIgnoreCase("id")) {
            driver.findElement(By.id(locatorValue)).clear();
            driver.findElement(By.id(locatorValue)).sendKeys(data);
        } else if (locatorType.equalsIgnoreCase("name")) {
            driver.findElement(By.name(locatorValue)).clear();
            driver.findElement(By.name(locatorValue)).sendKeys(data);
        } else if (locatorType.equalsIgnoreCase("xpath")) {
            driver.findElement(By.xpath(locatorValue)).clear();
            driver.findElement(By.xpath(locatorValue)).sendKeys(data);
        }
    }

    /*
    To achieve the synchronization
     */
    public static void waitForElement(WebDriver driver, String locatorType, String locatorValue) {
        WebDriverWait wait = new WebDriverWait(driver, 20);

        if (locatorType.equalsIgnoreCase("id")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorValue)));
        } else if (locatorType.equalsIgnoreCase("name")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorValue)));
        } else if (locatorType.equalsIgnoreCase("xpath")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorValue)));
        }
    }

    /*
    To perform the title validation for a page
     */
    public static void titleValidation(WebDriver driver, String exp_title) {
        String act_title = driver.getTitle();

        Assert.assertEquals(exp_title, act_title);
    }
    /*
    To generate the date
     */
    public static String generateDate() {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_ss");

        return sdf.format(date);
    }

    /*
    To capture the data from the field in the application
     */
    public static void captureData(WebDriver driver, String locatorType, String locatorValue) throws Throwable {
        Thread.sleep(1000);

        String data = "";

        if (locatorType.equalsIgnoreCase("id")) {
            data = driver.findElement(By.id(locatorValue)).getAttribute("value");
        } else if (locatorType.equalsIgnoreCase("xpath")) {
            data = driver.findElement(By.xpath(locatorValue)).getText();
        }
        System.out.println("Data is "+data);

        FileWriter fw = new FileWriter("./CapturedData/Data.txt");

        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(data);

        bw.flush();

        bw.close();

    }


    /*
    To perform the select action in the application
     */
    public static void selectAction(WebDriver driver, String locatorType, String locatorValue, String data) {
        WebElement element = null;
        if (locatorType.equalsIgnoreCase("id")) {
            element = driver.findElement(By.id(locatorValue));
        } else if (locatorType.equalsIgnoreCase("name")) {
            element = driver.findElement(By.name(locatorValue));
        } else if (locatorType.equalsIgnoreCase("xpath")) {
            element = driver.findElement(By.xpath(locatorValue));
        }
        Select dropDown = new Select(element);
        try {
            dropDown.selectByVisibleText(data);
        } catch (Exception e) {
            dropDown.selectByValue(data);
        }

    }

    /*
    To sotre the data from the field in the application
     */

    public static void storeData(WebDriver driver, String locatorType, String locatorValue) {
        if (locatorType.equalsIgnoreCase("id")) {
            driver.findElement(By.id(locatorValue)).click();
        } else if (locatorType.equalsIgnoreCase("name")) {
            driver.findElement(By.name(locatorValue)).click();
        } else if (locatorType.equalsIgnoreCase("xpath")) {
            driver.findElement(By.xpath(locatorValue)).click();
        }
    }
    /*
    To retrieve the data that is captured
     */
    public static void getCapturedData() {
        System.out.println("Data value is "+String.valueOf(ProductDetails.productPrice));
    }
    /*
    To verify the order subtotal
     */

    public static void verifyOrderSubTotal(WebDriver driver) {
        By productPrice = By.xpath("//span[@class='product-unit-price']");
        By productQty = By.xpath("//input[contains(@name,'itemquantity')]");
        By subTotal = By.xpath("//span[text()='Sub-Total:']/parent::td/following-sibling::td//span[@class='product-price']");

        String productPriceValue = driver.findElement(productPrice).getText();
        String productQtyValue = driver.findElement(productQty).getAttribute("value");
        String productSubTotalValue = driver.findElement(subTotal).getText();
        System.out.println("product price is: "+productPriceValue);
        System.out.println("product qty value: "+productQtyValue);
        System.out.println("product sub total value: "+productSubTotalValue);
        double orderSubTotal = Double.parseDouble(productPriceValue)*Double.parseDouble(productQtyValue);
        System.out.println("Expected sub total is: "+String.valueOf(orderSubTotal));
        Assert.assertEquals(orderSubTotal, Double.parseDouble(productSubTotalValue));
    }
}