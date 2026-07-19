package tests;

import io.qameta.allure.*;
import org.testng.annotations.*;
import utils.PropertyReader;

import java.util.List;

import static enums.TitleNaming.HEADER;
import static org.testng.Assert.*;
import static pages.BasePage.BASE_URL;
import static user.UserFactory.whitAdminPermission;

@Epic("SauceDemo")
@Feature("Checkout: Complete")
@Owner("Elizbarian Vahram @Vahram_Elizbaryan")
@TmsLink("SeleniumDemo")
public class CheckoutCompleteTest extends BaseTest {
    private static final String URL_INVENTORY = BASE_URL + "inventory.html";
    private static final List<String> ITEM_NAMES = List.of("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");
    private static final String firstName = PropertyReader.getProperty("saucedemo.first_name");
    private static final String lastName = PropertyReader.getProperty("saucedemo.last_name");
    private static final String postalCode = PropertyReader.getProperty("saucedemo.postal_code");

    private static final String MSG_BADGE_NOT_APPEAR = "The shopping badge did not appear!";
    private static final String MSG_URL_MISMATCH = "The URL does not match the expected! Redirect to page failed!";
    private static final String MSG_TITLE_MISMATCH = "The page title doesn't match";
    private static final String MSG_THANK_YOU_MISMATCH =
            "The message 'Thank you for your order!' doesn't match";
    private static final String EXPECTED_THANK_YOU = "Thank you for your order!";

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
        checkoutOverviewPage.getFinishButton().click();
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

    @Story("Проверяем, что отображается сообщение")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkThankYouMessageTest() {
        assertEquals(checkoutCompletePage.getThankYouMessage(), EXPECTED_THANK_YOU, MSG_THANK_YOU_MISMATCH);
    }

    @Story("Проверяем возврат на страницу 'Products'")
    @Severity(SeverityLevel.CRITICAL)
    @Test(invocationCount = TEST_REPEAT_COUNT)
    public void checkBackToProductsPage() {
        checkoutCompletePage.clickBackToProductsPage();
        assertEquals(driver.getCurrentUrl(), URL_INVENTORY, MSG_URL_MISMATCH);
    }
}
