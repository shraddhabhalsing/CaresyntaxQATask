package com.qa.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.qa.base.Base;

public class AmazonPage extends Base {
	SoftAssert asst=new SoftAssert();
	ArrayList<String>collectnToStorePrdktNameLst=new ArrayList<String>();
	ArrayList<String>collectnToStorePrdktIdLst=new ArrayList<String>();
	int cnt=0;

	// Find elements at run time
	@FindBy(xpath="//a[contains(text(),'Amazon.de')]")
	WebElement textLink;

	@FindBy(xpath="//input[@type='submit' and @name='accept']")
	WebElement cookiesAkzeptierenBtn;

	@FindBy(xpath="//a[@class='nav-a nav-a-2   nav-progressive-attribute' and @id='nav-link-accountList']//span[contains(text(),'Konto und Listen')]")
	WebElement kontoundListenBtn;

	@FindBy(xpath="//input[@type='email']")
	WebElement emailTextBox;

	@FindBy(xpath="//input[@type='submit']")
	WebElement submitBtn;

	@FindBy(xpath="//input[@type='password']")
	WebElement passwortTextBox;

	@FindBy(xpath="//input[@id='signInSubmit']")
	WebElement anmeldenBtn; 

	@FindBy(xpath="//input[@type='text' and @id='twotabsearchtextbox']")
	WebElement searchTextBox; 

	@FindBys(@FindBy(xpath="//div[@class='s-main-slot s-result-list s-search-results sg-row']//div"))
	List<WebElement> productList;

	@FindBy(xpath="//input[@id='add-to-cart-button']")
	WebElement addToCartBtn;

	@FindBys(@FindBy(xpath="//a[@id='nav-cart']"))
	WebElement einKaufswagenBtn;

	@FindBy(xpath="tent']")
	WebElement cartCount;

	//Initialize WebElements mentioned in the current page
	public void initializeWebElement(){
		PageFactory.initElements(driver, this);
	}

	// Login Method
	public void login(String userName, String password)
	{   textLink.click();
	WebDriverWait wt1= new WebDriverWait(driver,30);
	wt1.until(ExpectedConditions.elementToBeClickable(cookiesAkzeptierenBtn));
	cookiesAkzeptierenBtn.click();
	kontoundListenBtn.click();
	emailTextBox.sendKeys(userName);
	submitBtn.click();
	passwortTextBox.sendKeys(password);
	anmeldenBtn.click();
	}

