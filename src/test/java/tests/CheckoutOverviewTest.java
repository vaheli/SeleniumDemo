package tests;

import io.qameta.allure.*;
import org.testng.annotations.*;
import pages.BasketPage;
import pages.CheckoutCompletePage;
import pages.ProductsPage;
import utils.PropertyReader;

import java.util.List;

import static enums.TitleNaming.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;
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
    private static final List<String> ITEM_NAMES = List.of(
            "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt"
    );
    private static final String EXPECTED_COLOR_RGB = "61, 220, 145";

    private static final String firstName = PropertyReader.getProperty("saucedemo.first_name");
    private static final String lastName = PropertyReader.getProperty("saucedemo.last_name");
    private static final String postalCode = PropertyReader.getProperty("saucedemo.postal_code");
    private static final int EXPECTED_MENU_ITEMS_COUNT = 4;

    private static final String MENU_ITEMS_COUNT_DESCRIPTION = "Number of items in the menu (should be %d)";
    private static final String TITLE_DESCRIPTION = "Page title (expected: '%s')";
    private static final String SHOPPING_BADGE_DESCRIPTION = "An icon with the number of items in the basket";
    private static final String MSG_TITLE_MISMATCH = "The page title does not match! Expected: '%s'";
    private static final String MSG_URL_REDIRECT_CHECKOUT_COMPLETE = "Redirect should lead to Checkout complete page URL";
    private static final String MSG_URL_REDIRECT_PRODUCTS = "Redirect should lead to Products page URL";
    private static final String MSG_TOTAL_PRICE_MISMATCH =
            "The total amount of goods does not match! Expected: %.2f, received: %.2f";
    private static final String MSG_FINISH_COLOR_MISMATCH =
            "The background color of the finish button does not match the expected color!";

    private static final String EXPECTED_HEADER_TITLE = HEADER.getDisplayName();
    private static final String EXPECTED_HEADER_SUBTITLE = FINISH.getDisplayName();

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
        checkoutPage.fillCheckoutForm(firstName, lastName, postalCode);
        checkoutPage.clickContinueButton();
    }

    @Story("Проверка заголовка страницы")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void mainTitleShouldBeCorrect() {
        String headerTitle = checkoutOverviewPage.navigationPanel.getHeaderTitle();
        assertThat(headerTitle)
                .as(TITLE_DESCRIPTION, EXPECTED_HEADER_TITLE)
                .isEqualTo(EXPECTED_HEADER_TITLE);
    }

    @Story("Завершение оформления заказа")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkSwitchFinishButton() {
        CheckoutCompletePage completePage = checkoutOverviewPage.clickFinishButton();

        assertThat(completePage.getCurrentUrl())
                .as(MSG_URL_REDIRECT_CHECKOUT_COMPLETE)
                .isEqualTo(URL_CHECKOUT_COMPLETE);

        assertThat(basketPage.navigationPanel.getPageTitle())
                .as(MSG_TITLE_MISMATCH, EXPECTED_HEADER_SUBTITLE)
                .isEqualTo(EXPECTED_HEADER_SUBTITLE);
    }

    @Story("Проверяем количество ссылок в 'Бургер-Меню'")
    @Severity(SeverityLevel.NORMAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkBurgerMenuItemsCount() {
        checkoutOverviewPage.navigationPanel.clickBurgerMenu();

        assertThat(checkoutOverviewPage.navigationPanel.getMenuItemsCount())
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

    @Story("Проверяем возврат на страницу 'Products'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shouldRedirectToBasketPageAfterCancelButtonClick() {
        ProductsPage page = checkoutOverviewPage.clickCancelButton();

        assertThat(page.getCurrentUrl())
                .as(MSG_URL_REDIRECT_PRODUCTS)
                .isEqualTo(URL_INVENTORY);
    }

    @Story("Проверка итоговой суммы товаров")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkTotalAmountOfGoods() {
        double actualTotal = checkoutOverviewPage.getTotalItemsPrice();
        double expectedTotal = checkoutOverviewPage.getItemTotal();

        assertThat(actualTotal)
                .as(MSG_TOTAL_PRICE_MISMATCH, expectedTotal, actualTotal)
                .isEqualTo(expectedTotal, withPrecision(0.01));
    }

    @Story("Проверяем цвет фона кнопки 'Finish'")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkFinishButtonBackgroundColor() {
        assertThat(checkoutOverviewPage.getFinishButtonBackgroundColor())
                .as(MSG_FINISH_COLOR_MISMATCH)
                .contains(EXPECTED_COLOR_RGB);
    }
}
