package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

public class LoginPage {
    WebDriver driver;

    private static final String BASE_URL = "https://www.saucedemo.com/";

    private final By loginInput = By.xpath("//*[@id = 'user-name']");
    private final By passwordInput = By.xpath("//*[@id = 'password']");
    private final By submitButton = By.xpath("//*[@id = 'login-button']");
    private final By error = By.xpath("//*[@data-test='error']");
    private final By errorIconUsername = By.xpath("//div[input[@id='user-name']]" +
            "//*[@data-icon='times-circle']");
    private final By errorIconPassword = By.xpath("//div[input[@id='password']]" +
            "//*[@data-icon='times-circle']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(BASE_URL);
    }

    public void login(String username, String password) {
        enterCredentials(username,password);
        driver.findElement(submitButton).click();
    }

    public void enterCredentials(String username, String password) {
        driver.findElement(loginInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
    }

    public String getErrorText() {
        return driver.findElement(error).getText();
    }

    public String loginAttribute() {
        return getElement(loginInput).getAttribute("value");
    }

    public String passwordAttribute() {
        return getElement(passwordInput).getAttribute("value");
    }

    public String loginButtonAttribute() {
        return getElement(submitButton).getAttribute("data-test");
    }

    public boolean isErrorDisplayed() {
        return isErrorElementDisplayed(error);
    }

    public boolean isUsernameErrorIconDisplayed() {
        return isErrorElementDisplayed(errorIconUsername);
    }

    public boolean isPasswordErrorIconDisplayed() {
        return isErrorElementDisplayed(errorIconPassword);
    }

    public WebElement getElement(By locator) {
        return driver.findElement(locator);
    }

    public boolean isErrorElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
