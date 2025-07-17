package model;

/**
 * Represents a special type of {@link Voucher} that is combined with a {@link Giftcard}.
 * This class extends {@link Voucher} and implements the {@link IGiftcard} interface,
 * allowing it to provide discounts from both its voucher properties and an associated gift card.
 * The total discount is capped at the price of the {@link MenuItem} the voucher applies to.
 */
public class VoucherGiftcard extends Voucher implements IGiftcard {

    private Giftcard giftcard;

    /**
     * Constructs a new {@code VoucherGiftcard} object.
     * Initializes the voucher part with a discount value (rate) and a {@link MenuItem},
     * and associates it with a {@link Giftcard}.
     * The gift card cannot be null.
     *
     * @param discountValue The discount rate for the voucher part (e.g., 0.1 for 10%). Must be between 0.01 and 0.99.
     * @param menuItem      The {@link MenuItem} to which this voucher applies. Cannot be null.
     * @param giftcard      The {@link Giftcard} to associate with this voucher. Cannot be null.
     * @throws IllegalArgumentException if the giftcard is null, or if parameters for the parent {@link Voucher} are invalid.
     */
    public VoucherGiftcard(double discountValue, MenuItem menuItem, Giftcard giftcard) {
        super(discountValue, menuItem);
        setGiftcard(giftcard);
    }

    /**
     * Calculates the total discount amount provided by this VoucherGiftcard.
     * It combines the discount from the voucher (percentage of menu item price)
     * and the value of the associated {@link Giftcard}.
     * The total discount cannot exceed the price of the {@link MenuItem}.
     * Overrides {@link Voucher#getTotalDiscountAmount()} and implements {@link IGiftcard#getTotalDiscountAmount()}.
     *
     * @return The total combined discount amount, capped at the menu item's price.
     */
    @Override
    public double getTotalDiscountAmount() {
        if (super.getTotalDiscountAmount() + giftcard.getTotalDiscountAmount() >= super.getMenuItem().getPrice()) {
            return super.getMenuItem().getPrice();
        } else {
            return super.getMenuItem().getPrice() - (super.getTotalDiscountAmount() + giftcard.getTotalDiscountAmount());
        }
    }

    /**
     * Checks if this VoucherGiftcard is currently applicable.
     * It is applicable if both the voucher part (inherited from {@link Voucher#isApplicable()})
     * and the associated {@link Giftcard} ({@link Giftcard#isApplicable()}) are applicable.
     * Overrides {@link Voucher#isApplicable()} and implements {@link IGiftcard#isApplicable()}.
     *
     * @return {@code true} if both the voucher and gift card components are applicable, {@code false} otherwise.
     */
    @Override
    public boolean isApplicable() {
        return super.isApplicable() && giftcard.isApplicable();
    }

    /**
     * Gets the nominal value of the associated {@link Giftcard}.
     * Implements the {@link IGiftcard#getGiftcardValue()} method.
     *
     * @return The value of the associated gift card as a double.
     */
    @Override
    public double getGiftcardValue() {
        return giftcard.getGiftcardValue();
    }

    /**
     * Sets the {@link Giftcard} associated with this VoucherGiftcard.
     * The gift card cannot be null.
     *
     * @param giftcard The {@link Giftcard} to associate. Cannot be null.
     * @throws IllegalArgumentException if the giftcard is null.
     */
    private void setGiftcard(Giftcard giftcard) {
        if (giftcard == null) {
            throw new IllegalArgumentException("Giftcard cannot be null");
        }
        this.giftcard = giftcard;
    }

    /**
     * Returns a string representation of the {@code VoucherGiftcard} object.
     * Includes details of the associated gift card, menu item, and voucher discount rate.
     *
     * @return A string detailing the VoucherGiftcard's information.
     */
    @Override
    public String toString() {
        return "VoucherGiftcard{" +
                "giftcard=" + giftcard +
                ", menuItem=" + getMenuItem() +
                ", discount=" + getDiscountRate() +
                '}';
    }
}
