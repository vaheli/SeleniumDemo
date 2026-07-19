package tests;

import io.qameta.allure.*;
import org.testng.annotations.*;
import utils.PropertyReader;

import java.util.List;

import static enums.TitleNaming.*;
import static org.testng.Assert.*;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.whitAdminPermission;

@Epic("SauceDemo")
@Feature("Checkout")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class CheckoutTest extends BaseTest {
    private static final String URL_CART = BASE_URL + "cart.html";
    private static final String URL_CHECKOUT_OVERVIEW = BASE_URL + "checkout-step-two.html";
    private static final List<String> ITEM_NAMES = List.of("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");
    private static final String EXPECTED_PLACEHOLDER_FIRST_NAME = "First Name";
    private static final String EXPECTED_PLACEHOLDER_LAST_NAME = "Last Name";
    private static final String FIRST_NAME = PropertyReader.getProperty("saucedemo.first_name");
    private static final String LAST_NAME = PropertyReader.getProperty("saucedemo.last_name");
    private static final String INVALID_ZIP = PropertyReader.getProperty("saucedemo.postal_code");

    private static final String MSG_BADGE_NOT_APPEAR = "The shopping badge did not appear!";
    private static final String MSG_TITLE_MISMATCH = "The page title doesn't match";
    private static final String MSG_BADGE_COLOR_MISMATCH = "The background color of the shopping cart does not match the expected color!";
    private static final String MSG_CONTINUE_COLOR_MISMATCH = "The background color of the continue button does not match the expected color!";
    private static final String MSG_URL_MISMATCH = "The URL does not match the expected! Redirect to page failed!";
    private static final String MSG_PLACEHOLDER_MISMATCH = "The placeholder for the '%s' field does not match!";
    private static final String MSG_ZIP_ACCEPTS_INVALID_CHARS =
            "Validation error: The 'Zip/Postal Code' field accepts invalid characters (%s). " +
                    "Expected behavior: The field should accept only numbers. (0-9).";

    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(whitAdminPermission());
        productsPage
                .addItemsToCarts(ITEM_NAMES)
                .navigationPanel.clickShoppingCart();
        basketPage.getCheckout().click();
    }

    @Story("Проверяем заголовок страницы")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkMainTitle() {
        assertEquals(checkHeaderTitle(), HEADER.getDisplayName(), MSG_TITLE_MISMATCH);
    }

    @Story("Проверяем количество ссылок в 'Бургер-Меню'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkClickBurgerMenu() {
        checkoutPage.navigationPanel.clickBurgerMenu();

        assertEquals(checkoutPage.navigationPanel.getMenuItemsCount(), 4);
    }

    @Story("Проверяем значок корзины покупок")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void isShoppingCartDisplayed() {
        assertTrue(navigationPanel.isShoppingBadgePresentWithWait(), MSG_BADGE_NOT_APPEAR);
    }

    @Story("Проверяем, соответствует ли текст плейсхолдера полю 'First Name'")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkPlaceholderTextByFirstName() {
        assertEquals(checkoutPage.getFieldFirstNamePlaceholder(), EXPECTED_PLACEHOLDER_FIRST_NAME,
                MSG_PLACEHOLDER_MISMATCH.formatted(EXPECTED_PLACEHOLDER_FIRST_NAME));
    }

    @Story("Проверяем, соответствует ли текст плейсхолдера полю 'Last Name'")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkPlaceholderTextByLastName() {
        assertEquals(checkoutPage.getFieldLastNamePlaceholder(), EXPECTED_PLACEHOLDER_LAST_NAME,
                MSG_PLACEHOLDER_MISMATCH.formatted(EXPECTED_PLACEHOLDER_LAST_NAME));
    }

    @Story("Проверяем ввод некорректных данных в поле 'ZIP/Postal code'")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("SeleniumDemo")
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkInputValueByZipCode() {
        assertTrue(checkoutPage.isMatchesFieldPostalCode(INVALID_ZIP), MSG_ZIP_ACCEPTS_INVALID_CHARS.formatted(INVALID_ZIP));
    }

    @Story("Проверяем переход на страницу 'Предварительный просмотр'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchToCheckoutOverview() {
        checkoutPage.fillCheckoutForm(FIRST_NAME, LAST_NAME, INVALID_ZIP);
        checkoutPage.getContinueButton().click();

        assertEquals(driver.getCurrentUrl(), URL_CHECKOUT_OVERVIEW, MSG_URL_MISMATCH);
        assertEquals(basketPage.navigationPanel.getPageTitle(), CHECKOUT_OVERVIEW.getDisplayName(), MSG_TITLE_MISMATCH);
    }

    @Story("Проверяем возврат на страницу 'Корзина товаров'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchToBasketByCancel() {
        checkoutPage.getCancelButton().click();

        assertEquals(driver.getCurrentUrl(), URL_CART, MSG_URL_MISMATCH);
    }

    @DataProvider(name = "checkColorInCheckout")
    public Object[][] checkColorInCheckout() {
        return new Object[][]{
                {"ContinueColor", "rgb(255, 255, 255)", MSG_CONTINUE_COLOR_MISMATCH},
                {"CartBadgeColor", "rgb(226, 35, 26)", MSG_BADGE_COLOR_MISMATCH}
        };
    }

    @Story("Проверяем цвет фона")
    @Severity(SeverityLevel.MINOR)
    @Test(dataProvider = "checkColorInCheckout", invocationCount = TEST_REPEAT_COUNT)
    public void checkCheckoutBackgroundColor(String action, String expected, String message) {
        checkoutPage.getContinueButton();
        String actual = switch (action) {
            case "ContinueColor" -> checkoutPage.getContinueBackgroundColor();
            case "CartBadgeColor" -> checkoutPage.navigationPanel.getCartBadgeBackgroundColor();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };

        assertEquals(actual, expected, message);
    }
}
