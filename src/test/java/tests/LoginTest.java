package tests;

import org.testng.annotations.*;

import static org.testng.Assert.*;

public class LoginTest extends BaseTest {
    private static final String USERNAME_VALID = "standard_user";
    private static final String USERNAME_INVALID = "invalid_user";
    private static final String USERNAME_LOCKED = "locked_out_user";
    private static final String USERNAME_WITH_CAPS = "Standard_user";
    private static final String PASSWORD_VALID = "secret_sauce";
    private static final String PASSWORD_INVALID = "invalid_password";
    private static final String PASSWORD_EMPTY = "";
    private static final String USERNAME_EMPTY = "";

    private static final String URL_INVENTORY = "https://www.saucedemo.com/inventory.html";
    private static final String PAGE_TITLE_PRODUCTS = "Products";
    private static final String LOGIN_BUTTON_DATA_TEST = "login-button";

    private static final String ERROR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    private static final String ERROR_PASSWORD_REQUIRED = "Epic sadface: Password is required";
    private static final String ERROR_LOCKED_USER = "Epic sadface: Sorry, this user has been locked out.";
    private static final String ERROR_INVALID_CREDENTIALS = "Epic sadface: Username and password do not match any user in this service";

    private static final String MSG_USERNAME_MISMATCH = "The entered username does not match the expected username!";
    private static final String MSG_PASSWORD_MISMATCH = "The entered password does not match the expected password!";
    private static final String MSG_BUTTON_ATTRIBUTE = "The button attribute does not match!";
    private static final String MSG_URL_MISMATCH = "The URL does not match the expected! Redirect to the main page failed!";
    private static final String MSG_TITLE_MISMATCH = "The page title doesn't match";
    private static final String MSG_ERROR_NOT_APPEAR = "The error message did not appear!";
    private static final String MSG_ERROR_TEXT_MISMATCH = "The error text does not match!";
    private static final String MSG_ICON_NOT_DISPLAYED = "The error icon is not displayed";
    private static final String MSG_ICON_USERNAME = MSG_ICON_NOT_DISPLAYED + " in the login field!";
    private static final String MSG_ICON_PASSWORD = MSG_ICON_NOT_DISPLAYED + " in the password field!";

    @BeforeMethod
    private void openURL() {
        loginPage.open();
    }

    @Test
    public void checkEnterLogin() {
        loginPage.enterCredentials(USERNAME_VALID, PASSWORD_EMPTY);

        String actualUsername = loginPage.loginAttribute();
        assertEquals(actualUsername, USERNAME_VALID, MSG_USERNAME_MISMATCH);
    }

    @Test
    public void checkEnterPassword() {
        loginPage.enterCredentials(USERNAME_EMPTY, PASSWORD_VALID);

        String actualPassword = loginPage.passwordAttribute();
        assertEquals(actualPassword, PASSWORD_VALID, MSG_PASSWORD_MISMATCH);
    }

    @Test
    public void checkLoginButton() {
        loginPage.enterCredentials(USERNAME_VALID, PASSWORD_VALID);

        String submitButton = loginPage.loginButtonAttribute();
        assertEquals(submitButton, LOGIN_BUTTON_DATA_TEST, MSG_BUTTON_ATTRIBUTE);
    }

    @Test
    public void checkClickLoginButton() {
        loginPage.login(USERNAME_VALID, PASSWORD_VALID);

        assertEquals(driver.getCurrentUrl(), URL_INVENTORY, MSG_URL_MISMATCH);
        assertEquals(productsPage.getTitle(), PAGE_TITLE_PRODUCTS, MSG_TITLE_MISMATCH);
    }

    @Test
    public void checkEmptyLogin() {
        loginPage.login(USERNAME_EMPTY, PASSWORD_VALID);

        assertError(loginPage.isUsernameErrorIconDisplayed(), ERROR_USERNAME_REQUIRED);
    }

    @Test
    public void checkLoginWithLockedUser() {
        loginPage.login(USERNAME_LOCKED, PASSWORD_VALID);

        assertLoginError(ERROR_LOCKED_USER);
    }

    @Test
    public void checkLoginWithCapsLock() {
        loginPage.login(USERNAME_WITH_CAPS, PASSWORD_VALID);

        assertLoginError(ERROR_INVALID_CREDENTIALS);
    }

    @Test
    public void checkEmptyPassword() {
        loginPage.login(USERNAME_VALID, PASSWORD_EMPTY);

        assertError(loginPage.isPasswordErrorIconDisplayed(), ERROR_PASSWORD_REQUIRED);
    }

    @Test
    public void checkIncorrectLogin() {
        loginPage.login(USERNAME_INVALID, PASSWORD_VALID);

        assertError(loginPage.isUsernameErrorIconDisplayed(), ERROR_INVALID_CREDENTIALS);
    }

    @Test
    public void checkIncorrectPassword() {
        loginPage.login(USERNAME_VALID, PASSWORD_INVALID);

        assertError(loginPage.isPasswordErrorIconDisplayed(), ERROR_INVALID_CREDENTIALS);
    }

    @Test
    public void checkErrorIconDisplayed() {
        loginPage.login(USERNAME_INVALID, PASSWORD_VALID);

        assertTrue(loginPage.isUsernameErrorIconDisplayed(), MSG_ICON_USERNAME);
        assertTrue(loginPage.isUsernameErrorIconDisplayed(), MSG_ICON_PASSWORD);
    }

    private void assertLoginError(String expectedError) {
        assertTrue(loginPage.isErrorDisplayed(), MSG_ERROR_NOT_APPEAR);
        assertEquals(loginPage.getErrorText(), expectedError, MSG_ERROR_TEXT_MISMATCH);
    }

    private void assertError(boolean isIconDisplayed, String expectedError) {
        assertTrue(isIconDisplayed, MSG_ICON_NOT_DISPLAYED);
        assertTrue(loginPage.isErrorDisplayed(), MSG_ERROR_NOT_APPEAR);
        assertEquals(loginPage.getErrorText(), expectedError, MSG_ERROR_TEXT_MISMATCH);
    }
}
