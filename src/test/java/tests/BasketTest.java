package tests;

import io.qameta.allure.*;
import org.testng.annotations.*;
import pages.*;

import java.util.List;

import static enums.TitleNaming.*;
import static org.assertj.core.api.Assertions.assertThat;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.whitAdminPermission;

@Epic("SauceDemo")
@Feature("Shopping Cart")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class BasketTest extends BaseTest {
    private static final List<String> ITEM_NAMES = List.of(
            "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt"
    );
    private static final String URL_CHECKOUT = BASE_URL + "checkout-step-one.html";
    private static final String URL_PRODUCTS = BASE_URL + "inventory.html"; //Могу перенести в BaseTest и сделать public
    private static final String EXPECTED_COLOR_RGB = "61, 220, 145";
    private static final String EXPECTED_CONTINUE_SHOPPING_TEXT = "Continue Shopping";
    private static final int EXPECTED_MENU_ITEMS_COUNT = 4;

    private static final String TITLE_DESCRIPTION = "Page title (expected: '%s')";
    private static final String PRODUCTS_PAGE_TITLE_DESCRIPTION = "Product Page title";
    private static final String SHOPPING_BADGE_DESCRIPTION = "An icon with the number of items in the basket";
    private static final String CART_ITEMS_DESCRIPTION = "Items in the cart after adding products";
    private static final String MENU_ITEMS_COUNT_DESCRIPTION = "Number of items in the menu (should be %d)";
    private static final String CART_ITEMS_COUNT_DESCRIPTION = "The number of items in the basket";
    private static final String CHECKOUT_PAGE_TITLE_DESCRIPTION = "Title of the checkout page";
    private static final String CHECKOUT_BUTTON_DISPLAY_DESCRIPTION =
            "The 'Checkout' button should be displayed in the shopping cart";
    private static final String REDIRECT_URL_CHECKOUT_DESCRIPTION = "Redirect should lead to Checkout page URL";
    private static final String REDIRECT_TO_PRODUCTS_DESCRIPTION = "Redirect should lead to Products page";
    private static final String CHECKOUT_BUTTON_COLOR_DESCRIPTION = "Background color of the 'Checkout' button";
    private static final String CONTINUE_SHOPPING_BUTTON_TEXT_DESCRIPTION = "The text of the 'Continue Shopping' button";

    private static final String EXPECTED_TITLE = HEADER.getDisplayName();

    @BeforeMethod
    private void openURL() {
        loginPage.open();
        loginPage.login(whitAdminPermission());
        loginPage.clickLoginButton();
        productsPage
                .addItemsToCarts(ITEM_NAMES)
                .navigationPanel
                .clickShoppingCart(BasketPage.class);
    }

    @Story("Проверка заголовка страницы")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void mainTitleShouldBeCorrect() {
        String headerTitle = basketPage.navigationPanel.getHeaderTitle();
        assertThat(headerTitle)
                .as(TITLE_DESCRIPTION, EXPECTED_TITLE)
                .isEqualTo(EXPECTED_TITLE);
    }

    @Story("Проверяем количество ссылок в 'Бургер-Меню'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkBurgerMenuItemsCount() {
        basketPage.navigationPanel.clickBurgerMenu();

        assertThat(basketPage.navigationPanel.getMenuItemsCount())
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

    @Story("Проверка товаров в корзине после добавления")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void cartItemsShouldMatchAddedProducts() {
        List<String> actualItems = basketPage.getProductsNames();

        assertThat(actualItems)
                .as(CART_ITEMS_COUNT_DESCRIPTION)
                .hasSize(ITEM_NAMES.size());

        assertThat(actualItems)
                .as(CART_ITEMS_DESCRIPTION)
                .containsExactlyElementsOf(ITEM_NAMES);
    }

    @Story("Проверка перехода на страницу оформления заказа")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shouldRedirectToCheckoutPage() {
        assertThat(basketPage.isDisplayedCheckoutButton())
                .as(CHECKOUT_BUTTON_DISPLAY_DESCRIPTION)
                .isTrue();

        CheckoutPage checkoutPage = basketPage.clickCheckoutButton();

        assertThat(checkoutPage.getCurrentUrl())
                .as(REDIRECT_URL_CHECKOUT_DESCRIPTION)
                .isEqualTo(URL_CHECKOUT);

        assertThat(checkoutPage.navigationPanel.getPageTitle())
                .as(CHECKOUT_PAGE_TITLE_DESCRIPTION)
                .isEqualTo(CHECKOUT.getDisplayName());
    }

    @Story("Проверяем возврат на страницу 'Products' с нажатием кнопки 'Continue shopping'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shouldRedirectToProductsPageAfterContinueShopping() {
        ProductsPage page = basketPage.clickContinueShoppingButton();

        assertThat(page.getCurrentUrl())
                .as(REDIRECT_TO_PRODUCTS_DESCRIPTION)
                .isEqualTo(URL_PRODUCTS);

        assertThat(page.navigationPanel.getPageTitle())
                .as(PRODUCTS_PAGE_TITLE_DESCRIPTION)
                .isEqualTo(PRODUCTS.getDisplayName());
    }

    @Story("Проверяем цвет фона кнопки 'Checkout'")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkoutButtonShouldHaveCorrectBackgroundColor() {
        assertThat(basketPage.getCheckoutBackgroundColor())
                .as(CHECKOUT_BUTTON_COLOR_DESCRIPTION)
                .contains(EXPECTED_COLOR_RGB);
    }

    @Story("Проверяем, что отображается текст кнопки 'Continue Shopping'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void continueShoppingButtonTextShouldBeCorrect() {
        assertThat(basketPage.getContinueShoppingButtonText())
                .as(CONTINUE_SHOPPING_BUTTON_TEXT_DESCRIPTION)
                .isEqualTo(EXPECTED_CONTINUE_SHOPPING_TEXT);
    }
}
