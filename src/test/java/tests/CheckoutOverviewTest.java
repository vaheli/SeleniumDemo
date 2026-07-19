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
@Feature("Checkout: Overview")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class CheckoutOverviewTest extends BaseTest {
    private static final String URL_INVENTORY = BASE_URL + "inventory.html";
    private static final String URL_CHECKOUT_COMPLETE = BASE_URL + "checkout-complete.html";
    private static final List<String> ITEM_NAMES = List.of("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");

    private static final String firstName = PropertyReader.getProperty("saucedemo.first_name");
    private static final String lastName = PropertyReader.getProperty("saucedemo.last_name");
    private static final String postalCode = PropertyReader.getProperty("saucedemo.postal_code");

    private static final String MSG_BADGE_NOT_APPEAR = "The shopping badge did not appear!";
    private static final String MSG_TITLE_MISMATCH = "The page title doesn't match";
    private static final String MSG_URL_MISMATCH = "The URL does not match the expected! Redirect to page failed!";
    private static final String MSG_TOTAL_PRICE_MISMATCH =
            "The total amount of goods does not match the expected amount!";
    private static final String MSG_FINISH_COLOR_MISMATCH = "The background color of the finish button does not match the expected color!";

    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(whitAdminPermission());
        productsPage
                .addItemsToCarts(ITEM_NAMES)
                .navigationPanel.clickShoppingCart();
        basketPage.getCheckout().click();
        checkoutPage.fillCheckoutForm(firstName, lastName, postalCode);
        checkoutPage.getContinueButton().click();
    }

    @Story("Проверяем заголовок страницы")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkMainTitle() {
        assertEquals(checkHeaderTitle(), HEADER.getDisplayName(), MSG_TITLE_MISMATCH);
    }

    @Story("Проверяем переход на страницу 'Оформление заказа завершено!'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchFinishButton() {
        checkoutOverviewPage.getFinishButton().click();

        assertEquals(driver.getCurrentUrl(), URL_CHECKOUT_COMPLETE, MSG_URL_MISMATCH);
        assertEquals(basketPage.navigationPanel.getPageTitle(), FINISH.getDisplayName(), MSG_TITLE_MISMATCH);
    }

    @Story("Проверяем количество ссылок в 'Бургер-Меню'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkClickBurgerMenu() {
        checkoutOverviewPage.navigationPanel.clickBurgerMenu();

        assertEquals(checkoutOverviewPage.navigationPanel.getMenuItemsCount(), 4);
    }

    @Story("Проверяем значок корзины покупок")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void isShoppingCartDisplayed() {
        assertTrue(navigationPanel.isShoppingBadgePresentWithWait(), MSG_BADGE_NOT_APPEAR);
    }

    @Story("Проверяем возврат на страницу 'Products'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchToProductsByCancel() {
        checkoutOverviewPage.getCancelButton().click();

        assertEquals(driver.getCurrentUrl(), URL_INVENTORY, MSG_URL_MISMATCH);
    }

    @Story("Проверяем итого с общей суммой товаров")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkTotalAmountOfGoods() {
        assertEquals(checkoutOverviewPage.getItemsPrice(), checkoutOverviewPage.getItemTotal(), MSG_TOTAL_PRICE_MISMATCH);
    }

    @Story("Проверяем цвет фона кнопки 'Finish'")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkFinishButtonBackgroundColor() {
        assertEquals(checkoutOverviewPage.getFinishButtonBackgroundColor(),
                "rgba(61, 220, 145, 1)", MSG_FINISH_COLOR_MISMATCH);
    }
}
