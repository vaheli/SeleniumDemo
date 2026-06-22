import org.openqa.selenium.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class LoginTest extends BaseTest {
    private static final String LOGIN_BUTTON = "//*[@id = 'login-button']";
    private static final String USERNAME_FIELD = "//*[@id = 'user-name']";
    private static final String PASSWORD_FIELD = "//*[@id = 'password']";
    private static final String ERROR_MESSAGE = "//*[@data-test='error']";
    private static final String ERROR_ICON_USERNAME = "//div[input[@id='user-name']]//*[@data-icon='times-circle']";
    private static final String ERROR_ICON_PASSWORD = "//div[input[@id='password']]//*[@data-icon='times-circle']";

    private static final String ERROR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    private static final String ERROR_PASSWORD_REQUIRED = "Epic sadface: Password is required";
    private static final String ERROR_INVALID_CREDENTIALS = "Epic sadface: Username and password " +
            "do not match any user in this service";
    private static final String ICON_IS_NOT_DISPLAYED = "Иконка ошибки не отображается!";
    private static final String TEXT_DOES_NOT_MATCH = "Текст ошибки не совпадает!";
    private static final String MESSAGE_NOT_APPEAR = "Сообщение об ошибке не появилось!";

    private final String username = "standard_user";
    private final String password = "secret_sauce";

    private WebElement usernameField;
    private WebElement passwordField;
    private WebElement loginButton;

    @BeforeMethod
    public void initElements() {
        usernameField = driver.findElement(By.xpath(USERNAME_FIELD));
        passwordField = driver.findElement(By.xpath(PASSWORD_FIELD));
        loginButton = driver.findElement(By.xpath(LOGIN_BUTTON));
    }

    private WebElement getErrorMessage() {
        try {
            return driver.findElement(By.xpath(ERROR_MESSAGE));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private WebElement getErrorIconUsername() {
        try {
            return driver.findElement(By.xpath(ERROR_ICON_USERNAME));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private WebElement getErrorIconPassword() {
        try {
            return driver.findElement(By.xpath(ERROR_ICON_PASSWORD));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private void enterUsername(String value) {
        usernameField.sendKeys(value);
    }

    private void enterPassword(String value) {
        passwordField.sendKeys(value);
    }

    private void clickLoginButton() {
        loginButton.click();
    }

    @Test
    public void checkLogin() {
        enterUsername(username);

        assertEquals(usernameField.getAttribute("value"), username);
    }

    @Test
    public void checkPassword() {
        enterPassword(password);

        assertEquals(passwordField.getAttribute("value"), password);
    }

    @Test
    public void checkLoginButton() {
        enterUsername(username);
        enterPassword(password);

        assertEquals(loginButton.getAttribute("data-test"), "login-button");

        clickLoginButton();

        assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html");
    }

    @Test
    public void checkIncorrectLogin() {
        enterUsername("");
        enterPassword(password);
        clickLoginButton();

        WebElement errorMessage = getErrorMessage();
        WebElement errorIcon = getErrorIconUsername();

        assertNotNull(errorMessage, MESSAGE_NOT_APPEAR);
        assertNotNull(errorIcon, ICON_IS_NOT_DISPLAYED);

        boolean isErrorMessageVisible = errorMessage.isDisplayed();
        boolean isErrorIconVisible = errorIcon.isDisplayed();
        String errorText = errorMessage.getText();

        assertTrue(isErrorMessageVisible, MESSAGE_NOT_APPEAR);
        assertTrue(isErrorIconVisible, ICON_IS_NOT_DISPLAYED);
        assertEquals(errorText, ERROR_USERNAME_REQUIRED, TEXT_DOES_NOT_MATCH);
    }

    @Test
    public void checkIncorrectPassword() {
        enterUsername(username);
        enterPassword("");
        clickLoginButton();

        WebElement errorMessage = getErrorMessage();
        WebElement errorIcon = getErrorIconPassword();

        assertNotNull(errorMessage, MESSAGE_NOT_APPEAR);
        assertNotNull(errorIcon, ICON_IS_NOT_DISPLAYED);

        boolean isErrorMessageVisible = errorMessage.isDisplayed();
        boolean isErrorIconVisible = errorIcon.isDisplayed();
        String errorText = errorMessage.getText();

        assertTrue(isErrorMessageVisible, MESSAGE_NOT_APPEAR);
        assertTrue(isErrorIconVisible, ICON_IS_NOT_DISPLAYED);
        assertEquals(errorText, ERROR_PASSWORD_REQUIRED, TEXT_DOES_NOT_MATCH);
    }

    @Test
    public void checkInvalidLogin() {
        enterUsername("invalid_user");
        enterPassword(password);
        clickLoginButton();

        WebElement errorMessage = getErrorMessage();
        WebElement errorIconUsername = getErrorIconUsername();
        WebElement errorIconPassword = getErrorIconPassword();

        assertNotNull(errorMessage, MESSAGE_NOT_APPEAR);
        assertNotNull(errorIconUsername, ICON_IS_NOT_DISPLAYED + " у поля логина");
        assertNotNull(errorIconPassword, ICON_IS_NOT_DISPLAYED + " у поля пароля");

        boolean isErrorMessageVisible = errorMessage.isDisplayed();
        boolean isErrorIconUsernameVisible = errorIconUsername.isDisplayed();
        boolean isErrorIconPasswordVisible = errorIconPassword.isDisplayed();
        String errorText = errorMessage.getText();

        assertTrue(isErrorMessageVisible, MESSAGE_NOT_APPEAR);
        assertTrue(isErrorIconUsernameVisible, ICON_IS_NOT_DISPLAYED + " у поля логина");
        assertTrue(isErrorIconPasswordVisible, ICON_IS_NOT_DISPLAYED + " у поля пароля");
        assertEquals(errorText, ERROR_INVALID_CREDENTIALS, TEXT_DOES_NOT_MATCH);
    }
}
