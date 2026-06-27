package tests;

import enums.CartAction;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class ProductsTest extends BaseTest {
    private static final String ITEM_NAME = "Sauce Labs Backpack";
    private static final int ADDED_ITEM_COUNT = 3;
    private static final String COLOR_BADGE_RGBA = "rgba(226, 35, 26, 1)";

    private static final String MSG_BADGE_NOT_APPEAR = "The shopping badge did not appear!!";
    private static final String MSG_BADGE_COLOR_MISMATCH = "The background color of the shopping cart does not match the expected color!";
    private static final String MSG_COUNT_MISMATCH = "The number of purchases in the cart does not match the expected number!";
    private static final String MSG_BUTTON_TEXT_MISMATCH = "The button text does not match the expected text!";

    private static final String EXPECTED_BUTTON_TEXT = "Remove";
    private static final String ITEM_COUNT = "1";

    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(USERNAME_VALID, PASSWORD_VALID);
    }

    @DataProvider(name = "getItems")
    public Object[][] getItems() {
        return new Object[][]{
                {CartAction.BADGE_VISIBILITY, ITEM_NAME, true, MSG_BADGE_NOT_APPEAR},
                {CartAction.REMOVE_FROM_CART, ITEM_NAME, EXPECTED_BUTTON_TEXT, MSG_BUTTON_TEXT_MISMATCH},
                {CartAction.BADGE_COUNT, ITEM_NAME, ITEM_COUNT, MSG_COUNT_MISMATCH},
                {CartAction.BADGE_COUNT, ADDED_ITEM_COUNT, String.valueOf(ADDED_ITEM_COUNT), MSG_COUNT_MISMATCH},
                {CartAction.BADGE_COLOR, ITEM_NAME, COLOR_BADGE_RGBA, MSG_BADGE_COLOR_MISMATCH}
        };
    }

    @Test(dataProvider = "getItems")
    public void checkGoodsAdded(CartAction action, Object value, Object expected, String message) {
        addItemToCart(value);
        assertCartAction(action, expected, message);
    }

    private void addItemToCart(Object value) {
        if (value instanceof String) {
            productsPage.addItemToCart((String) value);
        } else if (value instanceof Integer) {
            productsPage.addItemToCart((Integer) value);
        } else {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getSimpleName());
        }
    }

    private void assertCartAction(CartAction action, Object expected, String expectedMessage) {
        Object actual = executeAction(action);
        assertEquals(actual, expected, expectedMessage);
    }

    private Object executeAction(CartAction action) {
        return switch (action) {
            case BADGE_VISIBILITY -> productsPage.isShoppingBadgePresentWithWait();
            case REMOVE_FROM_CART -> productsPage.removeItemFromCart(ITEM_NAME);
            case BADGE_COUNT -> productsPage.getShoppingCount();
            case BADGE_COLOR -> productsPage.getCartBadgeBackgroundColor();
        };
    }
}
