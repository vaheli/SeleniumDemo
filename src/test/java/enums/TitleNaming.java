package enums;

import lombok.*;

@AllArgsConstructor
@Getter
public enum TitleNaming {
    HEADER("Swag Labs"),
    PRODUCTS("Products"),
    CARTS("Your Cart"),
    CHECKOUT("Checkout: Your Information"),
    CHECKOUT_OVERVIEW("Checkout: Overview"),
    FINISH("Checkout: Complete!");

    private final String displayName;
}
