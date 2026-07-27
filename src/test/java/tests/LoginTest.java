package tests;

import enums.*;
import io.qameta.allure.*;
import org.testng.annotations.*;
import pages.ProductsPage;
import user.User;

import static enums.IconType.*;
import static enums.TitleNaming.*;
import static org.assertj.core.api.Assertions.assertThat;
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
    private static final String ERROR_INVALID_CREDENTIALS =
            "Epic sadface: Username and password do not match any user in this service";
    private static final String MSG_ERROR_NOT_APPEAR = "An error message was expected, but it was not displayed!";
    private static final String MSG_ERROR_TEXT_MISMATCH = "The error text does not match! Expected: '%s', received: '%s'";
    private static final String MSG_ICON_NOT_DISPLAYED = "The error icon is not displayed for the field: %s";
    private static final String MSG_URL_MISMATCH =
            "After successful login, you should be redirected to the products page. Expected URL: %s";
    private static final String MSG_TITLE_MISMATCH = "The page title does not match! Expected: '%s', received: '%s'";

    private static final String EXPECTED_HEADER_SUBTITLE = PRODUCTS.getDisplayName();

    @BeforeMethod
    private void openURL() {
        loginPage.open();
    }

    @Story("Успешная авторизация")
    @Severity(SeverityLevel.BLOCKER)
    @Test(testName = "Check click login button", invocationCount = TEST_REPEAT_COUNT)
    public void shouldRedirectToProductsPageAfterSuccessfulLogin() {
        loginPage.login(whitAdminPermission());
        ProductsPage page = loginPage.clickLoginButton();

        String actualUrl = page.getCurrentUrl();
        assertThat(actualUrl)
                .as(MSG_URL_MISMATCH, URL_INVENTORY)
                .isEqualTo(URL_INVENTORY);

        String actualTitle = page.navigationPanel.getPageTitle();
        assertThat(actualTitle)
                .as(MSG_TITLE_MISMATCH, EXPECTED_HEADER_SUBTITLE, actualTitle)
                .isEqualTo(EXPECTED_HEADER_SUBTITLE);
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
        loginPage.clickLoginButton();

        assertError(iconType, errorMessage);
    }

    private void assertError(IconType iconType, String expectedError) {
        assertThat(loginPage.isErrorDisplayed())
                .as(MSG_ERROR_NOT_APPEAR)
                .isTrue();

        String actualError = loginPage.getErrorText();
        assertThat(actualError)
                .as(MSG_ERROR_TEXT_MISMATCH, expectedError, actualError)
                .isEqualTo(expectedError);

        if (iconType != NONE) {
            assertThat(isErrorIconDisplayed(iconType))
                    .as(MSG_ICON_NOT_DISPLAYED, iconType)
                    .isTrue();
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
