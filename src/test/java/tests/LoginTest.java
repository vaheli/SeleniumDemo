package tests;

import enums.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Тесты для проверки функциональности авторизации.
 * Включает проверки валидной авторизации, различных ошибок ввода
 * и поведения системы при неверных учетных данных.
 */
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

    private static final String ERROR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    private static final String ERROR_PASSWORD_REQUIRED = "Epic sadface: Password is required";
    private static final String ERROR_LOCKED_USER = "Epic sadface: Sorry, this user has been locked out.";
    private static final String ERROR_INVALID_CREDENTIALS = "Epic sadface: Username and password do not match any user in this service";
    private static final String MSG_URL_MISMATCH = "The URL does not match the expected! Redirect to the main page failed!";
    private static final String MSG_TITLE_MISMATCH = "The page title doesn't match";
    private static final String MSG_ERROR_NOT_APPEAR = "The error message did not appear!";
    private static final String MSG_ERROR_TEXT_MISMATCH = "The error text does not match!";
    private static final String MSG_ICON_NOT_DISPLAYED = "The error icon is not displayed";

    /**
     * Открывает URL страницы входа перед каждым тестом.
     */
    @BeforeMethod
    private void openURL() {
        loginPage.open();
    }

    /**
     * Проверяет успешную авторизацию и переход на страницу товаров.
     * Тест запускается 5 раз.
     */
    @Test(testName = "Check click login button", invocationCount = 5)
    public void checkClickLoginButton() {
        loginPage.login(USERNAME_VALID, PASSWORD_VALID);

        assertEquals(driver.getCurrentUrl(), URL_INVENTORY, MSG_URL_MISMATCH);
        assertEquals(productsPage.getTitle(), "Products", MSG_TITLE_MISMATCH);
    }

    /**
     * Поставщик данных для тестов с некорректными учетными данными.
     * Включает пустые поля, неверные данные, заблокированного пользователя
     * и различные варианты неверного ввода.
     *
     * @return массив объектов с тестовыми данными
     */
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

    /**
     * Проверяет авторизацию с некорректными учетными данными.
     * Проверяет появление сообщения об ошибке, его текст
     * и отображение иконок ошибок.
     * Тест запускается 5 раз для каждого набора данных.
     *
     * @param username       имя пользователя
     * @param password       пароль
     * @param errorMessage   ожидаемое сообщение об ошибке
     * @param iconType       тип ожидаемой иконки ошибки
     */
    @Test(dataProvider = "incorrectLoginData", testName = "Authorization under credits: {0, 1}", invocationCount = 5)
    public void checkIncorrectLogin(String username, String password,
                                    String errorMessage, IconType iconType) {
        loginPage.login(username, password);

        assertError(iconType, errorMessage);
    }

    /**
     * Проверяет ошибку авторизации: наличие сообщения, текст и иконки.
     *
     * @param iconType    тип ожидаемой иконки ошибки
     * @param expectedError ожидаемый текст ошибки
     */
    private void assertError(IconType iconType, String expectedError) {
        assertTrue(loginPage.isErrorDisplayed(), MSG_ERROR_NOT_APPEAR);
        assertEquals(loginPage.getErrorText(), expectedError, MSG_ERROR_TEXT_MISMATCH);

        if (iconType != IconType.NONE) {
            assertTrue(isErrorIconDisplayed(iconType), MSG_ICON_NOT_DISPLAYED);
        }
    }

    /**
     * Проверяет отображение иконок ошибок в зависимости от типа.
     *
     * @param iconType тип иконки
     * @return true если соответствующие иконки отображаются
     */
    private boolean isErrorIconDisplayed(IconType iconType) {
        return switch (iconType) {
            case USERNAME -> loginPage.isUsernameErrorIconDisplayed();
            case PASSWORD -> loginPage.isPasswordErrorIconDisplayed();
            case BOTH -> loginPage.isUsernameErrorIconDisplayed() && loginPage.isPasswordErrorIconDisplayed();
            case NONE -> true;
        };
    }
}
