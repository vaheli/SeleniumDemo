package tests;

import io.qameta.allure.*;
import org.testng.annotations.*;
import pages.*;
import utils.PropertyReader;

import java.util.List;

import static enums.TitleNaming.*;
import static org.assertj.core.api.Assertions.assertThat;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.whitAdminPermission;

@Epic("SauceDemo")
@Feature("Checkout")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class CheckoutTest extends BaseTest {
    private static final String URL_CART = BASE_URL + "cart.html";
    private static final String URL_CHECKOUT_OVERVIEW = BASE_URL + "checkout-step-two.html";
    private static final List<String> ITEM_NAMES = List.of(
            "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt"
    );
    private static final String EXPECTED_PLACEHOLDER_FIRST_NAME = "First Name";
    private static final String EXPECTED_PLACEHOLDER_LAST_NAME = "Last Name";
    private static final String FIRST_NAME = PropertyReader.getProperty("saucedemo.first_name");
    private static final String LAST_NAME = PropertyReader.getProperty("saucedemo.last_name");
    private static final String INVALID_ZIP = PropertyReader.getProperty("saucedemo.postal_code");
    private static final int EXPECTED_MENU_ITEMS_COUNT = 4;
    private static final String EXPECTED_CONTINUE_COLOR_RGB = "255, 255, 255";
    private static final String EXPECTED_CART_BADGE_COLOR_RGB = "226, 35, 26";

    private static final String MENU_ITEMS_COUNT_DESCRIPTION = "Number of items in the menu (should be %d)";
    private static final String TITLE_DESCRIPTION = "Page title (expected: '%s')";
    private static final String SHOPPING_BADGE_DESCRIPTION = "An icon with the number of items in the basket";
    private static final String MSG_TITLE_MISMATCH = "The page title does not match! Expected: '%s', received: '%s'";
    private static final String MSG_COLOR_MISMATCH =
            "The color of element '%s' does not match! Expected: %s, received: %s";
    private static final String MSG_URL_MISMATCH =
            "After clicking 'Continue', you should be taken to the order preview page. Expected URL: %s";
    private static final String MSG_PLACEHOLDER_MISMATCH =
            "Placeholder for field '%s' does not match! Expected: '%s', received: '%s'";
    private static final String MSG_ZIP_ACCEPTS_INVALID_CHARS =
            "Validation error: The 'Zip/Postal Code' field accepts invalid characters (%s). " +
                    "Expected behavior: The field should accept only numbers. (0-9).";

    private static final String EXPECTED_HEADER_TITLE = HEADER.getDisplayName();
    private static final String EXPECTED_HEADER_DESCRIPTION = CHECKOUT_OVERVIEW.getDisplayName();

    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(whitAdminPermission());
        loginPage.clickLoginButton();
        productsPage
                .addItemsToCarts(ITEM_NAMES)
                .navigationPanel
                .clickShoppingCart(BasketPage.class);
        basketPage.clickCheckoutButton();
    }

    @Story("Проверка заголовка страницы")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void mainTitleShouldBeCorrect() {
        String headerTitle = checkoutPage.navigationPanel.getHeaderTitle();
        assertThat(headerTitle)
                .as(TITLE_DESCRIPTION, EXPECTED_HEADER_TITLE)
                .isEqualTo(EXPECTED_HEADER_TITLE);
    }

    @Story("Проверяем количество ссылок в 'Бургер-Меню'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkBurgerMenuItemsCount() {
        checkoutPage.navigationPanel.clickBurgerMenu();

        assertThat(checkoutPage.navigationPanel.getMenuItemsCount())
                .as(MENU_ITEMS_COUNT_DESCRIPTION, EXPECTED_MENU_ITEMS_COUNT)
                .isEqualTo(EXPECTED_MENU_ITEMS_COUNT);
    }

    @Story("Проверка отображения значка корзины покупок")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shoppingCartBadgeShouldBeDisplayed() {
        assertThat(navigationPanel.isShoppingBadgePresentWithWait())
                .as(SHOPPING_BADGE_DESCRIPTION)
                .isTrue();
    }

    @Story("Проверка placeholder для поля 'First Name'")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkPlaceholderTextByFirstName() {
        String actualValue = checkoutPage.getFirstNamePlaceholderValue();
        assertThat(actualValue)
                .as(MSG_PLACEHOLDER_MISMATCH,
                        EXPECTED_PLACEHOLDER_FIRST_NAME,
                        EXPECTED_PLACEHOLDER_FIRST_NAME,
                        actualValue)
                .isEqualTo(EXPECTED_PLACEHOLDER_FIRST_NAME);
    }

    @Story("Проверка placeholder для поля 'Last Name'")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkPlaceholderTextByLastName() {
        String actualValue = checkoutPage.getLastNamePlaceholderValue();
        assertThat(actualValue)
                .as(MSG_PLACEHOLDER_MISMATCH,
                        EXPECTED_PLACEHOLDER_LAST_NAME,
                        EXPECTED_PLACEHOLDER_LAST_NAME,
                        actualValue)
                .isEqualTo(EXPECTED_PLACEHOLDER_LAST_NAME);
    }

    @Story("Проверяем ввод некорректных данных в поле 'ZIP/Postal code'")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("SeleniumDemo")
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void zipCodeShouldNotAcceptInvalidCharacters() {
        boolean actual = checkoutPage.isPostalCodeValid(INVALID_ZIP);

        assertThat(actual)
                .as(MSG_ZIP_ACCEPTS_INVALID_CHARS, INVALID_ZIP)
                .isTrue();
    }

    @Story("Переход на страницу Checkout Overview")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shouldRedirectToCheckoutOverviewAfterContinue() {
        checkoutPage.fillCheckoutForm(FIRST_NAME, LAST_NAME, INVALID_ZIP);

        CheckoutOverviewPage overviewPage = checkoutPage.clickContinueButton();

        String actualUrl = overviewPage.getCurrentUrl();
        assertThat(actualUrl)
                .as(MSG_URL_MISMATCH, URL_CHECKOUT_OVERVIEW)
                .isEqualTo(URL_CHECKOUT_OVERVIEW);

        String actualTitle = overviewPage.navigationPanel.getPageTitle();
        assertThat(actualTitle)
                .as(MSG_TITLE_MISMATCH, EXPECTED_HEADER_DESCRIPTION, actualTitle)
                .isEqualTo(EXPECTED_HEADER_DESCRIPTION);
    }

    @Story("Проверяем возврат на страницу 'Корзина товаров'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchToBasketByCancel() {
        BasketPage page = checkoutPage.clickCancelButton();

        assertThat(page.getCurrentUrl())
                .as(MSG_URL_MISMATCH, URL_CART)
                .isEqualTo(URL_CART);
    }

    @DataProvider(name = "checkColorInCheckout")
    public Object[][] getColorData() {
        return new Object[][]{
                {"ContinueColor", EXPECTED_CONTINUE_COLOR_RGB, MSG_COLOR_MISMATCH},
                {"CartBadgeColor", EXPECTED_CART_BADGE_COLOR_RGB, MSG_COLOR_MISMATCH}
        };
    }

    @Story("Проверяем цвет фона")
    @Severity(SeverityLevel.MINOR)
    @Test(dataProvider = "checkColorInCheckout", invocationCount = TEST_REPEAT_COUNT)
    public void shouldVerifyElementColorsOnCheckoutPage(String action, String expected, String message) {
        checkoutPage.getContinueButton();
        String actual = switch (action) {
            case "ContinueColor" -> checkoutPage.getContinueButtonColor();
            case "CartBadgeColor" -> checkoutPage.navigationPanel.getBadgeColor();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };

        assertThat(actual)
                .as(message, expected, expected, actual)
                .contains(expected);
    }
}
