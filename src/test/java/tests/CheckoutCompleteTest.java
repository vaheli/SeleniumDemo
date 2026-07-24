package tests;

import io.qameta.allure.*;
import org.testng.annotations.*;
import pages.BasketPage;
import pages.ProductsPage;
import utils.PropertyReader;

import java.util.List;

import static enums.TitleNaming.HEADER;
import static org.assertj.core.api.Assertions.*;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.whitAdminPermission;

@Epic("SauceDemo")
@Feature("Checkout: Complete")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class CheckoutCompleteTest extends BaseTest {
    private static final String URL_INVENTORY = BASE_URL + "inventory.html";
    private static final List<String> ITEM_NAMES = List.of(
            "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt"
    );
    private static final String firstName = PropertyReader.getProperty("saucedemo.first_name");
    private static final String lastName = PropertyReader.getProperty("saucedemo.last_name");
    private static final String postalCode = PropertyReader.getProperty("saucedemo.postal_code");
    private static final int EXPECTED_MENU_ITEMS_COUNT = 4;

    private static final String MENU_ITEMS_COUNT_DESCRIPTION = "Number of items in the menu (should be %d)";
    private static final String SHOPPING_BADGE_DESCRIPTION = "An icon with the number of items in the basket";
    private static final String THANK_YOU_MESSAGE_DESCRIPTION = "The text of the message after the successful order";
    private static final String TITLE_DESCRIPTION = "Page title (expected: '%s')";
    private static final String MSG_URL_REDIRECT = "Redirect should lead to Products page URL";
    private static final String EXPECTED_THANK_YOU = "Thank you for your order!";

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
        basketPage.clickCheckoutButton();
        checkoutPage.fillCheckoutForm(firstName, lastName, postalCode);
        checkoutPage.clickContinueButton();
        checkoutOverviewPage.clickFinishButton();
    }

    @Story("Проверка заголовка страницы")
    @Severity(SeverityLevel.MINOR)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void mainTitleShouldBeCorrect() {
        String headerTitle = checkoutCompletePage.navigationPanel.getHeaderTitle();
        assertThat(headerTitle)
                .as(TITLE_DESCRIPTION, EXPECTED_TITLE)
                .isEqualTo(EXPECTED_TITLE);
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

    @Story("Проверка сообщения благодарности после оформления заказа")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shouldDisplayThankYouMessage() {
        assertThat(checkoutCompletePage.getThankYouMessage())
                .as(THANK_YOU_MESSAGE_DESCRIPTION)
                .isEqualTo(EXPECTED_THANK_YOU);
    }

    @Story("Проверяем возврат на страницу 'Products'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void shouldRedirectToProductsPageAfterBackButtonClick() {
        ProductsPage page = checkoutCompletePage.clickBackToProductsPage();

        assertThat(page.getCurrentUrl())
                .as(MSG_URL_REDIRECT)
                .isEqualTo(URL_INVENTORY);
    }
}
