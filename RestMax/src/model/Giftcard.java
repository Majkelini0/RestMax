package model;

/**
 * Represents a gift card, which is a type of {@link Discount} and implements the {@link IGiftcard} interface.
 * A gift card has a specific monetary value that can be used as a discount.
 * It inherits client association from the {@link Discount} class.
 */
public class Giftcard extends Discount implements IGiftcard {
    private double giftcardValue;

    /**
     * Constructs a new {@code Giftcard} object with a specified value.
     * The gift card value must be greater than 0.
     *
     * @param giftcardValue The monetary value of the gift card. Must be positive.
     * @throws IllegalArgumentException if giftcardValue is not greater than 0.
     */
    public Giftcard(double giftcardValue) {
        setGiftcardValue(giftcardValue);
    }

    /**
     * Gets the total discount amount provided by this gift card, which is its nominal value.
     * Implements the {@link Discount#getTotalDiscountAmount()} and {@link IGiftcard#getTotalDiscountAmount()} methods.
     *
     * @return The value of the gift card.
     */
    @Override
    public double getTotalDiscountAmount() {
        return giftcardValue;
    }

    /**
     * Sets the monetary value of this gift card.
     * The value must be greater than 0.
     *
     * @param giftcardValue The value to set for the gift card.
     * @throws IllegalArgumentException if giftcardValue is not greater than 0.
     */
    private void setGiftcardValue(double giftcardValue) {
        if (giftcardValue <= 0) {
            throw new IllegalArgumentException("Giftcard value must be greater than 0");
        }
        this.giftcardValue = giftcardValue;
    }

    /**
     * Gets the nominal value of this gift card.
     * Implements the {@link IGiftcard#getGiftcardValue()} method.
     *
     * @return The monetary value of the gift card.
     */
    @Override
    public double getGiftcardValue() {
        return giftcardValue;
    }

    /**
     * Checks if this gift card is currently applicable.
     * A gift card is applicable if it is associated with a client (inherited from {@link Discount#isApplicable()})
     * and its value is greater than 0.
     * Overrides the {@link Discount#isApplicable()} and implements {@link IGiftcard#isApplicable()}.
     *
     * @return {@code true} if the gift card is applicable, {@code false} otherwise.
     */
    @Override
    public boolean isApplicable() {
        return super.isApplicable() && getGiftcardValue() > 0;
    }

    /**
     * Returns a string representation of the {@code Giftcard} object.
     * Includes the gift card's value.
     *
     * @return A string detailing the gift card's value.
     */
    @Override
    public String toString() {
        return "Giftcard{" +
                "giftcardValue=" + giftcardValue +
                '}';
    }
}
