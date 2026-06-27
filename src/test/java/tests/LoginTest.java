package tests;

import enums.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class LoginTest extends BaseTest {
    private static final String USERNAME_INVALID = "invalid_user";
    private static final String USERNAME_LOCKED = "locked_out_user";
    private static final String USERNAME_WITH_CAPS = "Standard_user";
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

    @BeforeMethod
    private void openURL() {
        loginPage.open();
    }

    @DataProvider(name = "fieldTestData")
    public Object[][] fieldTestData() {
        return new Object[][]{
                {USERNAME_VALID, PASSWORD_EMPTY, USERNAME_VALID, FieldType.USERNAME, MSG_USERNAME_MISMATCH},
                {USERNAME_EMPTY, PASSWORD_VALID, PASSWORD_VALID, FieldType.PASSWORD, MSG_PASSWORD_MISMATCH},
                {USERNAME_VALID, PASSWORD_VALID, LOGIN_BUTTON_DATA_TEST, FieldType.BUTTON, MSG_BUTTON_ATTRIBUTE},
        };
    }

    @Test(dataProvider = "fieldTestData", testName = "Check field: {2}")
    public void checkFieldTestData(String username, String password,
                                   String expectedValue, FieldType expectedFieldType,
                                   String errorMessage) {
        loginPage.enterCredentials(username, password);

        String actualValue = switch (expectedFieldType) {
            case USERNAME -> loginPage.loginAttribute();
            case PASSWORD -> loginPage.passwordAttribute();
            case BUTTON -> loginPage.loginButtonAttribute();
        };

        assertEquals(actualValue, expectedValue, errorMessage);
    }

    @Test(testName = "Check click login button")
    public void checkClickLoginButton() {
        loginPage.login(USERNAME_VALID, PASSWORD_VALID);

        assertEquals(driver.getCurrentUrl(), URL_INVENTORY, MSG_URL_MISMATCH);
        assertEquals(productsPage.getTitle(), PAGE_TITLE_PRODUCTS, MSG_TITLE_MISMATCH);
    }

    @DataProvider(name = "incorrectLoginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {USERNAME_EMPTY, PASSWORD_VALID, ERROR_USERNAME_REQUIRED, IconType.USERNAME},
                {USERNAME_VALID, PASSWORD_EMPTY, ERROR_PASSWORD_REQUIRED, IconType.PASSWORD},
                {USERNAME_INVALID, PASSWORD_VALID, ERROR_INVALID_CREDENTIALS, IconType.BOTH},
                {USERNAME_VALID, PASSWORD_INVALID, ERROR_INVALID_CREDENTIALS, IconType.PASSWORD},
                {USERNAME_LOCKED, PASSWORD_VALID, ERROR_LOCKED_USER, IconType.BOTH},
                {USERNAME_WITH_CAPS, PASSWORD_VALID, ERROR_INVALID_CREDENTIALS, IconType.BOTH},
        };
    }

    @Test(dataProvider = "incorrectLoginData", testName = "Authorization under credits: {0, 1}")
    public void checkIncorrectLogin(String username, String password,
                                    String errorMessage, IconType iconType) {
        loginPage.login(username, password);

        assertError(iconType, errorMessage);
    }

    private void assertError(IconType iconType, String expectedError) {
        assertTrue(loginPage.isErrorDisplayed(), MSG_ERROR_NOT_APPEAR);
        assertEquals(loginPage.getErrorText(), expectedError, MSG_ERROR_TEXT_MISMATCH);

        if (iconType != IconType.NONE) {
            assertTrue(isErrorIconDisplayed(iconType), MSG_ICON_NOT_DISPLAYED);
        }
    }

    private boolean isErrorIconDisplayed(IconType iconType) {
        return switch (iconType) {
            case USERNAME -> loginPage.isUsernameErrorIconDisplayed();
            case PASSWORD -> loginPage.isPasswordErrorIconDisplayed();
            case BOTH -> loginPage.isUsernameErrorIconDisplayed() && loginPage.isPasswordErrorIconDisplayed();
            case NONE -> true;
        };
    }
}
