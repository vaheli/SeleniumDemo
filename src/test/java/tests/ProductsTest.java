package tests;

import io.qameta.allure.*;
import org.testng.annotations.*;

import java.util.List;

import static enums.TitleNaming.*;
import static org.testng.Assert.*;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.whitAdminPermission;

@Epic("SauceDemo")
@Feature("Product Management")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class ProductsTest extends BaseTest {
    private static final int ADDED_ITEM_COUNT = 3;
    private static final String ITEM_NAME = "Sauce Labs Backpack";
    private static final List<String> ITEM_NAMES = List.of("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");
    private static final String URL_CART = BASE_URL + "cart.html";

    private static final String MSG_BADGE_NOT_APPEAR = "The shopping badge did not appear!";
    private static final String MSG_BADGE_COLOR_MISMATCH = "The background color of the shopping cart does not match the expected color!";
    private static final String MSG_COUNT_MISMATCH = "The number of purchases in the cart does not match the expected number!";
    private static final String MSG_BUTTON_TEXT_MISMATCH = "The button text does not match the expected text!";
    private static final String MSG_URL_MISMATCH = "The URL does not match the expected! Redirect to page failed!";
    private static final String MSG_TITLE_MISMATCH = "The page title doesn't match";
    private static final String MSG_EMPTY_CART =
            "Error: going to the shopping cart is possible even with an empty shopping cart! " +
                    "Expected: stay on the current page, received: go to the shopping cart page";

    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(whitAdminPermission());
    }

    @Story("Проверяем количество ссылок в 'Бургер-Меню'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkClickBurgerMenu() {
        productsPage.navigationPanel.clickBurgerMenu();

        assertEquals(productsPage.navigationPanel.getMenuItemsCount(), 4);
    }

    @DataProvider(name = "getItems")
    public Object[][] getItems() {
        return new Object[][]{
                {ITEM_NAME, "removeItem", "Remove", MSG_BUTTON_TEXT_MISMATCH},
                {ITEM_NAME, "getCount", "1", MSG_COUNT_MISMATCH},
                {ITEM_NAME, "getColor", "rgb(226, 35, 26)", MSG_BADGE_COLOR_MISMATCH}
        };
    }

    @Story("Проверка корзины после добавления товара")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "getItems", invocationCount = TEST_REPEAT_COUNT)
    public void checkGoodsAdded(String value, String action, String expected, String message) {
        productsPage.addItemToCart(value);
        String actual = switch (action) {
            case "removeItem" -> productsPage.removeItemFromCart(value);
            case "getCount" -> productsPage.navigationPanel.getShoppingCount();
            case "getColor" -> productsPage.navigationPanel.getCartBadgeBackgroundColor();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
        assertEquals(actual, expected, message);
    }

    @Story("Проверяем значок корзины покупок")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void isShoppingBadgeDisplayed() {
        productsPage.addItemToCart(ITEM_NAME);

        assertTrue(navigationPanel.isShoppingBadgePresentWithWait(), MSG_BADGE_NOT_APPEAR);
    }

    @DataProvider(name = "checkGoodsInCart")
    public Object[][] getGoodsInCart() {
        return new Object[][]{
                {"По количеству", new CartTestData(ADDED_ITEM_COUNT)},
                {"По названию", new CartTestData(ITEM_NAMES)},
        };
    }

    @Story("Добавление товаров в корзину")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "checkGoodsInCart", invocationCount = TEST_REPEAT_COUNT)
    public void checkMultipleItemsInCart(String testName, CartTestData testData) {
        if (testData.isListType()) {
            productsPage.addItemsToCarts(testData.getItems());
        } else {
            productsPage.addItemToCarts(testData.getQuantity());
        }

        assertEquals(productsPage.navigationPanel.getShoppingCount(), Integer.toString(ADDED_ITEM_COUNT), MSG_COUNT_MISMATCH);
    }

    @Story("Проверяем переход на страницу 'Корзина товаров'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchToBasket() {
        productsPage
                .addItemsToCarts(ITEM_NAMES)
                .navigationPanel.clickShoppingCart();

        assertEquals(driver.getCurrentUrl(), URL_CART, MSG_URL_MISMATCH);
        assertEquals(checkHeaderTitle(), HEADER.getDisplayName(), MSG_TITLE_MISMATCH);
        assertEquals(productsPage.navigationPanel.getPageTitle(), CARTS.getDisplayName(), MSG_TITLE_MISMATCH);
    }

    @Story("Проверяем переход на страницу 'Корзина товаров' с пустой корзиной")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("SeleniumDemo")
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchToEmptyBasket() {
        String currentUrl = driver.getCurrentUrl();
        productsPage.navigationPanel.clickShoppingCart();

        assertEquals(driver.getCurrentUrl(), currentUrl, MSG_EMPTY_CART);
    }

    public static class CartTestData {

        private final List<String> items;
        private final Integer quantity;

        public CartTestData(List<String> items) {
            this.items = items;
            this.quantity = null;
        }

        public CartTestData(Integer quantity) {
            this.items = null;
            this.quantity = quantity;
        }

        public boolean isListType() {
            return items != null;
        }

        public List<String> getItems() {
            return items;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}
