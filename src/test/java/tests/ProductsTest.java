package tests;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Тесты для проверки функциональности управления товарами в корзине.
 * Включает проверки добавления товаров, отображения значка корзины
 * и количества товаров.
 */
public class ProductsTest extends BaseTest {

    private static final String USERNAME_VALID = "standard_user";
    private static final String PASSWORD_VALID = "secret_sauce";
    private static final String ITEM_NAME = "Sauce Labs Backpack";
    private static final int ADDED_ITEM_COUNT = 3;

    private static final String MSG_BADGE_NOT_APPEAR = "The shopping badge did not appear!!";
    private static final String MSG_BADGE_COLOR_MISMATCH = "The background color of the shopping cart does not match the expected color!";
    private static final String MSG_COUNT_MISMATCH = "The number of purchases in the cart does not match the expected number!";
    private static final String MSG_BUTTON_TEXT_MISMATCH = "The button text does not match the expected text!";

    /**
     * Открывает URL и выполняет авторизацию перед каждым тестом.
     */
    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(USERNAME_VALID, PASSWORD_VALID);
    }

    /**
     * Поставщик данных для тестов добавления товаров.
     * Проверяет текст кнопки "Remove", количество товаров в корзине
     * и цвет фона бейджа корзины.
     *
     * @return массив объектов с тестовыми данными
     */
    @DataProvider(name = "getItems")
    public Object[][] getItems() {
        return new Object[][]{
                {ITEM_NAME, "removeItem", "Remove", MSG_BUTTON_TEXT_MISMATCH},
                {ITEM_NAME, "getCount", "1", MSG_COUNT_MISMATCH},
                {ITEM_NAME, "getColor", "rgba(226, 35, 26, 1)", MSG_BADGE_COLOR_MISMATCH}
        };
    }

    /**
     * Проверяет состояние товара после добавления в корзину.
     * Тест проверяет текст кнопки, количество товаров и цвет значка.
     * Тест запускается 5 раз для каждого набора данных.
     *
     * @param value    название товара
     * @param action   действие для проверки (removeItem, getCount, getColor)
     * @param expected ожидаемый результат
     * @param message  сообщение об ошибке
     */
    @Test(dataProvider = "getItems", testName = "Checking condition", invocationCount = 5)
    public void checkGoodsAdded(String value, String action, String expected, String message) {
        productsPage.addItemToCart(value);
        String actual = switch (action) {
            case "removeItem" -> productsPage.removeItemFromCart(value);
            case "getCount" -> productsPage.getShoppingCount();
            case "getColor" -> productsPage.getCartBadgeBackgroundColor();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
        assertEquals(actual, expected, message);
    }

    /**
     * Проверяет отображение значка корзины после добавления товара.
     * Тест запускается 5 раз.
     */
    @Test(testName = "Checking Shopping Badge Displayed", invocationCount = 5)
    public void isShoppingBadgeDisplayed() {
        productsPage.addItemToCart(ITEM_NAME);

        assertTrue(productsPage.isShoppingBadgePresentWithWait(), MSG_BADGE_NOT_APPEAR);
    }

    /**
     * Проверяет добавление нескольких товаров в корзину.
     * Тест добавляет 3 товара и проверяет количество в корзине.
     * Тест запускается 5 раз.
     */
    @Test(testName = "Checking adding multiple items in cart", invocationCount = 5)
    public void checkMultipleItemsInCart() {
        productsPage.addItemToCarts(ADDED_ITEM_COUNT);

        assertEquals(productsPage.getShoppingCount(), Integer.toString(ADDED_ITEM_COUNT), MSG_COUNT_MISMATCH);
    }
}
