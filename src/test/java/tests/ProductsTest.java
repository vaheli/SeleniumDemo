package tests;

import io.qameta.allure.*;
import lombok.Getter;
import org.testng.annotations.*;
import pages.BasketPage;

import java.util.List;

import static enums.TitleNaming.*;
import static org.assertj.core.api.Assertions.assertThat;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.whitAdminPermission;

@Epic("SauceDemo")
@Feature("Product Management")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class ProductsTest extends BaseTest {
    private static final int ADDED_ITEM_COUNT = 3;
    private static final String EXPECTED_ADDED_ITEM_COUNT = "3";
    private static final String ITEM_NAME = "Sauce Labs Backpack";
    private static final List<String> ITEM_NAMES = List.of(
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt"
    );
    private static final String URL_CART = BASE_URL + "cart.html";
    private static final int EXPECTED_MENU_ITEMS_COUNT = 4;

    private static final String MENU_ITEMS_COUNT_DESCRIPTION = "Number of items in the menu (should be %d)";
    private static final String MSG_BADGE_COLOR_MISMATCH =
            "The color of the basket icon does not match! Expected: rgb(226, 35, 26)";
    private static final String SHOPPING_BADGE_DESCRIPTION = "An icon with the number of items in the basket";
    private static final String MSG_COUNT_MISMATCH = "The number of items in the cart does not match! Expected: %d";
    private static final String MSG_BUTTON_TEXT_MISMATCH = "The button text does not match! Expected: 'Remove'";
    private static final String MSG_TITLE_MISMATCH
            = "The page title does not match! Expected: '%s', received: '%s'";
    private static final String MSG_EMPTY_CART =
            "The transition to the shopping cart was successful, even though it is empty! Expected URL: %s, actual URL: %s";
    private static final String TITLE_DESCRIPTION = "Page title (expected: '%s')";
    private static final String MSG_URL_MISMATCH =
            "After clicking 'Shopping cart', you should be taken to the basket page. Expected URL: %s";
    private static final String MSG_CART_SHOULD_BE_EMPTY =
            "The basket must be empty before starting the test!";

    private static final String EXPECTED_HEADER_TITLE = HEADER.getDisplayName();
    private static final String EXPECTED_HEADER_DESCRIPTION = CARTS.getDisplayName();

    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(whitAdminPermission());
        loginPage.clickLoginButton();
    }

    @Story("Проверяем количество ссылок в 'Бургер-Меню'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkBurgerMenuItemsCount() {
        productsPage.navigationPanel.clickBurgerMenu();

        assertThat(productsPage.navigationPanel.getMenuItemsCount())
                .as(MENU_ITEMS_COUNT_DESCRIPTION, EXPECTED_MENU_ITEMS_COUNT)
                .isEqualTo(EXPECTED_MENU_ITEMS_COUNT);
    }

    @DataProvider(name = "getItems")
    public Object[][] getItems() {
        return new Object[][]{
                {ITEM_NAME, "removeItem", "Remove", MSG_BUTTON_TEXT_MISMATCH},
                {ITEM_NAME, "getCount", "1", MSG_COUNT_MISMATCH},
                {ITEM_NAME, "getColor", "226, 35, 26", MSG_BADGE_COLOR_MISMATCH}
        };
    }

    @Story("Валидация корзины после добавления товара")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "getItems", invocationCount = TEST_REPEAT_COUNT)
    public void shouldVerifyCartAfterAddingItem(String value, String action, String expected, String message) {
        productsPage.addItemToCart(value);
        String actual = switch (action) {
            case "removeItem" -> productsPage.removeItemFromCart(value);
            case "getCount" -> productsPage.navigationPanel.getShoppingCount();
            case "getColor" -> productsPage.navigationPanel.getBadgeColor();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };

        assertThat(actual)
                .as(message)
                .contains(expected);
    }

    @Story("Проверка отображения значка корзины покупок")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shoppingCartBadgeShouldBeDisplayed() {
        productsPage.addItemToCart(ITEM_NAME);

        assertThat(productsPage.navigationPanel.isShoppingBadgePresentWithWait())
                .as(SHOPPING_BADGE_DESCRIPTION)
                .isTrue();
    }

    @DataProvider(name = "checkGoodsInCart")
    public Object[][] getGoodsInCart() {
        return new Object[][]{
                {"Добавление по количеству (3 товара)", new CartTestData(ADDED_ITEM_COUNT)},
                {"Добавление по названиям", new CartTestData(ITEM_NAMES)},
        };
    }

    @Story("Добавление товаров в корзину")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "checkGoodsInCart", invocationCount = TEST_REPEAT_COUNT)
    public void shouldAddMultipleItemsToCart(String testName, CartTestData testData) {
        if (testData.isListType()) {
            productsPage.addItemsToCarts(testData.getItems());
        } else {
            productsPage.addItemToCarts(testData.getQuantity());
        }

        String actualCount = productsPage.navigationPanel.getShoppingCount();
        assertThat(actualCount)
                .as(MSG_COUNT_MISMATCH, ADDED_ITEM_COUNT)
                .isEqualTo(EXPECTED_ADDED_ITEM_COUNT);
    }

    @Story("Проверяем переход на страницу 'Корзина товаров'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shouldRedirectToBasketPageAfterAddingItems() {
        BasketPage page = productsPage
                .addItemsToCarts(ITEM_NAMES)
                .navigationPanel
                .clickShoppingCart(BasketPage.class);

        String actualUrl = page.getCurrentUrl();
        assertThat(actualUrl)
                .as(MSG_URL_MISMATCH, URL_CART)
                .isEqualTo(URL_CART);

        String actualHeader = page.navigationPanel.getHeaderTitle();
        assertThat(actualHeader)
                .as(TITLE_DESCRIPTION, EXPECTED_HEADER_TITLE)
                .isEqualTo(EXPECTED_HEADER_TITLE);

        String actualTitle = page.navigationPanel.getPageTitle();
        assertThat(actualTitle)
                .as(MSG_TITLE_MISMATCH, EXPECTED_HEADER_DESCRIPTION, actualTitle)
                .isEqualTo(EXPECTED_HEADER_DESCRIPTION);
    }

    @Story("Запрет перехода в пустую корзину")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("SeleniumDemo")
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shouldNotRedirectToBasketWhenEmpty() {
        boolean cartEmpty = productsPage.navigationPanel.isCartEmpty();
        assertThat(cartEmpty)
                .as(MSG_CART_SHOULD_BE_EMPTY)
                .isTrue();

        String currentUrl = driver.getCurrentUrl();
        navigationPanel.clickShoppingCart(BasketPage.class);

        String actualUrl = driver.getCurrentUrl();
        assertThat(actualUrl)
                .as(MSG_EMPTY_CART, currentUrl, actualUrl)
                .isEqualTo(currentUrl);
    }

    @Getter
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
    }
}
