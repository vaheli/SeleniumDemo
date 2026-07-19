package enums;

public enum TitleNaming {
    HEADER("Swag Labs"),
    PRODUCTS("Products"),
    CARTS("Your Cart"),
    CHECKOUT("Checkout: Your Information"),
    CHECKOUT_OVERVIEW("Checkout: Overview"),
    FINISH("Checkout: Complete!");

    private final String displayName;

    TitleNaming(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
