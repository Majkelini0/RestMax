package model;

/**
 * Represents a voucher, which is a type of {@link Discount} that applies to a specific {@link MenuItem}.
 * A voucher provides a percentage-based discount on the price of the associated menu item.
 * It inherits client association from the {@link Discount} class.
 */
public class Voucher extends Discount {

    private MenuItem menuItem;
    private double discountRate; // < 0.01 , 0.99 >

    /**
     * Constructs a new {@code Voucher} object.
     * Initializes the voucher with a discount rate and a specific {@link MenuItem}.
     * The discount rate must be between 0.01 (1%) and 0.99 (99%).
     * The menu item cannot be null.
     *
     * @param discountValue The discount rate for the voucher (e.g., 0.1 for 10%). Must be between 0.01 and 0.99.
     * @param menuItem      The {@link MenuItem} to which this voucher applies. Cannot be null.
     * @throws IllegalArgumentException if the discountValue is not within the valid range or if menuItem is null.
     */
    public Voucher(double discountValue, MenuItem menuItem) {
        setDiscountRate(discountValue);
        setMenuItem(menuItem);
    }

    /**
     * Calculates the total discount amount provided by this voucher.
     * This is determined by multiplying the price of the associated {@link MenuItem} by the discount rate.
     * Implements the {@link Discount#getTotalDiscountAmount()} method.
     *
     * @return The calculated discount amount as a double.
     */
    @Override
    public double getTotalDiscountAmount() {
        return menuItem.getPrice() * discountRate;
    }

    /**
     * Sets the discount rate for this voucher.
     * The rate must be between 0.01 (1%) and 0.99 (99%).
     *
     * @param discountValue The discount rate to set.
     * @throws IllegalArgumentException if discountValue is not between 0.01 and 0.99.
     */
    private void setDiscountRate(double discountValue) {
        if (discountValue < 0.01 || discountValue > 0.99) {
            throw new IllegalArgumentException("Discount value must be between 1% and 99%");
        }
        this.discountRate = discountValue;
    }

    /**
     * Gets the discount rate of this voucher.
     *
     * @return The discount rate (e.g., 0.1 for 10%).
     */
    public double getDiscountRate() {
        return discountRate;
    }

    /**
     * Sets the {@link MenuItem} to which this voucher applies.
     * The menu item cannot be null.
     *
     * @param menuItem The {@link MenuItem} to associate with this voucher.
     * @throws IllegalArgumentException if menuItem is null.
     */
    private void setMenuItem(MenuItem menuItem) {
        if (menuItem == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }
        this.menuItem = menuItem;
    }

    /**
     * Gets the {@link MenuItem} associated with this voucher.
     * This method is protected, intended for use by subclasses or within the package.
     *
     * @return The {@link MenuItem} to which this voucher applies.
     */
    protected MenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * Checks if this voucher is currently applicable.
     * A voucher is applicable if it is associated with a client (inherited from {@link Discount#isApplicable()})
     * and has an associated {@link MenuItem}.
     * Overrides the {@link Discount#isApplicable()} method.
     *
     * @return {@code true} if the voucher is applicable, {@code false} otherwise.
     */
    @Override
    public boolean isApplicable() {
        return super.isApplicable() && getMenuItem() != null;
    }

    /**
     * Returns a string representation of the {@code Voucher} object.
     * Includes details of the associated menu item and the discount rate.
     *
     * @return A string detailing the voucher's information.
     */
    @Override
    public String toString() {
        return "Voucher{" +
                "menuItem=" + menuItem +
                ", discount=" + discountRate +
                '}';
    }
}