	//Method to search and select products
	public Boolean enterAndSearchProductName(String productName,int expProduct)
	{   Boolean status=false;
	String vSearchBoxValue=searchTextBox.getAttribute("value");
	if (!vSearchBoxValue.equals(""))
	{
		searchTextBox.clear();
	}
	searchTextBox.sendKeys(productName);
	try {
		Thread.sleep(4000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	searchTextBox.sendKeys(Keys.ENTER);
	List<WebElement> getProductIdlstWebElmnts=driver.findElements(By.xpath("//div[@class='s-main-slot s-result-list s-search-results sg-row']//div[starts-with(@data-component-type,'s-search-result')]"));
	WebDriverWait wt2= new WebDriverWait(driver,30);
	wt2.until(ExpectedConditions.visibilityOfAllElements(getProductIdlstWebElmnts));
	collectnToStorePrdktIdLst.add(getProductIdlstWebElmnts.get(expProduct).getAttribute("data-asin") );
	try
	{ if (getProductIdlstWebElmnts.get(expProduct).getAttribute("data-asin")!="")
	{List<WebElement> getProductNamelstWebelement=driver.findElements(By.xpath("//div[@class='s-main-slot s-result-list s-search-results sg-row']//div[starts-with(@data-component-type,'s-search-result')]//h2//a"));
	String getProductNameText=getProductNamelstWebelement.get(expProduct).getText();
	collectnToStorePrdktNameLst.add(getProductNameText);	
	getProductNamelstWebelement.get(expProduct).click();
	status=true;
	}
	}catch(Exception e)
	{
		System.out.println(e);
	}
	return status;
	}

	public void addToCart()
	{  
	try
	{
	addToCartBtn.click();
	WebDriverWait wt3=new WebDriverWait(driver,30);
	wt3.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='a-section attach-warranty-button-row']//div[@class='a-button-stack']//span[2]//input")));
	
	
	WebElement cartBtnWebElemnt=driver.findElement(By.xpath("//div[@class='a-section attach-warranty-button-row']//div[@class='a-button-stack']//span[2]//input"));

	if (cartBtnWebElemnt.isDisplayed())
	{
		cartBtnWebElemnt.click();

	}
	
	}catch(Exception e)
	{
		System.out.println(e);
		
	}
   try
	{
	WebElement e2=driver.findElement(By.xpath("//a[@class='a-link-normal close-button']"));
	if (e2.isDisplayed())
	{
		e2.click();

	}

	}catch(Exception e)
	{
		System.out.println(e);


	}
	}

	// Method to add Products to the cart
	public boolean verifyCartItem()
	{ if (einKaufswagenBtn.isDisplayed())
	{
		einKaufswagenBtn.click();
	}
	for (int j=0;j<collectnToStorePrdktIdLst.size();j++)
	{
		String expectedProduct1=collectnToStorePrdktIdLst.get(j);
		List<WebElement> fndProduct=driver.findElements(By.xpath("//form[@id='activeCartViewForm']//div[@class='a-section a-spacing-mini sc-list-body sc-java-remote-feature']//div[@class='a-row sc-list-item sc-list-item-border sc-java-remote-feature']"));
		for (int e=0;e<=fndProduct.size();e++)
		{
			String actProdkt=fndProduct.get(e).getAttribute("data-asin");
			if (actProdkt.contains(expectedProduct1))
			{
				cnt++;
				break;
			}
		}
	}

	if (cnt==2)
	{
		return true;
	}

	else
	{
		return false;
	}
	}

	//Method  to validate that user able to save the product for later purchase by clicking on “Save for later” button.

	public Boolean verifyUserIsAbleTosaveProductForLater()
	{	Boolean verifyNoOfProdktSavedForLatr = false;
	cnt=0;
	Collections.reverse(collectnToStorePrdktIdLst);
	try
	{
		for (int j=0;j<collectnToStorePrdktIdLst.size();j++)
		{
			String chkprdt=collectnToStorePrdktIdLst.get(j);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<WebElement>fndProductShoppingVenture=driver.findElements(By.xpath("//form[@id='activeCartViewForm']//div[@class='a-row sc-list-item sc-list-item-border sc-java-remote-feature']"));
			String srcProdktShoppingVenture=fndProductShoppingVenture.get(j).getAttribute("data-asin");
			if (chkprdt.contains(srcProdktShoppingVenture))
			{
				List<WebElement> ls5=driver.findElements(By.xpath("//form[@id='activeCartViewForm']//div[@class='a-row sc-list-item sc-list-item-border sc-java-remote-feature']//div[@class='a-row sc-action-links']//span[3]//input"));
				WebDriverWait w= new WebDriverWait(driver,30);
				w.until(ExpectedConditions.visibilityOfAllElements(ls5));
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (ls5.get(0).isEnabled())
				{
					//System.out.println(ls5.get(0).isEnabled());
					ls5.get(0).click();
				}
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<WebElement> fndProductInSavedForLater=driver.findElements(By.xpath("//form[@id='savedCartViewForm']//div[@class='a-row sc-list-item sc-list-item-border sc-java-remote-feature']"));
				WebDriverWait w1= new WebDriverWait(driver,30);
				w1.until(ExpectedConditions.visibilityOfAllElements(fndProductInSavedForLater));
				for (int k=0;k<=fndProductInSavedForLater.size();k++)
				{
					String ProductInSavedForLater=fndProductInSavedForLater.get(k).getAttribute("data-asin");
					if (ProductInSavedForLater.contains(srcProdktShoppingVenture))
					{
						cnt++;
						break;
					}
				}	
			}
		}

	}catch(Exception e)
	{
		System.out.println(e);
	}
	if (cnt==2)
	{
		verifyNoOfProdktSavedForLatr= true;
	}
	else
	{
		verifyNoOfProdktSavedForLatr= false;
	}
	return verifyNoOfProdktSavedForLatr;
	}

	//Method to validate that user can move the product back to basket by clicking on “Move to Basket”

	public Boolean verifyUserIsAbleToMoveProductBackToBasket( )
	{   cnt=0;
	Boolean veriNoOfProdktwhenMovedBackToBaskt = false;
	Collections.reverse(collectnToStorePrdktIdLst);
	try
	{
		for (int j=0;j<collectnToStorePrdktIdLst.size();j++)
		{
			String chkprdt=collectnToStorePrdktIdLst.get(j);
			List<WebElement>lstOfProductToMoveBack=	driver.findElements(By.xpath("//form[@id='savedCartViewForm']//div[@class='a-row sc-list-item sc-list-item-border sc-java-remote-feature']"));
			for (int s=0;s<lstOfProductToMoveBack.size();s++)
			{
				String l=lstOfProductToMoveBack.get(s).getAttribute("data-asin");
				if (l.equals(chkprdt))
				{
					List<WebElement>ls6=driver.findElements(By.xpath("//form[@id='savedCartViewForm']//div[@class='a-row sc-list-item sc-list-item-border sc-java-remote-feature']//div[@class='a-row sc-action-links']//span[2]//input"));
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (ls6.get(0).isEnabled())
					{
						ls6.get(0).click();
					}

					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List<WebElement>fndProductShoppingVenture=driver.findElements(By.xpath("//form[@id='activeCartViewForm']//div[@class='a-row sc-list-item sc-list-item-border sc-java-remote-feature']"));
					for (int d=0;d<fndProductShoppingVenture.size();d++)
					{
						String z=fndProductShoppingVenture.get(d).getAttribute("data-asin");

						if (z.contains(l))
						{
							cnt++;
							break;
						}
					}
				}
			}
		}
	}catch(Exception e)
	{
		System.out.println(e);
	}
	if (cnt==2)
	{
		veriNoOfProdktwhenMovedBackToBaskt= true;
	}
	else
	{
		veriNoOfProdktwhenMovedBackToBaskt= false;
	}
	return veriNoOfProdktwhenMovedBackToBaskt;
	}
}
