package tests;

import io.qameta.allure.*;
import org.testng.annotations.*;

import java.util.List;

import static enums.TitleNaming.*;
import static org.testng.Assert.*;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.whitAdminPermission;

@Epic("SauceDemo")
@Feature("Shopping Cart")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class BasketTest extends BaseTest {
    private static final List<String> ITEM_NAMES = List.of("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");
    private static final String URL_CHECKOUT = BASE_URL + "checkout-step-one.html";
    private static final String URL_INVENTORY = BASE_URL + "inventory.html"; //Могу перенести в BaseTest и сделать public

    private static final String MSG_BADGE_NOT_APPEAR = "The shopping badge did not appear!";
    private static final String MSG_TITLE_MISMATCH = "The page title doesn't match";
    private static final String MSG_URL_MISMATCH = "The URL does not match the expected! Redirect to page failed!";
    private static final String MSG_BADGE_COLOR_MISMATCH = "The background color of the checkout-button does not match the expected color!";
    private static final String MSG_BUTTON_TEXT_MISMATCH = "The button text does not match the expected text!";
    private static final String MSG_CART_ITEMS_MISMATCH =
            "The items in the basket do not match the expected ones!";

    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(whitAdminPermission());
        productsPage
                .addItemsToCarts(ITEM_NAMES)
                .navigationPanel.clickShoppingCart();
    }

    @Story("Проверяем заголовок страницы")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkMainTitle() {
        assertEquals(checkHeaderTitle(), HEADER.getDisplayName(), MSG_TITLE_MISMATCH);
    }

    @Story("Проверяем добавление товаров в корзину из списка")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkAddedGoods() {
        assertEquals(basketPage.getProductsNames(), ITEM_NAMES, MSG_CART_ITEMS_MISMATCH);
    }

    @Story("Проверяем количество ссылок в 'Бургер-Меню'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkClickBurgerMenu() {
        basketPage.navigationPanel.clickBurgerMenu();

        assertEquals(basketPage.navigationPanel.getMenuItemsCount(), 4);
    }

    @Story("Проверяем значок корзины покупок")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void isShoppingBadgeDisplayed() {
        assertTrue(navigationPanel.isShoppingBadgePresentWithWait(), MSG_BADGE_NOT_APPEAR);
    }

    @Story("Проверяем переход на страницу 'Предварительный просмотр'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchToCheckout() {
        basketPage.getCheckout().click();

        assertEquals(driver.getCurrentUrl(), URL_CHECKOUT, MSG_URL_MISMATCH);
        assertEquals(basketPage.navigationPanel.getPageTitle(), CHECKOUT.getDisplayName(), MSG_TITLE_MISMATCH);
    }

    @Story("Проверяем возврат на страницу 'Products' с нажатием кнопки 'Continue shopping'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkClickContinueShopping() {
        basketPage.getContinueShopping().click();

        assertEquals(driver.getCurrentUrl(), URL_INVENTORY, MSG_URL_MISMATCH);
    }

    @Story("Проверяем цвет фона кнопки 'Checkout'")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkCheckoutColor() {
        assertEquals(basketPage.getCheckoutBackgroundColor(), "rgba(61, 220, 145, 1)", MSG_BADGE_COLOR_MISMATCH);
    }

    @Story("Проверяем, что отображается текст кнопки 'Continue Shopping'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkContinueShoppingButtonText() {
        assertEquals(basketPage.getContinueShopping().getText(), "Continue Shopping", MSG_BUTTON_TEXT_MISMATCH);
    }
}
