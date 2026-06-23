package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import java.util.*;

public class LoginPage {
    WebDriver driver;
    public final By loginInput = By.xpath("//*[@id = 'user-name']");
    public final By passwordInput = By.xpath("//*[@id = 'password']");
    public final By submitButton = By.xpath("//*[@id = 'login-button']");
    public final By error = By.xpath("//*[@data-test='error']");
    public final By errorIconUsername = By.xpath("//div[input[@id='user-name']]" +
            "//*[@data-icon='times-circle']");
    public final By errorIconPassword = By.xpath("//div[input[@id='password']]" +
            "//*[@data-icon='times-circle']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://www.saucedemo.com/");
    }

    public void login(String username, String password) {
        driver.findElement(loginInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(submitButton).click();
    }

    public void enterCredentials(String username, String password) {
        driver.findElement(loginInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
    }

    public boolean isErrorDisplayed() {
        try {
            return driver.findElement(error).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getErrorText() {
        return driver.findElement(error).getText();
    }

    public WebElement getFindElement(By locator) {
        return driver.findElement(locator);
    }

    public Map<String, Boolean> areErrorIconsDisplayed(By userLocator, By passwordLocator) {
        Map<String, Boolean> results = new HashMap<>();
        try {
            results.put("username", driver.findElement(userLocator).isDisplayed());
        } catch (NoSuchElementException e) {
            results.put("username", false);
        }
        try {
            results.put("password", driver.findElement(passwordLocator).isDisplayed());
        } catch (NoSuchElementException e) {
            results.put("password", false);
        }
        return results;
    }

    public boolean isErrorIconDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
