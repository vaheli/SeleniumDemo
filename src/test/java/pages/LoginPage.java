package pages;

import org.openqa.selenium.*;

public class LoginPage extends BasePage{
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String ATTRIBUTE_DATA_TEST = "data-test";
    private static final String ATTRIBUTE_USERNAME = "username";
    private static final String ATTRIBUTE_PASSWORD = "password";
    private static final String ATTRIBUTE_LOGIN_BUTTON = "login-button";
    private static final String ATTRIBUTE_ERROR = "error";

    private final By loginInput = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_USERNAME));
    private final By passwordInput = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_PASSWORD));
    private final By submitButton = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_LOGIN_BUTTON));
    private final By error = By.xpath(DATA_TEXT_PATTERN.formatted(ATTRIBUTE_ERROR));
    private final By errorIconUsername = By.xpath(DATA_ICON_PATTERN.formatted(ATTRIBUTE_USERNAME));
    private final By errorIconPassword = By.xpath(DATA_ICON_PATTERN.formatted(ATTRIBUTE_PASSWORD));

    public LoginPage(WebDriver driver) {
        super(driver);
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
        return getElement(error).getText();
    }

    public String loginAttribute() {
        return getElement(loginInput,ATTRIBUTE_VALUE);
    }

    public String passwordAttribute() {
        return getElement(passwordInput,ATTRIBUTE_VALUE);
    }

    public String loginButtonAttribute() {
        return getElement(submitButton,ATTRIBUTE_DATA_TEST);
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

    public String getElement(By locator, String attribute) {
        return driver.findElement(locator).getAttribute(attribute);
    }

    public boolean isErrorElementDisplayed(By locator) {
        try {
            return getElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
