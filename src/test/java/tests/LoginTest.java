package tests;

import enums.*;
import io.qameta.allure.*;
import org.testng.annotations.*;
import user.User;

import static enums.IconType.*;
import static enums.TitleNaming.*;
import static org.testng.Assert.*;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.*;

@Epic("SauceDemo")
@Feature("Authentication")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class LoginTest extends BaseTest {
    private static final String URL_INVENTORY = BASE_URL + "inventory.html";

    private static final String ERROR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    private static final String ERROR_PASSWORD_REQUIRED = "Epic sadface: Password is required";
    private static final String ERROR_LOCKED_USER = "Epic sadface: Sorry, this user has been locked out.";
    private static final String ERROR_INVALID_CREDENTIALS = "Epic sadface: Username and password do not match any user in this service";
    private static final String MSG_ERROR_NOT_APPEAR = "The error message did not appear!";
    private static final String MSG_ERROR_TEXT_MISMATCH = "The error text does not match!";
    private static final String MSG_ICON_NOT_DISPLAYED = "The error icon is not displayed";
    private static final String MSG_URL_MISMATCH = "The URL does not match the expected! Redirect to page failed!";
    private static final String MSG_TITLE_MISMATCH = "The page title doesn't match";

    @BeforeMethod
    private void openURL() {
        loginPage.open();
    }

    @Story("Проверяем, что логин успешен — перешли на страницу 'Products'")
    @Severity(SeverityLevel.BLOCKER)
    @Test(testName = "Check click login button", invocationCount = TEST_REPEAT_COUNT)
    public void checkClickLoginButton() {
        loginPage.login(whitAdminPermission());

        assertEquals(driver.getCurrentUrl(), URL_INVENTORY, MSG_URL_MISMATCH);
        assertEquals(productsPage.navigationPanel.getPageTitle(), PRODUCTS.getDisplayName(), MSG_TITLE_MISMATCH);
    }

    @DataProvider(name = "incorrectLoginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"Empty username", whitEmptyLoginPermission(), ERROR_USERNAME_REQUIRED, USERNAME},
                {"Empty password", whitEmptyPasswordPermission(), ERROR_PASSWORD_REQUIRED, PASSWORD},
                {"Invalid login", whitInvalidLoginPermission(), ERROR_INVALID_CREDENTIALS, BOTH},
                {"Invalid password", whitInvalidPasswordPermission(), ERROR_INVALID_CREDENTIALS, PASSWORD},
                {"Locked user", whitLockedPermission(), ERROR_LOCKED_USER, BOTH},
                {"Whit caps login", whitCapsLoginPermission(), ERROR_INVALID_CREDENTIALS, BOTH},
        };
    }

    @Story("Проверка некорректного ввода данных.")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "incorrectLoginData", invocationCount = TEST_REPEAT_COUNT)
    public void checkIncorrectLogin(String testName, User user, String errorMessage, IconType iconType) {
        loginPage.login(user);

        assertError(iconType, errorMessage);
    }

    private void assertError(IconType iconType, String expectedError) {
        assertTrue(loginPage.isErrorDisplayed(), MSG_ERROR_NOT_APPEAR);
        assertEquals(loginPage.getErrorText(), expectedError, MSG_ERROR_TEXT_MISMATCH);

        if (iconType != NONE) {
            assertTrue(isErrorIconDisplayed(iconType), MSG_ICON_NOT_DISPLAYED);
        }
    }

    @Story("Получаем иконки ошибки")
    @Severity(SeverityLevel.MINOR)
    private boolean isErrorIconDisplayed(IconType iconType) {
        return switch (iconType) {
            case USERNAME -> loginPage.isUsernameErrorIconDisplayed();
            case PASSWORD -> loginPage.isPasswordErrorIconDisplayed();
            case BOTH -> loginPage.isUsernameErrorIconDisplayed() && loginPage.isPasswordErrorIconDisplayed();
            case NONE -> true;
        };
    }
}
