package com.qa.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.qa.base.Base;
import com.qa.page.AmazonPage;
import com.qa.util.CommonUtil;

public class AmazonPageTest extends Base {

	//Create Object of MainPage where OR and methods are maintained
	AmazonPage mainPageObj=new AmazonPage();
	public static ExtentHtmlReporter pathHtml;
	public static ExtentReports exReport;
	public static ExtentTest exLog,exLog1,exLog2,exLog3,exLog4,exLog5,exLog6,exLog7,exLog8;

	//Prerequisite steps to be executed prior executing actual TestCases

	@BeforeTest
	public void basicSetUp()
	{
		DriversetUp();
		pathHtml=new ExtentHtmlReporter(System.getProperty("user.dir")+prop.getProperty("ReportPath"));
		exReport=new ExtentReports();
		exReport.attachReporter(pathHtml);

	}

	// Launch URL TestCase

	@Test(priority=1)
	public void launchURL(){
		String pageTitle=LaunchBrowser();
		mainPageObj.initializeWebElement();
		exLog=exReport.createTest("Verify user can launch URL", "Automation");
		if (pageTitle.equals("Amazon.de Die Website wurde nicht gefunden"))
		{
			exLog.log(Status.PASS,"URL is launched successfully") ;
		}

		else

		{
			exLog.log(Status.FAIL,"Failed to launch URL") ;
		}
	}



	@DataProvider
	public Object[][] passSheet()
	{
		Object[][] val=CommonUtil.readDataFromExcel("InputData");
		return val;
	}


	// TestCase to verify user is able to Login in application

	@Test(priority=2,dataProvider="passSheet")
	@Parameters({"userName","password"})
	public void loginAmazon(String userName ,String password )
	{
		mainPageObj.login(userName, password);
		String mainPageTitle=driver.getTitle();
		exLog1=exReport.createTest("Verify user logged in app", "Automation");
		if (mainPageTitle.equals("Amazon Anmelden"))
		{
			exLog1.log(Status.PASS,"User logged in successfully") ;
		}
		else
		{
			exLog1.log(Status.FAIL,"Failed to logged in ") ;
		}

	}

	//Test Case to search for “Drucker" and select the first product from list 

	@Test(priority=3)
	@Parameters({"productName","expectedProduct"})
	public void selectFirstProduct(String productName ,int expectedProduct )
	{
		Boolean vSearchProductSelected=mainPageObj.enterAndSearchProductName(productName, expectedProduct);
		exLog2=exReport.createTest("Verify user is able search and select first product", "Automation");
		if (vSearchProductSelected)
		{
			exLog2.log(Status.PASS,"User successfully searched and selected the first product") ;
		}
		else
		{
			exLog2.log(Status.FAIL,"Failed to search and select the first product") ;
		}
	}

	// TestCase to add first product to basket

	@Test(priority=4)
	public void addFirstProductToCart()
	{
		mainPageObj.addToCart();
		String noOfProductaAddedToCart = null;
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='nav-cart-count-container']//span[@id='nav-cart-count']")));
			WebElement cartQty=driver.findElement(By.xpath("//div[@id='nav-cart-count-container']//span[@id='nav-cart-count']"));
			noOfProductaAddedToCart=cartQty.getText();
		}catch(Exception e)
		{
			System.out.println(e);
		}
		exLog3=exReport.createTest("Click on cart button", "Automation");
		if (noOfProductaAddedToCart.equals("1"))
		{
			exLog3.log(Status.PASS,"User successfully clicked on cart button") ;
		}
		else
		{
			exLog3.log(Status.FAIL,"Failed clicked on cart button ") ;
		}
	}

	//TestCase to search for “Drucker” and  select the second product from list 

	@Test(priority=5)
	@Parameters({"productName","expectedProduct1"})
	public void selectSecondProduct(String productName ,int expectedProduct1 )
	{
		Boolean vSearchProductSelected=mainPageObj.enterAndSearchProductName(productName, expectedProduct1);
		exLog4=exReport.createTest("Verify user is able to search and select second product", "Automation");
		if (vSearchProductSelected)
		{
			exLog4.log(Status.PASS,"User successfully searched and selected the second product") ;
		}
		else
		{
			exLog4.log(Status.FAIL,"Failed to search and select the second product") ;
		}
	}

	// TestCase to add second product to basket

	@Test(priority=6)
	public void addSecondProductToCart()
	{    
		String noOfProductaAddedToCart1=null;
		mainPageObj.addToCart();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='nav-cart-count-container']//span[@id='nav-cart-count']")));
			WebElement cartQty1=driver.findElement(By.xpath("//div[@id='nav-cart-count-container']//span[@id='nav-cart-count']"));
			noOfProductaAddedToCart1=cartQty1.getText();
		}catch(Exception e)
		{
			System.out.println(e);
		}
		exLog5=exReport.createTest("Add second product to the cart", "Automation");
		if (noOfProductaAddedToCart1.equals("2"))
		{
			exLog5.log(Status.PASS,"User successfully added second product to the cart button") ;
		}
		else
		{
			exLog5.log(Status.FAIL,"Failed to add second product to the cart ") ;
		}
	}


	// Testcase to validate added items are visible on “Shopping basket” list

	@Test(priority=7)
	public void verifyItemsInCart()
	{
		Boolean verifyCarList=mainPageObj.verifyCartItem();
		exLog6=exReport.createTest("Validate added items are visible on Shopping basket list", "Automation");
		if (verifyCarList)
		{
			exLog6.log(Status.PASS,"Added items are visible on Shopping basket list") ;
		}
		else
		{
			exLog6.log(Status.FAIL,"Failed to add items are visible on Shopping basket list") ;
		}
	}

	//TestCase to validate that user able to save the product for later purchase by clicking on “Save for later” button.

	@Test(priority=8)
	public void verifySaveProductForLater()
	{  Boolean vAbleTosaveProductForLater=mainPageObj.verifyUserIsAbleTosaveProductForLater();
	exLog7=exReport.createTest("Validate that user able to save the product for later purchase by clicking on Save for later button.", "Automation");
	if (vAbleTosaveProductForLater)
	{
		exLog7.log(Status.PASS,"User is able to save the product for later purchase") ;
	}
	else
	{
		exLog7.log(Status.FAIL,"Failed to save the product for later purchase") ;
	}
	}

	//TestCase to validate that user can move the product back to basket by clicking on “Move to Basket”

	@Test(priority=9)
	public void verifyMoveProductBackToBasket()
	{   Boolean vAbleToMoveProductBackToBasket=mainPageObj.verifyUserIsAbleToMoveProductBackToBasket();
	exLog8=exReport.createTest("Validate that user can move the product back to basket by clicking on Move to Basket", "Automation");
	if (vAbleToMoveProductBackToBasket)
	{
		exLog8.log(Status.PASS,"User is able to move the product back to basket") ;
	}
	else
	{
		exLog8.log(Status.FAIL,"Failed to move the product back to basket") ;
	}
	}

	//Close browser

	@AfterTest
	public void closeBrowser() {
		exReport.flush();
	    driver.close();
	}
}
