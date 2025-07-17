package model;

/**
 * Defines the contract for gift card functionalities.
 * Implementations of this interface should provide methods to get the gift card's value,
 * calculate the total discount amount it provides, and check if it is currently applicable.
 */
public interface IGiftcard {

    /**
     * Gets the nominal value of the gift card.
     *
     * @return The value of the gift card as a double.
     */
    double getGiftcardValue();

    /**
     * Calculates the total discount amount that can be obtained using this gift card.
     * This might be the same as {@link #getGiftcardValue()} or could be different based on specific rules.
     *
     * @return The total discount amount as a double.
     */
    double getTotalDiscountAmount();

    /**
     * Checks if the gift card is currently applicable.
     * This could depend on factors like expiry date, minimum purchase, or association with a client.
     *
     * @return {@code true} if the gift card is applicable, {@code false} otherwise.
     */
    boolean isApplicable();
}
